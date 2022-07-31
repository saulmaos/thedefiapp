package com.thedefiapp.data.remote.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ChainResponse(
    @Expose
    @SerializedName("id")
    val id: String,
    @Expose
    @SerializedName("community_id")
    val communityId: Long = 0,
    @Expose
    @SerializedName("name")
    val name: String,
    @Expose
    @SerializedName("logo_url")
    val logoUrl: String,
    @Expose
    @SerializedName("native_token_id")
    val nativeTokenId: String,
    @Expose
    @SerializedName("wrapped_token_id")
    val wrappedNativeTokenAddress: String,
    @Expose
    @SerializedName("usd_value")
    val usdValue: Double
)
