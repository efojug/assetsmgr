package com.efojug.assetsmgr.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

val ioScope = CoroutineScope(Dispatchers.IO)

suspend fun runInUiThread(block: suspend () -> Unit) = withContext(Dispatchers.Main) {
    block.invoke()
}