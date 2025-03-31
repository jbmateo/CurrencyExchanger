package com.example.currencyexchanger.domain.rule


interface CommissionFeeRule {
    /**
     * Calculates a commission fee for a conversion if applicable.
     * @param amount The amount being converted.
     * @param exchangeCount The number of exchanges performed so far.
     * @param from The source currency code.
     * @return The commission fee if this rule applies, or 0.0 if it doesn't.
     */
    fun calculateCommission(amount: Double, exchangeCount: Int, from: String): Double?
}