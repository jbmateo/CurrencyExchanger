package com.example.currencyexchanger.util

fun Double.toCurrencyString(decimals: Int = 2): String {
    return String.format("%.${decimals}f", this)
}