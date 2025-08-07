package com.adev.compose.markdowtext.markdown.elements

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter
import coil3.imageLoader
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.toBitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


@Composable
fun ImageElement(
    url: String,
    description: String = "",
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    if (url.isBlank()) return

    val context = LocalContext.current
    var bitmapSize by remember(url) { mutableStateOf<Size?>(null) }

    // 1. 预下载拿原始宽高（仅一次）
    LaunchedEffect(url) {
        withContext(Dispatchers.IO) {
            val req = ImageRequest.Builder(context).data(url).build()
            val res = context.imageLoader
                .execute(req)
            res.image?.toBitmap()?.let { bmp ->
                bitmapSize = Size(bmp.width.toFloat(), bmp.height.toFloat())
            }
        }
    }

    // 2. 真正布局时拿到 Box 的可用宽度
    var boxWidthPx by remember { mutableIntStateOf(0) }
    val density = LocalDensity.current
    val boxHeightDp = remember(bitmapSize, boxWidthPx) {
        if (bitmapSize == null || boxWidthPx == 0) 200.dp
        else {
            val ratio = bitmapSize!!.height / bitmapSize!!.width
            val boxWidthDp = with(density) { boxWidthPx.toDp() }
            boxWidthDp * ratio
        }
    }

    Box(
        modifier
            .fillMaxWidth()
            .onSizeChanged { boxWidthPx = it.width }   // 关键：拿到实际宽度
            .height(boxHeightDp)
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() }
    ) {
        val painter = rememberAsyncImagePainter(
            ImageRequest.Builder(context)
                .data(url)
                .crossfade(true)
                .build()
        )

        when (painter.state) {
            is AsyncImagePainter.State.Loading -> Placeholder()
            is AsyncImagePainter.State.Error   -> ErrorPlaceholder()
            else                               -> Unit
        }

        Image(
            painter = painter,
            contentDescription = description,
            contentScale = ContentScale.Fit,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
private fun BoxScope.Placeholder() {
    CircularProgressIndicator(Modifier.align(Alignment.Center))
}

@Composable
private fun BoxScope.ErrorPlaceholder() {
    Text("加载失败", Modifier.align(Alignment.Center))
}