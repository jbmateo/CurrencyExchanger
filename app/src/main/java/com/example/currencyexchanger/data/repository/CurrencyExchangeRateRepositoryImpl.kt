package com.example.currencyexchanger.data.repository

import com.example.currencyexchanger.data.api.CurrencyExchangeRateAPI
import com.example.currencyexchanger.data.api.ExchangeRateResponse
import com.example.currencyexchanger.domain.repository.CurrencyExchangeRateRepository
import javax.inject.Inject

class CurrencyExchangeRateRepositoryImpl @Inject constructor(
    private val api: CurrencyExchangeRateAPI
) : CurrencyExchangeRateRepository {
    override suspend fun getExchangeRate(from: String, to: String): Double {
        val response = api.getRates()

        // Case 1: The selling currency is the API base.
        if (from == response.base) {
            return response.rates[to]
                ?: throw IllegalArgumentException("Rate not available for target currency: $to")
        } else {
            // For a non-base currency, calculate the rate relative to the base.
            val rateFrom = response.rates[from]
                ?: throw IllegalArgumentException("Rate not available for source currency: $from")
            val rateTo = response.rates[to]
                ?: throw IllegalArgumentException("Rate not available for target currency: $to")

            // Conversion rate: rateTo divided by rateFrom.
            return rateTo / rateFrom
        }
    }

    override suspend fun getExchangeRateResponse(): ExchangeRateResponse = api.getRates()
}