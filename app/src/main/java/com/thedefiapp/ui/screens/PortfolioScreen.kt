package com.thedefiapp.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.constraintlayout.compose.ConstraintLayout
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.thedefiapp.R
import com.thedefiapp.data.models.*
import com.thedefiapp.ui.common.FixedGrid
import com.thedefiapp.ui.common.Tag
import com.thedefiapp.ui.common.TagList
import com.thedefiapp.ui.common.TokenLogoWithChainLogo
import com.thedefiapp.utils.Overview
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.Cog
import compose.icons.fontawesomeicons.solid.Copy

@ExperimentalCoilApi
@ExperimentalFoundationApi
@Composable
fun PortfolioScreen(
    portfolio: Portfolio,
    onOverviewCardPressed: (type: Overview) -> Unit
) {
    val smallPadding = dimensionResource(id = R.dimen.padding_small)
    val normalPadding = dimensionResource(id = R.dimen.padding_normal)
    LazyColumn(
        Modifier.fillMaxSize(),
        contentPadding = PaddingValues(top = normalPadding, bottom = normalPadding)
    ) {
        item {
            PortfolioTotalBalanceCompose(portfolioTotalBalance = portfolio.portfolioTotalBalance)
        }
        item {
            Spacer(modifier = Modifier.padding(top = normalPadding))
            SectionHeader(
                stringResource(id = R.string.title_blockchains),
                modifier = Modifier.padding(start = normalPadding, end = normalPadding)
            )
            Spacer(modifier = Modifier.padding(top = smallPadding))
            LazyRow(contentPadding = PaddingValues(start = normalPadding, end = normalPadding)) {
                itemsIndexed(portfolio.blockChains) { index, it ->
                    PortfolioBlockchainCard(it)
                    if (index != portfolio.blockChains.lastIndex) {
                        Spacer(modifier = Modifier.padding(end = normalPadding))
                    }
                }
            }
        }
        item {
            Spacer(modifier = Modifier.padding(top = normalPadding))
            SectionHeader(
                stringResource(id = R.string.title_overview),
                modifier = Modifier.padding(start = normalPadding, end = normalPadding)
            )
            Spacer(modifier = Modifier.padding(top = smallPadding))
            FixedGrid(
                columns = 2,
                items = portfolio.overViews,
                modifier = Modifier.padding(start = normalPadding, end = normalPadding)
            ) {
                PortfolioOverviewCard(
                    overViewBalance = it,
                    onOverviewCardPressed = onOverviewCardPressed
                )
            }
        }
        item {
            Spacer(modifier = Modifier.padding(top = normalPadding))
            SectionHeader(
                stringResource(id = R.string.title_protocols),
                modifier = Modifier.padding(start = normalPadding)
            )
            Spacer(modifier = Modifier.padding(top = smallPadding))
        }
        itemsIndexed(portfolio.protocols) { index, it ->
            PortfolioProtocolCard(protocolBalance = it)
            if (index != portfolio.protocols.lastIndex) {
                Spacer(modifier = Modifier.padding(top = smallPadding))
            }
        }
    }
}

@Composable
private fun SectionHeader(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        modifier = modifier,
        color = MaterialTheme.colors.onBackground.copy(alpha = 0.85f),
        style = MaterialTheme.typography.body2
    )
}

@Composable
fun PortfolioTotalBalanceCompose(portfolioTotalBalance: PortfolioTotalBalance) {
    val normalPadding = dimensionResource(id = R.dimen.padding_normal)
    Column(Modifier.padding(start = normalPadding, end = normalPadding)) {
        val smallPadding = dimensionResource(id = R.dimen.padding_small)
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = stringResource(id = R.string.title_portfolio_total_balance),
                style = MaterialTheme.typography.caption,
                modifier = Modifier.weight(1f)
            )
            IconButton(
                onClick = { /*TODO*/ },
                modifier = Modifier.size(dimensionResource(id = R.dimen.icon_size_small))
            ) {
                Icon(imageVector = FontAwesomeIcons.Solid.Cog, contentDescription = null)
            }
        }
        Text(
            text = stringResource(id = R.string.fiat_price, portfolioTotalBalance.totalBalance),
            style = MaterialTheme.typography.h3,
            fontWeight = FontWeight.Medium
        )
        Spacer(modifier = Modifier.padding(top = smallPadding))
        ConstraintLayout {
            val (profits, address, icon) = createRefs()
            Text(
                text = stringResource(
                    id = R.string.portfolio_daily_earnings,
                    portfolioTotalBalance.todayEarnings
                ),
                color = MaterialTheme.colors.primary,
                style = MaterialTheme.typography.subtitle1,
                modifier = Modifier.constrainAs(profits) {
                    start.linkTo(parent.start)
                }
            )
            Tag(
                text = portfolioTotalBalance.address,
                modifier = Modifier.constrainAs(address) {
                    baseline.linkTo(profits.baseline)
                    start.linkTo(profits.end, margin = smallPadding)
                }
            )
            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                IconButton(onClick = { /*TODO*/ }, modifier = Modifier
                    .size(dimensionResource(id = R.dimen.icon_size_extra_small))
                    .constrainAs(icon) {
                        bottom.linkTo(address.bottom)
                        start.linkTo(address.end, margin = smallPadding)
                    }) {
                    Icon(
                        imageVector = FontAwesomeIcons.Solid.Copy,
                        contentDescription = null
                    )
                }
            }
        }
    }
}

