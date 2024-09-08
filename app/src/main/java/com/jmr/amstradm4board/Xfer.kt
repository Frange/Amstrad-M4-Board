package com.jmr.amstradm4board

import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import java.io.File
import java.io.IOException

class Xfer(private val ip: String) {

    private val client = OkHttpClient()

    private fun getUrl(path: String): String {
        return "http://$ip/$path"
    }

    private fun getHeaders(): Headers {
        return Headers.Builder().add("User-Agent", "cpcxfer").build()
    }

    fun resetM4() {
        val url = getUrl("config.cgi?mres")
        val request = Request.Builder()
            .url(url)
            .headers(getHeaders())
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                println("Reset M4 failed")
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    println("M4 Reset")
                }
            }
        })
    }

    fun resetCPC() {
        val url = getUrl("config.cgi?cres")
        val request = Request.Builder()
            .url(url)
            .headers(getHeaders())
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                println("Reset CPC failed")
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    println("CPC Reset")
                }
            }
        })
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

    fun listFiles(cpcFolder: String) {
        val url = HttpUrl.Builder()
            .scheme("http")
            .host(ip)
            .addPathSegment("config.cgi")
            .addQueryParameter("ls", cpcFolder)
            .build()

        val request = Request.Builder()
            .url(url)
            .headers(getHeaders())
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                println("Failed to list files")
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    println("Files in folder $cpcFolder: ${response.body?.string()}")
                }
            }
        })
    }
}
