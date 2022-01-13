package com.thedefiapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import coil.annotation.ExperimentalCoilApi
import com.thedefiapp.R
import com.thedefiapp.data.models.OverViewBalance
import com.thedefiapp.data.models.details.FarmingPool
import com.thedefiapp.data.models.details.FarmingProtocol
import com.thedefiapp.data.models.details.TokenInWallet
import com.thedefiapp.ui.common.PoolLogos
import com.thedefiapp.ui.common.TokenLogoWithChainLogo
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.Times

@Composable
fun DetailsScreenSkeleton(
    overview: OverViewBalance,
    onClosePressed: () -> Unit,
    header: @Composable RowScope.() -> Unit,
    scrollableContent: @Composable () -> Unit
) {
    Column(Modifier.fillMaxHeight()) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(dimensionResource(id = R.dimen.padding_normal)),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { onClosePressed() },
                modifier = Modifier.size(dimensionResource(id = R.dimen.icon_size_small))
            ) {
                Icon(
                    imageVector = FontAwesomeIcons.Solid.Times,
                    contentDescription = null
                )
            }
            Spacer(modifier = Modifier.padding(start = dimensionResource(id = R.dimen.padding_normal)))
            Text(text = stringResource(id = overview.type.title))
            header()
        }
        Divider(
            thickness = dimensionResource(id = R.dimen.bottom_separator_thickness),
            color = Color.Gray
        )
        scrollableContent()
    }
}

@Composable
fun ValueHeaderSimple(balance: String, modifier: Modifier = Modifier) {
    Text(
        text = stringResource(id = R.string.fiat_price, balance),
        modifier = modifier,
        textAlign = TextAlign.End,
        color = MaterialTheme.colors.primary,
        style = MaterialTheme.typography.subtitle2
    )
}

@Composable
fun ValueHeaderWithEarnings(
    balance: String,
    earnings: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = stringResource(id = R.string.fiat_price, balance),
        modifier = modifier,
        textAlign = TextAlign.End,
        style = MaterialTheme.typography.subtitle2
    )
    Spacer(modifier = Modifier.padding(start = dimensionResource(id = R.dimen.padding_small)))
    Text(
        text = stringResource(id = R.string.portfolio_daily_earnings, earnings),
        style = MaterialTheme.typography.caption,
        color = MaterialTheme.colors.primary
    )
}

@Composable
fun BottomDivider(isLastItem: Boolean, modifier: Modifier = Modifier) {
    if (!isLastItem)
        Divider(
            thickness = dimensionResource(id = R.dimen.bottom_separator_thickness),
            color = Color.Gray.copy(alpha = .4f), modifier = modifier
        )
}

@ExperimentalCoilApi
@Composable
fun TokensInWallet(tokens: List<TokenInWallet>) {
    LazyColumn(contentPadding = PaddingValues(vertical = dimensionResource(id = R.dimen.padding_small))) {
        itemsIndexed(tokens) { index, it ->
            TokenInWalletCard(it)
            BottomDivider(
                isLastItem = index == tokens.lastIndex,
                modifier = Modifier.padding(
                    end = dimensionResource(id = R.dimen.padding_small),
                    start = dimensionResource(id = R.dimen.padding_small) + dimensionResource(id = R.dimen.icon_size_wrapped)
                            + dimensionResource(id = R.dimen.padding_small)
                )
            )
        }
    }
}

@ExperimentalCoilApi
@Composable
fun TokenInWalletCard(token: TokenInWallet) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(dimensionResource(id = R.dimen.padding_small)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TokenLogoWithChainLogo(
            token.tokenLogoUrl,
            token.chainLogoUrl,
            dimensionResource(id = R.dimen.icon_size_normal)
        )
        Spacer(modifier = Modifier.padding(start = dimensionResource(id = R.dimen.padding_small)))
        Column(Modifier.weight(1f)) {
            Text(
                text = token.name,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.body2
            )
            Text(
                text = stringResource(id = R.string.token_price, token.price),
                style = MaterialTheme.typography.caption
            )
        }
        Column(Modifier.weight(2f), horizontalAlignment = Alignment.End) {
            Text(
                text = stringResource(id = R.string.token_amount, token.amount, token.ticker),
                style = MaterialTheme.typography.body2,
                color = MaterialTheme.colors.secondary
            )
            Text(
                text = stringResource(id = R.string.fiat_price, token.amountValue),
                style = MaterialTheme.typography.caption
            )
        }
    }
}

