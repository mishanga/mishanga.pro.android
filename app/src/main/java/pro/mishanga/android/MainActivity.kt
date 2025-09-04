package pro.mishanga.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import pro.mishanga.android.ui.component.AppTopBar
import pro.mishanga.android.ui.component.BannerDetailScreen
import pro.mishanga.android.ui.component.InterstitialScreen
import pro.mishanga.android.ui.component.PrimaryButton
import pro.mishanga.android.ui.component.RewardedScreen
import pro.mishanga.android.ui.component.SettingsScreen
import pro.mishanga.android.ui.screen.BannerScreen
import pro.mishanga.android.ui.theme.CustomBackground
import pro.mishanga.android.ui.theme.MishangaProTheme


object Routes {
    const val Main = "main"
    const val Banner = "banner"
    const val BannerDetail = "banner_detail"
    const val Interstitial = "interstitial"
    const val Rewarded = "rewarded"
    const val Settings = "settings"
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent { AppNav() }
    }
}

@Composable
fun AppNav() {
    MishangaProTheme {
        val navController = rememberNavController()
        NavHost(navController = navController, startDestination = Routes.Main) {
            composable(Routes.Main) { MainScreen(navController) }
            composable(Routes.Banner) {
                BannerScreen(
                    onBack = { navController.popBackStack() },
                    topBar = { title, onBack ->
                        AppTopBar(
                            title = title,
                            canNavigateBack = true,
                            onBack = onBack,
                            onSettings = null
                        )
                    },
                    onNavigateToDetail = { name, width, height ->
                        navController.navigate("$Routes.BannerDetail/$name/$width/$height")
                    }
                )
            }
            composable("$Routes.BannerDetail/{name}/{width}/{height}/{width2}/{height2}") { backStackEntry ->
                val name = backStackEntry.arguments?.getString("name") ?: "banner"
                val width = backStackEntry.arguments?.getString("width")?.toIntOrNull() ?: 300
                val height = backStackEntry.arguments?.getString("height")?.toIntOrNull() ?: 250
                val width2 = backStackEntry.arguments?.getString("width2")?.toIntOrNull() ?: 320
                val height2 = backStackEntry.arguments?.getString("height2")?.toIntOrNull() ?: 50

                BannerDetailScreen(
                    onBack = { navController.popBackStack() },
                    topBar = { title, onBack ->
                        AppTopBar(
                            title = title,
                            canNavigateBack = true,
                            onBack = onBack,
                            onSettings = null
                        )
                    },
                    name = name,
                    firstSizeBanner = Pair(width, height),
                    secondSizeBanner = Pair(width2, height2)
                )
            }
            composable(Routes.Interstitial) {
                InterstitialScreen(onBack = { navController.popBackStack() }) { title, onBack ->
                    AppTopBar(
                        title = title,
                        canNavigateBack = true,
                        onBack = onBack,
                        onSettings = null
                    )
                }
            }
            composable(Routes.Rewarded) {
                RewardedScreen(onBack = { navController.popBackStack() }) { title, onBack ->
                    AppTopBar(
                        title = title,
                        canNavigateBack = true,
                        onBack = onBack,
                        onSettings = null
                    )
                }
            }
            composable(Routes.Settings) {
                SettingsScreen(onBack = { navController.popBackStack() }) { title, onBack ->
                    AppTopBar(
                        title = title,
                        canNavigateBack = true,
                        onBack = onBack,
                        onSettings = null
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MainScreen(nav: NavHostController) {
    CustomBackground {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            AppTopBar(
                title = "Mishanga PRO",
                canNavigateBack = false,
                onBack = null,
                onSettings = { nav.navigate(Routes.Settings) })
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Top action buttons
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    PrimaryButton(
                        text = "Banner",
                        modifier = Modifier.fillMaxWidth(0.9f)
                    ) { nav.navigate("$Routes.BannerDetail/Banners/300/250/320/50")}
                    Spacer(Modifier.height(8.dp))
                    PrimaryButton(
                        text = "Interstitial",
                        modifier = Modifier.fillMaxWidth(0.9f)
                    ) { nav.navigate(Routes.Interstitial) }
                    Spacer(Modifier.height(8.dp))
                    PrimaryButton(
                        text = "Rewarded",
                        modifier = Modifier.fillMaxWidth(0.9f)
                    ) { nav.navigate(Routes.Rewarded) }
                    Spacer(Modifier.height(12.dp))
                    PrimaryButton(
                        text = "Settings",
                        modifier = Modifier.fillMaxWidth(0.9f)
                    ) { nav.navigate(Routes.Settings) }
                    Spacer(Modifier.height(8.dp))
                }
            }
        }
    }
}

@Preview
@Composable
private fun MainPreview() {
    AppNav()
}
