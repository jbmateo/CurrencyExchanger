package com.example.currencyexchanger.ui.exchanger

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.currencyexchanger.domain.model.Account
import com.example.currencyexchanger.domain.model.ExchangeResult
import com.example.currencyexchanger.domain.repository.CurrencyExchangeRateRepository
import com.example.currencyexchanger.domain.usecase.ExchangeCurrencyUseCase
import com.example.currencyexchanger.ui.exchanger.adapter.CurrencyBalance
import com.example.currencyexchanger.util.toCurrencyString
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConversionViewModel @Inject constructor(
    private val exchangeCurrencyUseCase: ExchangeCurrencyUseCase,
    private val exchangeRateRepository: CurrencyExchangeRateRepository
) : ViewModel() {

    // LiveData to hold the list of currency balances for the wallet.
    private val _walletBalances = MutableLiveData<List<CurrencyBalance>>()
    val walletBalances: LiveData<List<CurrencyBalance>> get() = _walletBalances

    // LiveData to hold results of exchange
    private val _conversionResult = MutableLiveData<ExchangeResult>()
    val conversionResult: LiveData<ExchangeResult> get() = _conversionResult

    // LiveData showing which currencies the user can sell
    private val _sellCurrencyOptions = MutableLiveData<List<String>>()
    val sellCurrencyOptions: LiveData<List<String>> get() = _sellCurrencyOptions

    // LiveData holding the list of currency codes for the spinners
    private val _currencyOptions = MutableLiveData<List<String>>()
    val currencyOptions: LiveData<List<String>> get() = _currencyOptions

    // LiveData for net exchange preview
    private val _exchangePreview = MutableLiveData<String>()
    val exchangePreview: LiveData<String> get() = _exchangePreview

    private val _commissionFee = MutableLiveData<String>()
    val commissionFee : LiveData<String> get() = _commissionFee

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    //initial account
    private val account: Account = Account(
        balances = mutableMapOf("EUR" to 1000.0)
    )

    init {
        loadWalletBalances()
        startExchangeRateSync()
    }

    //synchronize currency rates every 5seconds
    private fun startExchangeRateSync() {
        viewModelScope.launch {
            var previousOptions: List<String>? = null
            while (isActive) {
                try {
                    val response = exchangeRateRepository.getExchangeRateResponse()
                    val options = (listOf(response.base) + response.rates.keys)
                        .distinct()
                        .sorted()

                    if (previousOptions == null || previousOptions != options) {
                        _currencyOptions.value = options
                        previousOptions = options
                    }
                } catch (e: Exception) {
                    Log.d("Fetch API", e.toString())
                }
                delay(5000L)
            }
        }
    }

    // initial wallet
    private fun loadWalletBalances() {
        val currencyList = account.balances.map { (currency, balance) ->
            CurrencyBalance(currency, balance)
        }
        _walletBalances.value = currencyList
        updateSellCurrencyOptions(account)
    }

    private fun updateWalletBalances(newAccount: Account) {
        val currencyList = newAccount.balances.map { (currency, balance) ->
            CurrencyBalance(currency, balance)
        }
        _walletBalances.value = currencyList
        updateSellCurrencyOptions(newAccount)
    }

    private fun updateSellCurrencyOptions(account: Account) {
        _sellCurrencyOptions.value = account.balances.keys.toList().sorted()
    }

    fun previewConversion(amount: Double, from: String, to: String) {
        viewModelScope.launch {
            try {
                val preview = exchangeCurrencyUseCase.previewConversion(amount, from, to)
                _exchangePreview.value = if (preview.netAmount > 0) "+ ${preview.netAmount.toCurrencyString()}" else ""
                _commissionFee.value = if(preview.commissionFee > 0) "Commission Fee: - ${preview.commissionFee.toCurrencyString()} ${from}" else ""
                _error.value = ""
            } catch (e: Exception) {
                Log.d("Preview Conversion", e.toString())
            }
        }
    }

    fun exchangeCurrency(amount: Double, from: String, to: String) {
        viewModelScope.launch {
            try {
                val result = exchangeCurrencyUseCase.exchange(amount, from, to)
                _conversionResult.value = result
                _error.value = ""
                updateWalletBalances(result.updatedAccount)
            } catch (e: Exception) {
                _error.value = e.message ?: "Conversion failed"
            }
        }
    }
}