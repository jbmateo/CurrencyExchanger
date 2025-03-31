package com.example.currencyexchanger.domain.rule

/**
 * A standard commission rule that always applies a fixed commission rate.
 * @param commissionRate can be changed
 */
class StandardCommissionRule(private val commissionRate: Double = 0.007) : CommissionFeeRule {
    override fun calculateCommission(amount: Double, exchangeCount: Int, from: String): Double {
        return amount * commissionRate
    }
}