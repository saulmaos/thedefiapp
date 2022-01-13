package com.thedefiapp.usecases.debank

import com.thedefiapp.data.remote.response.ComplexProtocolListResponse
import com.thedefiapp.data.remote.response.TokenWalletResponse
import com.thedefiapp.data.repositories.DebankRepository
import com.thedefiapp.data.remote.response.TotalBalanceResponse
import com.thedefiapp.data.repositories.AddressProvider
import javax.inject.Inject

class GetTotalBalanceUseCase @Inject constructor(
    private val debankRepository: DebankRepository,
    private val addressProvider: AddressProvider
) {
    suspend operator fun invoke(): TotalBalanceResponse {
        return debankRepository.getTotalBalance(addressProvider.getAddress())
    }
}

class GetComplexProtocolListUseCase @Inject constructor(
    private val debankRepository: DebankRepository,
    private val addressProvider: AddressProvider
) {
    suspend operator fun invoke(): List<ComplexProtocolListResponse> {
        return debankRepository.getComplexProtocolList(addressProvider.getAddress())
    }
}

class GetTokensListUseCase @Inject constructor(
    private val debankRepository: DebankRepository,
    private val addressProvider: AddressProvider
) {
    suspend operator fun invoke(): List<TokenWalletResponse> {
        return debankRepository.getTokesList(addressProvider.getAddress())
    }
}