@ExperimentalCoilApi
@Composable
fun PortfolioBlockchainCard(blockChainBalance: BlockChainBalance) {
    Card(modifier = Modifier.clickable { }) {
        val smallPadding = dimensionResource(id = R.dimen.padding_small)
        Row(modifier = Modifier.padding(smallPadding)) {
            Image(
                painter = rememberImagePainter(data = blockChainBalance.iconUrl),
                contentDescription = null,
                modifier = Modifier.size(dimensionResource(id = R.dimen.icon_size_normal))
            )
            Spacer(modifier = Modifier.padding(start = smallPadding))
            Column {
                Text(text = blockChainBalance.chain, style = MaterialTheme.typography.body2)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = stringResource(id = R.string.fiat_price, blockChainBalance.balance),
                        style = MaterialTheme.typography.subtitle2,
                        modifier = Modifier.alignByBaseline()
                    )
                    Spacer(modifier = Modifier.padding(start = smallPadding))
                    Text(
                        text = stringResource(
                            id = R.string.number_percentage,
                            blockChainBalance.balancePercentage
                        ),
                        style = MaterialTheme.typography.caption,
                        modifier = Modifier.alignByBaseline()
                    )
                }
            }
        }
    }
}

@Composable
fun PortfolioOverviewCard(
    overViewBalance: OverViewBalance,
    onOverviewCardPressed: (type: Overview) -> Unit
) {
    Card(modifier = Modifier.clickable { onOverviewCardPressed(overViewBalance.type) }) {
        val smallPadding = dimensionResource(id = R.dimen.padding_small)
        Row(modifier = Modifier.padding(smallPadding)) {
            Icon(
                imageVector = overViewBalance.type.imageVector,
                contentDescription = null,
                modifier = Modifier
                    .size(dimensionResource(id = R.dimen.icon_size_normal))
                    .clip(CircleShape)
                    .background(MaterialTheme.colors.primary.copy(alpha = .5f))
                    .padding(dimensionResource(id = R.dimen.padding_for_logo_inside_circle))
            )
            Spacer(modifier = Modifier.padding(start = smallPadding))
            Column {
                Text(
                    text = stringResource(id = overViewBalance.type.title),
                    style = MaterialTheme.typography.body2
                )
                Text(
                    text = stringResource(id = R.string.fiat_price, overViewBalance.balance),
                    style = MaterialTheme.typography.subtitle2
                )
            }
        }
    }
}

@ExperimentalCoilApi
@Composable
fun PortfolioProtocolCard(protocolBalance: ProtocolBalance) {
    val smallPadding = dimensionResource(id = R.dimen.padding_small)
    val normalPadding = dimensionResource(id = R.dimen.padding_normal)
    Card(modifier = Modifier
        .fillMaxWidth()
        .padding(start = normalPadding, end = normalPadding)
        .clickable { }) {
        Row(modifier = Modifier.padding(smallPadding)) {
            TokenLogoWithChainLogo(
                protocolBalance.iconUrl,
                protocolBalance.chainLogoUrl,
                dimensionResource(id = R.dimen.icon_size_normal)
            )
            Spacer(modifier = Modifier.padding(start = smallPadding))
            Column(modifier = Modifier.align(Alignment.CenterVertically)) {
                Text(text = protocolBalance.protocolName, style = MaterialTheme.typography.body2)
                TagList(texts = protocolBalance.protocolTypes)
            }
            Text(
                text = stringResource(id = R.string.fiat_price, protocolBalance.balance),
                textAlign = TextAlign.End,
                modifier = Modifier
                    .weight(1f)
                    .align(Alignment.CenterVertically),
                style = MaterialTheme.typography.subtitle2
            )
        }
    }
}
