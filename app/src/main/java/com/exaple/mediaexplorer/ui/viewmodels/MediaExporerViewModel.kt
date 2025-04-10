package com.exaple.mediaexplorer.ui.viewmodels

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import com.exaple.mediaexplorer.ITEMS
import com.exaple.mediaexplorer.data.models.LoadResponse
import com.exaple.mediaexplorer.data.models.PDF
import com.exaple.mediaexplorer.data.repository.Repository
import com.exaple.mediaexplorer.ui.models.AudioExplorerItem
import com.exaple.mediaexplorer.ui.models.Media
import com.exaple.mediaexplorer.ui.models.MediaExplorerItem
import com.exaple.mediaexplorer.ui.models.PdfExplorerItem
import com.exaple.mediaexplorer.ui.models.Type
import com.exaple.mediaexplorer.ui.models.VideoExplorerItem
import com.exaple.mediaexplorer.ui.models.WeatherExplorerItem
import com.exaple.mediaexplorer.ui.models.WebExplorerItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.PrintWriter
import java.io.StringWriter
import kotlin.properties.Delegates

open class MediaExporerViewModelClass: ViewModel() {

    private val repository = Repository

    private val _selectedItem = MutableStateFlow( 0 )
    val selectedItem: StateFlow<Int> = _selectedItem.asStateFlow()

    private val _items = MutableStateFlow( Media( items = ITEMS ) )
    val items: StateFlow<Media> = _items.asStateFlow()

    private val _downloading = MutableStateFlow( true )
    val downloading: StateFlow<Boolean> = _downloading.asStateFlow()

    private val _loadingMessages = MutableStateFlow( listOf<String>() )
    val loadingMessages: StateFlow<List<String>> = _loadingMessages.asStateFlow()

    private val _ready = MutableStateFlow( false )
    val ready: StateFlow<Boolean> = _ready.asStateFlow()

    var count: Int by Delegates.observable(initialValue = 0) { property, oldValue, newValue ->
        var value = newValue == 0
        if ( value ){
            _ready.update { true } //
        }
    }

    fun init( context: Context ) {

        repository.init( context )

        items.value.items.forEach { media ->

            when ( media.type ){

                Type.Image -> {
                    viewModelScope.launch ( Dispatchers.IO ){
                        media.downloadResponse = download( media )
                        val uri = media.downloadResponse?.saveResource?.uri
                        if ( uri != null ){
                            media.loaded = true
                        }
                    }
                }

                Type.AudioMix -> {
                    viewModelScope.launch {
                        val res = async ( Dispatchers.IO ){
                            downloadAudioMix( media )
                        }.await()
                        media.downloadResponse = res.first
                        ( media.type as Type.AudioMix ).downloadResponse = res.second
                        val uri = media.downloadResponse?.saveResource?.uri
                        if ( uri != null ){
                            ( media as AudioExplorerItem ).viewModel.init(
                                mediaItems = listOf(
                                    MediaItem.fromUri( uri )
                                ),
                                context = context
                            )
                            media.loaded = true
                        }
                    }
                }

                Type.Video -> {
                    viewModelScope.launch {
                        async ( Dispatchers.IO ){
                            media.downloadResponse = download( media )
                        }.await()
                        val uri = media.downloadResponse?.saveResource?.uri
                        if ( uri != null ){
                            ( media as VideoExplorerItem ).viewModel.init(
                                mediaItems = listOf(
                                    MediaItem.fromUri( uri )
                                ),
                                context = context
                            )
                            media.loaded = true
                        }
                    }
                }

                Type.Pdf -> {
                    viewModelScope.launch ( Dispatchers.IO ){
                        media.downloadResponse = download( media )
                        val uri = media.downloadResponse?.saveResource?.uri
                        if ( uri != null ){
                            ( media as PdfExplorerItem ).viewModel.setSelectedPDF(
                                PDF(
                                    fileName = media.name,
                                    uri = uri
                                )
                            )
                            media.loaded = true
                        }
                    }
                }

                /*Type.Web -> {
                    viewModelScope.launch ( Dispatchers.IO ){
                        ( media as WebExplorerItem ).viewModel.go(
                            context = context,
                            url = media.data,
                            navigation = false
                        )
                        media.loaded = true
                    }
                }

                Type.Weather -> {
                    viewModelScope.launch ( Dispatchers.IO ){
                        ( media as WeatherExplorerItem ).viewModel.updateForecastByName(
                            cityName = media.data
                        )
                        media.loaded = true
                    }
                }*/

            }

        }

    }

    private suspend fun downloadAudioMix( media: MediaExplorerItem ): Pair<LoadResponse, LoadResponse> {
        var lResponse = Pair( LoadResponse(), LoadResponse())
        _loadingMessages.update { current ->
            val new = current.toMutableList()
            new.add( "Downloading start... ${media.name}" )
            new
        }
        count += 2
        val res1 = viewModelScope.async {
            repository.downLoadResource(
                url = media.data,
                fileName = media.name
            )
        }
        val res2 = viewModelScope.async {
            repository.downLoadResource(
                url = ( media.type as Type.AudioMix ).data,
                fileName = media.type.name
            )
        }
        try {
            lResponse = lResponse.copy(
                first = res1.await(),
                second = res2.await()
            )
        } catch (e: Exception) {
            val sw = StringWriter()
            e.printStackTrace(PrintWriter(sw))
            val errorStackTrace = sw.toString()
            Log.e("MediaExporerViewModelClass.downloadAudioMix()", errorStackTrace)
        }
        count -= 2
        _loadingMessages.update { current ->
            val new = current.toMutableList()
            new.add( "Downloading finish... ${media.name}" )
            new
        }
        return lResponse
    }

    private suspend fun download( media: MediaExplorerItem ): LoadResponse {
        var lResponse = LoadResponse()
        _loadingMessages.update { current ->
            val new = current.toMutableList()
            new.add( "Downloading start... ${media.name}" )
            new
        }
        count++
        val res = viewModelScope.async {
            repository.downLoadResource(
                url = media.data,
                fileName = media.name
            )
        }
        try {
            lResponse = res.await()
        } catch (e: Exception) {
            val sw = StringWriter()
            e.printStackTrace(PrintWriter(sw))
            val errorStackTrace = sw.toString()
            Log.e("MediaExporerViewModelClass.download()", errorStackTrace)
        }
        count--
        _loadingMessages.update { current ->
            val new = current.toMutableList()
            new.add( "Downloading finish... ${media.name}" )
            new
        }
        return lResponse
    }

}

object MediaExporerViewModel: MediaExporerViewModelClass()