package edu.nd.pmcburne.hello.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface PlacemarkApiService {
    @GET("placemarks.json")
    suspend fun getPlacemarks(): List<PlacemarkDto>
}

object RetrofitInstance {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://www.cs.virginia.edu/~wxt4gm/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun create(): PlacemarkApiService = retrofit.create(PlacemarkApiService::class.java)
}