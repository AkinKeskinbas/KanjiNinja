package com.keak.kanjininja.navigation

import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json

class RouterImpl(
    private val navHostController: NavHostController,
    private val startDestination: Screens = Screens.Splash
) : Router {
    override fun goToSplashToHomeScreen() {
        navigate(Screens.Home, removeFromHistory = true, singleTop = true)
    }

    override fun goToHomeScreen() {
        navigate(Screens.Home)
    }

    override fun goToKanjiPreviewScreen() {
        navigate(Screens.KanjiPreview)
    }

    override fun goToQuizScreen(kanji: String) {
        navigate(Screens.Quiz(kanji = kanji))
    }

    private fun navigate(
        screen: Screens,
        removeFromHistory: Boolean = false,
        singleTop: Boolean = false,
        removeBackStack: Boolean = false
    ) {
        navHostController.apply {
            navigate(screen) {
                if (removeFromHistory) {
                    if (singleTop) {
                        popUpTo(startDestination) {
                            inclusive = true
                        }
                    } else {
                        popUpTo(0) {
                            saveState = false
                        }
                    }

                } else if (removeBackStack) {
                    popUpTo(screen) {
                        inclusive = true
                    }
                } else {
                    restoreState = true
                }
                launchSingleTop = singleTop
            }
        }
    }

    class CustomNavType<T : Parcelable>(
        private val clazz: Class<T>,
        private val serializer: KSerializer<T>,
    ) : NavType<T>(isNullableAllowed = false) {
        override fun get(bundle: Bundle, key: String): T? =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                bundle.getParcelable(key, clazz) as T
            } else {
                @Suppress("DEPRECATION") // for backwards compatibility
                bundle.getParcelable(key)
            }

        override fun put(bundle: Bundle, key: String, value: T) =
            bundle.putParcelable(key, value)

        override fun parseValue(value: String): T = Json.decodeFromString(serializer, value)

        override fun serializeAsValue(value: T): String = Json.encodeToString(serializer, value)

        override val name: String = clazz.name
    }

}
