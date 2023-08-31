package com.efojug.assetsmgr.manager

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import com.efojug.assetsmgr.util.ioScope
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

data class Assets(
    val remark: String, val amount: Float, val date: Long = System.currentTimeMillis()
)

class AssetsManager(
    private val dataStore: DataStore<Preferences>
) {
    private val ASSETS_SET_KEY = stringSetPreferencesKey("assets")
    private val gson = Gson()

    fun addExpenses(remark: String, amount: Float) {
        val json = gson.toJson(Assets(remark, amount))

        ioScope.launch {
            dataStore.edit {
                it[ASSETS_SET_KEY] = it[ASSETS_SET_KEY]?.plus(json) ?: setOf(json)
            }
        }
    }

    fun getAllExpenses(): Flow<List<Assets>> =
        dataStore.data
            .map {
                val set = it[ASSETS_SET_KEY] ?: setOf()
                set.map {
                    gson.fromJson(it, Assets::class.java)
                }
            }
}