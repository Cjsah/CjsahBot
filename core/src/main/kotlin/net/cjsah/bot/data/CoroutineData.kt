package net.cjsah.bot.data

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.cancel
import java.util.concurrent.ExecutorService

data class CoroutineData(val executor: ExecutorService) {
    val scope = CoroutineScope(executor.asCoroutineDispatcher())

    fun shutdown() {
        scope.cancel()
        executor.shutdown()
    }
}
