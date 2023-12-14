package com.efojug.assetsmgr.util.store

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

open class PreferenceItem<T>(
    private val key: Preferences.Key<T>,
    private val defaultValue: T,
    private val dataStore: DataStore<Preferences>
) {
    val dataFlow = dataStore.data.map { it[key] }

    suspend fun get(): T {
        return dataFlow.first() ?: defaultValue
    }

    suspend fun set(value: T) {
        dataStore.edit {
            it[key] = value
        }
    }

    suspend fun onChange(block: (T) -> Unit) {
        dataFlow.collect {
            if (it != null) {
                block(it)
            }
        }
    }

    suspend fun reset() {
        set(defaultValue)
    }
}