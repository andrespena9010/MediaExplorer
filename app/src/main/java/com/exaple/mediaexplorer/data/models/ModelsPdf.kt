package com.exaple.mediaexplorer.data.models

import android.graphics.Bitmap
import android.net.Uri

data class PdfPage(
    val bitmap: Bitmap?,
    val pageLoading: Boolean,
    val cachedBitmap: Boolean
)

data class SaveResource (
    var success: Boolean = false,
    var message: String = "",
    var uri: Uri? = null,
    val exceptions: MutableList<Throwable> = mutableListOf()
)

data class GetResourceResponse (
    var success: Boolean = false,
    var message: String = "",
    var data: ByteArray = byteArrayOf(),
    val exceptions: MutableList<Throwable> = mutableListOf()
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as GetResourceResponse

        return data.contentEquals(other.data)
    }

    override fun hashCode(): Int {
        return data.contentHashCode()
    }
}

data class LoadResponse (
    val getResourceResponse: GetResourceResponse = GetResourceResponse(),
    val saveResource: SaveResource = SaveResource()
)