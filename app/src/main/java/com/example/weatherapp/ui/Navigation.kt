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
        composable("WeatherCard/{id}",
            arguments = listOf(navArgument("id") { defaultValue = "No id" })

        ){ backStackEntry ->
            WeatherCard(backStackEntry.arguments?.getString("id")?: "No_id", hiltViewModel())
        }
    }
}