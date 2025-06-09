package com.example.kurrencyconvert.data.api

import com.example.kurrencyconvert.data.model.ConversionResponse
import retrofit2.http.GET
import retrofit2.http.Query


interface CurrencyApi {
    @GET("convert")
    suspend fun convertCurrency(
        @Query("from") from: String,
        @Query("to") to: String,
        @Query("amount") amount: Double,
        @Query("access_key") accessKey: String
    ): ConversionResponse
} 