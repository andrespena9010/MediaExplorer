package com.exaple.mediaexplorer.ui.viewmodels

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.webkit.CookieManager
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebView.RENDERER_PRIORITY_IMPORTANT
import android.webkit.WebViewClient
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update


@SuppressLint("StaticFieldLeak")
open class WebViewModelClass(): ViewModel(){

    private lateinit var webView: WebView

    private val _navigation = MutableStateFlow( false )
    val navigation: StateFlow<Boolean> = _navigation.asStateFlow()

    fun getWebView(): WebView {
        return webView
    }

    @SuppressLint("SetJavaScriptEnabled")
    fun go( context: Context , url: String, navigation: Boolean, onFinishPage: () -> Unit ) {

        _navigation.update { navigation }

        webView = WebView( context )

        webView.clearCache(true)

        webView.webViewClient = object: WebViewClient(){
            override fun onPageFinished(view: WebView?, url: String?) {
                onFinishPage()
                super.onPageFinished(view, url)
            }
            override fun onLoadResource(view: WebView?, urlResource: String?) {
                view?.evaluateJavascript(
                    """
                        (function() {
                            metaViewport = document.createElement('meta');
                            metaViewport.name = 'viewport';
                            metaViewport.content = 'width=3840, height=2160';
                            document.head.appendChild(metaViewport);
                        })();
                        """.trimIndent(),
                    null
                )
                super.onLoadResource(view, urlResource)
            }
        }

        webView.settings.apply {
            javaScriptEnabled = true
            useWideViewPort = true
            loadWithOverviewMode = true
            domStorageEnabled = true
            allowFileAccess = true
            cacheMode = WebSettings.LOAD_DEFAULT
            textZoom = 100
            builtInZoomControls = true
            displayZoomControls = false
            setSupportZoom(true)
            loadsImagesAutomatically = true
            mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
            layoutAlgorithm = WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING
            mediaPlaybackRequiresUserGesture = false

        }

        val cookieManager = CookieManager.getInstance()
        cookieManager.setAcceptCookie(true)
        cookieManager.acceptThirdPartyCookies(webView)

        webView.setRendererPriorityPolicy(RENDERER_PRIORITY_IMPORTANT, true)
        webView.setLayerType(View.LAYER_TYPE_HARDWARE, null)

        webView.loadUrl( url )
        webView.invalidate()
    }

}

@SuppressLint("StaticFieldLeak")
object WebViewModel: WebViewModelClass()