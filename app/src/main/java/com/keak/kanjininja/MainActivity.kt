package com.keak.kanjininja

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.disk.DiskCache
import coil.memory.MemoryCache
import coil.util.DebugLogger
import com.keak.kanjininja.navigation.MainNavGraph
import com.keak.kanjininja.navigation.RouterImpl
import com.keak.kanjininja.ui.theme.KanjiNinjaTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity(), ImageLoaderFactory {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KanjiNinjaTheme() {
                val navController = rememberNavController()
                val backStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = backStackEntry?.destination
                val router = remember { RouterImpl(navController) }
                var bottomBarState by rememberSaveable { (mutableStateOf(false)) }
//                bottomBarState = currentRoute?.hierarchy?.any {
//                    it.hasRoute(Screens.Splash::class)
//                } == false && currentRoute.hierarchy.any {
//                    it.hasRoute(Screens.Login::class)
//                } == false && currentRoute.hierarchy.any {
//                    it.hasRoute(Screens.Detail::class)
//                } == false && currentRoute.hierarchy.any {
//                    it.hasRoute(Screens.ForceUpdate::class)
//                } == false
//                Scaffold(modifier = Modifier.fillMaxSize(), bottomBar = {
//                    BottomNavigationBar(
//                        navController = navController,
//                        bottomBarState = bottomBarState
//                    ) {
//
//                    }
//                }) { innerPadding ->
//                    Surface(
//                        color = MaterialTheme.colorScheme.background,
//                        modifier = Modifier.padding(innerPadding)
//                    ) {
//                        MainNavGraph(
//                            navController = navController,
//                            router = router,
//                            modifier = Modifier
//                        )
//                    }
//                }
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainNavGraph(
                        navController = navController,
                        router = router,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
    override fun newImageLoader(): ImageLoader {
        return ImageLoader.Builder(this)
            .memoryCache {
                MemoryCache.Builder(this)
                    .maxSizePercent(0.20)
                    .build()
            }
            .diskCache {
                DiskCache.Builder()
                    .directory(cacheDir.resolve("image_cache"))
                    .maxSizeBytes(5 * 1024 * 1024)
                    .build()
            }
            .logger(DebugLogger())
            .respectCacheHeaders(false)
            .build()
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    KanjiNinjaTheme() {
        Greeting("Android")
    }
}