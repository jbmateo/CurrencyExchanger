package com.example.currencyexchanger.domain.repository

import com.example.currencyexchanger.data.api.ExchangeRateResponse

interface CurrencyExchangeRateRepository {
    suspend fun getExchangeRate(from: String, to: String): Double
    suspend fun getExchangeRateResponse(): ExchangeRateResponse
}