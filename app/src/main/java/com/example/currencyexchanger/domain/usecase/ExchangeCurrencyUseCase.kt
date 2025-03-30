package com.example.currencyexchanger.domain.usecase

import com.example.currencyexchanger.domain.model.Account
import com.example.currencyexchanger.domain.model.ExchangeResult
import com.example.currencyexchanger.domain.model.PreviewConversionResult
import com.example.currencyexchanger.domain.repository.AccountRepository
import com.example.currencyexchanger.domain.repository.CurrencyExchangeRateRepository
import javax.inject.Inject

class ExchangeCurrencyUseCase @Inject constructor(
    private val exchangeRateRepository: CurrencyExchangeRateRepository,
    private val accountRepository: AccountRepository,
    private val commissionFeeCalculator: CommissionFeeCalculator
) {
    /**
     * Executes a currency exchange.
     * @param amount The amount to exchange (in the sold currency).
     * @param from The currency code being sold.
     * @param to The currency code being bought.
     * @return An ExchangeResult detailing the operation.
     * @throws IllegalArgumentException if the account balance is insufficient.
     */
    suspend fun exchange(amount: Double, from: String, to: String): ExchangeResult {
        // Validate input amount
        if (amount <= 0) {
            throw IllegalArgumentException("Amount must be positive")
        }

        // Get the current account and balance for the selling currency.
        val account: Account = accountRepository.getAccount()
        val currentBalance = account.getBalance(from)

        // Get the number of exchanges already performed.
        val exchangeCount = accountRepository.getExchangeCount()

        // Calculate commission fee.
        val commissionFee = commissionFeeCalculator.calculateCommission(amount, exchangeCount)

        // Check that the account has enough balance to cover the conversion plus any fee.
        if (currentBalance < (amount + commissionFee)) {
            throw IllegalArgumentException("Insufficient balance")
        }

        // Get the current exchange rate from the repository.
        val rate = exchangeRateRepository.getExchangeRate(from, to)
        // Calculate the amount received in the bought currency.
        val boughtAmount = amount * rate

        // Update account balances:
        // Deduct the sold amount and commission fee from the selling currency.
        account.updateBalance(from, currentBalance - amount - commissionFee)
        // Credit the bought currency with the converted amount.
        val currentBoughtBalance = account.getBalance(to)
        account.updateBalance(to, currentBoughtBalance + boughtAmount)

        // Record this conversion.
        accountRepository.incrementExchangeCount()
        accountRepository.updateAccount(account)

        return ExchangeResult(
            soldCurrency = from,
            soldAmount = amount,
            boughtCurrency = to,
            boughtAmount = boughtAmount,
            commissionFee = commissionFee,
            updatedAccount = account
        )
    }

    /**
     * Previews the conversion result without updating the account.
     * It calculates the net amount after applying the commission fee.
     */
    suspend fun previewConversion(amount: Double, from: String, to: String): PreviewConversionResult {
        val rate = exchangeRateRepository.getExchangeRate(from, to)
        val exchangeCount = accountRepository.getExchangeCount()
        val commissionFee = commissionFeeCalculator.calculateCommission(amount, exchangeCount)
        val netAmount = (amount - commissionFee) * rate

        // Return net amount without modifying account balances.
        return PreviewConversionResult(
            rate = rate,
            commissionFee = commissionFee,
            netAmount = netAmount,
            from = from,
            to = to
        )
    }
}