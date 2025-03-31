package com.example.currencyexchanger.domain.rule

/**
 * A rule that provides free commission if the conversion amount is below or equal to a threshold,
 * but only when the source currency matches the specified currency.
 *
 * @param freeCurrency The currency code for which this rule applies.
 * @param freeThreshold The maximum amount (in freeCurrency) for which the conversion is free.
 */
class FreeConversionAmountRule(
    private val freeCurrency: String,
    private val freeThreshold: Double
) : CommissionFeeRule {
    override fun calculateCommission(amount: Double, exchangeCount: Int, from: String): Double? {
        // Apply this rule only if the source currency matches and the amount is below or equal to the threshold.
        return if (from == freeCurrency && amount <= freeThreshold) 0.0 else null
    }
}