package com.example.weatherapp.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.accompanist.navigation.animation.AnimatedNavHost


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun MyGraph() {
    val navController = rememberNavController()

    AnimatedNavHost(
        navController = navController,
        startDestination = "WeatherScreen",
        enterTransition = {
            slideInHorizontally(initialOffsetX = { fullWidth -> fullWidth })
        },
        exitTransition = {
            slideOutHorizontally(targetOffsetX = { fullWidth -> -fullWidth })
        },
        popEnterTransition = {
            slideInHorizontally(initialOffsetX = { fullWidth -> -fullWidth })
        },
        popExitTransition = {
            slideOutHorizontally(targetOffsetX = { fullWidth -> fullWidth })
        }
    ) {
        composable("WeatherScreen") {
            WeatherScreen(hiltViewModel(), navController)
        }
        composable(
            "WeatherCard/{id}",
            arguments = listOf(navArgument("id") { defaultValue = "No id" })
        ) { backStackEntry ->
            WeatherCard(backStackEntry.arguments?.getString("id") ?: "No_id", hiltViewModel())
        }
    }
}
