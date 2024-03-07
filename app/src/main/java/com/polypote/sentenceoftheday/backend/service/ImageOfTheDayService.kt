package com.polypote.sentenceoftheday.backend.service

import android.app.Application
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.apache.hc.client5.http.classic.methods.HttpGet
import org.apache.hc.client5.http.impl.classic.HttpClients
import org.apache.hc.core5.http.ClassicHttpResponse
import org.apache.hc.core5.http.io.HttpClientResponseHandler
import org.apache.hc.core5.http.io.entity.EntityUtils
import org.apache.hc.core5.net.URIBuilder
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.net.URL
import java.nio.charset.StandardCharsets

class ImageOfTheDayService(private val application: Application)  {

    fun switchImages(){
        val filesDir = application.applicationContext.filesDir
        val inputFile = File(filesDir, tomorrowImageName)
        if(inputFile.exists()){
            val fileInputStream = inputFile.inputStream()
            val fileOutputStream = File(filesDir, todayImageName).outputStream()
            copyInputToOutput(fileInputStream, fileOutputStream)
        }
    }
    fun getImageOfTheDay() : String {
        val file = File(application.applicationContext.filesDir, todayImageName)
        return if(file.exists()) file.path else ""
    }
    suspend fun fetchImageFromUnsplash() {
        withContext(Dispatchers.IO) {
            val api_key = application.applicationContext.assets.open("key").bufferedReader().use { it.readText() }
            val uri = URIBuilder("https://api.unsplash.com/photos/random/")
                .addParameter("topics", "6sMVjTLSkeQ")
                .addParameter("client_id", api_key)
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
                    val jsonObject = JSONObject(it)
                    val downloadLink = jsonObject.getJSONObject("links").getString("download")
                    fetchImage(downloadLink)
                }
            }
        }
    }
    private fun fetchImage(downloadLink : String){
        val outputFileName = tomorrowImageName

        try {
            val url = URL(downloadLink)
            val connection = url.openConnection()
            connection.connect()
            val inputStream = connection.getInputStream()

            val file = File(application.applicationContext.filesDir, outputFileName)
            val fileOutputStream = FileOutputStream(file)

            copyInputToOutput(inputStream, fileOutputStream)
            Log.d("Image of the day service", "Image for tomorrow correctly downloaded")
        }catch (e : Exception){
            e.printStackTrace()
        }
    }

    private fun copyInputToOutput(inputStream: InputStream, fileOutputStream: FileOutputStream) {
        inputStream.use { input ->
            fileOutputStream.use { output ->
                input.copyTo(output)
            }
        }
    }

    companion object {
        private const val tomorrowImageName : String = "tomorrow_bg.jpg"
        private const val todayImageName : String = "today_bg.jpg"
    }
}