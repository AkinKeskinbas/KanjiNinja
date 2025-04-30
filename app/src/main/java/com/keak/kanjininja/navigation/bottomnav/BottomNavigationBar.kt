package com.keak.kanjininja.navigation.bottomnav

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.FloatingActionButton


import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingBasket
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

import com.keak.kanjininja.navigation.Screens
import com.keak.kanjininja.ui.theme.DodgerBlue
import com.keak.kanjininja.ui.theme.OsloGrey


@SuppressLint("RestrictedApi")
@Composable
fun BottomNavigationBar(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    bottomBarState: Boolean,
    fastOfferClick: () -> Unit
) {
    val items = listOf(
        BottomBarItem(
            title = "Home",//stringResource(R.string.home),
            image = Icons.Filled.Home,
            route = Screens.Home
        ),
        BottomBarItem(
            title = "Favorite",//stringResource(R.string.favorites),
            image = Icons.Filled.Favorite,
            route = Screens.Favorite
        ),
        BottomBarItem(
            title = "Basket",//stringResource(R.string.basket),
            image = Icons.Filled.ShoppingBasket,
            route = Screens.Basket
        ),
        BottomBarItem(
            title = "Profile",//stringResource(R.string.profile),
            image = Icons.Filled.Person,
            route = Screens.Profile
        ),
    )
    AnimatedVisibility(
        visible = bottomBarState,
        enter = slideInVertically(initialOffsetY = { -40 }) + expandVertically(expandFrom = Alignment.Top) + scaleIn(
            transformOrigin = TransformOrigin(0.5f, 0f)
        ) + fadeIn(initialAlpha = 0.3f),
        exit = slideOutVertically() + shrinkVertically() + fadeOut() + scaleOut(targetScale = 1.2f)
    ) {
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            BottomNavigation(
                backgroundColor = Color.White
            ) {
                val backStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = backStackEntry?.destination

                items.forEachIndexed { index, bottomBarItem ->
                    val isSelected = currentRoute?.hierarchy?.any {
                        it.hasRoute(bottomBarItem.route::class)
                    } == true
                    if (index == 2) {
                        Spacer(Modifier.weight(1f))
                        BottomNavigationItem(
                            icon = {
                                Icon(bottomBarItem.image, contentDescription = null)
                            },
                            label = {

                                Text(
                                    text = bottomBarItem.title,
                                    color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.secondary,
                                    style = MaterialTheme.typography.labelSmall,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            },
                            selected = isSelected,
                            selectedContentColor = DodgerBlue,
                            unselectedContentColor = OsloGrey,
                            onClick = {
                                if (isSelected.not()) {
                                    navController.navigate(bottomBarItem.route) {
                                        launchSingleTop = true
                                        restoreState = true
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                    }
                                }
                            }
                        )
                    } else {
                        BottomNavigationItem(
                            icon = {
                                Icon(bottomBarItem.image, contentDescription = null)
                            },
                            label = {
                                Text(
                                    text = bottomBarItem.title,
                                    color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.secondary,
                                    style = MaterialTheme.typography.labelSmall,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )

                            },
                            selected = currentRoute?.hierarchy?.any {
                                it.hasRoute(bottomBarItem.route::class)
                            } == true,
                            selectedContentColor = DodgerBlue,
                            unselectedContentColor = OsloGrey,
                            onClick = {
                                if (isSelected.not()) {
                                    navController.navigate(bottomBarItem.route) {
                                        launchSingleTop = true
                                        restoreState = true
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                    }
                                }

                            }
                        )
                    }
                }
            }
            val bottomNavigationHeight = LocalDensity.current.run { 34.dp.roundToPx() }

            FloatingActionButton(
                backgroundColor = Color.White,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .offset {
                        IntOffset(0, -bottomNavigationHeight / 4)
                    }
                    .border(3.dp, color = MaterialTheme.colorScheme.onPrimary, CircleShape)
                    .size(40.dp),
                shape = CircleShape,

                onClick = {
                    fastOfferClick.invoke()
                }
            ) {
                Icon(
                    Icons.Filled.Add,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(30.dp)
                )
            }
        }
    }

}