package com.thedefiapp.usecases.mappers

import com.thedefiapp.data.models.details.TokenInWallet
import com.thedefiapp.data.remote.response.TokenWalletResponse
import com.thedefiapp.usecases.utils.ConvertToFormattedNumberUseCase
import com.thedefiapp.utils.orZero
import com.thedefiapp.utils.toChainLogoUrl
import javax.inject.Inject

class ConvertToTokensInWalletUseCase @Inject constructor(
    private val convertToFormattedNumberUseCase: ConvertToFormattedNumberUseCase
) {
    operator fun invoke(tokenWalletResponse: List<TokenWalletResponse>): List<TokenInWallet> {
        return tokenWalletResponse
            .sortedByDescending {
                val value: Double = it.price?.times(it.amount).orZero()
                value
            }
            .map {
                TokenInWallet(
                    tokenLogoUrl = it.logoUrl.orEmpty(),
                    chainLogoUrl = it.chain.toChainLogoUrl(),
                    name = it.name,
                    ticker = it.symbol,
                    price = convertToFormattedNumberUseCase(it.price.orZero()),
                    amount = convertToFormattedNumberUseCase(it.amount, true),
                    amountValue = convertToFormattedNumberUseCase(
                        it.price?.times(it.amount).orZero()
                    )
                )
            }
    }
}
