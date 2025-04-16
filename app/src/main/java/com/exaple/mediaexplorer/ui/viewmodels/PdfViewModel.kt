package com.exaple.mediaexplorer.ui.viewmodels

import android.util.Log
import androidx.core.graphics.createBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil3.Bitmap
import com.exaple.mediaexplorer.data.models.PdfPage
import com.exaple.mediaexplorer.data.repository.Repository
import com.exaple.mediaexplorer.ui.render.PrincipalPdfRenderer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.time.Duration
import java.time.LocalTime
import java.util.concurrent.Executors

/**
 * ViewModel principal para la gestión de PDFs.
 *
 * @property repository Repositorio para la gestión de datos.
 */
open class PdfViewModelClass(
    private val repository: Repository = Repository
) : ViewModel() {

    private var pdfName = ""
    private lateinit var pdfRenderer: PrincipalPdfRenderer
    private lateinit var selectedPDF: File

    /**
     * Estado de carga de las páginas del PDF.
     */
    private val _pdfPages = MutableStateFlow<List<PdfPage>>(listOf())
    val pdfPages: StateFlow<List<PdfPage>> = _pdfPages.asStateFlow()

    private val loadThreadPool = Executors.newFixedThreadPool(2)
    private val renderThreadPool = Executors.newFixedThreadPool(20).asCoroutineDispatcher()
    private var renderStat = LocalTime.now()

    private var pagesCount = 0
    private var pagesRendered = 0
    private var minPagesToRender = 10
    private var visibleRange = 0 .. 5

    private var onGetMinPages: () -> Unit = {}

    /**
     * Establece el PDF seleccionado y guarda una copia.
     *
     * Esta función actualiza el estado del PDF seleccionado y llama a "" para guardar una copia del PDF.
     *
     * @param pdf PDF a seleccionar.
     */
    fun setSelectedPDF( pdf: File ) {
        selectedPDF = pdf
        pdfRenderer = PrincipalPdfRenderer( pdf )
        pdfName = pdf.name
        pagesCount = pdfRenderer.pageCount()
        _pdfPages.update { List<PdfPage>( pagesCount ){ PdfPage( bitmap = null, pageLoading = true, cachedBitmap = false) } }
        viewModelScope.launch {
            renderDocument()
        }
    }

    fun getPage( index: Int ): Bitmap {
        val bitmap = pdfPages.value[index].bitmap
        return bitmap ?: createBitmap(200,200)
    }

    fun dispose(){
        dropRamPage()
    }

    /**
     * Renderiza el documento PDF.
     *
     * Esta función renderiza todas las páginas del PDF y guarda los bitmaps en el repositorio.
     * Actualiza los estados de carga y páginas del PDF.
     */
    private suspend fun renderDocument() {
        renderStat = LocalTime.now()
        Log.i("TIMEPDF", "Inicia renderizado del Documento $renderStat")

        pagesRendered = 0

        try {
            for (pageIndex in 0..< 1) {

                if ( pagesRendered == minPagesToRender ) onGetMinPages()

                val bitmapName = "${selectedPDF.nameWithoutExtension}_$pageIndex.png"

                if ( repository.existBitmap(bitmapName) == null ) {

                    withContext ( renderThreadPool ){

                        _pdfPages.update { current ->
                            val list = current.toMutableList()
                            list[ pageIndex ] = list[ pageIndex ].copy( pageLoading = true )
                            list
                        }

                        /*val it = LocalTime.now()
                        Log.i("TIMEPDF", "Inicia renderizado ->>> (RENDER PAGINA $pageIndex)")*/

                        val bitmap = pdfRenderer.getBitmapPage( pageIndex )

                        /*Log.i("TIMEPDF", "Termina renderizado ->>> (RENDER PAGINA $pageIndex) ${Duration.between(it, LocalTime.now()).toMillis()} Milisegundos\n")
                        pagesRendered++*/

                        if (pagesRendered == pagesCount) {
                            Log.i("TIMEPDF", "---- Termina renderizado TOTAL Tiempo total ---- : ${Duration.between(renderStat, LocalTime.now()).toMillis()} Milisegundos, pagina: $pagesCount")
                        }

                        if ( bitmap != null ){

                            CoroutineScope( Dispatchers.IO ).launch {
                                repository.saveCacheBitmap(bitmap, bitmapName)
                            }

                        }

                        if ( pageIndex in  visibleRange ){

                            _pdfPages.update { current ->
                                val list = current.toMutableList()
                                list[ pageIndex ] = list[ pageIndex ].copy( bitmap = bitmap )
                                list
                            }

                        }

                        _pdfPages.update { current ->
                            val list = current.toMutableList()
                            list[ pageIndex ] = list[ pageIndex ].copy( pageLoading = false )
                            list
                        }

                    }

                } else {

                    if ( pageIndex in  visibleRange ){
                        loadBitmap( pageIndex )
                    }

                    pagesRendered++

                    if (pagesRendered == pagesCount) {
                        Log.i("TIMEPDF", "---- Termina renderizado TOTAL Tiempo total ---- : ${Duration.between(renderStat, LocalTime.now()).toMillis()} Milisegundos, pagina: $pagesCount")
                    }
                }

            }
        } catch (e: Exception) {
            Log.i("TIMEPDF", "Se cancela el renderizado... ${e.message}")
        }
    }

    /**
     * Libera una página de la memoria RAM.
     *
     * Esta función establece el bitmap de una página específica a null para liberar memoria.
     *
     *
     */
    private fun dropRamPage() {
        _pdfPages.update { current ->
            val list = current.toMutableList()
            list[ 0 ] = list[ 0 ].copy( bitmap = null )
            list
        }
    }

    /**
     * Carga un bitmap de una página del PDF.
     *
     * Esta función carga el bitmap de una página específica desde el repositorio si no está ya cargado.
     *
     * @param pageIndex Índice de la página a cargar.
     */
    private fun loadBitmap(pageIndex: Int) {

        loadThreadPool.execute {
            CoroutineScope(Dispatchers.Default).launch {

                if ( pdfPages.value[pageIndex].bitmap == null) {

                    val bitmapName = "${selectedPDF.nameWithoutExtension}_$pageIndex.png"

                    if (repository.existBitmap(bitmapName) != null) {

                        if ( pageIndex in visibleRange ){

                            val bitmap = repository.loadCacheBitmap(bitmapName)

                            _pdfPages.update { current ->
                                val list = current.toMutableList()
                                list[ pageIndex ] = list[ pageIndex ].copy( bitmap = bitmap )
                                list
                            }

                        }

                    }
                }

            }
        }

    }
}