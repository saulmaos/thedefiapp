package com.thedefiapp.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thedefiapp.R
import com.thedefiapp.data.models.BlockChainBalance
import com.thedefiapp.data.models.OverViewBalance
import com.thedefiapp.data.models.Portfolio
import com.thedefiapp.data.models.ProtocolBalance
import com.thedefiapp.data.models.details.FarmingProtocol
import com.thedefiapp.data.models.details.LendingProtocol
import com.thedefiapp.data.models.details.LiquidityProtocol
import com.thedefiapp.data.models.details.TokenInWallet
import com.thedefiapp.data.remote.response.*
import com.thedefiapp.data.repositories.AddressProvider
import com.thedefiapp.data.repositories.StringResourceRepository
import com.thedefiapp.usecases.balancecalculator.CalculateBalanceAndEarningsByOverviewTypeUseCase
import com.thedefiapp.usecases.balancecalculator.CalculateWalletBalanceUseCase
import com.thedefiapp.usecases.debank.GetComplexProtocolListUseCase
import com.thedefiapp.usecases.debank.GetTokensListUseCase
import com.thedefiapp.usecases.debank.GetTotalBalanceUseCase
import com.thedefiapp.usecases.mappers.*
import com.thedefiapp.usecases.room.DeleteAllAddressesUseCase
import com.thedefiapp.usecases.room.GetAllAddressesUseCase
import com.thedefiapp.usecases.room.InsertAddressUseCase
import com.thedefiapp.usecases.utils.ConvertToFormattedNumberUseCase
import com.thedefiapp.utils.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getTotalBalanceUseCase: GetTotalBalanceUseCase,
    private val getAllAddressesUseCase: GetAllAddressesUseCase,
    private val insertAddressUseCase: InsertAddressUseCase,
    private val deleteAllAddressesUseCase: DeleteAllAddressesUseCase,
    private val getComplexProtocolListUseCase: GetComplexProtocolListUseCase,
    private val getTokensListUseCase: GetTokensListUseCase,
    private val convertToOverViewBalanceListUseCase: ConvertToOverViewBalanceListUseCase,
    private val convertToPortfolioTotalBalanceUseCase: ConvertToPortfolioTotalBalanceUseCase,
    private val convertToBlockChainBalanceListUseCase: ConvertToBlockChainBalanceListUseCase,
    private val convertToProtocolBalanceUseCase: ConvertToProtocolBalanceUseCase,
    private val convertToTokensInWalletUseCase: ConvertToTokensInWalletUseCase,
    private val convertToFarmingProtocolsUseCase: ConvertToFarmingProtocolsUseCase,
    private val convertToLendingProtocolsUseCase: ConvertToLendingProtocolsUseCase,
    private val convertToLiquidityProtocolsUseCase: ConvertToLiquidityProtocolsUseCase,
    private val calculateWalletBalanceUseCase: CalculateWalletBalanceUseCase,
    private val calculateBalanceAndEarningsByOverviewTypeUseCase: CalculateBalanceAndEarningsByOverviewTypeUseCase,
    private val convertToFormattedNumberUseCase: ConvertToFormattedNumberUseCase,
    private val stringResourceRepository: StringResourceRepository,
    private val addressProvider: AddressProvider
) : ViewModel() {
    var mainScreenState: MainScreen by mutableStateOf(MainScreen.Loading)
        private set
    var textFieldError: String by mutableStateOf(EMPTY_STRING)
        private set
    var showRefresher: Boolean by mutableStateOf(false)
        private set
    var detailsScreenState: DetailsScreen by mutableStateOf(DetailsScreen.None)
        private set
    var bottomSheetExpandedState: Boolean by mutableStateOf(false)
        private set

    private var tokens: List<TokenWalletResponse>? = null
    private var complexProtocolResponse: List<ComplexProtocolListResponse>? = null

    fun initFlows() = viewModelScope.launch {
        getAllAddressesUseCase().distinctUntilChanged().collectLatest {
            if (it.isEmpty()) {
                mainScreenState = MainScreen.OnBoardingScreen
            } else {
                addressProvider.setAddress(it.first().address)
                requestTotalBalance()
            }
        }
    }

    private fun requestTotalBalance() = viewModelScope.launch {
        try {
            coroutineScope {
                val totalBalanceResponseDeferred = async { getTotalBalanceUseCase() }
                val complexProtocolResponseDeferred = async { getComplexProtocolListUseCase() }
                val tokensDeferred = async { getTokensListUseCase() }

                val totalBalanceResponse: TotalBalanceResponse =
                    totalBalanceResponseDeferred.await()
                complexProtocolResponse = complexProtocolResponseDeferred.await()
                tokens = tokensDeferred.await()

                val portfolioTotalBalance = convertToPortfolioTotalBalanceUseCase(
                    totalBalanceResponse,
                    complexProtocolResponse!!
                )
                val blockChainBalances: List<BlockChainBalance> =
                    convertToBlockChainBalanceListUseCase(totalBalanceResponse)
                val protocols: List<ProtocolBalance> =
                    convertToProtocolBalanceUseCase(complexProtocolResponse!!)

                val portfolio = Portfolio(
                    portfolioTotalBalance = portfolioTotalBalance,
                    blockChains = blockChainBalances,
                    protocols = protocols,
                    overViews = convertToOverViewBalanceListUseCase(
                        complexProtocolResponse!!,
                        tokens!!
                    )
                )
                showRefresher = false
                mainScreenState = MainScreen.PortfolioScreen(portfolio = portfolio)
            }
        } catch (e: Exception) {
            val errorMsg = e.getHttpError().message
            mainScreenState =
                if (errorMsg == HTTP_ERROR_BAD_ADDRESS) MainScreen.ErrorBadAddress(addressProvider.getAddress())
                else MainScreen.ErrorOnRequest
        }
    }

    fun onRetry() {
        mainScreenState = MainScreen.Loading
        requestTotalBalance()
    }

    fun onRefresh() {
        showRefresher = true
        requestTotalBalance()
    }

    fun onOnBoardingScreenSelected() {
        mainScreenState = MainScreen.OnBoardingScreen
    }

    fun onAddAddressPressed(address: String) {
        val addressCleaned = address.trim()
        if (addressCleaned.isBlank()) {
            textFieldError = stringResourceRepository.getString(R.string.error_address_empty)
            return
        }
        saveAddress(address)
    }

    private fun saveAddress(address: String) = viewModelScope.launch {
        deleteAllAddressesUseCase()
        insertAddressUseCase(address)
    }

    fun onAddressInputChange() {
        textFieldError = EMPTY_STRING
    }

    fun onOverViewPressed(overview: Overview) {
        when (overview) {
            Overview.WALLET -> {
                val balance = calculateWalletBalanceUseCase(tokens!!)
                val walletBalance = convertToFormattedNumberUseCase(balance)
                detailsScreenState = DetailsScreen.WalletTokensScreen(
                    convertToTokensInWalletUseCase(tokens!!),
                    OverViewBalance(Overview.WALLET, walletBalance, EMPTY_STRING)
                )
            }
            Overview.LIQUIDITY -> {
                val liquidityBalance = calculateBalanceAndEarningsByOverviewTypeUseCase(
                    complexProtocolResponse!!,
                    OVERVIEW_LIQUIDITY
                )
                val balanceFormatted = convertToFormattedNumberUseCase(liquidityBalance.first)
                val earningsFormatted = convertToFormattedNumberUseCase(liquidityBalance.second)
                detailsScreenState = DetailsScreen.LiquidityProtocolsScreen(
                    convertToLiquidityProtocolsUseCase(
                        complexProtocolResponse!!,
                        OVERVIEW_LIQUIDITY
                    ),
                    OverViewBalance(Overview.LIQUIDITY, balanceFormatted, earningsFormatted)
                )
            }
            Overview.YIELD -> {}
            Overview.LENDING -> {
                val lendingBalance = calculateBalanceAndEarningsByOverviewTypeUseCase(
                    complexProtocolResponse!!,
                    OVERVIEW_LENDING
                )
                val balanceFormatted = convertToFormattedNumberUseCase(lendingBalance.first)
                val earningsFormatted = convertToFormattedNumberUseCase(lendingBalance.second)
                detailsScreenState = DetailsScreen.LendingProtocolsScreen(
                    convertToLendingProtocolsUseCase(complexProtocolResponse!!, OVERVIEW_LENDING),
                    OverViewBalance(Overview.LENDING, balanceFormatted, earningsFormatted)
                )
            }
            Overview.FARMING -> {
                val balanceAndEarnings = calculateBalanceAndEarningsByOverviewTypeUseCase(
                    complexProtocolResponse!!,
                    OVERVIEW_FARMING
                )
                val balanceFormatted = convertToFormattedNumberUseCase(balanceAndEarnings.first)
                val earningsFormatted = convertToFormattedNumberUseCase(balanceAndEarnings.second)
                detailsScreenState = DetailsScreen.FarmingProtocolsScreen(
                    convertToFarmingProtocolsUseCase(complexProtocolResponse!!, OVERVIEW_FARMING),
                    OverViewBalance(Overview.FARMING, balanceFormatted, earningsFormatted)
                )
            }
            Overview.DEPOSITS -> {}
            Overview.STAKED -> {}
            Overview.INSURANCE -> {}
            Overview.LOCKED -> {}
            Overview.REWARDS -> {}
        }
        bottomSheetExpandedState = true
    }

    fun onBottomSheetHidden() {
        bottomSheetExpandedState = false
    }

    sealed class MainScreen {
        object OnBoardingScreen : MainScreen()
        data class PortfolioScreen(val portfolio: Portfolio) : MainScreen()
        object Loading : MainScreen()
        data class ErrorBadAddress(val address: String) : MainScreen()
        object ErrorOnRequest : MainScreen()
    }

    sealed class DetailsScreen {
        data class WalletTokensScreen(
            val tokens: List<TokenInWallet>,
            val wallet: OverViewBalance
        ) : DetailsScreen()

        data class FarmingProtocolsScreen(
            val farmingProtocols: List<FarmingProtocol>,
            val farming: OverViewBalance
        ) : DetailsScreen()

        data class LendingProtocolsScreen(
            val lendingProtocols: List<LendingProtocol>,
            val lending: OverViewBalance
        ) : DetailsScreen()

        data class LiquidityProtocolsScreen(
            val liquidityProtocols: List<LiquidityProtocol>,
            val liquidity: OverViewBalance
        ) : DetailsScreen()

        object None : DetailsScreen()
    }
}
