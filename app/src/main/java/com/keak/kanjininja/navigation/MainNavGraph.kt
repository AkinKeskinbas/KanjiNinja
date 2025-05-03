package com.keak.kanjininja.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.keak.base.screens.SplashScreen
import com.keak.kanjininja.screens.home.HomeScreen
import com.keak.kanjininja.screens.kanjipreview.KanjiPreviewScreen
import com.keak.kanjininja.screens.quiz.QuizScreen

@Composable
fun MainNavGraph(
    modifier: Modifier,
    navController: NavHostController,
    startDestination: Screens = Screens.Splash,
    router: Router,
    navGraphBuilder: NavGraphBuilder.() -> Unit = {},
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        navGraphBuilder.invoke(this)
        mainNavigation(router = router)
    }


}

fun NavGraphBuilder.mainNavigation(
    router: Router,
) {
    composable<Screens.Splash> {
        SplashScreen(router = router)
    }
    composable<Screens.Home> {
        HomeScreen(router = router)
    }
    composable<Screens.KanjiPreview> {
        KanjiPreviewScreen(router = router)
    }
    composable<Screens.Quiz> {
        val args = it.toRoute<Screens.Quiz>()
        QuizScreen(router = router, kanji = args.kanji)
    }
}
