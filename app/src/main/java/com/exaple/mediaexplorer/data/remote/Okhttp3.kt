package com.exaple.mediaexplorer.data.remote

import android.util.Log
import com.exaple.mediaexplorer.data.models.GetResourceResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.*
import java.io.PrintWriter
import java.io.StringWriter

object Okhttp3 {

    private val client = OkHttpClient()

    suspend fun getResource(url: String ): GetResourceResponse {

        val request = Request.Builder().url( url ).build()
        val res = GetResourceResponse()

        withContext( Dispatchers.IO ){
            try {
                client.newCall( request ).execute().use { response ->
                    if ( response.isSuccessful ) {
                        if ( response.body != null ) {
                            res.data = response.body!!.bytes()
                            res.success = true
                        } else {
                            res.success = false
                            res.message = "Error on network."
                        }
                    }
                }
            } catch ( e: Exception ){
                res.success = false
                res.message = "Error"
                res.exceptions.add( e )
                val sw = StringWriter()
                e.printStackTrace(PrintWriter(sw))
                val errorStackTrace = sw.toString()
                Log.e("Okhttp3.getPDF()", errorStackTrace)
            }
        }

        return res
    }

}