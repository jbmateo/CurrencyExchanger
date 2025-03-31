package com.example.currencyexchanger.domain.rule

/**
 * A rule that provides free commission for the first [threshold] conversions.
 *
 * @param threshold The number of conversions that are free.
 */
class FreeConversionsRule(private val threshold: Int) : CommissionFeeRule {
    override fun calculateCommission(amount: Double, exchangeCount: Int, from: String): Double? {
        return if (exchangeCount < threshold) 0.0 else null
    }
}