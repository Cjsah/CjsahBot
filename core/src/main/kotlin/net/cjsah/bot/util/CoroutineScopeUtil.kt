package net.cjsah.bot.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import net.cjsah.bot.data.CoroutineData
import java.util.concurrent.Executors
import kotlin.coroutines.EmptyCoroutineContext

object CoroutineScopeUtil {
    fun newThread(): CoroutineData {
        val executor = Executors.newSingleThreadExecutor()
        return CoroutineData(executor)
    }

    fun newScopeRun(block: suspend CoroutineScope.() -> Unit) {
        runBlocking {
            val thread = newThread()
            val job = thread.scope.launch(EmptyCoroutineContext, CoroutineStart.DEFAULT, block)
            job.join()
            thread.shutdown()
        }
    }
}