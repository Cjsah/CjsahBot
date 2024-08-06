package net.cjsah.bot.util

import cn.hutool.core.util.IdUtil
import net.cjsah.bot.FilePaths
import java.io.File

object MsgLogUtil {
    @JvmStatic
    fun saveImage(url: String): String {
        val now = DateUtil.now()
        val name = "${DateUtil.format(now, "HH:mm:ss")}-${IdUtil.fastSimpleUUID()}.png"
        var file = FilePaths.IMAGE_LOGS.resolve(DateUtil.format(now, "yyyy-MM-dd")).toFile()
        if (!file.exists() || !file.isDirectory) {
            file.mkdirs()
        }
        file = File(file, name)
        WebUtil.download(url, file)
        return name
    }
}