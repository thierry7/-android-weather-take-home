package com.example.weatherapp.ui

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

@Composable
fun MyGraph(){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "WeatherScreen"){
        composable("WeatherScreen"){ WeatherScreen(hiltViewModel(), navController) }
        composable("WeatherCard/{day}",
            arguments = listOf(navArgument("day") { defaultValue = "No day" })

        ){ backStackEntry ->
            WeatherCard(backStackEntry.arguments?.getString("day")?: "No_day", hiltViewModel())
        }
    }
}