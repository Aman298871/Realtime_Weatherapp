package com.example.realtimeweatherapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
//import coil.compose.AsyncImage
import coil3.compose.AsyncImage
import coil3.compose.rememberAsyncImagePainter
import com.example.realtimeweatherapp.api.NetworkResponse
import com.example.realtimeweatherapp.api.WeatherModel

@Composable
fun WeatherPage(viewModel: WeatherViewModel) {
    var city by remember { mutableStateOf("") }
    val weatherResult by viewModel.weatherResult.observeAsState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            OutlinedTextField(
                modifier = Modifier.weight(1f),
                value = city,
                onValueChange = { city = it },
                label = { Text(text = "Search For Any Location") }
            )
            IconButton(onClick = {
                viewModel.getData(city)
            }) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search For Any Location"
                )
            }
        }

        when (val result = weatherResult) {
            is NetworkResponse.Error -> Text(text = result.message, color = Color.Red)
            NetworkResponse.Loading -> ShimmerLoadingEffect()
            is NetworkResponse.Success -> WeatherDetails(data = result.data)
            null -> {}
        }
    }
}



@Composable
fun WeatherDetails(data: WeatherModel) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.Bottom
        ) {
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = "Location Icon",
                modifier = Modifier.size(40.dp)
            )
            Text(text = data.location.name, fontSize = 30.sp)
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = data.location.country, fontSize = 18.sp, color = Color.Gray)
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "${data.current.temp_c} °C",
            fontSize = 46.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Image(
            painter = rememberAsyncImagePainter("https:" + data.current.condition.icon),
            contentDescription = "Weather Icon",
            modifier = Modifier.size(64.dp)
        )
        Text(
            text = data.current.condition.text,
            fontSize = 20.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        Card {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    WeatherVal("Humidity", "${data.current.humidity}")
                    WeatherVal("Wind Speed", "${data.current.wind_kph} km/h")
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    WeatherVal("UV", "${data.current.uv}")
                    WeatherVal("Precipitation", "${data.current.precip_mm} mm")
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    WeatherVal("Local Time", data.location.localtime.split(" ")[1])
                    WeatherVal("Local Date", data.location.localtime.split(" ")[0])
                }
            }
        }
    }
}
@Composable
fun WeatherVal(key: String, value: String) {
    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = value, fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Text(text = key, fontWeight = FontWeight.SemiBold, color = Color.Gray)
    }
    }



@Composable
fun ShimmerLoadingEffect() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .background(Color.LightGray)
    )
}
