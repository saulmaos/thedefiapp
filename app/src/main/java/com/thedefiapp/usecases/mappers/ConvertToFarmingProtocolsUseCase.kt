package com.thedefiapp.usecases.mappers

import com.thedefiapp.data.models.details.FarmingPool
import com.thedefiapp.data.models.details.FarmingProtocol
import com.thedefiapp.data.remote.response.ComplexProtocolListResponse
import com.thedefiapp.data.remote.response.ProtocolDetailResponse
import com.thedefiapp.usecases.utils.ConvertToFormattedNumberUseCase
import com.thedefiapp.utils.*
import javax.inject.Inject

class ConvertToFarmingProtocolsUseCase @Inject constructor(
    private val convertToFormattedNumberUseCase: ConvertToFormattedNumberUseCase
) {
    operator fun invoke(
        complexProtocolListResponse: List<ComplexProtocolListResponse>,
        type: String
    ): List<FarmingProtocol> {
        val farmingProtocols: MutableList<FarmingProtocol> = mutableListOf()
        complexProtocolListResponse.forEach { complexProtocol ->
            var totalValue = 0.0
            var totalDailyEarnings = 0.0
            val farmingPools: List<FarmingPool> = complexProtocol.portfolioItems
                .filter { portfolioItem ->
                    return@filter portfolioItem.name == type
                }.map { pool ->
                    totalValue += pool.stats.netUsdValue
                    totalDailyEarnings += pool.stats.dailyNetYieldUsdValue
                    createFarmingPool(
                        pool.detail,
                        pool.stats.dailyNetYieldUsdValue,
                        pool.stats.netUsdValue
                    )
                }
            if (farmingPools.isNotEmpty())
                farmingProtocols.add(
                    FarmingProtocol(
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
                        pools = farmingPools
                    )
                )
        }
        return farmingProtocols
    }

    private fun createFarmingPool(
        detail: ProtocolDetailResponse,
        dailyEarnings: Double,
        stakedValue: Double
    ): FarmingPool {
        val poolImages: List<String> = detail.supplyTokens.map {
            it.logoUrl.orEmpty()
        }
        val staked: Map<String, String> = detail.supplyTokens.map {
            convertToFormattedNumberUseCase(
                it.amount,
                isCrypto = true,
                decideAmountDecimalsBasedOnNumberLength = true
            ) to it.symbol
        }.toMap()
        val rewardsAmount: Double = detail.rewardTokens?.first()?.amount.orZero()
        val rewardsTicker = detail.rewardTokens?.first()?.symbol.orEmpty()
        val rewardTokenPrice = detail.rewardTokens?.first()?.price.orZero()
        val rewardsValue = rewardsAmount * rewardTokenPrice
        val apr = detail.dailyFarmRate.orZero() * DAYS_365 * 100

        return FarmingPool(
            tokensLogoUrls = poolImages,
            staked = staked,
            rewardsAmount = convertToFormattedNumberUseCase(
                rewardsAmount,
                isCrypto = true,
                decideAmountDecimalsBasedOnNumberLength = true
            ),
            rewardsTicker = rewardsTicker,
            rewardsValue = convertToFormattedNumberUseCase(
                rewardsValue,
                decideAmountDecimalsBasedOnNumberLength = true
            ),
            dailyEarnings = convertToFormattedNumberUseCase(
                dailyEarnings,
                decideAmountDecimalsBasedOnNumberLength = true
            ),
            apr = convertToFormattedNumberUseCase(
                apr,
                isCrypto = false,
                amountOfDecimals = DEFAULT_AMOUNT_OF_DECIMALS_FOR_PERCENTAGES
            ),
            stakedValue = convertToFormattedNumberUseCase(
                stakedValue,
                decideAmountDecimalsBasedOnNumberLength = true
            )
        )
    }
}
