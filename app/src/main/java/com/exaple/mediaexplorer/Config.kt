package com.exaple.mediaexplorer

import com.exaple.mediaexplorer.ui.models.*

val ITEMS = listOf(
    ImageExplorerItem(
        name = "image1.png",
        data = "https://drive.google.com/uc?export=download&id=1hEOmZutQjMuyCcHkfpjfbJqPagVELARB",
        type = Type.Image,
        duration = 5000L
    ),
    AudioExplorerItem(
        name = "audio.m4a",
        data = "https://drive.google.com/uc?export=download&id=19UxIkSdVZgVJXHxT7oGJc-e6ZB3LB8Yd",
        type = Type.AudioMix,
        contentType = AudioMixType(
            name = "gif",
            type = Type.Gif,
            data = "https://drive.google.com/uc?export=download&id=1z3mitzi_WUZBOiUVTvzWBxfN4qHBH7Hy"
        ),
        duration = 5000L
    ),
    VideoExplorerItem(
        name = "video1.mp4",
        data = "https://drive.google.com/uc?export=download&id=1yDbyMdedPDgipYAUN_Xm1ygucbhp5RXt",
        type = Type.Video,
        duration = 5000L
    ),
    PdfExplorerItem(
        name = "example1.pdf",
        data = "https://drive.google.com/uc?export=download&id=1qmbycEfkiLbQvn6N80aeifqOfM2IheBd",
        type = Type.Pdf,
        duration = 5000L
    ),
    WebExplorerItem(
        name = "Orbys",
        data = "https://orbys.eu/",
        type = Type.Web,
        duration = 5000L
    ),
    WeatherExplorerItem(
        name = "",
        data = "Bogota",
        type = Type.Weather,
        duration = 5000L
    ),
    ImageExplorerItem(
        name = "image2.jpg",
        data = "https://drive.google.com/uc?export=download&id=1RLISAIZeLCAxww3LIX9VJvRkv_iowcqS",
        type = Type.Image,
        duration = 5000L
    ),
    AudioExplorerItem(
        name = "audio.m4a",
        data = "https://drive.google.com/uc?export=download&id=19UxIkSdVZgVJXHxT7oGJc-e6ZB3LB8Yd",
        type = Type.AudioMix,
        contentType = AudioMixType(
            name = "image3.png",
            type = Type.Image,
            data = "https://drive.google.com/uc?export=download&id=1cpuRm3Kbok3AjLps4YQl-cbUjYwZcV0H"
        ),
        duration = 5000L
    ),
    VideoExplorerItem(
        name = "video2.mp4",
        data = "",
        type = Type.Video,
        duration = 5000L
    ),
    PdfExplorerItem(
        name = "example2.pdf",
        data = "https://drive.google.com/uc?export=download&id=1kpwWze7zh9iifstFBgmWD125up3MXKAt",
        type = Type.Pdf,
        duration = 5000L
    ),
    WebExplorerItem(
        name = "Gucci",
        data = "https://www.gucci.com/us/en/st/what-new",
        type = Type.Web,
        duration = 5000L
    ),
    WeatherExplorerItem(
        name = "Castellon",
        data = "39.9833, -0.0333",
        type = Type.Weather,
        duration = 5000L
    )
)