package com.example.currencyexchanger.ui.dialog

import android.app.Dialog
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.currencyexchanger.databinding.DialogExchangeResultBinding
import com.example.currencyexchanger.domain.model.ExchangeResult

class ExchangeResultDialogFragment(private val result: ExchangeResult) : DialogFragment() {
    private var _binding: DialogExchangeResultBinding? = null
    private val binding get() = _binding!!

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = DialogExchangeResultBinding.inflate(LayoutInflater.from(context))

        binding.dialogTitle.text = "Currency converted"
        binding.dialogMessage.text = formatConversionResult(result)

        binding.dialogPositiveButton.setOnClickListener {
            dismiss()
        }

        return AlertDialog.Builder(requireContext())
            .setView(binding.root)
            .create()
    }

    private fun formatConversionResult(result: ExchangeResult): CharSequence {
        val sold = String.format("%.2f %s", result.soldAmount, result.soldCurrency)
        val bought = String.format("%.2f %s", result.boughtAmount, result.boughtCurrency)
        val fee = String.format("%.2f %s", result.commissionFee, result.soldCurrency)

        val resultText = "You have converted <b>$sold</b> to <b>$bought</b>. Commission Fee - <b>$fee</b>."
        return Html.fromHtml(resultText, Html.FROM_HTML_MODE_COMPACT)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}