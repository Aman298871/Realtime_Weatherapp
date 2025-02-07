import retrofit2.Response // Correct import for Retrofit response
import retrofit2.http.GET
import retrofit2.http.Query
import com.example.realtimeweatherapp.api.WeatherModel

interface Weatherapi {
    @GET("/v1/current.json")
    suspend fun getWeather(
        @Query("key") apiKey: String,
        @Query("q") city: String
    ): Response<WeatherModel> // Use retrofit2.Response
}