@ExperimentalCoilApi
@Composable
fun FarmingItems(tokens: List<FarmingProtocol>) {
    LazyColumn(contentPadding = PaddingValues(vertical = dimensionResource(id = R.dimen.padding_small))) {
        itemsIndexed(tokens) { index, it ->
            FarmingCard(it)
            BottomDivider(
                isLastItem = index == tokens.lastIndex,
                Modifier.padding(horizontal = dimensionResource(id = R.dimen.padding_small))
            )
        }
    }
}

@ExperimentalCoilApi
@Composable
fun FarmingCard(protocol: FarmingProtocol) {
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
        FarmingHeaderInCard()
        Column(Modifier.fillMaxWidth()) {
            protocol.pools.forEach { farmingPool ->
                Row(Modifier.fillMaxWidth()) {
                    PoolLogos(farmingPool.tokensLogoUrls)
                    Spacer(modifier = Modifier.padding(start = dimensionResource(id = R.dimen.padding_small)))
                    StakedBalance(staked = farmingPool.staked, modifier = Modifier.weight(1f))
                    RewardsApr(farmingPool = farmingPool, modifier = Modifier.weight(1f))
                    FarmingValue(farmingPool = farmingPool, modifier = Modifier.weight(.5f))
                }
            }
        }
    }
}

@ExperimentalCoilApi
@Composable
fun ProtocolHeader(
    protocolLogoUrl: String,
    chainLogoUrl: String,
    name: String,
    balance: String,
    earnings: String
) {
    Row {
        TokenLogoWithChainLogo(
            protocolLogoUrl = protocolLogoUrl,
            chainLogoUrl = chainLogoUrl,
            protocolLogoSize = dimensionResource(id = R.dimen.icon_size_small)
        )
        Spacer(modifier = Modifier.padding(start = dimensionResource(id = R.dimen.padding_small)))
        Text(text = name, modifier = Modifier.alignByBaseline())
        Text(
            text = stringResource(id = R.string.fiat_price, balance),
            modifier = Modifier
                .weight(1f)
                .alignByBaseline(),
            textAlign = TextAlign.End,
            style = MaterialTheme.typography.subtitle2
        )
        Spacer(modifier = Modifier.padding(start = dimensionResource(id = R.dimen.padding_small)))
        Text(
            text = stringResource(id = R.string.portfolio_daily_earnings, earnings),
            style = MaterialTheme.typography.caption,
            color = MaterialTheme.colors.primary,
            modifier = Modifier.alignByBaseline()
        )
    }
}

@Composable
private fun FarmingHeaderInCard() {
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
            text = stringResource(id = R.string.protocol_rewards_apr),
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

@Composable
private fun StakedBalance(staked: Map<String, String>, modifier: Modifier = Modifier) {
    Column(modifier) {
        staked.forEach {
            AmountWithTicker(amount = it.key, ticker = it.value)
        }
    }
}

@Composable
private fun RewardsApr(farmingPool: FarmingPool, modifier: Modifier = Modifier) {
    Column(modifier) {
        AmountWithTicker(farmingPool.rewardsAmount, farmingPool.rewardsTicker)
        Text(
            text = stringResource(id = R.string.price_in_parenthesis, farmingPool.rewardsValue),
            style = MaterialTheme.typography.caption,
        )
        Text(
            text = stringResource(id = R.string.number_percentage, farmingPool.apr),
            style = MaterialTheme.typography.subtitle2,
        )
    }
}

@Composable
private fun FarmingValue(farmingPool: FarmingPool, modifier: Modifier = Modifier) {
    Column(modifier, horizontalAlignment = Alignment.End) {
        Text(
            text = stringResource(id = R.string.fiat_price, farmingPool.stakedValue),
            style = MaterialTheme.typography.subtitle2,
            textAlign = TextAlign.End
        )
        Text(
            text = stringResource(
                id = R.string.portfolio_daily_earnings,
                farmingPool.dailyEarnings
            ), style = MaterialTheme.typography.subtitle2,
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
    Text(text = text)
}
