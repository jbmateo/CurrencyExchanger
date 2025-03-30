package com.example.currencyexchanger.ui.exchanger

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.currencyexchanger.R
import com.example.currencyexchanger.databinding.FragmentExchangerBinding
import com.example.currencyexchanger.ui.dialog.ExchangeResultDialogFragment
import com.example.currencyexchanger.ui.exchanger.adapter.WalletAdapter
import com.example.currencyexchanger.util.DecimalDigitsInputFilter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ExchangerFragment : Fragment() {

    private var _binding: FragmentExchangerBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ConversionViewModel by viewModels()

    private lateinit var walletAdapter: WalletAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExchangerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // Setup wallet adapter
        walletAdapter = WalletAdapter(emptyList())
        binding.walletCard.walletRecyclerView.adapter = walletAdapter
        val gridLayoutManager = GridLayoutManager(
            requireContext(), 3, GridLayoutManager.HORIZONTAL, false
        )
        binding.walletCard.walletRecyclerView.layoutManager = gridLayoutManager

        binding.amountEditText.apply {
            filters = arrayOf(DecimalDigitsInputFilter(6, 2))
            doOnTextChanged { text, start, before, count -> checkConversionPreview() }
        }

        binding.submitButton.setOnClickListener {
            getConversionParams()?.let { (amount, sellCurrency, buyCurrency) ->
                viewModel.exchangeCurrency(amount, sellCurrency, buyCurrency)
            }
        }

        viewModel.walletBalances.observe(viewLifecycleOwner) { balances ->
            walletAdapter.updateData(balances)
        }

        viewModel.currencyOptions.observe(viewLifecycleOwner) { options ->
            setupSpinner(binding.buyCurrencySpinner, options)
            checkConversionPreview()
        }

        viewModel.sellCurrencyOptions.observe(viewLifecycleOwner) { options ->
            setupSpinner(binding.sellCurrencySpinner, options)
        }

        viewModel.exchangePreview.observe(viewLifecycleOwner) { preview ->
            binding.previewText.text = preview
        }

        viewModel.conversionResult.observe(viewLifecycleOwner) { result ->
            result?.let {
                // Show the dialog when a valid conversion result is received.
                binding.amountEditText.text = null
                ExchangeResultDialogFragment(it).show(
                    childFragmentManager,
                    "ConversionResultDialog"
                )
            }
        }

        viewModel.commissionFee.observe(viewLifecycleOwner) { fee ->
            binding.feeText.text = fee
        }

        viewModel.error.observe(viewLifecycleOwner) { errorMsg ->
            binding.errorTextView.text = errorMsg
        }
    }

    private fun setupSpinner(spinner: Spinner, options: List<String>) {
        // Store the current selection (if any)
        val currentSelection = spinner.selectedItem?.toString()

        // Create a new adapter with the updated list
        val adapter = ArrayAdapter(requireContext(), R.layout.custom_spinner_item, options)
        adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_item)
        spinner.adapter = adapter

        // Restore the previous selection if it's still present in the new options
        currentSelection?.let {
            val index = options.indexOf(it)
            if (index >= 0) {
                spinner.setSelection(index)
            }
        }

        spinner.onItemSelectedListener = spinnerListener
    }

    // Create a single listener instance
    private val spinnerListener = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
            checkConversionPreview()
        }

        override fun onNothingSelected(parent: AdapterView<*>) { /* no action */
        }
    }

    private fun checkConversionPreview() {
        getConversionParams()?.let { (amount, sellCurrency, buyCurrency) ->
            viewModel.previewConversion(amount, sellCurrency, buyCurrency)
        }
    }

    private fun getConversionParams(): Triple<Double, String, String>? {
        val amount = binding.amountEditText.text.toString().toDoubleOrNull() ?: 0.0
        val sellCurrency = binding.sellCurrencySpinner.selectedItem?.toString() ?: return null
        val buyCurrency = binding.buyCurrencySpinner.selectedItem?.toString() ?: return null

        val params = Triple(amount, sellCurrency, buyCurrency)

        return params
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}