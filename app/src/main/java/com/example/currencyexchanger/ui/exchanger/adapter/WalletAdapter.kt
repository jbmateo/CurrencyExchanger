package com.example.currencyexchanger.ui.exchanger.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.currencyexchanger.databinding.ItemCurrencyBinding
import com.example.currencyexchanger.util.toCurrencyString

class WalletAdapter(private var currencies: List<CurrencyBalance>) :
    RecyclerView.Adapter<WalletAdapter.WalletViewHolder>() {

    inner class WalletViewHolder(val binding: ItemCurrencyBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WalletViewHolder {
        val binding =
            ItemCurrencyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WalletViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WalletViewHolder, position: Int) {
        val item = currencies[position]
        // Bind your data, for example:
        holder.binding.currencyCode.text = item.currency
        holder.binding.currencyAmount.text = item.balance.toCurrencyString()
    }

    override fun getItemCount(): Int = currencies.size

    fun updateData(newBalances: List<CurrencyBalance>) {
        currencies = newBalances
        notifyDataSetChanged()
    }
}