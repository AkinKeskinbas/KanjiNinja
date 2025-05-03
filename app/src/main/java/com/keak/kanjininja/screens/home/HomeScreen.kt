package com.keak.kanjininja.screens.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.keak.kanjininja.navigation.Router

@Composable
fun HomeScreen(modifier: Modifier = Modifier, router: Router) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center){
        Text(
            style = MaterialTheme.typography.bodyLarge,
            text = "Home Screen",
            modifier = Modifier.clickable{
                router.goToKanjiPreviewScreen()
            }
        )
    }
}