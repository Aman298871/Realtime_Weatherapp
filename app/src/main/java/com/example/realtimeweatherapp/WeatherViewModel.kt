package com.example.realtimeweatherapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.realtimeweatherapp.api.Constant
import com.example.realtimeweatherapp.api.NetworkResponse
import com.example.realtimeweatherapp.api.RetrofitInstance
import com.example.realtimeweatherapp.api.WeatherModel
import kotlinx.coroutines.launch

class WeatherViewModel : ViewModel() {
    private val weatherApi = RetrofitInstance.weatherapi

    private val _weatherResult = MutableLiveData<NetworkResponse<WeatherModel>>()
    val weatherResult: LiveData<NetworkResponse<WeatherModel>> get() = _weatherResult

    fun getData(city: String) {
        viewModelScope.launch {
            _weatherResult.value = NetworkResponse.Loading
            try {
                val response = weatherApi.getWeather(Constant.ApiKey, city)
                if (response.isSuccessful && response.body() != null) {
                    _weatherResult.value = NetworkResponse.Success(response.body()!!)
                } else {
                    _weatherResult.value = NetworkResponse.Error("Failed to Load Data: ${response.message()}")
                }
            } catch (e: Exception) {
                _weatherResult.value = NetworkResponse.Error("Network Request Failed")
            }
        }
    }
}