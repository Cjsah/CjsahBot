package net.cjsah.bot.util

import net.cjsah.bot.data.CoroutineData
import java.util.concurrent.Executors

object CoroutineScopeUtil {
    @JvmStatic
    fun newThread(): CoroutineData {
        val executor = Executors.newSingleThreadExecutor()
        return CoroutineData(executor)
    }
}