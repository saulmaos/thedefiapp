package com.thedefiapp.ui.common

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import com.thedefiapp.R
import com.thedefiapp.data.models.OverViewBalance
import kotlin.math.ceil

@Composable
fun FixedGrid(
    columns: Int,
    items: List<OverViewBalance>,
    modifier: Modifier = Modifier,
    content: @Composable (OverViewBalance) -> Unit
) {
    val rows = ceil(items.size.toFloat() / columns).toInt()
    var index = 0
    Column(modifier = modifier.fillMaxWidth()) {
        for (i in 0 until rows) {
            Row {
                for (j in 0 until columns) {
                    if (index < items.size) {
                        Box(
                            modifier = Modifier.weight(1f, fill = true),
                            propagateMinConstraints = true
                        ) {
                            content(items[index])
                        }
                        if (j != columns - 1) {
                            Spacer(modifier = Modifier.padding(end = dimensionResource(id = R.dimen.padding_normal)))
                        }
                    } else {
                        Spacer(Modifier.weight(1f, fill = true))
                    }
                    index++
                }
            }
            Spacer(modifier = Modifier.padding(top = dimensionResource(id = R.dimen.padding_small)))
        }
    }
}
