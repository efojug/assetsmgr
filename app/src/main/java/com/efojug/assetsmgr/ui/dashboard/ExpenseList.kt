package com.efojug.assetsmgr.ui.dashboard

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.efojug.assetsmgr.manager.Expense
import com.efojug.assetsmgr.manager.ExpenseManager
import com.efojug.assetsmgr.ui.theme.AssetsManagerTheme
import org.koin.core.context.GlobalContext

@Composable
fun Test() {
    val expenseList = remember {
        mutableStateListOf<Expense>()
    }

    LaunchedEffect(null) {
        expenseList.addAll(GlobalContext.get().get<ExpenseManager>().getAllExpense())
    }

    Column(Modifier.fillMaxSize()) {
        LazyColumn {
            items(expenseList) {
                Card(Modifier.padding(4.dp)) {
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(2.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(Modifier.weight(1f)) {
                            Text(
                                text = it.type.chinese,
                                fontWeight = FontWeight.Bold,
                            )
                            Text(text = it.remark, fontSize = 10.sp)
                            Text(text = it.date.toString())
                        }
                        Text(text = it.amount.toString() + "元", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

fun DashboardFragment.bindView(composeView: ComposeView) {
    composeView.setContent {
        AssetsManagerTheme {
            Test()
        }
    }
}