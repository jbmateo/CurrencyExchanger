package com.example.currencyexchanger.domain.usecase

import javax.inject.Inject

class DefaultCommissionFeeCalculator @Inject constructor() : CommissionFeeCalculator {
    override fun calculateCommission(amount: Double, exchangeCount: Int): Double {
        // First five conversions are free, after that 0.7% fee is applied.
        return if (exchangeCount < 5) 0.0 else amount * 0.007
    }
}