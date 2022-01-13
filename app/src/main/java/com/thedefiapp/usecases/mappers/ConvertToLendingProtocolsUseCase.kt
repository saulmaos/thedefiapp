package com.thedefiapp.usecases.mappers

import com.thedefiapp.data.models.details.LendingPool
import com.thedefiapp.data.models.details.LendingProtocol
import com.thedefiapp.data.remote.response.ComplexProtocolListResponse
import com.thedefiapp.data.remote.response.ProtocolTokenResponse
import com.thedefiapp.usecases.utils.ConvertToFormattedNumberUseCase
import com.thedefiapp.utils.DAYS_365
import com.thedefiapp.utils.DEFAULT_AMOUNT_OF_DECIMALS_FOR_PERCENTAGES
import com.thedefiapp.utils.orZero
import com.thedefiapp.utils.toChainLogoUrl
import javax.inject.Inject

class ConvertToLendingProtocolsUseCase @Inject constructor(
    private val convertToFormattedNumberUseCase: ConvertToFormattedNumberUseCase
) {
    operator fun invoke(
        complexProtocolListResponse: List<ComplexProtocolListResponse>,
        type: String
    ): List<LendingProtocol> {
        val lendingProtocols = mutableListOf<LendingProtocol>()

        complexProtocolListResponse.forEach { complexProtocol ->
            var totalValue = 0.0
            var totalDailyEarnings = 0.0
            var healthRate = 0.0
            val suppliedTokens = mutableListOf<LendingPool>()
            val borrowedTokens = mutableListOf<LendingPool>()
            complexProtocol.portfolioItems
                .filter { portfolioItem ->
                    return@filter portfolioItem.name == type
                }.map { pool ->
                    totalValue += pool.stats.netUsdValue
                    totalDailyEarnings += pool.stats.dailyNetYieldUsdValue
                    healthRate = pool.detail.healthRate.orZero()
                    suppliedTokens.addAll(createLendingPools(pool.detail.supplyTokens))
                    borrowedTokens.addAll(createLendingPools(pool.detail.borrowTokens))
                }
            if (suppliedTokens.isNotEmpty())
                lendingProtocols.add(
                    LendingProtocol(
                        name = complexProtocol.name,
                        protocolLogoUrl = complexProtocol.logoUrl,
                        chainLogoUrl = complexProtocol.chain.toChainLogoUrl(),
                        totalValue = convertToFormattedNumberUseCase(
                            totalValue,
                            decideAmountDecimalsBasedOnNumberLength = true
                        ),
                        totalDailyEarnings = convertToFormattedNumberUseCase(
                            totalDailyEarnings,
                            decideAmountDecimalsBasedOnNumberLength = true
                        ),
                        suppliedTokens = suppliedTokens,
                        borrowedTokens = borrowedTokens,
                        healthRate = convertToFormattedNumberUseCase(
                            healthRate,
                            decideAmountDecimalsBasedOnNumberLength = true
                        )
                    )
                )
        }

        return lendingProtocols
    }

    private fun createLendingPools(tokens: List<ProtocolTokenResponse>?): List<LendingPool> {
        val lendingPools = mutableListOf<LendingPool>()
        tokens?.forEach {
            val apr = it.dailyYieldRate.orZero() * DAYS_365 * 100
            val farmApr = it.dailyFarmRate.orZero() * DAYS_365 * 100
            val balanceValue = it.amount * it.price.orZero()
            val pool = LendingPool(
                tokenLogoUrl = it.logoUrl.orEmpty(),
                balance = convertToFormattedNumberUseCase(
                    it.amount,
                    decideAmountDecimalsBasedOnNumberLength = true
                ),
                balanceTicker = it.symbol,
                apr = convertToFormattedNumberUseCase(
                    apr,
                    isCrypto = false,
                    amountOfDecimals = DEFAULT_AMOUNT_OF_DECIMALS_FOR_PERCENTAGES
                ),
                farmApr = convertToFormattedNumberUseCase(
                    farmApr,
                    isCrypto = false,
                    amountOfDecimals = DEFAULT_AMOUNT_OF_DECIMALS_FOR_PERCENTAGES
                ),
                balanceValue = convertToFormattedNumberUseCase(
                    balanceValue,
                    decideAmountDecimalsBasedOnNumberLength = true
                )
            )
            lendingPools.add(pool)
        }
        return lendingPools
    }
}
