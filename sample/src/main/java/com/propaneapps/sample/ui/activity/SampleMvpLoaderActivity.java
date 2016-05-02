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
package com.propaneapps.sample.ui.activity;

import android.os.Bundle;

import com.propaneapps.sample.R;
import com.propaneapps.sample.domain.model.User;
import com.propaneapps.sample.presenter.SampleDownloadTaskPresenter;
import com.propaneapps.sample.ui.BaseSampleDownloadResultView;
import com.propaneapps.sample.ui.SampleDownloadResultView;
import com.propaneapps.tomorrow.base.BasePresenterActivity;
import com.propaneapps.tomorrow.common.FactoryWithType;

import java.util.List;

public class SampleMvpLoaderActivity extends BasePresenterActivity<SampleDownloadResultView<List<User>>, SampleDownloadTaskPresenter> {

    private SampleDownloadResultView<List<User>> view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample_mvp_loader);
        view = new BaseSampleDownloadResultView(findViewById(android.R.id.content));
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
