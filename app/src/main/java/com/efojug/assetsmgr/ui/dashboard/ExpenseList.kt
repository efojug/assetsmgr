package com.efojug.assetsmgr.ui.dashboard

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.dataStore
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.efojug.assetsmgr.Application
import com.efojug.assetsmgr.R
import com.efojug.assetsmgr.manager.Expense
import com.efojug.assetsmgr.manager.ExpenseManager
import com.efojug.assetsmgr.ui.theme.AssetsManagerTheme
import com.efojug.assetsmgr.util.extension.dataStore
import org.koin.core.context.GlobalContext
import org.koin.dsl.koinApplication
import org.koin.java.KoinJavaComponent.get
import java.text.SimpleDateFormat
import java.util.Date


@Composable
fun LottieWidget(
    spec: LottieCompositionSpec, text: String
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val composition by rememberLottieComposition(spec)
            val progress by animateLottieCompositionAsState(
                composition = composition,
                iterations = LottieConstants.IterateForever,
            )
            LottieAnimation(
                modifier = Modifier.size(200.dp),
                composition = composition,
                progress = { progress })
            Text(
                text = text, style = MaterialTheme.typography.titleLarge
            )
        }
    }
}

@Composable
fun Test() {
    val expenseList = remember {
        mutableStateListOf<Expense>()
    }

    val dataFormatter = remember {
        SimpleDateFormat("MM月dd日HH时mm分")
    }

    LaunchedEffect(Unit) {
        expenseList.addAll(GlobalContext.get().get<ExpenseManager>().getAllExpense())
    }

    Column(Modifier.fillMaxSize()) {
        when {
            expenseList.isEmpty() -> {
                LottieWidget(
                    spec = LottieCompositionSpec.RawRes(R.raw.empty), text = "空空如也"
                )
            }
            else -> {
                LazyColumn {
                    items(expenseList.reversed()) {
                        Card(
                            Modifier
                                .padding(4.dp)
                                .clickable {
                                    ExpenseManager(Application().dataStore).removeExpenses(it.date)
                                }) {
                            Row(
                                Modifier
                                    .fillMaxWidth()
                                    .padding(6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(Modifier.weight(1f)) {
                                    Text(
                                        text = it.type.chinese,
                                        fontWeight = FontWeight.Bold,
                                    )
                                    Text(text = it.remark, fontSize = 10.sp)
                                    Text(text = dataFormatter.format(Date(it.date)))
                                }
                                Text(
                                    //todo
                                    text = (if (it.type == Expense.Type.Income) "+" else "-") + it.amount.toString() + "元",
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

fun bindView(composeView: ComposeView) {
    composeView.setContent {
        AssetsManagerTheme {
            Test()
        }
    }
}