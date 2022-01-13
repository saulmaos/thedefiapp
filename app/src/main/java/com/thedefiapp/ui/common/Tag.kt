package com.thedefiapp.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import com.thedefiapp.R

@Composable
fun TagList(texts: List<String>) {
    Row {
        texts.forEachIndexed { index, it ->
            Tag(text = it)
            if (index != texts.lastIndex)
                Spacer(modifier = Modifier.padding(start = dimensionResource(id = R.dimen.padding_extra_small)))
        }
    }
}

@Composable
fun Tag(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        color = contentColorFor(backgroundColor = MaterialTheme.colors.secondary),
        style = MaterialTheme.typography.caption,
        modifier = modifier
            .clip(RoundedCornerShape(dimensionResource(id = R.dimen.extra_extra_small_rounded_corner)))
            .background(MaterialTheme.colors.secondary)
            .padding(
                start = dimensionResource(id = R.dimen.extra_extra_small_rounded_corner),
                end = dimensionResource(id = R.dimen.extra_extra_small_rounded_corner)
            )
    )
}
