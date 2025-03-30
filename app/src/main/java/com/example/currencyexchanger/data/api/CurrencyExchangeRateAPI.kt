package com.example.currencyexchanger.data.api

import retrofit2.http.GET

interface CurrencyExchangeRateAPI {
    @GET("currency-exchange-rates")
    suspend fun getRates(): ExchangeRateResponse
}