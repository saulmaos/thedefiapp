package com.thedefiapp.data.models.details

import androidx.compose.runtime.Immutable

@Immutable
data class TokenInWallet(
    val tokenLogoUrl: String,
    val chainLogoUrl: String,
    val name: String,
    val ticker: String,
    val price: String,
    val amount: String,
    val amountValue: String
)
