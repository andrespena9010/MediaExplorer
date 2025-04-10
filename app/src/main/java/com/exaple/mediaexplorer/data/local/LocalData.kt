package com.exaple.mediaexplorer.data.local

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import com.exaple.mediaexplorer.data.models.SaveResource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import android.util.Log
import androidx.core.net.toUri
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.io.FileOutputStream
import java.io.IOException
import java.io.PrintStream

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

    /**
     * Guarda un PDF en el almacenamiento local.
     *
     * @param name Nombre del archivo PDF.
     * @param data Datos del PDF en bytes.
     * @return Respuesta con el resultado de la operación.
     */
    suspend fun saveResource(
        name: String,
        data: ByteArray
    ): SaveResource {
        val res = SaveResource()
        val resource = File(resourcesDir, name)
        if (resource.exists()) {
            res.success = false
            res.message = "The Resource is already exists."
            res.uri = resource.toUri()
        } else {
            withContext(Dispatchers.IO) {
                try {
                    if (resource.createNewFile()) {
                        resource.writeBytes(data)
                        res.success = true
                        res.message = "Resource created."
                        res.uri = resource.toUri()
                    } else {
                        res.success = false
                        res.message = "Error creating the Resource."
                    }
                } catch (e: Exception) {
                    res.success = false
                    res.message = "Error"
                    res.exceptions.add(e)
                    val err = ""
                    e.printStackTrace(PrintStream(err))
                    Log.e("LocalData.savePDF() -> ", err)
                }
            }
        }
        return res
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
}