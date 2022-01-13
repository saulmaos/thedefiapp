package com.thedefiapp.data.models.details

import androidx.compose.runtime.Immutable

@Immutable
data class FarmingProtocol(
    val name: String,
    val protocolLogoUrl: String,
    val chainLogoUrl: String,
    val totalValue: String,
    val totalDailyEarnings: String,
    val pools: List<FarmingPool>
)

@Immutable
data class FarmingPool(
    val tokensLogoUrls: List<String>,
    val staked: Map<String, String>,
    val rewardsAmount: String,
    val rewardsTicker: String,
    val rewardsValue: String,
    val dailyEarnings: String,
    val apr: String,
    val stakedValue: String
)
