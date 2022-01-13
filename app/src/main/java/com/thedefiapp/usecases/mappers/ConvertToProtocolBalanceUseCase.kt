package com.thedefiapp.usecases.mappers

import com.thedefiapp.data.models.ProtocolBalance
import com.thedefiapp.data.remote.response.ComplexProtocolListResponse
import com.thedefiapp.usecases.utils.ConvertToFormattedNumberUseCase
import com.thedefiapp.utils.toChainLogoUrl
import javax.inject.Inject

class ConvertToProtocolBalanceUseCase @Inject constructor(
    private val convertToFormattedNumberUseCase: ConvertToFormattedNumberUseCase
) {
    operator fun invoke(complexProtocolListResponse: List<ComplexProtocolListResponse>): List<ProtocolBalance> {
        return complexProtocolListResponse
            .sortedByDescending { protocol ->
                var totalBalance = 0.0
                protocol.portfolioItems.forEach {
                    totalBalance += it.stats.netUsdValue
                }
                totalBalance
            }
            .map { protocol ->
                var totalBalance = 0.0
                val labels = mutableListOf<String>()
                protocol.portfolioItems.forEach {
                    totalBalance += it.stats.netUsdValue
                    if (labels.contains(it.name).not()) labels.add(it.name)
                }
                ProtocolBalance(
                    protocolName = protocol.name,
                    protocolTypes = labels,
                    balance = convertToFormattedNumberUseCase(totalBalance),
                    iconUrl = protocol.logoUrl,
                    chainLogoUrl = protocol.chain.toChainLogoUrl()
                )
            }
    }
}
