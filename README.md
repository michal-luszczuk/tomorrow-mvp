![logo](http://blog.propaneapps.com/wp-content/uploads/2016/04/logo.png)

## Please read [this article](http://blog.propaneapps.com/android/mvp-for-android/) to know why this library was created



## Tomorrow - Android MVP library with loaders
Library to help all of you in MVP pattern with loaders (to retain presenters) implementation without even knowing or having any idea how loaders really work.

## Gradle Dependency

1. Because Tomorrow library project is stored on `jcenter` maven repository, firstly you need to be sure that jcenter maven declaration in your project root `build.gradle` file.

    ```gradle 
    allprojects {
        repositories {
            jcenter()
       }
    }
    ```


2. Now you can easily add gradle dependency to your actual module `build.gradle` file 
    ```gradle
    compile 'com.propaneapps.tomorrow:library:1.0.1'
    ```
    
## Simple usage

1. Firsty you have to define your View layer class and Presenter class.

  Your preseter must extend `BasePresenter` class which handle lifecycle actions and also view actions.
  1. If you want to handle only configuration change retain, you don’t have to do anything, because by default **presenter** object will be retained.
Just remember that for the first time **presenter** `onCreate` method will be called with `null` bundle object

  2. If you want to additionally **handle proces kill** and its restoration you could use `onSaveInstanceState(Bundle bundle)` method to save **presenter** state, and later restore state using Bundle object passed in `onCreate(Bundle bundle)` method.
  
  ```java
  /**
 * Most common base representation of presenter class
 * with callback actions and MVP view layer support
 *
 * @param <V> Type of the view
 */
public class BasePresenter<V> implements Presenter<V> {

        private V view;
    
        /**
         * Called when presenter is created.
         * This will not e called if activity is recreated because of configuration change.
         *
         * @param bundle Bundle with saved state. Could be null when presenter is created for the first time.
         *               It will be filled with state data if presenter is recreated after activity/process kill
         */
        @Override
        public void onCreate(@Nullable Bundle bundle) {
    
        }
    
        /**
         * Called when presenter and it's component (Activity/Fragment) is going to be removed from memory
         * This is time when state should be saved if we want to handle activity/process kill.
         * This will not be called if activity is recreated because of configuration change.
         *
         * @param bundle Bundle object to which we could save our presenter state
         */
        @Override
        public void onSaveInstanceState(@NonNull Bundle bundle) {
    
        }
    
        /**
         * Called when component Activity is being removed from the memory (it's finishing, i.e. because of back button
         * press action)
         */
        @Override
        public void onDestroy() {
    
        }
    
        /**
         * Called when view handled by this presenter is available.
         * It will be called no later than Activity/Fragment onStart() method call.
         *
         * @param view Object representing MVP view layer
         */
        @Override
        public void bindView(V view) {
            this.view = view;
        }
    
        /**
         * Called when view is being unbind from presenter component.
         * It will be called no later than Activity/Fragment onStop() method call.
         */
        @Override
        public void unbindView() {
            this.view = null;
        }
    
        @Override
        public V getView() {
            return view;
        }
}
  ```
  


2. Having your view object and presenter object defined you could use `BasePresenterActivity` or `BasePresenterFragment` according to a component type you want to use.

  Regardless of which component we want to use, next step is to override two required methods.

  1. First one is `getViewLayer()` method which should return instance of object representing our MVP view layer.
  
  2. Second one is an **instance of factory object** which will be used later to automatically create our **Presenter** object. 
    1. First factory method `create` is to construct presenter object
    1. `getTypeClazz` should return class type of our presenter which our factory is creating
    
  Here is example of Fragment class with `SampleDownloadTaskPresenter`
  
    ```java
    public class SampleMvpLoaderFragment extends BasePresenterFragment<SampleDownloadResultView<List<User>>, SampleDownloadTaskPresenter> {
    
        private SampleDownloadResultView<List<User>> view;
    
        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View root = inflater.inflate(R.layout.activity_sample_mvp_loader, container, false);
            view = new BaseSampleDownloadResultView(root);
            return root;
        }
    
        @Override
        public FactoryWithType<SampleDownloadTaskPresenter> getPresenterFactory() {
            return new FactoryWithType<SampleDownloadTaskPresenter>() {
                @Override
                public Class<? extends SampleDownloadTaskPresenter> getTypeClazz() {
                    return SampleDownloadTaskPresenter.class;
                }
    
                @Override
                public SampleDownloadTaskPresenter create() {
                    return new SampleDownloadTaskPresenter();
                }
            };
        }
    
        @Override
        public SampleDownloadResultView<List<User>> getViewLayer() {
            return view;
        }
    }
    ```

## Sample

Please check sample application in `sample` module of this project.


# Internals
## How this library works
### Concept
Concept of this library is mainly to retain **Presenter** between configuration changes and to restore it's state in case of any other 
system events like i.e. process kill. It's Presenter layer separation from strong Activity/Fragment lifecycle events. 

Loaders looks like a very good tool for that.

### Loader
We know that loader could retain orientation changes inside `LoaderManager` that's why we could hold strong reference to any object.
Only thing is to properly get this reference later.

To create and store **presenter** object `ObjectRetainLoader` class is used.
It extends `Loader` class and it's implementation main goals are:

1. Use factory to create presenter for the first time
2. Hold reference to presenter object as long as it's possible
3. Deliver presenter reference. 

[See ObjectRetainLoader source code](https://github.com/michal-luszczuk/tomorrow-mvp/blob/master/library/src/main/java/com/propaneapps/tomorrow/loader/PresenterRetainLoader.java)

### Bridge between Activity/Fragment and Loaders

If we've got implementation of our Loader class next important thing is to use it with Activity/Fragment component properly.

All core logic for that is mostly stored inside `LoaderBridge` class.

`LoaderBridge` is responsible for retrieving **presenter** object with knowledge
of how **loaders** are stored and managed inside app components like Activity or Fragment.

We could divide it's behavior for two paths:

1. Use standard `initLoader` behavior on `LoaderManager` with callbacks to initialize/create loader and retrieve it's load result 
2. Search for already existing loader with reference to already created Presenter

[See LoaderBridge source code](https://github.com/michal-luszczuk/tomorrow-mvp/blob/master/library/src/main/java/com/propaneapps/tomorrow/loader/LoaderBridge.java)

Why we divide those behavior between two? Inside loaders implementation exists a bug 
which could cause calling Loader callback twice.
That's why we are directly looking for Loader reference from Loader manager and not using this callback way

### BasePresenterActivity & BasePresenterFragment
Classes are internally using `LoaderBridge` mechanism to make it all work.
Their role is to:

1. Use `LoaderBridge` to retrieve presenter object
2. Call specific lifecycle methods on presenter
3. Manage view layer and bind it to presenter object

[See BasePresenterActivity source code](https://github.com/michal-luszczuk/tomorrow-mvp/blob/master/library/src/main/java/com/propaneapps/tomorrow/base/BasePresenterActivity.java)

[See BasePresenterFragment source code](https://github.com/michal-luszczuk/tomorrow-mvp/blob/master/library/src/main/java/com/propaneapps/tomorrow/base/BasePresenterFragment.java)




