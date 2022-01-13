package com.thedefiapp.data.repositories

import com.thedefiapp.data.remote.DebankService
import com.thedefiapp.data.remote.response.ComplexProtocolListResponse
import com.thedefiapp.data.remote.response.TokenWalletResponse
import com.thedefiapp.data.remote.response.TotalBalanceResponse
import javax.inject.Inject

class DebankRepository @Inject constructor(private val debankService: DebankService) {
    suspend fun getTotalBalance(address: String): TotalBalanceResponse =
        debankService.getTotalBalance(address)

    suspend fun getComplexProtocolList(address: String): List<ComplexProtocolListResponse> =
        debankService.getComplexProtocolList(address)

    suspend fun getTokesList(address: String): List<TokenWalletResponse> =
        debankService.getTokensList(address, includeDerivativeTokens = false)
}
