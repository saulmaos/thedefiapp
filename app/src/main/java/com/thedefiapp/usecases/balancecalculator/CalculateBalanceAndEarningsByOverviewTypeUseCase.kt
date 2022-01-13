package com.thedefiapp.usecases.balancecalculator

import com.thedefiapp.data.remote.response.ComplexProtocolListResponse
import javax.inject.Inject

class CalculateBalanceAndEarningsByOverviewTypeUseCase @Inject constructor() {
    operator fun invoke(
        complexProtocolListResponse: List<ComplexProtocolListResponse>,
        type: String
    ): Pair<Double, Double> {
        var balance = 0.0
        var dailyEarnings = 0.0
        complexProtocolListResponse.forEach { complexProtocol ->
            complexProtocol.portfolioItems
                .filter { portfolioItem ->
                    return@filter portfolioItem.name == type
                }.forEach {
                    balance += it.stats.netUsdValue
                    dailyEarnings += it.stats.dailyNetYieldUsdValue
                }
        }
        return Pair(balance, dailyEarnings)
    }
}
