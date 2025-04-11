package com.exaple.mediaexplorer.data.local

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import java.io.File
import androidx.core.net.toUri
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.io.FileOutputStream
import java.io.IOException

/**
 * Objeto singleton para gestionar datos locales de PDFs y bitmaps.
 */
object LocalData {

    private lateinit var filesDir: File
    private lateinit var resourcesDir: File
    private lateinit var bitmapDir: File

    private val mutexMap = mutableMapOf<String, Mutex>()

    /**
     * Establece el directorio de archivos para almacenar PDFs y bitmaps.
     *
     * @param filesDir Directorio de archivos.
     */
    fun setFilesDir(filesDir: File) {
        this.filesDir = filesDir
        resourcesDir = File(LocalData.filesDir, "resources")
        bitmapDir = File(LocalData.filesDir, "bitmaps")
        if (!resourcesDir.exists()) resourcesDir.mkdir()
        if (!bitmapDir.exists()) bitmapDir.mkdir()
    }

    suspend fun getFile( name: String ): File? {
        mutexMap.getOrPut( name ) { Mutex() }.withLock {
            val file = File( resourcesDir, name )
            return if (file.exists()) {
                file
            } else {
                null
            }
        }
    }

    /**
     * Verifica si un archivo PDF existe en el almacenamiento local.
     *
     * @param fileName Nombre del archivo PDF.
     * @return Uri del archivo si existe, null en caso contrario.
     */
    fun exist(fileName: String): Uri? {
        val file = File(resourcesDir, fileName)
        return if (file.exists()) file.toUri() else null
    }

    fun existBitmap(fileName: String): Uri? {
        val file = File(bitmapDir, fileName)
        return if (file.exists()) file.toUri() else null
    }

    /**
     * Guarda un bitmap en la caché local.
     *
     * @param bitmap Bitmap a guardar.
     * @param bitmapName Nombre del archivo bitmap.
     */
    suspend fun saveCacheBitmap(bitmap: Bitmap, bitmapName: String) {
        mutexMap.getOrPut(bitmapName) { Mutex() }.withLock {
            val file = File(bitmapDir, bitmapName)
            var fileOutputStream: FileOutputStream? = null
            try {
                fileOutputStream = FileOutputStream(file)
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)
            } catch (e: IOException) {
                e.printStackTrace()
            } finally {
                try {
                    fileOutputStream?.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }

    /**
     * Carga un bitmap desde la caché local.
     *
     * @param bitmapName Nombre del archivo bitmap.
     * @return Bitmap cargado, o null si no existe.
     */
    suspend fun loadCacheBitmap(bitmapName: String): Bitmap? {
        mutexMap.getOrPut(bitmapName) { Mutex() }.withLock {
            val file = File(bitmapDir, bitmapName)
            return if (file.exists()) {
                BitmapFactory.decodeFile(file.absolutePath)
            } else {
                null
            }
        }
    }

    suspend fun loadBitmap( file: File ): Bitmap? {
        mutexMap.getOrPut( file.name ) { Mutex() }.withLock {
            return if (file.exists()) {
                BitmapFactory.decodeFile(file.absolutePath)
            } else {
                null
            }
        }
    }

    suspend fun loadDrawable( file: File ): ByteArray? {
        mutexMap.getOrPut( file.name ) { Mutex() }.withLock {
            return if (file.exists()) {
                file.readBytes()
            } else {
                null
            }
        }
    }
}