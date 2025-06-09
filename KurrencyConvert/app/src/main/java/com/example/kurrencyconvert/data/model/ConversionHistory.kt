package com.example.kurrencyconvert.data.model

data class ConversionHistory(
    val source: String = "",
    val target: String = "",
    val amount: Double = 0.0,
    val result: Double = 0.0,
    val timestamp: Long = 0
) 