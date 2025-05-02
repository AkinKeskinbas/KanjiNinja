package com.keak.kanjininja.common

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.SvgDecoder

@Composable
fun BaseAsyncImageComponent(
    modifier: Modifier = Modifier,
    stringImage: String,
    contentScale: ContentScale = ContentScale.Crop,
) {
    val context = LocalContext.current
    val imageLoader = ImageLoader.Builder(context)
        .components {
            add(SvgDecoder.Factory())
        }
        .build()
    AsyncImage(
        modifier = modifier,
        model = stringImage,
        contentDescription = null,
        contentScale = contentScale,
        imageLoader = imageLoader,
    )
}