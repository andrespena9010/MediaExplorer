package com.exaple.mediaexplorer

import com.exaple.mediaexplorer.ui.models.*

val ITEMS = listOf(
    WeatherExplorerItem(
        name = "",
        data = "Bogota",
        type = Type.Weather,
        duration = 10000L
    ),
    VideoExplorerItem(
        name = "video1.mp4",
        data = "https://drive.google.com/uc?export=download&id=1yDbyMdedPDgipYAUN_Xm1ygucbhp5RXt",
        type = Type.Video,
        duration = 10000L
    ),
    PdfExplorerItem(
        name = "example1.pdf",
        data = "https://drive.google.com/uc?export=download&id=1qmbycEfkiLbQvn6N80aeifqOfM2IheBd",
        type = Type.Pdf,
        duration = 10000L
    ),
    ImageExplorerItem(
        name = "image1.png",
        data = "https://drive.google.com/uc?export=download&id=1hEOmZutQjMuyCcHkfpjfbJqPagVELARB",
        type = Type.Image,
        duration = 10000L
    ),
    WebExplorerItem(
        name = "Orbys",
        data = "https://orbys.eu/",
        type = Type.Web,
        duration = 10000L
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
        duration = 10000L
    ),
    WeatherExplorerItem(
        name = "Castellon",
        data = "39.9833, -0.0333",
        type = Type.Weather,
        duration = 10000L
    ),
    VideoExplorerItem(
        name = "video2.mp4",
        data = "",
        type = Type.Video,
        duration = 10000L
    ),
    PdfExplorerItem(
        name = "example2.pdf",
        data = "https://drive.google.com/uc?export=download&id=1kpwWze7zh9iifstFBgmWD125up3MXKAt",
        type = Type.Pdf,
        duration = 10000L
    ),
    ImageExplorerItem(
        name = "image2.jpg",
        data = "https://drive.google.com/uc?export=download&id=1RLISAIZeLCAxww3LIX9VJvRkv_iowcqS",
        type = Type.Image,
        duration = 10000L
    ),
    WebExplorerItem(
        name = "Movistar",
        data = "https://www.movistarplus.es/el-partido-movistarplus?utm_source=havas0x0digitalpaid0x0unieditorial0x0marca&utm_medium=cf&utm_campaign=ott&utm_content=x0x0generica0x0dis0x0dir0x0bnr0x0pcc&utm_creative_format=FUTBOL&dclid=CLTjjMTy2YwDFWFIHQkdKtk3JA&gad_source=7",
        type = Type.Web,
        duration = 10000L
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
        duration = 10000L
    )
)
