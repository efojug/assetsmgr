package com.efojug.assetsmgr.util

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class FlowReceiver<T>(
    private val flow: Flow<T>,
    dispatcher: CoroutineDispatcher,
    private val onReceive: (T) -> Unit
) {
    private val scope = CoroutineScope(dispatcher)
    private val job = scope.launch {
        flow
            .flowOn(dispatcher)
            .collect {
                onReceive(it)
            }
    }

    fun stop() {
        job.cancel()
    }
}