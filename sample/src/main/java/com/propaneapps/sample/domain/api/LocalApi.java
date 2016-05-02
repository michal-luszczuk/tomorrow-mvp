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
package com.propaneapps.sample.domain.api;

import com.propaneapps.sample.domain.model.User;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;

public class LocalApi {

    public static final int THREE_SECONDS = 3000;
    public static final String BASE_GOOGLE_URL = "http://www.google.com";

    public static Observable<List<User>> fetchUsers() {
        return Observable.create(new Observable.OnSubscribe<List<User>>() {
            @Override
            public void call(Subscriber<? super List<User>> subscriber) {
                List<User> users = generateUsers();

                // calling google.com address to simulate connection error if there is no internet connection
                try {
                    URL url = new URL(BASE_GOOGLE_URL);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.getInputStream();
                } catch (IOException e) {
                    subscriber.onError(e);
                    return;
                }

                // slowing down this mock emulated call
                try {
                    Thread.sleep(THREE_SECONDS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                // posting result of fetch/generation
                subscriber.onNext(users);
            }
        });
    }

    private static List<User> generateUsers() {
        List<User> users = new ArrayList<>();

        users.add(new User("Superuser"));
        users.add(new User("John"));
        users.add(new User("Michael"));
        users.add(new User("root"));
        users.add(new User("Olivier"));
        users.add(new User("Harry"));
        users.add(new User("James"));
        users.add(new User("Thomas"));
        users.add(new User("Sam"));
        users.add(new User("Adam"));
        users.add(new User("Dylan"));
        users.add(new User("Logan"));
        return users;
    }
}
