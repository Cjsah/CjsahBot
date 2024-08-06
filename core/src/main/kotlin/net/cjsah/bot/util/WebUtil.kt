package net.cjsah.bot.util

import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URI
import kotlin.concurrent.thread

object WebUtil {
    @JvmStatic
    fun download(url: String, file: File) = thread(name = "BotDownloadService") {
        try {
            val huc = URI.create(url).toURL().openConnection() as HttpURLConnection
            huc.connect()
            huc.inputStream.use { input ->
                BufferedOutputStream(FileOutputStream(file)).use { output ->
                    input.copyTo(output)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}