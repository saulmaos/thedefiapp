package com.thedefiapp.ui

import android.content.ClipboardManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import com.thedefiapp.R
import com.thedefiapp.ui.common.AddressErrorCompose
import com.thedefiapp.ui.common.CryptoLoading
import com.thedefiapp.ui.common.PullToRefresh
import com.thedefiapp.ui.common.RequestFailedErrorCompose
import com.thedefiapp.ui.screens.*
import com.thedefiapp.ui.screens.details.LendingItems
import com.thedefiapp.ui.screens.details.LiquidityItems
import com.thedefiapp.ui.theme.TheDefiAppTheme
import dagger.hilt.android.AndroidEntryPoint

@ExperimentalMaterialApi
@ExperimentalFoundationApi
@ExperimentalComposeUiApi
@ExperimentalCoilApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApp {
                BottomSheet()
            }
        }
        mainViewModel.initFlows()
    }

    @ExperimentalMaterialApi
    @Composable
    private fun BottomSheet() {
        val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
            bottomSheetState = rememberBottomSheetState(
                initialValue = BottomSheetValue.Collapsed,
                confirmStateChange = {
                    if (it == BottomSheetValue.Collapsed) mainViewModel.onBottomSheetHidden()
                    true
                },
                animationSpec = tween(durationMillis = 500)
            )
        )
        LaunchedEffect(key1 = mainViewModel.bottomSheetExpandedState) {
            when (mainViewModel.bottomSheetExpandedState) {
                true -> bottomSheetScaffoldState.bottomSheetState.expand()
                else -> bottomSheetScaffoldState.bottomSheetState.collapse()
            }
        }
        val roundCorner = dimensionResource(id = R.dimen.padding_normal)
        BottomSheetScaffold(
            sheetContent = {
                DetailsScreen(
                    mainViewModel.detailsScreenState,
                    onClosePressed = {
                        mainViewModel.onBottomSheetHidden()
                    }
                )
            },
            sheetShape = RoundedCornerShape(topStart = roundCorner, topEnd = roundCorner),
            scaffoldState = bottomSheetScaffoldState,
            sheetGesturesEnabled = true,
            sheetPeekHeight = 0.dp
        ) {
            MainScreen(mainViewModel.mainScreenState)
        }
    }

    @Composable
    private fun MainScreen(mainScreenState: MainViewModel.MainScreen) {
        when (mainScreenState) {
            MainViewModel.MainScreen.OnBoardingScreen -> OnBoardingScreen(
                textError = mainViewModel.textFieldError,
                onAddAddressPressed = { mainViewModel.onAddAddressPressed(it) },
                onAddressInput = { mainViewModel.onAddressInputChange() },
                clipBoardText = getClipBoardText()
            )
            is MainViewModel.MainScreen.PortfolioScreen -> {
                val onRefresh = { mainViewModel.onRefresh() }
                PullToRefresh(isRefreshing = mainViewModel.showRefresher, onRefresh = onRefresh) {
                    PortfolioScreen(
                        mainScreenState.portfolio,
                        onOverviewCardPressed = { mainViewModel.onOverViewPressed(it) }
                    )
                }
            }
            MainViewModel.MainScreen.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CryptoLoading()
                }
            }
            is MainViewModel.MainScreen.ErrorBadAddress -> {
                AddressErrorCompose(address = mainScreenState.address) { mainViewModel.onOnBoardingScreenSelected() }
            }
            MainViewModel.MainScreen.ErrorOnRequest -> {
                RequestFailedErrorCompose { mainViewModel.onRetry() }
            }
        }
    }

    @Composable
    private fun DetailsScreen(
        detailsScreenState: MainViewModel.DetailsScreen,
        onClosePressed: () -> Unit
    ) {
        when (detailsScreenState) {
            MainViewModel.DetailsScreen.None -> {}
            is MainViewModel.DetailsScreen.WalletTokensScreen -> {
                DetailsScreenSkeleton(
                    overview = detailsScreenState.wallet, onClosePressed = onClosePressed,
                    header = {
                        ValueHeaderSimple(
                            balance = detailsScreenState.wallet.balance,
                            Modifier.weight(1f)
                        )
                    }
                ) {
                    TokensInWallet(tokens = detailsScreenState.tokens)
                }
            }
            is MainViewModel.DetailsScreen.FarmingProtocolsScreen -> {
                DetailsScreenSkeleton(
                    overview = detailsScreenState.farming, onClosePressed = onClosePressed,
                    header = {
                        ValueHeaderWithEarnings(
                            detailsScreenState.farming.balance,
                            detailsScreenState.farming.dailyEarnings,
                            Modifier.weight(1f)
                        )
                    }
                ) {
                    FarmingItems(tokens = detailsScreenState.farmingProtocols)
                }
            }
            is MainViewModel.DetailsScreen.LendingProtocolsScreen -> {
                DetailsScreenSkeleton(
                    overview = detailsScreenState.lending,
                    onClosePressed = onClosePressed,
                    header = {
                        ValueHeaderWithEarnings(
                            balance = detailsScreenState.lending.balance,
                            detailsScreenState.lending.dailyEarnings,
                            Modifier.weight(1f)
                        )
                    }
                ) {
                    LendingItems(tokens = detailsScreenState.lendingProtocols)
                }
            }
            is MainViewModel.DetailsScreen.LiquidityProtocolsScreen -> {
                DetailsScreenSkeleton(
                    overview = detailsScreenState.liquidity,
                    onClosePressed = onClosePressed,
                    header = {
                        ValueHeaderSimple(
                            balance = detailsScreenState.liquidity.balance,
                            Modifier.weight(1f)
                        )
                    }
                ) {
                    LiquidityItems(tokens = detailsScreenState.liquidityProtocols)
                }
            }
        }
    }

    private fun getClipBoardText(): MutableState<String> {
        val clipBoardManager =
            applicationContext.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        val textState =
            mutableStateOf(clipBoardManager.primaryClip?.getItemAt(0)?.text?.toString().orEmpty())
        clipBoardManager.addPrimaryClipChangedListener {
            textState.value = clipBoardManager.primaryClip?.getItemAt(0)?.text?.toString().orEmpty()
        }
        return textState
    }
}

@Composable
fun MyApp(content: @Composable () -> Unit) {
    TheDefiAppTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            content()
        }
    }
}
