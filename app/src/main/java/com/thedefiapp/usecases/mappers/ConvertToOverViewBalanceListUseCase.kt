package com.thedefiapp.usecases.mappers

import com.thedefiapp.data.models.OverViewBalance
import com.thedefiapp.data.remote.response.ComplexProtocolListResponse
import com.thedefiapp.data.remote.response.TokenWalletResponse
import com.thedefiapp.usecases.balancecalculator.CalculateBalanceAndEarningsByOverviewTypeUseCase
import com.thedefiapp.usecases.balancecalculator.CalculateWalletBalanceUseCase
import com.thedefiapp.usecases.utils.ConvertToFormattedNumberUseCase
import com.thedefiapp.utils.*
import javax.inject.Inject

class ConvertToOverViewBalanceListUseCase @Inject constructor(
    private val convertToFormattedNumberUseCase: ConvertToFormattedNumberUseCase,
    private val calculateWalletBalanceUseCase: CalculateWalletBalanceUseCase,
    private val calculateBalanceAndEarningsByOverviewTypeUseCase: CalculateBalanceAndEarningsByOverviewTypeUseCase
) {
    operator fun invoke(
        complexProtocolListResponse: List<ComplexProtocolListResponse>,
        tokenWalletResponse: List<TokenWalletResponse>
    ): List<OverViewBalance> {
        val wallet: Double = calculateWalletBalanceUseCase(tokenWalletResponse)
        val liquidityPool =
            calculateBalanceAndEarningsByOverviewTypeUseCase(
                complexProtocolListResponse,
                OVERVIEW_LIQUIDITY
            )
        val deposits = calculateBalanceAndEarningsByOverviewTypeUseCase(
            complexProtocolListResponse,
            OVERVIEW_DEPOSIT
        )
        val farming = calculateBalanceAndEarningsByOverviewTypeUseCase(
            complexProtocolListResponse,
            OVERVIEW_FARMING
        )
        val lending = calculateBalanceAndEarningsByOverviewTypeUseCase(
            complexProtocolListResponse,
            OVERVIEW_LENDING
        )
        val yieldBalance =
            calculateBalanceAndEarningsByOverviewTypeUseCase(
                complexProtocolListResponse,
                OVERVIEW_YIELD
            )
        val stakedBalance =
            calculateBalanceAndEarningsByOverviewTypeUseCase(
                complexProtocolListResponse,
                OVERVIEW_STAKED
            )
        val insuranceBalance =
            calculateBalanceAndEarningsByOverviewTypeUseCase(
                complexProtocolListResponse,
                OVERVIEW_INSURANCE
            )
        val blockedBalance =
            calculateBalanceAndEarningsByOverviewTypeUseCase(
                complexProtocolListResponse,
                OVERVIEW_LOCKED
            )
        val rewardsBalance = calculateRewardsBalance(complexProtocolListResponse)

        val overviews = mutableListOf<OverViewBalance>()
        overviews.addOverView(Overview.WALLET, wallet)
        overviews.addOverView(Overview.LIQUIDITY, liquidityPool.first)
        overviews.addOverView(Overview.YIELD, yieldBalance.first)
        overviews.addOverView(Overview.LENDING, lending.first)
        overviews.addOverView(Overview.FARMING, farming.first)
        overviews.addOverView(Overview.DEPOSITS, deposits.first)
        overviews.addOverView(Overview.STAKED, stakedBalance.first)
        overviews.addOverView(Overview.INSURANCE, insuranceBalance.first)
        overviews.addOverView(Overview.LOCKED, blockedBalance.first)
        overviews.addOverView(Overview.REWARDS, rewardsBalance)
        return overviews
    }

    private fun MutableList<OverViewBalance>.addOverView(overview: Overview, balance: Double) {
        if (balance > 0) this.add(createOverview(overview, balance))
    }

    private fun createOverview(overview: Overview, balance: Double): OverViewBalance {
        return OverViewBalance(
            type = overview,
            balance = convertToFormattedNumberUseCase(balance),
            dailyEarnings = EMPTY_STRING
        )
    }

    private fun calculateRewardsBalance(complexProtocolListResponse: List<ComplexProtocolListResponse>): Double {
        val firstBalance =
            calculateBalanceAndEarningsByOverviewTypeUseCase(
                complexProtocolListResponse,
                OVERVIEW_REWARDS
            )
        var secondBalance = 0.0
        complexProtocolListResponse.forEach { protocol ->
            protocol.portfolioItems.forEach { item ->
                item.detail.rewardTokens?.forEach { rewardToken ->
                    secondBalance += rewardToken.amount * rewardToken.price.orZero()
                }
            }
        }
        return firstBalance.first + secondBalance
    }
}
