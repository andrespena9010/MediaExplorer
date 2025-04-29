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

    private val _inTransition = MutableStateFlow( false )
    val inTransition: StateFlow<Boolean> = _inTransition.asStateFlow()

    private val _items = MutableStateFlow<List<MediaExplorerItem>>( listOf() )
    val items: StateFlow<List<MediaExplorerItem>> = _items.asStateFlow()

    private val _transition = MutableStateFlow<TransitionEffect>( Transition.SlideOutLeft )
    val transition: StateFlow<TransitionEffect> = _transition.asStateFlow()

    private val _transitionTime = MutableStateFlow( 1500 )
    val transitionTime: StateFlow<Int> = _transitionTime.asStateFlow()

    private val _activeScreen = MutableStateFlow( false )
    val activeScreen: StateFlow<Boolean> = _activeScreen.asStateFlow()

    private var job: Job? = null

    fun init( context: Context ) {
        val list = mutableListOf<MediaExplorerItem>()
        ITEMS.forEach { media ->
            when ( media.type ){
                Type.Image -> { list.add( media.toImageItem() ) }
                Type.AudioMix -> { list.add( media.toAudioItem() ) }
                Type.Video -> { list.add( media.toVideoItem() ) }
                Type.Pdf -> { list.add( media.toPdfItem() ) }
                Type.Web -> { list.add( media.toWebItem() ) }
                Type.Weather -> { list.add( media.toWeatherItem() ) }
            }
        }
        _items.update { list.toList() }
        repository.init( context )
        if ( _items.value.isNotEmpty() ) loadMedia( 0, context )
        if ( _items.value.size > 1 ) loadMedia( 1, context )
    }

    fun loadMedia( index: Int, context: Context){
        if ( index < _items.value.size ){
            viewModelScope.launch {
                _items.update {
                    val upItems = it.toMutableList()
                    upItems[index] = when ( upItems[index].type ) {

                        Type.Image -> {
                            val image = upItems[index] as ImageItem
                            val file = repository.getFile( image.uuid )
                            if ( file != null ) image.load( bitmap = repository.loadBitmap( file ) )
                            image.copy( active = true )
                        }

                        Type.AudioMix -> {
                            val audio = upItems[index] as AudioItem
                            val audioUri = repository.getFile( audio.uuid )?.toUri()
                            if ( audioUri != null ){
                                val mixType = audio.contentType
                                val contentFile = repository.getFile( mixType.uuid )
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
                            val video = upItems[index] as VideoItem
                            val videUri = repository.getFile( video.uuid )?.toUri()
                            if ( videUri != null ){
                                video.load(
                                    uriMedia = videUri,
                                    context = context
                                )
                            }
                            video.copy( active = true )
                        }

                        Type.Pdf -> {
                            val pdf = upItems[index] as PdfItem
                            val file = repository.getFile( pdf.uuid )
                            if ( file != null ) pdf.load( file = file )
                            pdf.copy( active = true )
                        }

                        Type.Web -> {
                            val web = upItems[index] as WebItem
                            web.load(
                                url = web.data,
                                context = context
                            )
                            web.copy( active = true )
                        }

                        Type.Weather -> {
                            val weather = upItems[index] as WeatherItem
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
    }

    fun disposeMedia( index: Int ){
        if ( index < _items.value.size ){
            viewModelScope.launch {
                _items.update {
                    val upItems = it.toMutableList()
                    upItems[index].dispose()
                    upItems
                }
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
                    var trans = if ( index == items.value.size - 1 ) 0L else transitionTime.value.toLong()
                    when ( media.type ){

                        Type.Image -> {
                            delay( media.duration )
                            _items.update {
                                val upItems = it.toMutableList()
                                upItems[index] = ( it[index] as ImageItem ).copy( active = false )
                                upItems
                            }
                        }

                        Type.AudioMix -> {
                            _activeScreen.update { true }
                            val audio = media as AudioItem
                            withContext ( Dispatchers.Main ){
                                audio.viewModel.play()
                            }
                            delay( media.duration )
                            _activeScreen.update { false }
                            withContext ( Dispatchers.Main ){
                                audio.viewModel.pause()
                            }
                            delay(100)
                            _items.update {
                                val upItems = it.toMutableList()
                                upItems[index] = ( it[index] as AudioItem ).copy( active = false )
                                upItems
                            }
                        }

                        Type.Video -> {
                            val video = media as VideoItem
                            withContext ( Dispatchers.Main ){
                                video.viewModel.play()
                            }
                            delay( media.duration )
                            withContext ( Dispatchers.Main ){
                                video.viewModel.pause()
                            }
                            delay(100)
                            _items.update {
                                val upItems = it.toMutableList()
                                upItems[index] = ( it[index] as VideoItem ).copy( active = false )
                                upItems
                            }
                        }

                        Type.Pdf -> {
                            val pdf = media as PdfItem
                            delay( media.duration )



                            _items.update {
                                val upItems = it.toMutableList()
                                upItems[index] = ( it[index] as PdfItem ).copy( active = false )
                                upItems
                            }
                        }

                        Type.Web -> {
                            delay( media.duration )
                            _items.update {
                                val upItems = it.toMutableList()
                                upItems[index] = ( it[index] as WebItem ).copy( active = false )
                                upItems
                            }
                        }

                        Type.Weather -> {
                            delay( media.duration )
                            _items.update {
                                val upItems = it.toMutableList()
                                upItems[index] = ( it[index] as WeatherItem ).copy( active = false )
                                upItems
                            }
                        }

                    }
                    _inTransition.update { true }
                    delay( trans )
                    _inTransition.update { false }
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
                            ( media as VideoItem ).viewModel.pause()
                        }
                    }

                    Type.AudioMix -> {
                        viewModelScope.launch ( Dispatchers.Main ){
                            ( media as AudioItem ).viewModel.pause()
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