package com.efojug.assetsmgr.util

import com.efojug.assetsmgr.manager.Expense
import com.efojug.assetsmgr.manager.ExpenseManager
import kotlinx.coroutines.launch
import org.koin.core.context.GlobalContext

object ExpenseManagerJavaBridge {
    private val expenseManager = GlobalContext.get().get<ExpenseManager>()

    @JvmStatic
    fun addExpensesAsync(expense: Expense) = ioScope.launch {
        expenseManager.addExpenses(expense)
    }

    @JvmStatic
    fun getAllExpensesAsync(callback: (List<Expense>) -> Unit) = ioScope.launch {
        val expenseList = expenseManager.getAllExpense()
        runInUiThread {
            callback(expenseList)
        }
    }
}