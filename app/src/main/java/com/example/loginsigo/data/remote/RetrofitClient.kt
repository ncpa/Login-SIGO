import com.example.loginsigo.data.remote.ApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Objeto singleton para crear y configurar una instancia de Retrofit.
 */
object RetrofitClient {
    private const val BASE_URL = "http://189.206.96.198:8080/"

    /**
     * Instancia de Retrofit configurada con la URL base y un convertidor de Gson.
     * La creación es diferida (lazy) para que se inicialice solo cuando se accede por primera vez.
     */
    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    /**
     * Implementación de la interfaz [ApiService] para realizar las llamadas a la API.
     * La creación es diferida (lazy) para que se inicialice solo cuando se accede por primera vez.
     */
    val apiService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}
