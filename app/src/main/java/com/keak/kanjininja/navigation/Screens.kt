package com.keak.kanjininja.navigation

import kotlinx.serialization.Serializable

sealed class Screens {
    @Serializable
    data object Splash : Screens()

    @Serializable
    data object Home : Screens()
    @Serializable
    data object KanjiPreview : Screens()

    @Serializable
    data class Quiz(
        val kanji: String
    ) : Screens()
    @Serializable
    data object Favorite : Screens()

    @Serializable
    data object Basket : Screens()

    @Serializable
    data object BasketOperations : Screens()

    @Serializable
    data object Profile : Screens()
}