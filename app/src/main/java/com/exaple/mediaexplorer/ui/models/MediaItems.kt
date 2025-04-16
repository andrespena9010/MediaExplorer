package com.exaple.mediaexplorer.ui.models

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.core.graphics.createBitmap
import androidx.media3.common.MediaItem
import com.exaple.mediaexplorer.ui.viewmodels.ExoPlayerViewModelClass
import com.exaple.mediaexplorer.ui.viewmodels.PdfViewModelClass
import com.exaple.mediaexplorer.ui.viewmodels.WeatherViewModelClass
import com.exaple.mediaexplorer.ui.viewmodels.WebViewModelClass
import java.io.File

interface MediaExplorerItem {
    val name: String
    val data: String
    val type: MediaType
    val duration: Long
    val active: Boolean
    var load: Boolean

    fun load(
        bitmap: Bitmap?
    )

    fun load(
        weatherSearch: String
    )

    fun load(
        file: File
    )

    fun load(
        url: String,
        context: Context
    )

    fun load(
        uriMedia: Uri,
        context: Context
    )

    fun load(
        bitmap: Bitmap?,
        uriMedia: Uri,
        context: Context
    )

    fun load(
        byteArray: ByteArray?,
        uriMedia: Uri,
        context: Context
    )
    fun dispose()
}

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
    override val active: Boolean = false,
    override var load: Boolean = false,
    private var bitmap: Bitmap? = null
): MediaExplorerItem {
    override fun load(uriMedia: Uri, context: Context) {}
    override fun load(bitmap: Bitmap?, uriMedia: Uri, context: Context) {}
    override fun load(byteArray: ByteArray?, uriMedia: Uri, context: Context) {}
    override fun load(weatherSearch: String) {}
    override fun load(file: File) {}
    override fun load(url: String, context: Context) {}

    override fun load( bitmap: Bitmap? ) {
        this.bitmap = bitmap
        this.load = true
    }

    override fun dispose() {
        this.load = false
        this.bitmap = null
    }

    fun getSaveBitmap(): Bitmap {
        return bitmap ?: createBitmap(200,200)
    }
}

data class AudioExplorerItem(
    override val name: String = "",
    override val data: String = "",
    override val type: MediaType,
    override val duration: Long = 0L,
    override val active: Boolean = false,
    val contentType: AudioMixType,
    val viewModel: ExoPlayerViewModelClass = ExoPlayerViewModelClass(),
    var bitmap: Bitmap? = null,
    var byteArray: ByteArray? = null,
    override var load: Boolean = false
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

    override fun load(bitmap: Bitmap?) {}
    override fun load(weatherSearch: String) {}
    override fun load(file: File) {}
    override fun load(url: String, context: Context) {}

    override fun load(
        uriMedia: Uri,
        context: Context
    ) {
        this.viewModel.init(
            mediaItems = listOf(
                MediaItem.fromUri(
                    uriMedia
                )
            ),
            context = context,
            duration = duration
        )
        this.load = true
    }

    override fun load(
        bitmap: Bitmap?,
        uriMedia: Uri,
        context: Context
    ) {
        this.bitmap = bitmap
        this.viewModel.init(
            mediaItems = listOf(
                MediaItem.fromUri(
                    uriMedia
                )
            ),
            context = context,
            duration = duration
        )
        this.load = true
    }

    override fun load(
        byteArray: ByteArray?,
        uriMedia: Uri,
        context: Context
    ) {
        this.byteArray = byteArray
        this.viewModel.init(
            mediaItems = listOf(
                MediaItem.fromUri(
                    uriMedia
                )
            ),
            context = context,
            duration = duration
        )
        this.load = true
    }

    override fun dispose() {
        this.load = false
        this.bitmap = null
        this.byteArray = null
        viewModel.dispose()
    }

    fun getSaveBitmap(): Bitmap {
        return bitmap ?: createBitmap(200,200)
    }
}

