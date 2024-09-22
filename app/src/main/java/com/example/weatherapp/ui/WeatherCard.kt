package com.example.weatherapp.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weatherapp.R
import com.example.weatherapp.domain.util.DataResult
import com.example.weatherapp.domain.weather.WeatherData
import com.example.weatherapp.ui.theme.DarkBlue
import com.example.weatherapp.ui.theme.DeepBlue
import com.example.weatherapp.ui.viewmodel.WeatherViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WeatherCard(
    day: String,
    viewModel: WeatherViewModel
) {

    viewModel.getDailyData(day)
    val weather by viewModel.dailyWeather.collectAsState()

    when(weather){
        is DataResult.Loading -> {
            CircularProgressIndicator()
        }
        is DataResult.Success ->{

            val currentDate = LocalDate.now()

            val weatherDate = (weather as DataResult.Success<WeatherData>).data.time.toLocalDate() // Assuming `weatherData.time` is a LocalDateTime
            val formattedDate = if (weatherDate == currentDate) {
                "Today"
            } else {
                (weather as DataResult.Success<WeatherData>).data.time
                    .format(DateTimeFormatter.ofPattern("EEEE"))
            }
            val formattedTime = if (weatherDate == currentDate) {
                (weather as DataResult.Success<WeatherData>).data.time.format(DateTimeFormatter.ofPattern("HH:mm"))
            } else {
                ""
            }
            Box(modifier = Modifier.fillMaxSize().background(DarkBlue))
            {

                Card(
                    colors = CardDefaults.cardColors(containerColor = DeepBlue,
                        contentColor = DeepBlue),
                    modifier = Modifier
                        .padding(16.dp)
                        .height(400.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .background(DeepBlue),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = if (formattedTime.isEmpty()) formattedDate
                            else "$formattedDate, $formattedTime",
                            modifier = Modifier.align(Alignment.End),
                            color = Color.White,
                            fontSize = 25.sp,

                            )
                        Spacer(modifier = Modifier.height(16.dp))
                        Image(
                            painter = painterResource(
                                id = (weather as DataResult.Success<WeatherData>)
                                    .data.weatherType.iconRes),
                            contentDescription = null,
                            modifier = Modifier.height(120.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "${(weather as DataResult.Success<WeatherData>).data.temperature}°F",
                            fontSize = 50.sp,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = (weather as DataResult.Success<WeatherData>).data.weatherType.weatherDesc,
                            fontSize = 20.sp,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(32.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {

                            WeatherDataDisplay(
                                value = (weather as DataResult.Success<WeatherData>).data.humidity.roundToInt(),
                                unit = "hpa",
                                icon = ImageVector.vectorResource(id = R.drawable.baseline_pressure),
                                iconTint = Color.White,
                                textStyle = TextStyle(color = Color.White)
                            )
                            WeatherDataDisplay(
                                value = (weather as DataResult.Success<WeatherData>).data.rainDrop.roundToInt(),
                                unit = "%",
                                icon = ImageVector.vectorResource(id = R.drawable.baseline_drop),
                                iconTint = Color.White,
                                textStyle = TextStyle(color = Color.White)
                            )
                            WeatherDataDisplay(
                                value = (weather as DataResult.Success<WeatherData>).data.windSpeed.roundToInt(),
                                unit = "mi/h",
                                icon = ImageVector.vectorResource(id = R.drawable.baseline_wind),
                                iconTint = Color.White,
                                textStyle = TextStyle(color = Color.White)
                            )
                        }
                    }

                }


            }


        }
        is DataResult.Error ->{
            Text(
                text = "Failed to load weather data.",
                color = Color.Red,
                modifier = Modifier.padding(16.dp)
            )
        }

    }

}

@Composable
fun WeatherDataDisplay(
    value: Int,
    unit: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = TextStyle(),
    iconTint: Color = Color.White
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = iconTint,
            modifier = Modifier.size(25.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = "$value$unit",
            style = textStyle
        )
    }
}