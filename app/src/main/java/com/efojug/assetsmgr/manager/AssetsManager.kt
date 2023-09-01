package com.efojug.assetsmgr.manager

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import com.efojug.assetsmgr.util.ioScope
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class Expenses(
    val amount: Float,
    val type: Type,
    val remark: String = "",
    val date: Long = System.currentTimeMillis()
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

    fun addExpenses(expenses: Expenses) {
        val json = gson.toJson(expenses)

        ioScope.launch {
            dataStore.edit {
                it[ASSETS_SET_KEY] = (it[ASSETS_SET_KEY] ?: setOf()) + json
            }
        }
    }

    fun getAllExpenses(): Flow<List<Expenses>> =
        dataStore.data.map {
            val set = it[ASSETS_SET_KEY] ?: setOf()
            set.map { gson.fromJson(it, Expenses::class.java) }
        }

    fun getAllExpensesBlock(callback: (List<Expenses>) -> Unit) = ioScope.launch {
        dataStore.data.collect {
            val jsonSet = it[ASSETS_SET_KEY] ?: setOf()
            withContext(Dispatchers.Main) {
                callback(
                    jsonSet.map { gson.fromJson(it, Expenses::class.java) }
                )
            }
        }
    }
}