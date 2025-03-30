package com.example.currencyexchanger.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.currencyexchanger.R
import com.example.currencyexchanger.ui.exchanger.ExchangerFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, ExchangerFragment())
                .commit()
        }
    }
}