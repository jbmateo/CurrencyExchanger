package com.example.currencyexchanger.domain.model

//Result of exchange operation
data class ExchangeResult(
    val soldCurrency: String,
    val soldAmount: Double,
    val boughtCurrency: String,
    val boughtAmount: Double,
    val commissionFee: Double,
    val updatedAccount: Account
)

data class PreviewConversionResult(
    val rate: Double,
    val commissionFee: Double,
    val netAmount: Double,
    val from: String,
    val to: String
)