package net.cjsah.bot.util

import com.alibaba.fastjson2.JSONObject
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.request
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import io.ktor.http.contentType
import net.cjsah.bot.client

object RequestUtil {
    private val baseUrl = ""

    suspend fun get(url: String): JSONObject {
        return this.resolve(url, HttpMethod.Get)
    }

    suspend fun post(url: String, data: JSONObject): JSONObject {
        return this.resolve(url, HttpMethod.Get, data)
    }

    suspend fun resolve(url: String, requestMethod: HttpMethod, body: JSONObject? = null): JSONObject {
        val res = client.request(baseUrl + url) {
            method = requestMethod
            contentType(ContentType.Application.Json)
            if (body != null) {
                setBody(body)
            }
        }

        val body = res.body<String>()
        return JsonUtil.deserialize(body)
    }
}