package com.keak.kanjininja.navigation.bottomnav

import androidx.compose.ui.graphics.vector.ImageVector
import com.keak.kanjininja.navigation.Screens

data class BottomBarItem(
    val title: String,
    val image: ImageVector,
    val route: Screens,
    val isActive:Boolean = true
)