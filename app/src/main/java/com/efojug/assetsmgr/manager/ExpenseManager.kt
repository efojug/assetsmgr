package com.efojug.assetsmgr.manager

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import com.efojug.assetsmgr.util.ioScope
import com.efojug.assetsmgr.util.runInUiThread
import com.google.gson.Gson
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

data class Expense(
    val amount: Float,
    val type: Type,
    val remark: String = "",
    val date: Long = System.currentTimeMillis()
) {
    enum class Type(val chinese: String) {
        Income("收入"), Study("学习用品"), Life("生活用品"), Other("其他");

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
    private val TOTAL_AMOUNT_KEY = floatPreferencesKey("total_amount")
    private val ASSETS_SET_KEY = stringSetPreferencesKey("assets")
    private val gson = Gson()

    suspend fun getTotalAmount(): Float {
        return dataStore.data.first()[TOTAL_AMOUNT_KEY] ?: 0f
    }

    fun addExpenses(expense: Expense) {
        val json = gson.toJson(expense)

        ioScope.launch {
            dataStore.edit {
                it[ASSETS_SET_KEY] = (it[ASSETS_SET_KEY] ?: setOf()) + json
                it[TOTAL_AMOUNT_KEY] = (it[TOTAL_AMOUNT_KEY] ?: 0f) + expense.amount
            }
        }
    }

    fun removeExpenses(date: Long) {
        ioScope.launch {
            dataStore.edit { preferences ->
                preferences[ASSETS_SET_KEY]?.let {
                    var totalAmount = preferences[TOTAL_AMOUNT_KEY] ?: 0f
                    val newSet = it.filter {
                        val expense = gson.fromJson(it, Expense::class.java)
                        if (expense.date == date) {
                            totalAmount -= expense.amount
                        }
                        expense.date != date
                    }.toSet()
                    preferences[ASSETS_SET_KEY] = newSet
                    preferences[TOTAL_AMOUNT_KEY] = totalAmount.coerceAtLeast(0f)
                }
            }
        }
    }

    fun removeAllExpense() {
        ioScope.launch {
            dataStore.edit { preferences ->
                preferences[ASSETS_SET_KEY] = emptySet() // 将所有费用从 set 移除
                preferences[TOTAL_AMOUNT_KEY] = 0f // 将总金额设置为 0
            }
        }
    }

    suspend fun getAllExpense(): List<Expense> {
        val jsonList = dataStore.data.first()[ASSETS_SET_KEY] ?: setOf()
        return jsonList.map {
            gson.fromJson(it, Expense::class.java)
        }
    }

    fun getAllExpensesBlock(callback: (List<Expense>) -> Unit) = ioScope.launch {
        dataStore.data.collect { it ->
            val jsonSet = it[ASSETS_SET_KEY] ?: setOf()
            runInUiThread {
                callback(jsonSet.map { gson.fromJson(it, Expense::class.java) })
            }
        }
    }
}