package com.example.currencyexchanger.domain.usecase

interface CommissionFeeCalculator {
    /**
     * Calculates the commission fee for a conversion.
     * @param amount The amount being converted.
     * @param exchangeCount The number of exchanges that have already occurred.
     */
    fun calculateCommission(amount: Double, exchangeCount: Int): Double
}