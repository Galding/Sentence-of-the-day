package com.polypote.sentenceoftheday.backend.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import org.apache.hc.client5.http.async.methods.SimpleHttpRequest
import org.apache.hc.client5.http.async.methods.SimpleRequestBuilder
import org.apache.hc.client5.http.classic.HttpClient
import org.apache.hc.client5.http.classic.methods.HttpGet
import org.apache.hc.client5.http.impl.classic.HttpClients
import org.apache.hc.core5.http.ClassicHttpResponse
import org.apache.hc.core5.http.io.HttpClientResponseHandler
import org.apache.hc.core5.http.io.entity.EntityUtils
import org.apache.hc.core5.http.io.entity.StringEntity
import org.apache.hc.core5.http.protocol.HttpCoreContext
import org.apache.hc.core5.net.URIBuilder
import java.nio.charset.StandardCharsets

class ImageOfTheDayService : Service() {
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        fetchImageFromUnsplash();
        return START_NOT_STICKY
    }

    private fun fetchImageFromUnsplash() {
        val uri = URIBuilder("https://api.unsplash.com/photos/random/")
            .addParameter("topic", "6sMVjTLSkeQ")
            .addParameter("client_id", "zaeze")
            .build()
        val httpGet = HttpGet(uri)

        HttpClients.createDefault().use { it ->
            it.execute(httpGet, object : HttpClientResponseHandler<String> {
                override fun handleResponse(response: ClassicHttpResponse): String {
                    val status = response.code
                    if (status >= 200 && status < 300) {
                        val entity = response.entity
                        return entity?.let { EntityUtils.toString(it, StandardCharsets.UTF_8) } ?: ""
                    } else {
                        throw IllegalStateException("Unexpected response status: $status")
                    }
                }
            }).also {
                println("response body $it")
            }

        }
    }

}