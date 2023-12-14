package com.efojug.assetsmgr.util

import com.efojug.assetsmgr.manager.Expense
import com.efojug.assetsmgr.manager.ExpenseManager
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.koin.core.context.GlobalContext

object ExpenseManagerJavaBridge {
    private val expenseManager = GlobalContext.get().get<ExpenseManager>()

    fun addExpensesAsync(expense: Expense) = ioScope.launch {
        expenseManager.addExpenses(expense)
    }

    fun getAllExpensesAsync(callback: (List<Expense>) -> Unit) = ioScope.launch {
        val expenseList = expenseManager.getAllExpense()
        runInUiThread {
            callback(expenseList)
        }
    }

    fun removeAllExpense() {
        runBlocking {
            expenseManager.removeAllExpense()
        }
    }
}