package com.example.currencyexchanger.data.api

data class ExchangeRateResponse(
    val base: String,
    val date: String,
    val rates: Map<String, Double>
)