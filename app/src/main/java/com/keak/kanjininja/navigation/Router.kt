package com.keak.kanjininja.navigation

interface Router {
    fun goToSplashToHomeScreen()
    fun goToHomeScreen()
    fun goToKanjiPreviewScreen()
    fun goToQuizScreen(kanji: String)
}