package com.thedefiapp.ui.screens.details

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import coil.annotation.ExperimentalCoilApi
import com.thedefiapp.R
import com.thedefiapp.data.models.details.LiquidityPool
import com.thedefiapp.data.models.details.LiquidityProtocol
import com.thedefiapp.ui.common.PoolLogos
import com.thedefiapp.ui.screens.BottomDivider
import com.thedefiapp.ui.screens.ProtocolHeader

@ExperimentalCoilApi
@Composable
fun LiquidityItems(tokens: List<LiquidityProtocol>) {
    LazyColumn(contentPadding = PaddingValues(vertical = dimensionResource(id = R.dimen.padding_small))) {
        itemsIndexed(tokens) { index, it ->
            LiquidityCard(it)
            BottomDivider(
                isLastItem = index == tokens.lastIndex,
                Modifier.padding(horizontal = dimensionResource(id = R.dimen.padding_small))
            )
        }
    }
}

@ExperimentalCoilApi
@Composable
private fun LiquidityCard(protocol: LiquidityProtocol) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(dimensionResource(id = R.dimen.padding_small))
    ) {
        ProtocolHeader(
            protocolLogoUrl = protocol.protocolLogoUrl,
            chainLogoUrl = protocol.chainLogoUrl,
            name = protocol.name,
            balance = protocol.totalValue,
            earnings = protocol.totalDailyEarnings
        )
        Spacer(modifier = Modifier.padding(top = dimensionResource(id = R.dimen.padding_small)))
        LiquidityHeaderInCard()
        LiquidityPoolItems(protocol.pools)
    }
}

@Composable
private fun LiquidityHeaderInCard() {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(
            text = stringResource(id = R.string.protocol_pool),
            style = MaterialTheme.typography.body2
        )
        Text(
            text = stringResource(id = R.string.protocol_staked),
            style = MaterialTheme.typography.body2,
            textAlign = TextAlign.Center
        )
        Text(
            text = stringResource(id = R.string.protocol_staked_value),
            style = MaterialTheme.typography.body2,
            textAlign = TextAlign.End
        )
    }
}

@ExperimentalCoilApi
@Composable
private fun LiquidityPoolItems(pools: List<LiquidityPool>) {
    pools.forEach { farmingPool ->
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            PoolLogos(farmingPool.tokensLogoUrls)
            StakedBalance(staked = farmingPool.balance)
            LiquidityValue(liquidityPool = farmingPool)
        }
    }
}

@Composable
private fun StakedBalance(staked: Map<String, String>, modifier: Modifier = Modifier) {
    Column(modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        staked.forEach {
            AmountWithTicker(amount = it.key, ticker = it.value)
        }
    }
}

@Composable
private fun LiquidityValue(liquidityPool: LiquidityPool, modifier: Modifier = Modifier) {
    Column(modifier, horizontalAlignment = Alignment.End) {
        Text(
            text = stringResource(id = R.string.fiat_price, liquidityPool.balanceValue),
            style = MaterialTheme.typography.subtitle2,
            textAlign = TextAlign.End
        )
    }
}

@Composable
private fun AmountWithTicker(amount: String, ticker: String) {
    val text = buildAnnotatedString {
        withStyle(MaterialTheme.typography.subtitle2.toSpanStyle()) {
            append("$amount ")
        }
        withStyle(MaterialTheme.typography.caption.toSpanStyle()) {
            append(ticker)
        }
    }
    Text(text = text, textAlign = TextAlign.Center)
}
