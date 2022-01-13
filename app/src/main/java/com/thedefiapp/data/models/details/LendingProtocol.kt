package com.thedefiapp.data.models.details

import androidx.compose.runtime.Immutable

@Immutable
data class LendingProtocol(
    val name: String,
    val protocolLogoUrl: String,
    val chainLogoUrl: String,
    val healthRate: String,
    val totalValue: String,
    val totalDailyEarnings: String,
    val suppliedTokens: List<LendingPool>,
    val borrowedTokens: List<LendingPool> = emptyList()
)

@Immutable
data class LendingPool(
    val tokenLogoUrl: String,
    val balance: String,
    val balanceTicker: String,
    val apr: String,
    val farmApr: String,
    val balanceValue: String
)
