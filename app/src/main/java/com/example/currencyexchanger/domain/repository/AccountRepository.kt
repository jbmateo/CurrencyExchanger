package com.example.currencyexchanger.domain.repository

import com.example.currencyexchanger.domain.model.Account

interface AccountRepository {
    fun getAccount(): Account
    fun updateAccount(account: Account)
    fun getExchangeCount(): Int
    fun incrementExchangeCount()
}