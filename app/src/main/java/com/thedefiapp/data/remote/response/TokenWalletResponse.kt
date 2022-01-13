package com.thedefiapp.data.remote.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TokenWalletResponse(
    @Expose
    @SerializedName("id")
    val id: String,
    @Expose
    @SerializedName("chain")
    val chain: String,
    @Expose
    @SerializedName("name")
    val name: String,
    @Expose
    @SerializedName("symbol")
    val symbol: String,
    @Expose
    @SerializedName("decimals")
    val decimals: Int,
    @Expose
    @SerializedName("logo_url")
    val logoUrl: String?,
    @Expose
    @SerializedName("protocol_id")
    val protocolId: String,
    @Expose
    @SerializedName("price")
    val price: Double?,
    @Expose
    @SerializedName("is_verified")
    val isVerified: Boolean?,
    @Expose
    @SerializedName("is_core")
    val isCore: Boolean?,
    @Expose
    @SerializedName("is_wallet")
    val isWallet: Boolean,
    @Expose
    @SerializedName("time_at")
    val timeAt: Long?,
    @Expose
    @SerializedName("amount")
    val amount: Double,
)
