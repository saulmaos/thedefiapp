package com.thedefiapp.usecases.balancecalculator

import com.thedefiapp.data.remote.response.TokenWalletResponse
import com.thedefiapp.utils.orZero
import javax.inject.Inject

class CalculateWalletBalanceUseCase @Inject constructor() {
    operator fun invoke(tokens: List<TokenWalletResponse>): Double {
        var totalBalance = 0.0
        tokens
            .forEach {
                val balance: Double = it.amount * it.price.orZero()
                totalBalance += balance
            }
        return totalBalance
    }
}
