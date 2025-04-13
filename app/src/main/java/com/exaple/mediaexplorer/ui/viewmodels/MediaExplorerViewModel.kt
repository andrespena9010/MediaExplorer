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
import kotlinx.coroutines.withContext
import kotlin.properties.Delegates

open class MediaExplorerViewModelClass: ViewModel() {

    private val repository = Repository

    private val _selectedItem = MutableStateFlow<MediaExplorerItem>( None() )
    val selectedItem: StateFlow<MediaExplorerItem> = _selectedItem.asStateFlow()

    private val _items = MutableStateFlow<List<MediaExplorerItem>>( ITEMS.asReversed() )
    val items: StateFlow<List<MediaExplorerItem>> = _items.asStateFlow()

    private val _ready = MutableStateFlow( false )
    val ready: StateFlow<Boolean> = _ready.asStateFlow()

    private val _transition = MutableStateFlow<TransitionEffect>( Transition.SlideToLeft )
    val transition: StateFlow<TransitionEffect> = _transition.asStateFlow()

    private var itemCount: Int by Delegates.observable(initialValue = 0) { property, oldValue, newValue ->
        val value = newValue == 0
        if ( value ) _ready.update { true }
    }

    private var job: Job? = null

    fun init( context: Context ) {

        repository.init( context )

        items.value.forEach { media ->

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
                    }
                }

                Type.Weather -> {
                    viewModelScope.launch ( Dispatchers.IO ){
                        itemCount++
                        ( media as WeatherExplorerItem ).viewModel.updateForecastByName(
                            cityName = media.data,
                            onLoadFinish = { itemCount-- }
                        )
                    }
                }

            }

        }

    }

    fun start(){
        viewModelScope.launch {
            job?.cancel()
            job = CoroutineScope(Dispatchers.Default).launch {
                for (index in items.value.indices.reversed()) {
                    val media = items.value[index]
                    _selectedItem.update { media }
                    when ( media.type ){

                        Type.Image -> {
                            delay( media.duration )
                            _items.update {
                                val upItems = it.toMutableList()
                                upItems[index] = ( it[index] as ImageExplorerItem ).copy( active = false )
                                upItems
                            }
                        }

                        Type.AudioMix -> {
                            withContext ( Dispatchers.Main ){
                                val audio = media as AudioExplorerItem
                                audio.viewModel.play()
                                delay( media.duration )
                                audio.viewModel.pause()
                                audio.viewModel.seekTo(0L)
                            }
                            _items.update {
                                val upItems = it.toMutableList()
                                upItems[index] = ( it[index] as AudioExplorerItem ).copy( active = false )
                                upItems
                            }
                        }

                        Type.Video -> {
                            withContext ( Dispatchers.Main ){
                                val video = media as VideoExplorerItem
                                video.viewModel.play()
                                delay( media.duration )
                                video.viewModel.pause()
                                video.viewModel.seekTo(0L)
                            }
                            _items.update {
                                val upItems = it.toMutableList()
                                upItems[index] = ( it[index] as VideoExplorerItem ).copy( active = false )
                                upItems
                            }
                        }

                        Type.Pdf -> {
                            delay( media.duration )
                            _items.update {
                                val upItems = it.toMutableList()
                                upItems[index] = ( it[index] as PdfExplorerItem ).copy( active = false )
                                upItems
                            }
                        }

                        Type.Web -> {
                            delay( media.duration )
                            _items.update {
                                val upItems = it.toMutableList()
                                upItems[index] = ( it[index] as WebExplorerItem ).copy( active = false )
                                upItems
                            }
                        }

                        Type.Weather -> {
                            delay( media.duration )
                            _items.update {
                                val upItems = it.toMutableList()
                                upItems[index] = ( it[index] as WeatherExplorerItem ).copy( active = false )
                                upItems
                            }
                        }

                    }

                }
                restart()
            }
        }
    }

    fun restart() {
        job?.cancel()
        items.value.forEach { media ->
            when ( media.type ){

                Type.Video -> {
                    viewModelScope.launch ( Dispatchers.Main ){
                        ( media as VideoExplorerItem ).viewModel.pause()
                        media.viewModel.seekTo(0)
                    }
                }

                Type.AudioMix -> {
                    viewModelScope.launch ( Dispatchers.Main ){
                        ( media as AudioExplorerItem ).viewModel.pause()
                        media.viewModel.seekTo(0)
                    }
                }

            }
        }

        items.value.forEachIndexed { index,_ ->
            _items.update {
                val upItems = it.toMutableList()
                upItems[index] = when ( upItems[index].type ) {

                    Type.Image -> {
                        ( upItems[index] as ImageExplorerItem ).copy( active = true )
                    }
                    Type.AudioMix -> {
                        ( upItems[index] as AudioExplorerItem ).copy( active = true )
                    }
                    Type.Video -> {
                        ( upItems[index] as VideoExplorerItem ).copy( active = true )
                    }
                    Type.Pdf -> {
                        ( upItems[index] as PdfExplorerItem ).copy( active = true )
                    }
                    Type.Web -> {
                        ( upItems[index] as WebExplorerItem ).copy( active = true )
                    }
                    Type.Weather -> {
                        ( upItems[index] as WeatherExplorerItem ).copy( active = true )
                    }

                    else -> {
                        None()
                    }
                }

                upItems
            }
        }
        _selectedItem.update { None() }
    }

    fun setTransition(
        transition: TransitionEffect
    ){
        _transition.update { transition }
    }

}

object MediaExplorerViewModel: MediaExplorerViewModelClass()