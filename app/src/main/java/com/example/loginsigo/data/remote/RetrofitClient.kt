import com.example.loginsigo.data.remote.ApiService;

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
private const val BASE_URL = "http://189.206.96.198:8080/"

// Creamos la instancia de Retrofit
val retrofit: Retrofit by lazy {
    Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
}

// Creamos la implementación de la interfaz de la API
val apiService: ApiService by lazy {
    retrofit.create(ApiService::class.java)
}
}