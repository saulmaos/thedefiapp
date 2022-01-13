package com.thedefiapp.data.repositories

import com.thedefiapp.utils.NumberFormatter

interface NumberFormatterRepository {
    fun changeNumber(number: Double): NumberFormatter.Builder
    fun setIsCrypto(isCrypto: Boolean): NumberFormatter.Builder
    fun changeAmountOfDecimals(amount: Int): NumberFormatter.Builder
    fun decideAmountDecimalsBasedOnNumberLength(decide: Boolean): NumberFormatter.Builder
    fun build(): String
}
