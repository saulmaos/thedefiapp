package com.thedefiapp.utils

import com.google.gson.Gson
import com.thedefiapp.data.models.HttpError
import retrofit2.HttpException

fun String.toChainLogoUrl(): String {
    return when (this) {
        CHAIN_BSC -> BNB_LOGO_URL
        CHAIN_ETH -> ETH_LOG_URL
        CHAIN_MATIC -> MATIC_LOGO_URL
        CHAIN_XDAI -> XDAI_LOGO_URL
        CHAIN_FTM -> FTM_LOGO_URL
        CHAIN_OKT -> OKT_LOGO_URL
        CHAIN_HECO -> HECO_LOGO_URL
        CHAIN_AVAX -> AVAX_LOGO_URL
        else -> ETH_LOG_URL
    }
}

fun String.toShortAddressFormat(): String {
    if (this.length < 9) return this
    val firstPart = this.substring(0, 4)
    val index = this.length - 4
    val secondPart = this.subSequence(index, this.length)
    return "$firstPart...$secondPart"
}

fun Double?.orZero(): Double {
    return this ?: 0.0
}

fun Exception.getHttpError(): HttpError {
    if (this !is HttpException) return HttpError()
    val errorJson = this.response()?.errorBody()?.charStream()?.readText()
    errorJson?.let {
        return Gson().fromJson(errorJson, HttpError::class.java)
    } ?: return HttpError()
}
