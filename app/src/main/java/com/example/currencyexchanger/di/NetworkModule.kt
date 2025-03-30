package com.example.currencyexchanger.di

import com.example.currencyexchanger.data.api.CurrencyExchangeRateAPI
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    // Set the base URL using the API endpoint provided.
    private const val BASE_URL = "https://developers.paysera.com/tasks/api/"

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun provideCurrencyExchangeRateAPI(retrofit: Retrofit): CurrencyExchangeRateAPI =
        retrofit.create(CurrencyExchangeRateAPI::class.java)

//    // Optional: Provide ConversionHistoryAPI if needed.
//    @Provides
//    @Singleton
//    fun provideConversionHistoryAPI(retrofit: Retrofit): ConversionHistoryAPI =
//        retrofit.create(ConversionHistoryAPI::class.java)
}