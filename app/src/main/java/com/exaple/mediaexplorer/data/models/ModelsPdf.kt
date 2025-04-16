package com.exaple.mediaexplorer.data.models

import android.graphics.Bitmap

data class PdfPage(
    val bitmap: Bitmap?,
    val pageLoading: Boolean,
    val cachedBitmap: Boolean
)