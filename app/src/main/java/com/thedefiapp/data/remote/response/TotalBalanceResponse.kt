package com.thedefiapp.data.remote.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TotalBalanceResponse(
    @Expose
    @SerializedName("total_usd_value")
    val totalUsdValue: Double,
    @Expose
    @SerializedName("chain_list")
    val chains: List<ChainResponse>
)
