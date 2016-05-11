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
package com.propaneapps.tomorrow.base;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.propaneapps.tomorrow.common.FactoryWithType;
import com.propaneapps.tomorrow.loader.LoaderBridge;
import com.propaneapps.tomorrow.loader.OnPresenterProvidedListener;
import com.propaneapps.tomorrow.presenter.Presenter;

/**
 * Base MVP presenter retain with loader activity (ActionBarActivity)
 *
 * @param <V> Type of view
 * @param <P> Type of presenter
 */
public abstract class BasePresenterActionBarActivity<V, P extends Presenter<V>> extends ActionBarActivity implements OnPresenterProvidedListener<P> {

    private static final int DEFAULT_BASE_LOADER_ID = 20000;
    private P presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new LoaderBridge<P>(this, getSupportLoaderManager(), DEFAULT_BASE_LOADER_ID)
                .retrievePresenter(savedInstanceState, getPresenterFactory(), this);
    }

    public abstract FactoryWithType<P> getPresenterFactory();

    @Override
    public void onPresenterProvided(P presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.bindView(getViewLayer());
    }

    public abstract V getViewLayer();

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        presenter.onSaveInstanceState(outState);
    }

    @Override
    public void onStop() {
        super.onStop();
        presenter.unbindView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isFinishing()) {
            presenter.onDestroy();
        }
    }
}
