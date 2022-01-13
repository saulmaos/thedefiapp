package com.thedefiapp.usecases.mappers

import com.thedefiapp.data.models.BlockChainBalance
import com.thedefiapp.data.remote.response.TotalBalanceResponse
import com.thedefiapp.usecases.utils.ConvertToFormattedNumberUseCase
import com.thedefiapp.utils.DEFAULT_AMOUNT_OF_DECIMALS_FOR_PERCENTAGES
import javax.inject.Inject

class ConvertToBlockChainBalanceListUseCase @Inject constructor(
    private val convertToFormattedNumberUseCase: ConvertToFormattedNumberUseCase
) {
    operator fun invoke(totalBalanceResponse: TotalBalanceResponse): List<BlockChainBalance> {
        return totalBalanceResponse.chains
            .sortedByDescending { it.usdValue }
            .map {
                val percentage = (it.usdValue / totalBalanceResponse.totalUsdValue) * 100
                BlockChainBalance(
                    chain = it.name,
                    balance = convertToFormattedNumberUseCase(it.usdValue),
                    balancePercentage = convertToFormattedNumberUseCase(
                        percentage,
                        amountOfDecimals = DEFAULT_AMOUNT_OF_DECIMALS_FOR_PERCENTAGES
                    ),
                    iconUrl = it.logoUrl
                )
            }
    }
}
