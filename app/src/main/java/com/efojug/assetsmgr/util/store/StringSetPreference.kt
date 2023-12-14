package com.efojug.assetsmgr.util.store

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences

class StringSetPreferenceItem(
    key: Preferences.Key<Set<String>>,
    defaultValue: Set<String>,
    dataStore: DataStore<Preferences>
) : PreferenceItem<Set<String>>(key, defaultValue, dataStore) {
    suspend fun add(value: String) {
        val set = get()
        set(set + value)
    }

    suspend fun remove(value: String) {
        set(get() - value)
    }
}