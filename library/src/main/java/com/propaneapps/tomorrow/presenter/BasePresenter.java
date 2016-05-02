/*
 * Copyright (C) 2016 Michał Łuszczuk.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.propaneapps.tomorrow.presenter;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

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
