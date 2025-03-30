package com.example.currencyexchanger.domain.model

data class Account(
    // A mapping from currency code to its current balance.
    val balances: MutableMap<String, Double>
) {
    fun getBalance(currency : String) : Double = balances[currency] ?: 0.0
    fun updateBalance(currency: String, newBalance: Double) {
        balances[currency] = newBalance
    }
}