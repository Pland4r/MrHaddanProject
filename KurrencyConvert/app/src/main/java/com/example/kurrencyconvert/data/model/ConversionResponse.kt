package com.example.kurrencyconvert.data.model

data class ConversionResponse(
    val success: Boolean,
    val query: Query,
    val info: Info,
    val result: Double
)

data class Query(
    val from: String,
    val to: String,
    val amount: Double
)

data class Info(
    val rate: Double
) 