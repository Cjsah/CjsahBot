package net.cjsah.bot

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import net.cjsah.bot.util.CoroutineScopeUtil
import java.util.concurrent.Executors

class HeartBeatTimer(private var nextTime: Long, private val callback: suspend CoroutineScope.() -> Unit) {
    private var job: Job? = null
    private var taskCount = 0
    private val scope = CoroutineScope(Executors.newSingleThreadExecutor().asCoroutineDispatcher())

    init {
        startTimer()
    }

    private fun startTimer() {
        job = scope.launch {
            while (isActive) {
                delay(nextTime)
                task()
            }
        }
    }

    private fun task() {
        if (++taskCount >= 3) {
            stop()
            log.warn("连接已断开!")
            CoroutineScopeUtil.launch(callback)
        }
    }

    fun heart(intervalMillis: Long) {
        reset()
        nextTime = intervalMillis
        startTimer()
    }

    private fun reset() {
        job?.cancel()
        taskCount = 0
    }

    fun stop() {
        job?.cancel()
        scope.cancel()
    }
}