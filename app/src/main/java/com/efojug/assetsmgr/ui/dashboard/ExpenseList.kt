package com.efojug.assetsmgr.ui.dashboard

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView

@Composable
fun Test() {
    Text(text = "SB")
}

fun DashboardFragment.bindView(composeView: ComposeView) {
    composeView.setContent {
        Test()
    }
}