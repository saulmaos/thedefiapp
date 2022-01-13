package com.thedefiapp.data.remote.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ComplexProtocolListResponse(
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
    @SerializedName("site_url")
    val siteUrl: String,
    @Expose
    @SerializedName("logo_url")
    val logoUrl: String,
    @Expose
    @SerializedName("has_supported_portfolio")
    val hasSupportedPortfolio: Boolean,
    @Expose
    @SerializedName("tvl")
    val tvl: Double,
    @Expose
    @SerializedName("portfolio_item_list")
    val portfolioItems: List<PortfolioItemResponse>
)

data class PortfolioItemResponse(
    @Expose
    @SerializedName("stats")
    val stats: ProtocolStatsResponse,
    @Expose
    @SerializedName("update_at")
    val updateAt: Double,
    @Expose
    @SerializedName("name")
    val name: String,
    @Expose
    @SerializedName("detail_types")
    val detailTypes: List<String>,
    @Expose
    @SerializedName("detail")
    val detail: ProtocolDetailResponse
)

data class ProtocolStatsResponse(
    @Expose
    @SerializedName("asset_usd_value")
    val assetUsdValue: Double,
    @Expose
    @SerializedName("debt_usd_value")
    val debtUsdValue: Double,
    @Expose
    @SerializedName("net_usd_value")
    val netUsdValue: Double,
    @Expose
    @SerializedName("daily_yield_usd_value")
    val dailyYieldUsdValue: Double,
    @Expose
    @SerializedName("daily_cost_usd_value")
    val dailyCostUsdValue: Double,
    @Expose
    @SerializedName("daily_net_yield_usd_value")
    val dailyNetYieldUsdValue: Double
)

data class ProtocolDetailResponse(
    @Expose
    @SerializedName("supply_token_list")
    val supplyTokens: List<ProtocolTokenResponse>,
    @Expose
    @SerializedName("borrow_token_list")
    val borrowTokens: List<ProtocolTokenResponse>?,
    @Expose
    @SerializedName("reward_token_list")
    val rewardTokens: List<ProtocolTokenResponse>?,
    @Expose
    @SerializedName("health_rate")
    val healthRate: Double?,
    @Expose
    @SerializedName("daily_farm_rate")
    val dailyFarmRate: Double?,
)

data class ProtocolTokenResponse(
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
    val isVerified: Boolean,
    @Expose
    @SerializedName("is_core")
    val isCore: Boolean,
    @Expose
    @SerializedName("is_wallet")
    val isWallet: Boolean,
    @Expose
    @SerializedName("time_at")
    val timeAt: Long,
    @Expose
    @SerializedName("amount")
    val amount: Double,
    @Expose
    @SerializedName("daily_yield_rate")
    val dailyYieldRate: Double?,
    @Expose
    @SerializedName("is_collateral")
    val isCollateral: Boolean?,
    @Expose
    @SerializedName("daily_cost_rate")
    val dailyCostRate: Double?,
    @Expose
    @SerializedName("category")
    val category: String?,
    @Expose
    @SerializedName("daily_farm_rate")
    val dailyFarmRate: Double?,
)
