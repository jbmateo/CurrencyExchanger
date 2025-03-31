package com.example.currencyexchanger.di

import com.example.currencyexchanger.data.repository.AccountRepositoryImpl
import com.example.currencyexchanger.data.repository.CurrencyExchangeRateRepositoryImpl
import com.example.currencyexchanger.domain.repository.AccountRepository
import com.example.currencyexchanger.domain.repository.CurrencyExchangeRateRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideCurrencyExchangeRateRepository(
        api: com.example.currencyexchanger.data.api.CurrencyExchangeRateAPI
    ): CurrencyExchangeRateRepository = CurrencyExchangeRateRepositoryImpl(api)

    @Provides
    @Singleton
    fun provideAccountRepository(): AccountRepository = AccountRepositoryImpl()

}