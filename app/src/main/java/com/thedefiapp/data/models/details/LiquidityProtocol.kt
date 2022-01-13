package com.thedefiapp.data.models.details

import androidx.compose.runtime.Immutable

@Immutable
data class LiquidityProtocol(
    val name: String,
    val protocolLogoUrl: String,
    val chainLogoUrl: String,
    val totalValue: String,
    val totalDailyEarnings: String,
    val pools: List<LiquidityPool>
)

@Immutable
data class LiquidityPool(
    val tokensLogoUrls: List<String>,
    val balance: Map<String, String>,
    val balanceValue: String
)