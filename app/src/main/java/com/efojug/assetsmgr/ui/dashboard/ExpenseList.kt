package com.efojug.assetsmgr.ui.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FixedThreshold
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.rememberSwipeableState
import androidx.compose.material.swipeable
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.efojug.assetsmgr.R
import com.efojug.assetsmgr.manager.Expense
import com.efojug.assetsmgr.manager.ExpenseManager
import com.efojug.assetsmgr.ui.theme.AssetsManagerTheme
import kotlinx.coroutines.Dispatchers
import org.koin.core.context.GlobalContext
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
            LottieAnimation(modifier = Modifier.size(200.dp),
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
    val expenseManager = GlobalContext.get().get<ExpenseManager>()

    val expenseList by expenseManager.getAllExpenseFlow()
        .collectAsState(initial = SnapshotStateList(), context = Dispatchers.IO)

    val dataFormatter = remember {
        SimpleDateFormat("MM月dd日HH时mm分")
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
                    items(expenseList.reversed(), key = { it.date }) {
                        ExpenseItem(expense = it, dataFormatter = dataFormatter) {
                            expenseManager.removeExpenses(it.date)
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun ExpenseItem(
    expense: Expense, dataFormatter: SimpleDateFormat, onRemoveExpense: (Expense) -> Unit
) {
    val swipeableState = rememberSwipeableState(initialValue = false)
    val maxSlideValue = 280.dp

    Box(
        Modifier
            .padding(4.dp)
            .height(80.dp)
    ) {
        Box(modifier = Modifier
            .fillMaxHeight()
            .width(maxSlideValue)
            .background(Color.Red, CardDefaults.shape)
            .clickable { onRemoveExpense(expense) }
            .align(Alignment.CenterEnd)) {
            Icon(
                imageVector = Icons.Outlined.Delete,
                contentDescription = "delete expense",
                modifier = Modifier.align(Alignment.CenterEnd).padding(end = 16.dp)
            )
        }

        Card(
            Modifier
                .fillMaxSize()
                .offset { IntOffset(swipeableState.offset.value.toInt(), 0) }) {
            Box {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(6.dp)
                        .swipeable(swipeableState,
                            anchors = mapOf(0f to false, -1f * maxSlideValue.value to true),
                            orientation = Orientation.Horizontal,
                            thresholds = { from, to ->
                                if (from) {
                                    FixedThreshold(0.dp)
                                } else {
                                    FractionalThreshold(0.8f)
                                }
                            }), verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(Modifier.weight(1f)) {
                        Text(
                            text = expense.type.chinese,
                            fontWeight = FontWeight.Bold,
                        )
                        Text(text = expense.remark, fontSize = 10.sp)
                        Text(text = dataFormatter.format(Date(expense.date)))
                    }
                    Text(
                        text = (if (expense.type == Expense.Type.Income) "+" else "-") + expense.amount.toString() + "元",
                        fontWeight = FontWeight.Bold
                    )
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