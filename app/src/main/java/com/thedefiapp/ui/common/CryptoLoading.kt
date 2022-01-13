package com.thedefiapp.ui.common

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.thedefiapp.R

@Composable
fun CryptoLoading(height: Dp = 100.dp) {
    val image = ImageBitmap.imageResource(id = R.drawable.eth_logo)
    val color = MaterialTheme.colors.primary
    val yAnim = rememberInfiniteTransition()
    val dy by yAnim.animateFloat(
        initialValue = height.value,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing)
        )
    )
    Canvas(modifier = Modifier.size(height)) {
        val rate = size.height / image.height
        val newWidth = image.width * rate
        val xStart = ((size.width / 2) - (newWidth / 2)).toInt()
        clipRect(top = dy.dp.toPx(), bottom = height.toPx()) {
            drawImage(
                image = image,
                colorFilter = ColorFilter.tint(color),
                dstOffset = IntOffset(xStart, 0),
                dstSize = IntSize(newWidth.toInt(), size.height.toInt())
            )
        }
    }
}
