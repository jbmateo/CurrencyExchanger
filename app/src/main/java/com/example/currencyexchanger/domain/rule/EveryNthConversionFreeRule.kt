package com.example.currencyexchanger.domain.rule

/**
 * A flexible rule that provides free commission on every Nth conversion.
 *
 * @param n The frequency of free conversions. For example, if n is 10,
 * then every 10th conversion (i.e. when (exchangeCount + 1) % n == 0) is free.
 */
class EveryNthConversionFreeRule(private val n: Int) : CommissionFeeRule {
    init {
        require(n > 0) { "n must be greater than zero" }
    }

    override fun calculateCommission(amount: Double, exchangeCount: Int, from: String): Double? {
        return if ((exchangeCount + 1) % n == 0) 0.0 else null
    }
}