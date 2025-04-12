package com.exaple.mediaexplorer.ui.models

import android.graphics.Bitmap
import com.exaple.mediaexplorer.ui.viewmodels.ExoPlayerViewModelClass
import com.exaple.mediaexplorer.ui.viewmodels.PdfViewModelClass
import com.exaple.mediaexplorer.ui.viewmodels.WeatherViewModelClass
import com.exaple.mediaexplorer.ui.viewmodels.WebViewModelClass

data class Media(
    val items: List<MediaExplorerItem> = listOf()
)

interface MediaExplorerItem {
    val name: String
    val data: String
    val type: MediaType
    val duration: Long
    val active: Boolean
}
data class None (
    override val name: String = "",
    override val data: String = "",
    override val type: MediaType = Type.None,
    override val duration: Long = 0L,
    override val active: Boolean = false
): MediaExplorerItem

data class AudioMixType(
    var name: String = "",
    var type: MediaType = Type.None,
    var data: String = ""
): MediaType

data class ImageExplorerItem(
    override val name: String = "",
    override val data: String = "",
    override val type: MediaType,
    override val duration: Long = 0L,
    override val active: Boolean = true,
    var bitmap: Bitmap? = null
): MediaExplorerItem

data class AudioExplorerItem(
    override val name: String = "",
    override val data: String = "",
    override val type: MediaType,
    override val duration: Long = 0L,
    override val active: Boolean = true,
    val contentType: AudioMixType,
    val viewModel: ExoPlayerViewModelClass = ExoPlayerViewModelClass(),
    var bitmap: Bitmap? = null,
    var byteArray: ByteArray? = null
): MediaExplorerItem {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AudioExplorerItem

        if (duration != other.duration) return false
        if (active != other.active) return false
        if (name != other.name) return false
        if (data != other.data) return false
        if (type != other.type) return false
        if (contentType != other.contentType) return false
        if (viewModel != other.viewModel) return false
        if (bitmap != other.bitmap) return false
        if (!byteArray.contentEquals(other.byteArray)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = duration.hashCode()
        result = 31 * result + active.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + data.hashCode()
        result = 31 * result + type.hashCode()
        result = 31 * result + contentType.hashCode()
        result = 31 * result + viewModel.hashCode()
        result = 31 * result + (bitmap?.hashCode() ?: 0)
        result = 31 * result + (byteArray?.contentHashCode() ?: 0)
        return result
    }
}

data class VideoExplorerItem(
    override val name: String = "",
    override val data: String = "",
    override val type: MediaType,
    override val duration: Long = 0L,
    override val active: Boolean = true,
    val viewModel: ExoPlayerViewModelClass = ExoPlayerViewModelClass()
): MediaExplorerItem

data class PdfExplorerItem(
    override val name: String = "",
    override val data: String = "",
    override val type: MediaType,
    override val duration: Long = 0L,
    override val active: Boolean = true,
    val viewModel: PdfViewModelClass = PdfViewModelClass()
): MediaExplorerItem

data class WebExplorerItem(
    override val name: String = "",
    override val data: String = "",
    override val type: MediaType,
    override val duration: Long = 0L,
    override val active: Boolean = true,
    val viewModel: WebViewModelClass = WebViewModelClass()
): MediaExplorerItem

data class WeatherExplorerItem(
    override val name: String = "",
    override val data: String = "",
    override val type: MediaType,
    override val duration: Long = 0L,
    override val active: Boolean = true,
    val viewModel: WeatherViewModelClass = WeatherViewModelClass()
): MediaExplorerItem

interface MediaType

sealed class Type {
    object None: MediaType
    object Image: MediaType
    object Gif: MediaType
    object AudioMix: MediaType
    object Audio: MediaType
    object Video: MediaType
    object Pdf: MediaType
    object Web: MediaType
    object Weather: MediaType
}