package com.efojug.assetsmgr.manager

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import com.efojug.assetsmgr.util.ioScope
import com.efojug.assetsmgr.util.runInUiThread
import com.google.gson.Gson
import kotlinx.coroutines.launch

data class Expense(
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
                return values().firstOrNull { it.chinese == chinese }
            }
        }
    }
}

class ExpenseManager(
    private val dataStore: DataStore<Preferences>
) {
    private val ASSETS_SET_KEY = stringSetPreferencesKey("assets")
    private val gson = Gson()

    fun addExpenses(expense: Expense) {
        val json = gson.toJson(expense)

        ioScope.launch {
            dataStore.edit {
                it[ASSETS_SET_KEY] = (it[ASSETS_SET_KEY] ?: setOf()) + json
            }
        }
    }

    fun removeExpenses(date: Long) {
        ioScope.launch {
            dataStore.edit { preferences ->
                preferences[ASSETS_SET_KEY]?.let {
                    val newSet =
                        it.filter { gson.fromJson(it, Expense::class.java).date != date }.toSet()
                    preferences[ASSETS_SET_KEY] = newSet
                }
            }
        }
    }

    fun getAllExpensesBlock(callback: (List<Expense>) -> Unit) = ioScope.launch {
        dataStore.data.collect { it ->
            val jsonSet = it[ASSETS_SET_KEY] ?: setOf()
            runInUiThread {
                callback(
                    jsonSet.map { gson.fromJson(it, Expense::class.java) }
                )
            }
        }
    }
}