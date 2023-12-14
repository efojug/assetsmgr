package com.efojug.assetsmgr.manager

import com.efojug.assetsmgr.util.store.StoreHelper
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.transform

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
    storeHelper: StoreHelper
) {
    private val expensePreferenceItem = storeHelper.stringSet("expenses")
    private val gson = Gson()

    suspend fun getTotalAmount(): Float {
        return expensePreferenceItem.get().sumOf {
            val expense = gson.fromJson(it, Expense::class.java)
            expense.amount.toDouble()
        }.toFloat()
    }

    suspend fun addExpenses(expense: Expense) {
        val json = gson.toJson(expense)
        expensePreferenceItem.add(json)
    }

    suspend fun removeExpenses(date: Long) {
        val set = expensePreferenceItem.get()
        set.forEach {
            val expense = convertFromString(it)
            if (expense.date == date) {
                expensePreferenceItem.remove(it)
            }
        }
    }

    suspend fun removeAllExpense() {
        expensePreferenceItem.reset()
    }

    suspend fun getAllExpense(): List<Expense> {
        return expensePreferenceItem.get().map {
            gson.fromJson(it, Expense::class.java)
        }
    }

    fun getAllExpenseFlow(): Flow<List<Expense>> {
        return expensePreferenceItem.dataFlow.transform {
            emit(it?.map { convertFromString(it) } ?: emptyList())
        }
    }

    private fun convertFromString(str: String): Expense {
        return gson.fromJson(str, Expense::class.java)
    }
}