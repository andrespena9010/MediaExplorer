package com.exaple.mediaexplorer.ui.viewmodels

import android.content.Context
import android.util.Size
import android.view.TextureView
import androidx.annotation.OptIn
import androidx.compose.ui.unit.IntSize
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.TrackGroup
import androidx.media3.common.TrackSelectionOverride
import androidx.media3.common.Tracks
import androidx.media3.common.VideoSize
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.DefaultLoadControl
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.analytics.AnalyticsListener
import androidx.media3.exoplayer.trackselection.DefaultTrackSelector
import com.exaple.mediaexplorer.ui.models.Language
import com.exaple.mediaexplorer.ui.models.MediaPlayerItems
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 *
 * ViewModel principal para el reproductor multimedia con ExoPlayer.
 *
 * Funcionalidades clave:
 *
 * Gestión del estado del reproductor:
 * Control de reproducción (play/pause)
 * Seguimiento de posición y duración
 * Configuración de volumen y mute
 * Operaciones multimedia:
 * Saltos temporales (forward/backward)
 * Cambio entre tracks de audio
 * Navegación entre items multimedia
 * Control de interfaz:
 * Auto-ocultamiento de controles UI
 * Gestión de estados de carga
 * Visualización de opciones de idioma
 *
 */

open class ExoPlayerViewModelClass: ViewModel() {

    // Instancia principal del reproductor ExoPlayer
    private lateinit var player: ExoPlayer

    private var size = IntSize(0,0)

    @OptIn(UnstableApi::class)
    fun init(
        mediaItems: List<MediaItem>,
        duration: Long,
        context: Context
    ): ExoPlayer? {

        val minBuffer = if ( duration > 5000 ) ( 5 * 1000 ) else duration.toInt()
        val maxBuffer = ( 20 * 1000 )

        val trackSelector = DefaultTrackSelector(context).apply {
            parameters = buildUponParameters()
                .setMaxVideoSize(3840, 2160)
                .setMaxVideoFrameRate(60)
                .setForceHighestSupportedBitrate(true)
                .build()
        }

        player = ExoPlayer.Builder(context)
            .setTrackSelector(trackSelector)
            .setLoadControl(
                DefaultLoadControl.Builder()
                    .setBufferDurationsMs(
                        minBuffer,
                        maxBuffer,
                        2000,
                        1000
                    )
                    .build()
            )
            .build()

        player.setMediaItems(mediaItems) // Asignación de contenido
        player.prepare() // Precarga

        player.addAnalyticsListener(object : AnalyticsListener {
            override fun onVideoSizeChanged(
                eventTime: AnalyticsListener.EventTime,
                width: Int,
                height: Int,
                unappliedRotationDegrees: Int,
                pixelWidthHeightRatio: Float
            ) {
                size = IntSize(width, height)
            }
        })

        return player
    }

    fun dispose(){
        player.stop()
        player.release()
    }

    fun play() {
        player.play()
    }

    fun pause(){
        player.pause()
    }

    fun getPlayer(): ExoPlayer {
        return player
    }

    fun getSize(): IntSize {
        return size
    }
}

// Implementación singleton del ViewModel
object ExoPlayerViewModel : ExoPlayerViewModelClass()