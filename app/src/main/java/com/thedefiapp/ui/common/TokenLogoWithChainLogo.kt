package com.thedefiapp.ui.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.thedefiapp.ui.theme.Gray

@ExperimentalCoilApi
@Composable
fun TokenLogoWithChainLogo(protocolLogoUrl: String, chainLogoUrl: String, protocolLogoSize: Dp) {
    val chainLogoSize: Dp = protocolLogoSize / 2
    val boxSize: Dp = (chainLogoSize.value * .25f).dp + protocolLogoSize
    Box(modifier = Modifier.size(boxSize)) {
        Image(
            painter = rememberImagePainter(data = protocolLogoUrl),
            contentDescription = null,
            modifier = Modifier
                .size(protocolLogoSize)
                .clip(CircleShape)
                .background(Gray)
                .align(Alignment.BottomStart)
        )
        Image(
            painter = rememberImagePainter(data = chainLogoUrl),
            contentDescription = null,
            modifier = Modifier
                .size(chainLogoSize)
                .clip(CircleShape)
                .background(Gray)
                .align(Alignment.TopEnd)
        )
    }
}
