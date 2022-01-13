package com.thedefiapp.data.models

import androidx.compose.runtime.Immutable
import com.thedefiapp.utils.Overview

@Immutable
data class PortfolioTotalBalance(
    val totalBalance: String,
    val todayEarnings: String,
    val address: String
)

@Immutable
data class BlockChainBalance(
    val chain: String,
    val balance: String,
    val balancePercentage: String,
    val iconUrl: String
)

@Immutable
data class OverViewBalance(
    val type: Overview,
    val balance: String,
    val dailyEarnings: String
)

@Immutable
data class ProtocolBalance(
    val protocolName: String,
    val protocolTypes: List<String>,
    val balance: String,
    val iconUrl: String,
    val chainLogoUrl: String
)

data class Portfolio(
    val portfolioTotalBalance: PortfolioTotalBalance,
    val blockChains: List<BlockChainBalance>,
    val overViews: List<OverViewBalance>,
    val protocols: List<ProtocolBalance>
)
