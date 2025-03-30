package com.example.currencyexchanger.fakes

import com.example.currencyexchanger.domain.model.Account
import com.example.currencyexchanger.domain.repository.AccountRepository

class FakeAccountRepository : AccountRepository {
    // Start with 1000 EUR and 0 USD balance.
    var _account = Account(balances = mutableMapOf("EUR" to 1000.0, "USD" to 0.0))
    private var exchangeCount = 0

    override fun getAccount(): Account = _account

    override fun updateAccount(account: Account) {
        // Update our fake account's balances
        this._account = account
    }

    override fun getExchangeCount(): Int = exchangeCount

    override fun incrementExchangeCount() {
        exchangeCount++
    }
}