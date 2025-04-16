package com.exaple.mediaexplorer.ui.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext

open class MediaExplorerViewModelClass: ViewModel() {

    private val repository = Repository

    private val _selectedItem = MutableStateFlow( -1 )
    val selectedItem: StateFlow<Int> = _selectedItem.asStateFlow()

    private val _items = MutableStateFlow<List<MediaExplorerItem>>( ITEMS )
    val items: StateFlow<List<MediaExplorerItem>> = _items.asStateFlow()

    private val _transition = MutableStateFlow<TransitionEffect>( Transition.SlideOutLeft )
    val transition: StateFlow<TransitionEffect> = _transition.asStateFlow()

    private val _transitionTime = MutableStateFlow( 2000 )
    val transitionTime: StateFlow<Int> = _transitionTime.asStateFlow()

    private var job: Job? = null

    fun init( context: Context ) {
        repository.init( context )
        loadMedia( 0, context )
        loadMedia( 1, context )
    }

    fun loadMedia( index: Int, context: Context){
        viewModelScope.launch {
            _items.update {
                val upItems = it.toMutableList()
                upItems[index] = when ( upItems[index].type ) {

                    Type.Image -> {
                        val image = ( upItems[index] as ImageExplorerItem )
                        val file = repository.getFile( image.name )
                        if ( file != null ) image.load( bitmap = repository.loadBitmap( file ) )
                        image.copy( active = true )
                    }

                    Type.AudioMix -> {
                        val audio = ( upItems[index] as AudioExplorerItem )
                        val audioUri = repository.getFile( audio.name )?.toUri()
                        if ( audioUri != null ){
                            val mixType = audio.contentType
                            val contentFile = repository.getFile( mixType.name )
                            if ( contentFile != null ){
                                when ( mixType.type ){
                                    Type.Image -> {
                                        audio.load(
                                            bitmap = repository.loadBitmap( contentFile ),
                                            uriMedia = audioUri,
                                            context = context
                                        )
                                    }
                                    Type.Gif -> {
                                        audio.load(
                                            byteArray = repository.loadByteArray( contentFile ),
                                            uriMedia = audioUri,
                                            context = context
                                        )
                                    }
                                }
                            }
                        }
                        audio.copy( active = true )
                    }

                    Type.Video -> {
                        val video = ( upItems[index] as VideoExplorerItem )
                        val videUri = repository.getFile( video.name )?.toUri()
                        if ( videUri != null ){
                            video.load(
                                uriMedia = videUri,
                                context = context
                            )
                        }
                        video.copy( active = true )
                    }

                    Type.Pdf -> {
                        val pdf = ( upItems[index] as PdfExplorerItem )
                        val file = repository.getFile( pdf.name )
                        if ( file != null ) pdf.load( file = file )
                        pdf.copy( active = true )
                    }

                    Type.Web -> {
                        val web = ( upItems[index] as WebExplorerItem )
                        web.load(
                            url = web.data,
                            context = context
                        )
                        web.copy( active = true )
                    }

                    Type.Weather -> {
                        val weather = ( upItems[index] as WeatherExplorerItem )
                        weather.load( weatherSearch = weather.data )
                        weather.copy( active = true )
                    }

                    else -> {
                        upItems[index]
                    }
                }

                upItems
            }
        }
    }

    fun disposeMedia( index: Int ){
        viewModelScope.launch {
            _items.update {
                val upItems = it.toMutableList()
                upItems[index].dispose()
                upItems
            }
        }
    }

    fun start( context: Context ){
        viewModelScope.launch {
            job?.cancel()
            job = CoroutineScope( Dispatchers.Default ).launch {
                items.value.forEachIndexed { index, media ->
                    _selectedItem.update { index }
                    if ( index < items.value.size - 2 ) loadMedia( index + 2, context )
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
                            val audio = media as AudioExplorerItem
                            withContext ( Dispatchers.Main ){
                                audio.viewModel.play()
                            }
                            delay( media.duration )
                            withContext ( Dispatchers.Main ){
                                audio.viewModel.pause()
                            }
                            delay(300)
                            _items.update {
                                val upItems = it.toMutableList()
                                upItems[index] = ( it[index] as AudioExplorerItem ).copy( active = false )
                                upItems
                            }
                        }

                        Type.Video -> {
                            val video = media as VideoExplorerItem
                            withContext ( Dispatchers.Main ){
                                video.viewModel.play()
                            }
                            delay( media.duration )
                            withContext ( Dispatchers.Main ){
                                video.viewModel.pause()
                            }
                            delay(300)
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
                    if ( index < items.value.size - 1 ) delay( transitionTime.value.toLong() - 300)
                    disposeMedia( index )
                    viewModelScope.async {
                        delay(1000)
                        Runtime.getRuntime().gc()
                    }
                }
                restart( context )
            }
        }
    }

    fun restart( context: Context ) {
        job?.cancel()
        items.value.forEachIndexed { index, media ->
            if ( media.load ){
                when ( media.type ){

                    Type.Video -> {
                        viewModelScope.launch ( Dispatchers.Main ){
                            ( media as VideoExplorerItem ).viewModel.pause()
                        }
                    }

                    Type.AudioMix -> {
                        viewModelScope.launch ( Dispatchers.Main ){
                            ( media as AudioExplorerItem ).viewModel.pause()
                        }
                    }

                }
                disposeMedia( index )
            }
        }
        loadMedia( 0, context )
        loadMedia( 1, context )
        _selectedItem.update { -1 }
        viewModelScope.async {
            delay(1000)
            Runtime.getRuntime().gc()
        }
    }

    fun setTransition(
        transition: TransitionEffect
    ){
        _transition.update { transition }
    }

}

object MediaExplorerViewModel: MediaExplorerViewModelClass()