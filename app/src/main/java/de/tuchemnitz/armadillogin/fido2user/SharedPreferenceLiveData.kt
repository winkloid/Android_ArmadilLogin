/*
 * Copyright 2019 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.tuchemnitz.armadillogin.fido2user

import android.content.SharedPreferences
import androidx.lifecycle.LiveData

fun SharedPreferences.liveStringSet(key: String, defaultValue: Set<String>): LiveData<Set<String>> {
    return SharedPreferenceLiveData(this, key, defaultValue, SharedPreferences::getStringSet)
}

internal class SharedPreferenceLiveData<T>(
        private val prefs: SharedPreferences,
        private val key: String,
        private val defaultValue: T,
        private val getter: (SharedPreferences).(String, T) -> T?
) : LiveData<T>() {

    private val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
        if (key == this.key) {
            value = prefs.getter(key, defaultValue)
        }
    }

    override fun onActive() {
        super.onActive()
        value = prefs.getter(key, defaultValue)
        prefs.registerOnSharedPreferenceChangeListener(listener)
    }

    override fun onInactive() {
        super.onInactive()
        prefs.unregisterOnSharedPreferenceChangeListener(listener)
    }
}
