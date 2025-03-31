package com.example.currencyexchanger.di

import com.example.currencyexchanger.domain.rule.CommissionFeeRule
import com.example.currencyexchanger.domain.rule.EveryNthConversionFreeRule
import com.example.currencyexchanger.domain.rule.FreeConversionAmountRule
import com.example.currencyexchanger.domain.rule.FreeConversionsRule
import com.example.currencyexchanger.domain.rule.StandardCommissionRule
import com.example.currencyexchanger.domain.usecase.CommissionFeeCalculator
import com.example.currencyexchanger.domain.usecase.FlexibleCommissionFeeCalculator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CommissionModule {

    /**
     * Defines the commission rules that will be applied
     * You can add other rules in .domain.rule
     */
    @Provides
    fun provideCommissionFeeRules(): List<CommissionFeeRule> {
        return listOf(
            // Every Nth conversion is free
//            EveryNthConversionFreeRule(n = 10),

            // Every N amount from X currency can be added here
//            FreeConversionAmountRule(freeCurrency = "EUR", freeThreshold = 200.0),

            // Free commission applies for the first N conversions
            FreeConversionsRule(threshold = 5),

            // Standard commission rule for all conversions (example: 0.007 = 0.7%).
            StandardCommissionRule(commissionRate = 0.007)
        )
    }

    @Provides
    @Singleton
    fun provideCommissionFeeCalculator(
        rules: List<@JvmSuppressWildcards CommissionFeeRule>
    ): CommissionFeeCalculator {
        return FlexibleCommissionFeeCalculator(rules)
    }
}