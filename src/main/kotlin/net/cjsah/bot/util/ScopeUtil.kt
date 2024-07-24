package net.cjsah.bot.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Job
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.launch
import java.util.concurrent.Executors
import kotlin.coroutines.EmptyCoroutineContext

object CoroutineScopeUtil {
    fun launch(block: suspend CoroutineScope.() -> Unit): Job {
        return CoroutineScope(Executors.newSingleThreadExecutor().asCoroutineDispatcher()).launch(EmptyCoroutineContext, CoroutineStart.DEFAULT, block)
    }

}