package com.jmr.amstradm4board

import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.io.IOException
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

class Xfer(
    private val ip: String
) {
    private val port = "443"

    private val retrofit = Retrofit.Builder()
        .baseUrl("http://$ip:$port/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val xferApi = retrofit.create(XferApi::class.java)

    private val client = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)) // Para depuración
        .build()

    private fun getUrl(path: String): String {
        return "http://$ip/$path"
    }

    private fun getHeaders(): Headers {
        return Headers.Builder().add("User-Agent", "cpcxfer").build()
    }

    // Funciones usando Retrofit
    suspend fun resetM4() {
        try {
            val response = xferApi.resetM4()
            if (response.isSuccessful) {
                println("M4 Reset")
            } else {
                println("Reset M4 failed: ${response.errorBody()?.string()}")
            }
        } catch (e: Exception) {
            println("Exception: $e")
        }
    }

    suspend fun resetCPC() {
        try {
            val response = xferApi.resetCPC()
            if (response.isSuccessful) {
                println("CPC Reset")
            } else {
                println("Reset CPC failed: ${response.errorBody()?.string()}")
            }
        } catch (e: Exception) {
            println("Exception: $e")
        }
    }

    suspend fun listFiles(folder: String): List<String>? {
        return try {
            val response = xferApi.listFiles(folder)
            if (response.isSuccessful) {
                response.body()
            } else {
                println("Failed to list files: ${response.errorBody()?.string()}")
                null
            }
        } catch (e: Exception) {
            println("Exception: $e")
            null
        }
    }

    fun uploadFile(filePath: String, destination: String) {
        val file = File(filePath)
        if (!file.exists()) {
            throw IllegalArgumentException("File does not exist")
        }

        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart(
                "upfile", file.name, RequestBody.create(
                    "application/octet-stream".toMediaTypeOrNull(),
                    file
                )
            )
            .build()

        val request = Request.Builder()
            .url(getUrl("upload.html"))
            .post(requestBody)
            .headers(getHeaders())
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                println("File upload failed")
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    println("File uploaded successfully")
                }
            }
        })
    }

    fun downloadFile(cpcFile: String) {
        val url = getUrl("sd/$cpcFile")
        val request = Request.Builder().url(url).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                println("Download failed")
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val downloadedFile = File(cpcFile)
                    downloadedFile.writeBytes(response.body!!.bytes())
                    println("File downloaded: ${downloadedFile.absolutePath}")
                }
            }
        })
    }

    fun executeProgram(cpcFile: String) {
        val url = HttpUrl.Builder()
            .scheme("http")
            .host(ip)
            .addPathSegment("config.cgi")
            .addQueryParameter("run2", cpcFile)
            .build()

        val request = Request.Builder()
            .url(url)
            .headers(getHeaders())
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                println("Execution failed")
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    println("Program executed: $cpcFile")
                }
            }
        })
    }

    fun makeDirectory(folder: String) {
        val dirPath = if (folder.startsWith("/")) folder else "/$folder"

        val url = HttpUrl.Builder()
            .scheme("http")
            .host(ip)
            .addPathSegment("config.cgi")
            .addQueryParameter("mkdir", dirPath)
            .build()

        val request = Request.Builder()
            .url(url)
            .headers(getHeaders())
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                println("Failed to create directory")
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    println("Directory created: $dirPath")
                }
            }
        })
    }
}
