package com.thedefiapp.utils

import com.thedefiapp.data.repositories.NumberFormatterRepository
import java.text.DecimalFormatSymbols
import java.util.*
import javax.inject.Inject

class NumberFormatter private constructor() {
    private companion object {
        const val BLANK_SPACE: String = " "
        const val POINT: String = "."
        const val COMA: String = ","
        const val REGEX_FOR_ZEROS_WITH_POINT_AS_DECIMAL_SEPARATOR = "\\.?0+\$"
        const val REGEX_FOR_ZEROS_WITH_COMA_AS_DECIMAL_SEPARATOR = ",?0+\$"
        const val REGEX_FOR_ZEROS_WITH_SPACE_AS_DECIMAL_SEPARATOR = "\\s?0+\$"
        const val REGEX_FOR_8_ZEROS_WITH_SPACE_AS_DECIMAL_SEPARATOR = "\\s{1,1}0{8,15}\$"
        const val REGEX_FOR_8_ZEROS_WITH_POINT_AS_DECIMAL_SEPARATOR = "\\.{1,1}0{8,15}\$"
        const val REGEX_FOR_8_ZEROS_WITH_COMA_AS_DECIMAL_SEPARATOR = ",{1,1}0{8,15}\$"
        const val ZERO = "0"
        const val NEGATIVE_ZERO = "-0"
        const val DEFAULT_DECIMALS_IN_CRYPTO = 8
        const val DEFAULT_DECIMALS_IN_FIAT = 2
        const val MIN_VALUE_WITH_ZERO_DECIMALS = 1000
        const val MIN_VALUE_WITH_TWO_DECIMALS = 1
    }

    private var formattedNumber: String = EMPTY_STRING
    private val decimalSeparator = DecimalFormatSymbols().decimalSeparator.toString()
    private var amountOfDecimals: Int = DEFAULT_DECIMALS_IN_FIAT
    private var number: Double = 0.0
    private var isCrypto: Boolean = false

    fun setNumber(
        number: Double,
        isCrypto: Boolean,
        decideAmountDecimalsBasedOnNumberLength: Boolean = false
    ) {
        this.number = number
        this.isCrypto = isCrypto
        this.amountOfDecimals = if (decideAmountDecimalsBasedOnNumberLength)
            calculateAmountOfDecimalsBaseOnNumberLength()
        else calculateAmountOfDecimals()
    }

    private fun calculateAmountOfDecimalsBaseOnNumberLength(): Int {
        return when {
            number >= MIN_VALUE_WITH_ZERO_DECIMALS -> 0
            number >= MIN_VALUE_WITH_TWO_DECIMALS -> DEFAULT_DECIMALS_IN_FIAT
            else -> calculateAmountOfDecimals()
        }
    }

    fun format() {
        formattedNumber = Formatter().format("%,.${amountOfDecimals}f", number).toString()
        removeZeroDecimals()
        removeMoreThan7ZeroDecimals()
        if (formattedNumber.isBlank()) formattedNumber = ZERO
        if (formattedNumber == NEGATIVE_ZERO) formattedNumber = ZERO
    }

    fun changeAmountOfDecimals(amount: Int) {
        amountOfDecimals = amount
    }

    override fun toString(): String = formattedNumber

    private fun calculateAmountOfDecimals(): Int {
        return if (isCrypto) DEFAULT_DECIMALS_IN_CRYPTO + calculateAmountOfExtraDecimalsForSmallCryptoPrices()
        else DEFAULT_DECIMALS_IN_FIAT + calculateAmountOfExtraDecimalsForSmallFiatPrices()
    }

    private fun calculateAmountOfExtraDecimalsForSmallFiatPrices(): Int {
        if (number == 0.0) return 0
        if (number >= 1) return 0
        return when {
            number < 0.000000001 -> 10
            number < 0.00000001 -> 8
            number < 0.0000001 -> 7
            number < 0.000001 -> 6
            number < 0.00001 -> 5
            number < 0.0001 -> 4
            number < 0.001 -> 3
            number < 0.01 -> 2
            else -> 0
        }
    }

    private fun calculateAmountOfExtraDecimalsForSmallCryptoPrices(): Int {
        if (number == 0.0) return 0
        return when {
            number < 0.00000001 -> 4
            else -> 0
        }
    }

    private fun removeZeroDecimals() {
        if (!formattedNumber.contains(decimalSeparator)) return
        val regex = when (decimalSeparator) {
            BLANK_SPACE -> REGEX_FOR_ZEROS_WITH_SPACE_AS_DECIMAL_SEPARATOR
            POINT -> REGEX_FOR_ZEROS_WITH_POINT_AS_DECIMAL_SEPARATOR
            COMA -> REGEX_FOR_ZEROS_WITH_COMA_AS_DECIMAL_SEPARATOR
            else -> return
        }
        formattedNumber = formattedNumber.replace(Regex(regex), EMPTY_STRING)
    }

    private fun removeMoreThan7ZeroDecimals() {
        val regex = when (decimalSeparator) {
            BLANK_SPACE -> REGEX_FOR_8_ZEROS_WITH_SPACE_AS_DECIMAL_SEPARATOR
            POINT -> REGEX_FOR_8_ZEROS_WITH_POINT_AS_DECIMAL_SEPARATOR
            COMA -> REGEX_FOR_8_ZEROS_WITH_COMA_AS_DECIMAL_SEPARATOR
            else -> return
        }
        formattedNumber = formattedNumber.replace(Regex(regex), EMPTY_STRING)
    }

    class Builder @Inject constructor() : NumberFormatterRepository {
        private var number: Double = 0.0
        private val numberFormatter = NumberFormatter()
        private var isCrypto = false
        private var decideAmountDecimalsBasedOnNumberLength = false
        private var amountOfDecimals = DO_NOT_CHANGE_AMOUNT_OF_DECIMALS

        companion object {
            const val DO_NOT_CHANGE_AMOUNT_OF_DECIMALS = -1
        }

        override fun changeNumber(number: Double): Builder {
            this.number = number
            return this
        }

        override fun setIsCrypto(isCrypto: Boolean): Builder {
            this.isCrypto = isCrypto
            return this
        }

        override fun changeAmountOfDecimals(amount: Int): Builder {
            amountOfDecimals = amount
            return this
        }

        override fun decideAmountDecimalsBasedOnNumberLength(decide: Boolean): Builder {
            this.decideAmountDecimalsBasedOnNumberLength = decide
            this.isCrypto = false
            return this
        }

        override fun build(): String {
            numberFormatter.setNumber(number, isCrypto, decideAmountDecimalsBasedOnNumberLength)
            if (amountOfDecimals != DO_NOT_CHANGE_AMOUNT_OF_DECIMALS) numberFormatter.changeAmountOfDecimals(
                amountOfDecimals
            )
            numberFormatter.format()
            return numberFormatter.toString()
        }
    }
}
