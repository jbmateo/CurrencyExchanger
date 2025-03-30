package com.example.currencyexchanger.fakes

import com.example.currencyexchanger.domain.usecase.CommissionFeeCalculator

class FakeCommissionFeeCalculator : CommissionFeeCalculator {
    override fun calculateCommission(amount: Double, exchangeCount: Int): Double {
        // For testing: first five exchanges are free, then 0.7% commission.
        return if (exchangeCount < 5) 0.0 else amount * 0.007
    }
}