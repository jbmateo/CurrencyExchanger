package com.example.currencyexchanger.fakes

import com.example.currencyexchanger.data.api.ExchangeRateResponse
import com.example.currencyexchanger.domain.repository.CurrencyExchangeRateRepository

class FakeCurrencyExchangeRateRepository : CurrencyExchangeRateRepository {
    override suspend fun getExchangeRate(from: String, to: String): Double {
        // For testing, if converting from EUR to USD, return 1.1
        return if (from == "EUR" && to == "USD") 1.1 else 1.0
    }

    override suspend fun getExchangeRateResponse(): ExchangeRateResponse {
        // Return a dummy response with EUR as base and two rates.
        return ExchangeRateResponse(
            base = "EUR",
            date = "2025-03-29",
            rates = mapOf("USD" to 1.1, "GBP" to 0.9)
        )
    }
}