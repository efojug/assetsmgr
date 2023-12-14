package com.efojug.assetsmgr.util.store

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore

class StoreHelper(
    context: Context,
) {
    private val Context.dataStore by preferencesDataStore("store")
    private val dataStore = context.dataStore

    fun string(key: String, defaultValue: String = ""): PreferenceItem<String> {
        return PreferenceItem(stringPreferencesKey(key), defaultValue, dataStore)
    }

    fun stringSet(
        key: String,
        defaultValue: Set<String> = emptySet()
    ): StringSetPreferenceItem {
        return StringSetPreferenceItem(stringSetPreferencesKey(key), defaultValue, dataStore)
    }

    fun boolean(key: String, defaultValue: Boolean = false): PreferenceItem<Boolean> {
        return PreferenceItem(booleanPreferencesKey(key), defaultValue, dataStore)
    }

    fun int(key: String, defaultValue: Int = 0): PreferenceItem<Int> {
        return PreferenceItem(intPreferencesKey(key), defaultValue, dataStore)
    }

    fun float(key: String, defaultValue: Float = 0f): PreferenceItem<Float> {
        return PreferenceItem(floatPreferencesKey(key), defaultValue, dataStore)
    }
}