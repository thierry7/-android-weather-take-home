package com.example.weatherapp.ui

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.weatherapp.domain.weather.WeatherData
import com.example.weatherapp.ui.theme.WeatherAppTheme
import com.example.weatherapp.domain.util.DataResult
import com.example.weatherapp.ui.viewmodel.WeatherViewModel
import com.example.weatherapp.ui.theme.DeepBlue
import com.example.weatherapp.ui.theme.DarkBlue
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WeatherAppTheme {
                WeatherScreen(hiltViewModel())
            }
        }
    }
}



@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WeatherScreen(viewModel: WeatherViewModel) {
    val weatherState by viewModel.weatherForecast.collectAsState()
    var zipCode by remember { mutableStateOf("") }

    Column(modifier = Modifier
        .padding(16.dp)
        .background(DarkBlue)
        .fillMaxSize()) {
        TextField(
            value = zipCode,
            onValueChange = { zipCode = it },
            label = { Text("Enter Zip Code") },
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = { viewModel.getDailyForecast(zipCode) },
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text("Get Weather")
        }

        when (weatherState) {
            is DataResult.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            is DataResult.Success -> {
                val weatherList = (weatherState as DataResult.Success<List<WeatherData>>).data
                LazyColumn {
                    items(weatherList) { weatherData ->
                        Card(modifier = Modifier
                            .padding(8.dp)
                            .background(DeepBlue)
                            .fillMaxHeight()) {
                            Row(modifier = Modifier
                                .fillMaxWidth()
                                .background(DeepBlue)
                                .height(90.dp),
                                verticalAlignment = Alignment.CenterVertically

                            ) {
                                Text(text ="${weatherData.time}",
                                    color = Color.White,
                                    textAlign = TextAlign.Center
                                )
                               Spacer(modifier = Modifier.width(12.dp))
                                Image(
                                    painter = painterResource(id = weatherData.weatherType.iconRes),
                                    contentDescription = null,
                                    modifier = Modifier.width(60.dp),
                                    alignment =  Alignment.Center
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(text=" ${weatherData.weatherType}", color = Color.White)
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(text=" ${weatherData.temperature}°F",
                                    fontSize = 20.sp,
                                    color = Color.White,
                                    textAlign = TextAlign.End,
                                    modifier = Modifier.weight(.2f)

                                )

                            }
                        }
                    }
                }
            }
            is DataResult.Error -> {
                Text("Failed to load weather data: ${(weatherState as DataResult.Error).exception.message}")
            }
        }
    }
}

@Composable
fun WeatherCard(weatherData: WeatherData, backgroundColor: Color){

}
