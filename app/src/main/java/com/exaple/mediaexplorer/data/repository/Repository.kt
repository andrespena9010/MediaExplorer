package com.exaple.mediaexplorer.data.repository

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import com.exaple.mediaexplorer.data.local.LocalData
import com.exaple.mediaexplorer.data.models.Forecast
import com.exaple.mediaexplorer.data.remote.RetrofitProvider
import java.io.File
import java.util.Locale

/**
 * Objeto singleton que actúa como repositorio para gestionar la descarga y almacenamiento de PDFs y bitmaps.
 */
object Repository {

    private val local = LocalData
    private val weatherApiRetrofit = RetrofitProvider.getWeatherApiService()

    fun init ( context: Context ){
        local.setFilesDir( context.filesDir )
    }

    suspend fun getForecastByCity( cityName: String, lang: Locale ): Forecast {
        return weatherApiRetrofit.getForecastByCity( cityName = cityName, lang = lang.language )
    }

    suspend fun getFile( name: String ): File? {
        return local.getFile( name )
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

    fun existBitmap(fileName: String): Uri? {
        return local.existBitmap(fileName)
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

    suspend fun loadBitmap( file: File ): Bitmap? {
        return local.loadBitmap( file )
    }

    suspend fun loadDrawable( file: File ): ByteArray? {
        return local.loadDrawable( file )
    }
}
