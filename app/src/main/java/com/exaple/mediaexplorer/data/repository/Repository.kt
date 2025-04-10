package com.exaple.mediaexplorer.data.repository

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import com.exaple.mediaexplorer.data.local.LocalData
import com.exaple.mediaexplorer.data.models.Forecast
import com.exaple.mediaexplorer.data.models.GetResourceResponse
import com.exaple.mediaexplorer.data.models.SaveResource
import com.exaple.mediaexplorer.data.models.LoadResponse
import com.exaple.mediaexplorer.data.remote.Okhttp3
import com.exaple.mediaexplorer.data.remote.RetrofitProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import java.io.PrintWriter
import java.io.StringWriter
import java.util.Locale

/**
 * Objeto singleton que actúa como repositorio para gestionar la descarga y almacenamiento de PDFs y bitmaps.
 */
object Repository {

    private val supervisor = SupervisorJob()

    /**
     * Ámbito de corrutina para ejecutar tareas asincrónicas.
     */
    @OptIn(DelicateCoroutinesApi::class)
    private val scope = CoroutineScope(GlobalScope.coroutineContext + supervisor)

    private val web = Okhttp3
    private val local = LocalData
    private val weatherApiRetrofit = RetrofitProvider.getWeatherApiService()

    fun init ( context: Context ){
        local.setFilesDir( context.filesDir )
    }

    suspend fun getForecastByCity( cityName: String, lang: Locale ): Forecast {
        return weatherApiRetrofit.getForecastByCity( cityName = cityName, lang = lang.language )
    }

    /**
     * Descarga un documento PDF desde una URL y lo guarda localmente.
     *
     * @param url URL del documento PDF a descargar.
     * @param fileName Nombre del archivo PDF a guardar.
     * @return Respuesta con el resultado de la operación.
     */
    suspend fun downLoadResource( url: String, fileName: String ): LoadResponse {
        if ( exist( fileName ) == null ){
            var getResponse = GetResourceResponse()
            val deferredWeb = scope.async {
                getResponse = web.getResource(url)
            }
            deferredWeb.join()

            var saveResponse = SaveResource()
            val deferredLocal = scope.async {
                if (getResponse.success) {
                    saveResponse = local.saveResource(fileName, getResponse.data)
                    getResponse.data = byteArrayOf()
                }
            }
            deferredLocal.join()

            try {
                deferredWeb.await()
                deferredLocal.await()
            } catch (e: Exception) {
                val sw = StringWriter()
                e.printStackTrace(PrintWriter(sw))
                val errorStackTrace = sw.toString()
                Log.e("Repository.setUri()", errorStackTrace)
            }

            return LoadResponse(getResourceResponse = getResponse, saveResource = saveResponse)
        }
        return LoadResponse(
            getResourceResponse = GetResourceResponse(
                success = true,
                message = "the file is already exist"
            ),
            saveResource = SaveResource(
                success = true,
                message = "the file is already exist"
            )
        )
    }

    /**
     * Verifica si un archivo PDF existe en el almacenamiento local.
     *
     * @param fileName Nombre del archivo PDF.
     * @return Uri del archivo si existe, null en caso contrario.
     */
    fun exist(fileName: String): Uri? {
        return local.exist(fileName)
    }

    /**
     * Guarda un bitmap en la caché local.
     *
     * @param bitmap Bitmap a guardar.
     * @param bitmapName Nombre del archivo bitmap.
     */
    suspend fun saveCacheBitmap(bitmap: Bitmap, bitmapName: String) {
        local.saveCacheBitmap(bitmap, bitmapName)
    }

    /**
     * Carga un bitmap desde la caché local.
     *
     * @param bitmapName Nombre del archivo bitmap.
     * @return Bitmap cargado, o null si no existe.
     */
    suspend fun loadCacheBitmap(bitmapName: String): Bitmap? {
        return local.loadCacheBitmap(bitmapName)
    }
}
