package com.thedefiapp.utils

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.vector.ImageVector
import com.thedefiapp.R
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.*

enum class Overview(@StringRes val title: Int, val imageVector: ImageVector) {
    WALLET(R.string.title_overview_wallet, FontAwesomeIcons.Solid.Wallet),
    LIQUIDITY(R.string.title_overview_liquidity, FontAwesomeIcons.Solid.Tint),
    YIELD(R.string.title_overview_yield, FontAwesomeIcons.Solid.DollarSign),
    LENDING(R.string.title_overview_lending, FontAwesomeIcons.Solid.HandHoldingUsd),
    FARMING(R.string.title_overview_farming, FontAwesomeIcons.Solid.Tractor),
    DEPOSITS(R.string.title_overview_deposits, FontAwesomeIcons.Solid.MoneyCheckAlt),
    STAKED(R.string.title_overview_staked, FontAwesomeIcons.Solid.Coins),
    INSURANCE(R.string.title_overview_insurance, FontAwesomeIcons.Solid.ShieldAlt),
    LOCKED(R.string.title_overview_locked, FontAwesomeIcons.Solid.Lock),
    REWARDS(R.string.title_overview_rewards, FontAwesomeIcons.Solid.Gem)
}
