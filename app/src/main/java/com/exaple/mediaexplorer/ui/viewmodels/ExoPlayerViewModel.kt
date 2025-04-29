package com.exaple.mediaexplorer.ui.viewmodels

import android.content.Context
import androidx.annotation.OptIn
import androidx.compose.ui.unit.IntSize
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.TrackGroup
import androidx.media3.common.TrackSelectionOverride
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.DefaultLoadControl
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.analytics.AnalyticsListener
import androidx.media3.exoplayer.trackselection.DefaultTrackSelector
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

    private val _currentPosition = MutableStateFlow(0L) // Posición actual en milisegundos
    val currentPosition: StateFlow<Long> = _currentPosition.asStateFlow()

    private val _isPlaying = MutableStateFlow(false) // Estado de reproducción
    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()

    private val _isMute = MutableStateFlow(false) // Estado de silencio
    val isMute: StateFlow<Boolean> = _isMute.asStateFlow()

    private val _fordward = MutableStateFlow(false) // Estado de avance rápido
    val fordward: StateFlow<Boolean> = _fordward.asStateFlow()

    private val _backward = MutableStateFlow(false) // Estado de retroceso rápido
    val backward: StateFlow<Boolean> = _backward.asStateFlow()

    private val _showControls = MutableStateFlow(true) // Visibilidad de controles
    val showControls: StateFlow<Boolean> = _showControls.asStateFlow()

    private val _loading = MutableStateFlow(false) // Estado de carga/buffering
    val loading: StateFlow<Boolean> = _loading.asStateFlow()

    private val _duration = MutableStateFlow(1L) // Duración total del media
    val duration: StateFlow<Long> = _duration.asStateFlow()

    private val _currentVolume = MutableStateFlow(0.7f) // Volumen actual (0-1)
    val currentVolume: StateFlow<Float> = _currentVolume.asStateFlow()

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

        player.addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(playbackState: Int) {
                super.onPlaybackStateChanged(playbackState)
                when (playbackState) {
                    Player.STATE_READY -> {
                        _loading.update { false } // Finaliza estado de carga
                        _duration.update { player.duration } // Actualiza duración
                    }
                    Player.STATE_BUFFERING -> {
                        _loading.update { true } // Activa indicador de carga
                    }
                    Player.STATE_ENDED -> {}
                    Player.STATE_IDLE -> {}
                }
            }

            override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                if (reason == Player.MEDIA_ITEM_TRANSITION_REASON_AUTO) {
                    _duration.update { player.duration }
                }
            }
        })

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

        updateUI()

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

    // Actualización periódica del estado de reproducción (cada 100ms)
    fun updateUI() {
        viewModelScope.launch {
            while (true) {
                _currentPosition.update { player.currentPosition }
                _isPlaying.update { player.isPlaying }
                _isMute.update { player.volume == 0f }
                delay(100)
            }
        }
    }

    // Salto temporal (adelante/atrás)
    fun jumpTo(time: Long, left: Boolean = false) {
        viewModelScope.launch {
            if (player.playbackState == Player.STATE_READY) {
                var advance = 0L
                if (left) {
                    _backward.update { true } // Activa feedback visual
                    advance = currentPosition.value - time
                } else {
                    _fordward.update { true }
                    advance = currentPosition.value + time
                }
                player.seekTo(advance)
                _currentPosition.update { player.currentPosition }
                delay(100) // Pequeño delay para feedback visual
                if (left) _backward.update { false } else _fordward.update { false }
            }
        }
    }

    // Métodos de control básico
    fun showControls(value: Boolean) {
        _showControls.update { value }
    }

    fun seekTo(millis: Long) {
        if (player.playbackState == Player.STATE_READY) {
            player.seekTo(millis)
            _currentPosition.update { player.currentPosition }
        }
    }

    // Navegación entre ítems multimedia
    fun goToNextMedia() {
        if (player.hasNextMediaItem()) {
            _currentPosition.update { 0L } // Reinicia posición
            player.seekToNext()
            player.play()
        }
    }

    fun goToPreviousMedia() {
        if (player.hasPreviousMediaItem()) {
            _currentPosition.update { 0L }
            player.seekToPrevious()
            player.play()
        }
    }

    // Control de volumen (0-100)
    fun setVolume(position: Long) {
        _currentVolume.update { position.toFloat() / 100 }
        player.volume = currentVolume.value
    }

    // Silenciar/desilenciar
    fun mute(value: Boolean) {
        if (value) {
            player.volume = 0f
        } else {
            player.volume = currentVolume.value
        }
    }
}