package com.foyer.hbc.domain.data.dashboard

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.foyer.hbc.R
import com.foyer.hbc.domain.model.UserEntity

enum class CarouselCellInfo(
    @DrawableRes val icon: Int,
    @ColorRes val backgroundIcon: Int,
    @StringRes val title: Int,
    @StringRes val subtitle: Int?
) {
    CHECKOUT(
        R.drawable.ic_payment,
        R.color.cornflower_blue,
        R.string.carousel_checkout_title,
        R.string.carousel_checkout_subtitle
    ),
    UNPAYED(
        R.drawable.ic_mood_bad,
        R.color.main_red,
        R.string.carousel_unpayed_title,
        R.string.carousel_unpayed_subtitle
    ),
    PAYED(
        R.drawable.ic_mood_good,
        R.color.tomato,
        R.string.carousel_payed_title,
        R.string.carousel_payed_subtitle
    ),
    SUM(
        R.drawable.ic_drink,
        R.color.yellow,
        R.string.carousel_sum_title,
        null
    )
}

sealed class CarouselCellType(
    open val info: CarouselCellInfo,
) {
    data class Checkout(
        override val info: CarouselCellInfo,
        val actualAmount: Double,
        val currentBill: Double?
    ) : CarouselCellType(info)

    data class UnPayed(
        override val info: CarouselCellInfo,
        val mostUnPayedUser: UserEntity?
    ) : CarouselCellType(info)

    data class Payed(
        override val info: CarouselCellInfo,
        val mostPayedUser: UserEntity?
    ) : CarouselCellType(info)

    data class TotalConsumptions(
        override val info: CarouselCellInfo,
        val totalConsumptions: Int
    ) : CarouselCellType(info)
}
