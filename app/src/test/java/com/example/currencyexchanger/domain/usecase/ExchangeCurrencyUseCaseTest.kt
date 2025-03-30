package com.example.currencyexchanger.domain.usecase

import com.example.currencyexchanger.domain.model.ExchangeResult
import com.example.currencyexchanger.domain.model.PreviewConversionResult
import com.example.currencyexchanger.fakes.FakeAccountRepository
import com.example.currencyexchanger.fakes.FakeCommissionFeeCalculator
import com.example.currencyexchanger.fakes.FakeCurrencyExchangeRateRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class ExchangeCurrencyUseCaseTest {

    private lateinit var exchangeCurrencyUseCase: ExchangeCurrencyUseCase
    private lateinit var fakeExchangeRateRepository: FakeCurrencyExchangeRateRepository
    private lateinit var fakeAccountRepository: FakeAccountRepository
    private lateinit var fakeCommissionFeeCalculator: FakeCommissionFeeCalculator

    @Before
    fun setUp() {
        fakeExchangeRateRepository = FakeCurrencyExchangeRateRepository()
        fakeAccountRepository = FakeAccountRepository()
        fakeCommissionFeeCalculator = FakeCommissionFeeCalculator()

        exchangeCurrencyUseCase = ExchangeCurrencyUseCase(
            exchangeRateRepository = fakeExchangeRateRepository,
            accountRepository = fakeAccountRepository,
            commissionFeeCalculator = fakeCommissionFeeCalculator
        )
    }

    @Test
    fun testSuccessfulConversion() = runBlocking {
        // Convert 100 EUR to USD
        val result: ExchangeResult = exchangeCurrencyUseCase.exchange(100.0, "EUR", "USD")

        // Assert conversion details:
        // Expected rate is 1.1 (from our fake repository),
        // Commission fee should be 0 since exchangeCount < 5.
        // So, bought amount should be 100 * 1.1 = 110.0
        assertEquals("EUR", result.soldCurrency)
        assertEquals("USD", result.boughtCurrency)
        assertEquals(100.0, result.soldAmount, 0.001)
        assertEquals(110.0, result.boughtAmount, 0.001)
        assertEquals(0.0, result.commissionFee, 0.001)

        // Verify account balance updates:
        // EUR balance should decrease by 100, leaving 900.0
        assertEquals(900.0, fakeAccountRepository._account.balances["EUR"] ?: 0.00, 0.001)
        // USD balance should increase by 110.0
        assertEquals(110.0, fakeAccountRepository._account.balances["USD"] ?: 0.00, 0.001)
    }

    @Test(expected = IllegalArgumentException::class)
    fun testConversionInsufficientFunds() {
        runBlocking {
            // Try to convert 1100 EUR from an account that only has 1000 EUR.
            exchangeCurrencyUseCase.exchange(1100.0, "EUR", "USD")
        }
    }

    @Test
    fun testPreviewConversion() = runBlocking {
        // For testing, let's assume:
        // - Converting 100 EUR to USD.
        // - The fake repository returns a rate of 1.1 for EUR to USD.
        // - The fake commission fee calculator returns 0 commission for the first 5 exchanges.
        val preview: PreviewConversionResult = exchangeCurrencyUseCase.previewConversion(100.0, "EUR", "USD")

        // Expected:
        // - Rate = 1.1
        // - Commission Fee = 0.0
        // - Net Amount = (100 - 0.0) * 1.1 = 110.0
        // - from = "EUR", to = "USD"
        assertEquals(1.1, preview.rate, 0.001)
        assertEquals(0.0, preview.commissionFee, 0.001)
        assertEquals(110.0, preview.netAmount, 0.001)
        assertEquals("EUR", preview.from)
        assertEquals("USD", preview.to)
    }
}