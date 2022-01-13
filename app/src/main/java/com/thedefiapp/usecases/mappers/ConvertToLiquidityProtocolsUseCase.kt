package com.thedefiapp.usecases.mappers

import com.thedefiapp.data.models.details.LiquidityPool
import com.thedefiapp.data.models.details.LiquidityProtocol
import com.thedefiapp.data.remote.response.ComplexProtocolListResponse
import com.thedefiapp.data.remote.response.ProtocolDetailResponse
import com.thedefiapp.usecases.utils.ConvertToFormattedNumberUseCase
import com.thedefiapp.utils.toChainLogoUrl
import javax.inject.Inject

class ConvertToLiquidityProtocolsUseCase @Inject constructor(
    private val convertToFormattedNumberUseCase: ConvertToFormattedNumberUseCase
) {
    operator fun invoke(
        complexProtocolListResponse: List<ComplexProtocolListResponse>,
        type: String
    ): List<LiquidityProtocol> {
        val liquidityProtocols: MutableList<LiquidityProtocol> = mutableListOf()
        complexProtocolListResponse.forEach { complexProtocol ->
            var totalValue = 0.0
            var totalDailyEarnings = 0.0
            val liquidityPools: List<LiquidityPool> = complexProtocol.portfolioItems
                .filter { portfolioItem ->
                    return@filter portfolioItem.name == type
                }.map { pool ->
                    totalValue += pool.stats.netUsdValue
                    totalDailyEarnings += pool.stats.dailyNetYieldUsdValue
                    createLiquidityPool(
                        pool.detail,
                        pool.stats.dailyNetYieldUsdValue,
                        pool.stats.netUsdValue
                    )
                }
            if (liquidityPools.isNotEmpty())
                liquidityProtocols.add(
                    LiquidityProtocol(
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
                        pools = liquidityPools
                    )
                )
        }
        return liquidityProtocols
    }

    private fun createLiquidityPool(
        detail: ProtocolDetailResponse,
        dailyEarnings: Double,
        stakedValue: Double
    ): LiquidityPool {
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

        return LiquidityPool(
            tokensLogoUrls = poolImages,
            balance = staked,
            balanceValue = convertToFormattedNumberUseCase(
                stakedValue,
                decideAmountDecimalsBasedOnNumberLength = true
            )
        )
    }
}
