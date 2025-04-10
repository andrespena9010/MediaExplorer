package com.exaple.mediaexplorer.ui.models

import android.graphics.Bitmap
import com.exaple.mediaexplorer.ui.viewmodels.ExoPlayerViewModelClass
import com.exaple.mediaexplorer.ui.viewmodels.PdfViewModelClass
import com.exaple.mediaexplorer.ui.viewmodels.WeatherViewModelClass
import com.exaple.mediaexplorer.ui.viewmodels.WebViewModelClass

data class Media(
    val items: List<MediaExplorerItem> = listOf()
)

open class MediaExplorerItem(
    val name: String = "",
    val data: String = "",
    val type: MediaType,
    val duration: Long = 0L,
    var loaded: Boolean = false,
    var active: Boolean = false
)

open class AudioMixType(
    var name: String = "",
    var type: MediaType = Type.None,
    var data: String = ""
): MediaType

open class ImageExplorerItem(
    name: String,
    data: String,
    type: MediaType,
    duration: Long,
    var bitmap: Bitmap? = null
): MediaExplorerItem(
    name = name,
    data = data,
    type = type,
    duration = duration
)

open class AudioExplorerItem(
    name: String,
    data: String,
    type: MediaType,
    duration: Long,
    val contentType: AudioMixType,
    val viewModel: ExoPlayerViewModelClass = ExoPlayerViewModelClass(),
    var bitmap: Bitmap? = null,
    var byteArray: ByteArray? = null
): MediaExplorerItem(
    name = name,
    data = data,
    type = type,
    duration = duration
)

open class VideoExplorerItem(
    name: String,
    data: String,
    type: MediaType,
    duration: Long,
    val viewModel: ExoPlayerViewModelClass = ExoPlayerViewModelClass()
): MediaExplorerItem(
    name = name,
    data = data,
    type = type,
    duration = duration
)

open class PdfExplorerItem(
    name: String,
    data: String,
    type: MediaType,
    duration: Long,
    val viewModel: PdfViewModelClass = PdfViewModelClass()
): MediaExplorerItem(
    name = name,
    data = data,
    type = type,
    duration = duration
)

open class WebExplorerItem(
    name: String,
    data: String,
    type: MediaType,
    duration: Long,
    val viewModel: WebViewModelClass = WebViewModelClass()
): MediaExplorerItem(
    name = name,
    data = data,
    type = type,
    duration = duration
)

open class WeatherExplorerItem(
    name: String,
    data: String,
    type: MediaType,
    duration: Long,
    val viewModel: WeatherViewModelClass = WeatherViewModelClass()
): MediaExplorerItem(
    name = name,
    data = data,
    type = type,
    duration = duration
)

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