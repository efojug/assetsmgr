package com.efojug.assetsmgr.manager

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import com.efojug.assetsmgr.util.ioScope
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

data class Assets(
    val amount: Float, val type: Type, val remark: String = "", val date: Long = System.currentTimeMillis()
) {
    enum class Type(val chinese: String) {
        SchoolSupplies("学习用品"),
        Food("伙食"),
        Other("其他");

        companion object {
            @JvmStatic
            fun fromChinese(chinese: String): Type? {
                return entries.firstOrNull { it.chinese == chinese }
            }
        }
    }
}

class AssetsManager(
    private val dataStore: DataStore<Preferences>
) {
    private val ASSETS_SET_KEY = stringSetPreferencesKey("assets")
    private val gson = Gson()

    fun addExpenses(assets: Assets) {
        val json = gson.toJson(assets)

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
                set.map { gson.fromJson(it, Assets::class.java) }
            }

    fun getAllExpensesBlock(): List<Assets> = runBlocking {
        val tempList = mutableListOf<Assets>()
        getAllExpenses().collect {
             tempList.addAll(it)
        }
        tempList
    }
}