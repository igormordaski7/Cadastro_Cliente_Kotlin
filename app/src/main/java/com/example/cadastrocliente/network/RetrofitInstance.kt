package com.example.cadastrocliente.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

data class UsuarioApi(
    val id: Int,
    val name: String,
    val email: String
)

interface ApiService {
    @GET("users")
    suspend fun getUsuarios(): List<UsuarioApi>
}

object RetrofitInstance {
    private const val BASE_URL = "https://jsonplaceholder.typicode.com/"

    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}
