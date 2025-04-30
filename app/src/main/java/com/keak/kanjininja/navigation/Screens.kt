package com.keak.kanjininja.navigation

import kotlinx.serialization.Serializable

sealed class Screens {
    @Serializable
    data object Splash : Screens()

    @Serializable
    data object Home : Screens()
    @Serializable
    data object Quiz : Screens()
    @Serializable
    data object Favorite : Screens()

    @Serializable
    data object Basket : Screens()

    @Serializable
    data object BasketOperations : Screens()

    @Serializable
    data object Profile : Screens()
}