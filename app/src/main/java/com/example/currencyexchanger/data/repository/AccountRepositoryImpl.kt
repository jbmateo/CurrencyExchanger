package com.example.currencyexchanger.data.repository

import com.example.currencyexchanger.domain.model.Account
import com.example.currencyexchanger.domain.repository.AccountRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AccountRepositoryImpl @Inject constructor() : AccountRepository {
    // Starting with 1000 EUR; additional currencies will be added as needed.
    private var account = Account(balances = mutableMapOf("EUR" to 1000.0))
    private var conversionCount = 0

    override fun getAccount(): Account = account

    override fun updateAccount(account: Account) {
        this.account = account
    }

    override fun getExchangeCount(): Int = conversionCount

    override fun incrementExchangeCount() {
        conversionCount++
    }
}