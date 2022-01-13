package com.thedefiapp.ui.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.thedefiapp.R
import com.thedefiapp.ui.theme.Gray

@ExperimentalCoilApi
@Composable
fun PoolLogos(tokensLogoUrls: List<String>) {
    val smallSize = dimensionResource(id = R.dimen.icon_size_small)
    val offset = (smallSize.value * -0.25f).dp
    Row {
        tokensLogoUrls.forEachIndexed { index, url ->
            Image(
                painter = rememberImagePainter(data = url),
                contentDescription = null,
                modifier = Modifier
                    .size(smallSize)
                    .offset(x = offset * index)
                    .clip(CircleShape)
                    .background(Gray)
            )
        }
    }
}
