package com.example.currencyexchanger.domain.usecase

import com.example.currencyexchanger.domain.rule.CommissionFeeRule
import javax.inject.Inject

/**
 * A flexible commission fee calculator that evaluates a list of commission rules.
 * The strategy here is: if any rule returns 0.0 (free conversion), commission is 0.
 * Otherwise, it uses the first non-null commission value found.
 */
class FlexibleCommissionFeeCalculator @Inject constructor(
    // List of rules to apply in order.
    private val rules: List<CommissionFeeRule>
) : CommissionFeeCalculator {
    override fun calculateCommission(amount: Double, exchangeCount: Int, from : String): Double {
        // Check all rules; if any returns 0.0, that rule takes precedence.
        rules.forEach { rule ->
            rule.calculateCommission(amount, exchangeCount, from)?.let { commission ->
                if (commission == 0.0) {
                    return 0.0
                }
            }
        }
        // Otherwise, use the first non-null commission found,
        // or fallback to a default commission if no rule applies.
        return rules.map { it.calculateCommission(amount, exchangeCount, from) }
            .firstOrNull() ?: (amount * 0.007)
    }
}