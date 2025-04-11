package com.exaple.mediaexplorer.ui.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import com.exaple.mediaexplorer.ITEMS
import com.exaple.mediaexplorer.data.repository.Repository
import com.exaple.mediaexplorer.ui.models.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import androidx.core.net.toUri
import com.exaple.mediaexplorer.ui.models.MediaExplorerItem
import com.exaple.mediaexplorer.ui.models.WebExplorerItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.update
import kotlin.properties.Delegates

open class MediaExplorerViewModelClass: ViewModel() {

    private val repository = Repository

    private val _selectedItem = MutableStateFlow( MediaExplorerItem( type = Type.None ) )
    val selectedItem: StateFlow<MediaExplorerItem> = _selectedItem.asStateFlow()

    private val _items = MutableStateFlow( Media( items = ITEMS.asReversed() ) )
    val items: StateFlow<Media> = _items.asStateFlow()

    private val _ready = MutableStateFlow( false )
    val ready: StateFlow<Boolean> = _ready.asStateFlow()

    private var itemCount: Int by Delegates.observable(initialValue = 0) { property, oldValue, newValue ->
        val value = newValue == 0
        if ( value ) _ready.update { true }
    }

    private var job: Job? = null

    fun init( context: Context ) {

        repository.init( context )

        items.value.items.forEach { media ->

            when ( media.type ){

                Type.Image ->{
                    viewModelScope.launch {
                        val file = repository.getFile( media.name )
                        if ( file != null ){
                            itemCount++
                            ( media as ImageExplorerItem ).bitmap = repository.loadBitmap( file )
                            itemCount--
                        }
                    }
                }

                Type.AudioMix -> {
                    viewModelScope.launch {
                        val mediaUri = repository.getFile( media.name )?.toUri()
                        if ( mediaUri != null ){
                            itemCount++
                            val audio = media as AudioExplorerItem
                            audio.viewModel.init(
                                mediaItems = listOf(
                                    MediaItem.fromUri(
                                        mediaUri
                                    )
                                ),
                                context = context
                            )
                            val mixType = audio.contentType
                            val contentFile = repository.getFile( mixType.name )
                            if ( contentFile != null ){
                                when ( mixType.type ){

                                    Type.Image -> {
                                        audio.bitmap = repository.loadBitmap( contentFile )
                                    }

                                    Type.Gif -> {
                                        audio.byteArray = repository.loadDrawable( contentFile )
                                    }
                                }
                            }
                            itemCount--
                        }
                    }
                }

                Type.Video -> {
                    viewModelScope.launch {
                        val mediaUri = repository.getFile( media.name )?.toUri()
                        if ( mediaUri != null ){
                            itemCount++
                            ( media as VideoExplorerItem ).viewModel.init(
                                mediaItems = listOf(
                                    MediaItem.fromUri(
                                        mediaUri
                                    )
                                ),
                                context = context
                            )
                            itemCount--
                        }
                    }
                }

                Type.Pdf -> {
                    viewModelScope.launch {
                        val file = repository.getFile( media.name )
                        if ( file != null ){
                            itemCount++
                            ( media as PdfExplorerItem ).viewModel.setSelectedPDF(
                                pdf = file,
                                minPages = 10,
                                onGetMinPages = { itemCount-- }
                            )
                        }
                    }
                }

                Type.Web -> {
                    viewModelScope.launch {
                        itemCount++
                        ( media as WebExplorerItem ).viewModel.go(
                            context = context,
                            url = media.data,
                            navigation = false,
                            onFinishPage = { itemCount-- }
                        )
                        media.loaded = true
                    }
                }

                Type.Weather -> {
                    viewModelScope.launch ( Dispatchers.IO ){
                        itemCount++
                        ( media as WeatherExplorerItem ).viewModel.updateForecastByName(
                            cityName = media.data,
                            onLoadFinish = { itemCount-- }
                        )
                        media.loaded = true
                    }
                }

            }

        }

    }

    fun start(){
        viewModelScope.launch {
            job?.cancel()
            job = CoroutineScope(Dispatchers.Default).launch {
                items.value.items.forEach { media ->
                    _selectedItem.update { media }
                    when ( media.type ){

                        Type.Image -> {
                            delay(3000)
                        }/*

                        Type.AudioMix -> {
                            withContext ( Dispatchers.Main ){
                                val audio = media as AudioExplorerItem
                                audio.viewModel.play()
                                delay(3000)
                                audio.viewModel.pause()
                                audio.viewModel.seekTo(0L)
                            }
                        }

                        Type.Video -> {
                            withContext ( Dispatchers.Main ){
                                val video = media as VideoExplorerItem
                                video.viewModel.play()
                                delay(5000)
                                video.viewModel.pause()
                                video.viewModel.seekTo(0L)
                            }
                        }

                        Type.Pdf -> {
                            delay(3000)
                        }

                        Type.Web -> {
                            delay(10000)
                        }

                        Type.Weather -> {
                            delay(3000)
                        }*/

                    }

                }
                restart(MediaExplorerItem(type = Type.None))
            }
        }
    }

    fun restart( selected: MediaExplorerItem ) {
        when ( selected.type ){

            Type.Video -> {
                ( selected as VideoExplorerItem ).viewModel.pause()
                selected.viewModel.seekTo(0)
            }

            Type.AudioMix -> {
                ( selected as AudioExplorerItem ).viewModel.pause()
                selected.viewModel.seekTo(0)
            }

        }
        _selectedItem.update { MediaExplorerItem( type = Type.None ) }
        job?.cancel()
        // detener procesos de reproduccion
    }

}

object MediaExplorerViewModel: MediaExplorerViewModelClass()