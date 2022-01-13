package com.thedefiapp.usecases.mappers

import com.thedefiapp.data.models.PortfolioTotalBalance
import com.thedefiapp.data.remote.response.ComplexProtocolListResponse
import com.thedefiapp.data.remote.response.TotalBalanceResponse
import com.thedefiapp.data.repositories.AddressProvider
import com.thedefiapp.usecases.utils.ConvertToFormattedNumberUseCase
import com.thedefiapp.utils.DEFAULT_AMOUNT_OF_DECIMALS_FOR_PRICES
import com.thedefiapp.utils.toShortAddressFormat
import javax.inject.Inject

class ConvertToPortfolioTotalBalanceUseCase @Inject constructor(
    private val convertToFormattedNumberUseCase: ConvertToFormattedNumberUseCase,
    private val addressProvider: AddressProvider
) {
    operator fun invoke(
        totalBalanceResponse: TotalBalanceResponse,
        complexProtocolListResponse: List<ComplexProtocolListResponse>
    ): PortfolioTotalBalance {
        var todayEarnings = 0.0
        complexProtocolListResponse.map { protocol ->
            protocol.portfolioItems.forEach {
                todayEarnings += it.stats.dailyNetYieldUsdValue
            }
        }
        return PortfolioTotalBalance(
            totalBalance = convertToFormattedNumberUseCase(totalBalanceResponse.totalUsdValue),
            todayEarnings = convertToFormattedNumberUseCase(
                todayEarnings,
                amountOfDecimals = DEFAULT_AMOUNT_OF_DECIMALS_FOR_PRICES
            ),
            address = addressProvider.getAddress().toShortAddressFormat()
        )
    }
}
