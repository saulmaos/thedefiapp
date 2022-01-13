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
import androidx.compose.ui.unit.dp

@Composable
fun TagList(texts: List<String>) {
    Row {
        texts.forEachIndexed { index, it ->
            Tag(text = it)
            if (index != texts.lastIndex)
                Spacer(modifier = Modifier.padding(start = 4.dp))
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
            .clip(RoundedCornerShape(2.dp))
            .background(MaterialTheme.colors.secondary)
            .padding(start = 2.dp, end = 2.dp)
    )
}
