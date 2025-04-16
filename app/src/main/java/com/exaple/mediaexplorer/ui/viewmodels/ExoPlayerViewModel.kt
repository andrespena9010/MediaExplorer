package com.exaple.mediaexplorer.ui.viewmodels

import android.content.Context
import androidx.annotation.OptIn
import androidx.compose.ui.unit.IntSize
import androidx.lifecycle.ViewModel
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.DefaultLoadControl
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.analytics.AnalyticsListener
import androidx.media3.exoplayer.trackselection.DefaultTrackSelector

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