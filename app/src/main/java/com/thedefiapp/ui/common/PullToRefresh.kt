package com.thedefiapp.ui.common

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.MutatePriority
import androidx.compose.foundation.MutatorMutex
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.unit.*
import com.thedefiapp.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue
import kotlin.math.roundToInt

@Composable
fun PullToRefresh(
    isRefreshing: Boolean = false,
    height: Dp = 50.dp,
    onRefresh: () -> Unit,
    scrollableContent: @Composable () -> Unit
) {
    val scope = rememberCoroutineScope()
    val state = rememberComposePullToRefreshState(isRefreshing = isRefreshing, height = height)
    state.isRefreshing.value = isRefreshing

    LaunchedEffect(
        !state.isRefreshing.value,
        state.isSwiping.value,
        state.maxOffset
    ) {
        if (!state.isSwiping.value) {
            state.animateBackToRest()
        }
    }

    val connection = remember(state, scope) {
        ComposePullToRefreshNestedScrollCompletion(state, scope) {
            onRefresh()
        }
    }.apply { this.enabled = !state.isRefreshing.value }

    Column(
        modifier = Modifier
            .nestedScroll(connection)
            .fillMaxSize()
    ) {
        val y = (state.indicatorOffset * 0.2f).coerceIn(0f, height.value).roundToInt()
        Loader(y = y, state.isRefreshing.value, state.isSwiping.value, height)
        scrollableContent()
    }
}

class ComposePullToRefreshNestedScrollCompletion(
    private val state: ComposePullToRefreshState,
    private val scope: CoroutineScope,
    private val onRefresh: () -> Unit
) : NestedScrollConnection {
    var enabled: Boolean = false
    override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset = when {
        !enabled -> Offset.Zero
        source == NestedScrollSource.Drag && available.y < 0 -> {
            onScroll(available)
        }
        else -> Offset.Zero
    }

    override fun onPostScroll(
        consumed: Offset,
        available: Offset,
        source: NestedScrollSource
    ): Offset = when {
        !enabled -> Offset.Zero
        source == NestedScrollSource.Drag && available.y > 0 -> {
            onScroll(available)
        }
        else -> Offset.Zero
    }

    private fun onScroll(available: Offset): Offset {
        state.isSwiping.value = true
        val delta = available.y
        val newOffset = (state.indicatorOffset + delta).coerceAtLeast(0f)

        return if (newOffset.absoluteValue >= 0.5f) {
            scope.launch {
                state.updateIndicator(delta)
            }
            Offset(x = 0f, y = delta)
        } else {
            Offset.Zero
        }
    }

    override suspend fun onPreFling(available: Velocity): Velocity = when {
        state.isSwiping.value && state.indicatorOffset >= state.maxOffset -> {
            onRefresh()
            Velocity.Zero
        }
        else -> {
            Velocity.Zero
        }
    }.also {
        state.isSwiping.value = false
    }

}

@Composable
private fun Loader(y: Int, isRefreshing: Boolean, isSwiping: Boolean, layoutSize: Dp) {
    val image = ImageBitmap.imageResource(id = R.drawable.eth_logo)
    val color = MaterialTheme.colors.primary
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.TopCenter
    ) {
        if (isRefreshing) CryptoLoading(height = layoutSize)
        else if (isSwiping || (!isRefreshing && y > 0)) Canvas(
            modifier = Modifier
                .width(layoutSize)
                .height(y.dp)
        ) {
            val height = layoutSize.toPx()
            val rate = height / image.height
            val newWidth = image.width * rate
            val xStart = ((size.width / 2) - (newWidth / 2)).toInt()
            clipRect(bottom = y.dp.toPx()) {
                drawImage(
                    image = image,
                    colorFilter = ColorFilter.tint(color),
                    dstOffset = IntOffset(xStart, 0),
                    dstSize = IntSize(newWidth.toInt(), height.toInt())
                )
            }
        }
    }
}

@Composable
fun rememberComposePullToRefreshState(
    isRefreshing: Boolean,
    height: Dp
): ComposePullToRefreshState {
    return remember {
        ComposePullToRefreshState(isRefreshing, height = height)
    }
}

class ComposePullToRefreshState(isRefreshing: Boolean, height: Dp) {
    private val _indicatorOffset = Animatable(0f)
    private val mutatorMutex = MutatorMutex()
    val isRefreshing = mutableStateOf(isRefreshing)
    val isSwiping = mutableStateOf(false)

    val indicatorOffset: Float
        get() = _indicatorOffset.value.coerceAtMost(maxOffset * 2)

    val maxOffset = height.value * 5

    internal suspend fun updateIndicator(delta: Float) {
        mutatorMutex.mutate(MutatePriority.UserInput) {
            _indicatorOffset.snapTo(_indicatorOffset.value + delta)
        }
    }

    internal suspend fun animateBackToRest() {
        mutatorMutex.mutate {
            _indicatorOffset.animateTo(
                if (isRefreshing.value) maxOffset else 0f,
                animationSpec = tween(durationMillis = 500)
            )
        }
    }
}
