package com.thedefiapp.usecases.utils

import com.thedefiapp.data.repositories.NumberFormatterRepository
import com.thedefiapp.utils.NumberFormatter.Builder.Companion.DO_NOT_CHANGE_AMOUNT_OF_DECIMALS
import javax.inject.Inject

class ConvertToFormattedNumberUseCase @Inject constructor(private val numberFormatterRepository: NumberFormatterRepository) {
    operator fun invoke(
        number: Double,
        isCrypto: Boolean = false,
        amountOfDecimals: Int = DO_NOT_CHANGE_AMOUNT_OF_DECIMALS,
        decideAmountDecimalsBasedOnNumberLength: Boolean = false
    ): String =
        numberFormatterRepository
            .changeNumber(number)
            .setIsCrypto(isCrypto)
            .changeAmountOfDecimals(amountOfDecimals)
            .decideAmountDecimalsBasedOnNumberLength(decideAmountDecimalsBasedOnNumberLength)
            .build()
}