data class VideoExplorerItem(
    override val name: String = "",
    override val data: String = "",
    override val type: MediaType,
    override val duration: Long = 0L,
    override val active: Boolean = false,
    val viewModel: ExoPlayerViewModelClass = ExoPlayerViewModelClass(),
    override var load: Boolean = false
): MediaExplorerItem {
    override fun load(bitmap: Bitmap?) {}
    override fun load(weatherSearch: String) {}
    override fun load(file: File) {}
    override fun load(url: String, context: Context) {}
    override fun load(bitmap: Bitmap?, uriMedia: Uri, context: Context) {}
    override fun load(byteArray: ByteArray?, uriMedia: Uri, context: Context) {}

    override fun load(
        uriMedia: Uri,
        context: Context
    ) {
        this.viewModel.init(
            mediaItems = listOf(
                MediaItem.fromUri(
                    uriMedia
                )
            ),
            context = context,
            duration = duration
        )
        this.load = true
    }

    override fun dispose() {
        this.load = false
        this.viewModel.dispose()
    }
}

data class PdfExplorerItem(
    override val name: String = "",
    override val data: String = "",
    override val type: MediaType,
    override val duration: Long = 0L,
    override val active: Boolean = false,
    val viewModel: PdfViewModelClass = PdfViewModelClass(),
    override var load: Boolean = false
): MediaExplorerItem {
    override fun load(bitmap: Bitmap?) {}
    override fun load(weatherSearch: String) {}
    override fun load(url: String, context: Context) {}
    override fun load(uriMedia: Uri, context: Context) {}
    override fun load(bitmap: Bitmap?, uriMedia: Uri, context: Context) {}
    override fun load(byteArray: ByteArray?, uriMedia: Uri, context: Context) {}

    override fun load(file: File) {
        this.viewModel.setSelectedPDF(
            pdf = file
        )
        this.load = true
    }

    override fun dispose() {
        this.load = false
        this.viewModel.dispose()
    }
}

data class WebExplorerItem(
    override val name: String = "",
    override val data: String = "",
    override val type: MediaType,
    override val duration: Long = 0L,
    override val active: Boolean = false,
    val viewModel: WebViewModelClass = WebViewModelClass(),
    override var load: Boolean = false
): MediaExplorerItem {
    override fun load(bitmap: Bitmap?) {}
    override fun load(weatherSearch: String) {}
    override fun load(file: File) {}
    override fun load(uriMedia: Uri, context: Context) {}
    override fun load(bitmap: Bitmap?, uriMedia: Uri, context: Context) {}
    override fun load(byteArray: ByteArray?, uriMedia: Uri, context: Context) {}

    override fun load(
        url: String,
        context: Context
    ) {
        this.viewModel.load(
            context = context,
            url = url
        )
        this.load = true
    }

    override fun dispose() {
        this.load = false
        this.viewModel.dispose()
    }
}

data class WeatherExplorerItem(
    override val name: String = "",
    override val data: String = "",
    override val type: MediaType,
    override val duration: Long = 0L,
    override val active: Boolean = false,
    val viewModel: WeatherViewModelClass = WeatherViewModelClass(),
    override var load: Boolean = false
): MediaExplorerItem {
    override fun load(bitmap: Bitmap?) {}
    override fun load(file: File) {}
    override fun load(url: String, context: Context) {}
    override fun load(uriMedia: Uri, context: Context) {}
    override fun load(bitmap: Bitmap?, uriMedia: Uri, context: Context) {}
    override fun load(byteArray: ByteArray?, uriMedia: Uri, context: Context) {}

    override fun load(
        weatherSearch: String
    ) {
        this.viewModel.updateForecastByName( weatherSearch )
        this.load = true
    }

    override fun dispose() {
        this.load = false
        this.viewModel.dispose()
    }
}

interface MediaType

sealed class Type {
    object None: MediaType
    object Image: MediaType
    object Gif: MediaType
    object AudioMix: MediaType
    object Video: MediaType
    object Pdf: MediaType
    object Web: MediaType
    object Weather: MediaType
}

interface TransitionEffect

open class Effect(
    val label: String
): TransitionEffect

sealed class Transition {
    object SlideOutLeft: Effect( label = "SlideOutLeft")
    object FadeScaleOut: Effect( label = "FadeScaleOut")
    object FadeOut: Effect( label = "FadeOut")
    object FadeOutLine: Effect( label = "FadeLine")
    object FadeOutCircle: Effect( label = "FadeCircle")
    object FadeOutCustom: Effect( label = "FadeCustom")
}