package com.thedefiapp.data.remote

import com.thedefiapp.data.remote.response.ComplexProtocolListResponse
import com.thedefiapp.data.remote.response.TokenWalletResponse
import com.thedefiapp.data.remote.response.TotalBalanceResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface DebankService {
    @GET(DEBANK_TOTAL_BALANCE)
    suspend fun getTotalBalance(@Query("id") id: String): TotalBalanceResponse

    @GET(DEBANK_COMPLEX_PROTOCOL_LIST)
    suspend fun getComplexProtocolList(@Query("id") id: String): List<ComplexProtocolListResponse>

    @GET(DEBANK_TOKEN_LIST)
    suspend fun getTokensList(
        @Query("id") id: String,
        @Query("is_all") includeDerivativeTokens: Boolean = false
    ): List<TokenWalletResponse>
}
