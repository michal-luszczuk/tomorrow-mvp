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
package com.propaneapps.tomorrow.loader;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;

import com.propaneapps.tomorrow.common.FactoryWithType;
import com.propaneapps.tomorrow.presenter.Presenter;

/**
 * Bridge to make it easier to implement usage of PresenterRetainLoader inside specific Fragment or Activity
 *
 * @param <P> Type of presenter
 */
public class LoaderBridge<P extends Presenter<?>> {

    private static final String TAG = LoaderBridge.class.getSimpleName();

    private int loaderId;
    private final Context context;
    private final LoaderManager loaderManager;

    private boolean debug = false;

    /**
     * @param context  Current context (used later to create retain loader)
     * @param manager  LoaderManager in which retain loader will be stored
     * @param loaderId Identifier for retain loader inside LoaderManager
     */
    public LoaderBridge(@NonNull Context context, @NonNull LoaderManager manager, int loaderId) {
        this.context = context;
        this.loaderId = loaderId;
        this.loaderManager = manager;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public void retrievePresenter(
            @Nullable Bundle savedInstanceState,
            @NonNull final FactoryWithType<P> factory,
            @NonNull final OnPresenterProvidedListener<P> presenterListener) {

        Loader<P> loader = loaderManager.getLoader(loaderId);

        if (loader instanceof PresenterRetainLoader) {
            retrievePresenterFromExistingLoaderAndInformListener((PresenterRetainLoader) loader, factory.getTypeClazz(), presenterListener);
        } else {
            loaderManager.initLoader(loaderId, savedInstanceState, new LoaderManager.LoaderCallbacks<P>() {
                @Override
                public Loader<P> onCreateLoader(int id, Bundle args) {
                    if (args == null) {
                        logIfDebugMode("Presenter loader need to be created : with empty bundle");
                    } else {
                        logIfDebugMode("Presenter loader need to be recreated : with filled bundle");
                    }

                    return new PresenterRetainLoader<>(context, args, factory);
                }

                @Override
                public void onLoadFinished(Loader<P> loader, P presenter) {
                    logIfDebugMode("Presenter retrieved after creation");
                    presenterListener.onPresenterProvided(presenter);
                }

                @Override
                public void onLoaderReset(Loader<P> loader) {
                    // empty not used implementation
                }
            });
        }
    }

    protected void retrievePresenterFromExistingLoaderAndInformListener(
            @NonNull PresenterRetainLoader loader,
            @NonNull Class<? extends P> presenterClazz,
            @NonNull OnPresenterProvidedListener<P> presenterListener) {

        P presenter = retrievePresenterFromExistingLoader(loader, presenterClazz);
        if (presenter != null) {
            presenterListener.onPresenterProvided(presenter);
        } else {
            throw new IllegalStateException("Loader presenter not of expected type");
        }

        logIfDebugMode("Presenter retrieved from existing loader");
    }

    /**
     * Method get retained Presenter object from PresenterRetainLoader
     *
     * @param loader         Loader retaining presenter object
     * @param presenterClazz Type of presenter we are expecting to get
     * @return Presenter object retained in PresenterRetainLoader or null if presenter is not retained or
     * type of presenter is other than expected
     */
    @Nullable
    private P retrievePresenterFromExistingLoader(PresenterRetainLoader loader, Class<? extends P> presenterClazz) {
        Object presenter = loader.getPresenter();
        if (presenterClazz.isInstance(presenter)) {
            return presenterClazz.cast(presenter);
        } else {
            return null;
        }
    }

    private void logIfDebugMode(String message) {
        Log.d(TAG, message);
    }
}
