package com.example.weatherapp.ui

import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.weatherapp.domain.util.DataResult
import com.example.weatherapp.domain.weather.WeatherData
import com.example.weatherapp.ui.theme.DarkBlue
import com.example.weatherapp.ui.theme.DeepBlue
import com.example.weatherapp.ui.theme.WeatherAppTheme
import com.example.weatherapp.ui.viewmodel.WeatherViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WeatherAppTheme {
                MyGraph()
            }
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WeatherScreen(viewModel: WeatherViewModel = hiltViewModel(), navController: NavController) {
    val weatherState by viewModel.weatherForecast.collectAsState()
    var zipCode by remember { mutableStateOf("") }
    val zipCodeRegex = Regex("^[0-9]{5}(?:-[0-9]{4})?$")
    val context = LocalContext.current

    Box(modifier = Modifier
        .fillMaxSize()
        .background(DarkBlue)
        .padding(top = 20.dp)
    ){

    Column(modifier = Modifier
        .padding(30.dp)
        .background(DarkBlue)
        .fillMaxSize()) {
        TextField(
            value = zipCode,
            onValueChange = { zipCode = it },
            label = { Text("Enter Zip Code", color = Color.White,
                textAlign = TextAlign.Center) },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = DeepBlue,
                focusedTextColor = Color.White,
                cursorColor = Color.White,
                focusedIndicatorColor = Color.White,
                unfocusedIndicatorColor = Color.White
            )
        )
        Button(
            onClick = { if (zipCode.isBlank()) {
                Toast.makeText(context, "Please enter a zip code", Toast.LENGTH_SHORT).show()
            } else if (!zipCodeRegex.matches(zipCode)) {
                Toast.makeText(context, "Invalid zip code format", Toast.LENGTH_SHORT).show()
            } else {
                viewModel.getDailyForecast(zipCode)
            } },
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

                        val currentDate = LocalDate.now()

                        // Format the weather date and compare with today
                        val weatherDate =
                            weatherData.time.toLocalDate() // Assuming `weatherData.time` is a LocalDateTime
                        val formattedDate = if (weatherDate == currentDate) {
                            "Today"
                        } else {
                            weatherData.time.format(DateTimeFormatter.ofPattern("EEEE")) // Name of the day (e.g., Monday)
                        }

                        Card(modifier = Modifier
                            .clickable {
                                navController.navigate("WeatherCard/${weatherData.id}")
                            }
                            .padding(top = 12.dp)
                            .background(DarkBlue)
                            .fillMaxSize(),
                            shape = RoundedCornerShape(20.dp),
                            )
                        {
                            Row(modifier = Modifier
                                .fillMaxWidth()
                                .background(DeepBlue)
                                .height(90.dp),
                                verticalAlignment = Alignment.CenterVertically

                            ) {
                                Text(
                                    text = formattedDate,
                                    modifier = Modifier
                                        .weight(.3f)
                                        .padding(8.dp),
                                    color = Color.White,
                                    fontSize = 18.sp,
                                    textAlign = TextAlign.Start
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Image(
                                    painter = painterResource(id = weatherData.weatherType.iconRes),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .width(40.dp)
                                        .weight(.2f),
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = " ${weatherData.weatherType}",
                                    color = Color.White,
                                    fontSize = 18.sp
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = " ${weatherData.temperature}°F",
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
}

