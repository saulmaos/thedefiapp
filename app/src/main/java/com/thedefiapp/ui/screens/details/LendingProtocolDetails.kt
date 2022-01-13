package com.thedefiapp.ui.screens.details

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.thedefiapp.R
import com.thedefiapp.data.models.details.LendingPool
import com.thedefiapp.data.models.details.LendingProtocol
import com.thedefiapp.ui.screens.BottomDivider
import com.thedefiapp.ui.screens.ProtocolHeader
import com.thedefiapp.ui.theme.Gray

@ExperimentalCoilApi
@Composable
fun LendingItems(tokens: List<LendingProtocol>) {
    LazyColumn(contentPadding = PaddingValues(vertical = dimensionResource(id = R.dimen.padding_small))) {
        itemsIndexed(tokens) { index, it ->
            LendingCard(it)
            BottomDivider(
                isLastItem = index == tokens.lastIndex,
                Modifier.padding(horizontal = dimensionResource(id = R.dimen.padding_small))
            )
        }
    }
}

@ExperimentalCoilApi
@Composable
private fun LendingCard(lendingProtocol: LendingProtocol) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(dimensionResource(id = R.dimen.padding_small))
    ) {
        ProtocolHeader(
            protocolLogoUrl = lendingProtocol.protocolLogoUrl,
            chainLogoUrl = lendingProtocol.chainLogoUrl,
            name = lendingProtocol.name,
            balance = lendingProtocol.totalValue,
            earnings = lendingProtocol.totalDailyEarnings
        )
        Spacer(modifier = Modifier.padding(top = dimensionResource(id = R.dimen.padding_small)))
        Text(
            text = stringResource(id = R.string.lending_health_rate, lendingProtocol.healthRate),
            style = MaterialTheme.typography.body2,
        )
        LendingHeaderInCard(stringResource(id = R.string.lending_supplied))
        LendingPoolItems(lendingProtocol.suppliedTokens)
        if (lendingProtocol.borrowedTokens.isNullOrEmpty().not()) {
            Spacer(modifier = Modifier.padding(top = dimensionResource(id = R.dimen.padding_small)))
            LendingHeaderInCard(stringResource(id = R.string.lending_borrowed))
            LendingPoolItems(lendingProtocol.borrowedTokens)
        }
    }
}

@Composable
private fun LendingHeaderInCard(poolType: String) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(
            text = poolType,
            style = MaterialTheme.typography.body2
        )
        Text(
            text = stringResource(id = R.string.lending_balance),
            style = MaterialTheme.typography.body2,
            textAlign = TextAlign.Center
        )
        Text(
            text = stringResource(id = R.string.lending_apr_and_farm_apr),
            style = MaterialTheme.typography.body2,
            textAlign = TextAlign.Center
        )
        Text(
            text = stringResource(id = R.string.lending_value),
            style = MaterialTheme.typography.body2,
            textAlign = TextAlign.End
        )
    }
}

@ExperimentalCoilApi
@Composable
private fun LendingPoolItems(pools: List<LendingPool>) {
    pools.forEach { pool ->
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Image(
                painter = rememberImagePainter(data = pool.tokenLogoUrl),
                contentDescription = null,
                modifier = Modifier
                    .size(dimensionResource(id = R.dimen.icon_size_small))
                    .clip(CircleShape)
                    .background(Gray)
            )
            Spacer(modifier = Modifier.padding(start = dimensionResource(id = R.dimen.padding_small)))
            Text(
                text = stringResource(id = R.string.token_amount, pool.balance, pool.balanceTicker),
                style = MaterialTheme.typography.subtitle2,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )
            Text(
                text = stringResource(id = R.string.two_number_percentage, pool.apr, pool.farmApr),
                style = MaterialTheme.typography.subtitle2,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )
            Text(
                text = stringResource(id = R.string.fiat_price, pool.balanceValue),
                style = MaterialTheme.typography.subtitle2,
                modifier = Modifier.weight(.5f),
                textAlign = TextAlign.End
            )
        }
        Spacer(modifier = Modifier.padding(top = dimensionResource(id = R.dimen.padding_extra_small)))
    }
}
