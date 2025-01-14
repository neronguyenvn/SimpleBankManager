package org.hyperskill.simplebankmanager.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import org.hyperskill.simplebankmanager.MainActivityViewModel
import org.hyperskill.simplebankmanager.R
import org.hyperskill.simplebankmanager.databinding.FragmentCalculateExchangeBinding
import org.hyperskill.simplebankmanager.util.showShortToast

class CalculateExchangeFragment : Fragment() {

    private val binding by lazy { FragmentCalculateExchangeBinding.inflate(layoutInflater) }
    private val viewModel by activityViewModels<MainActivityViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupSpinnerAdapters()
        binding.calculateExchangeToSpinner.setSelection(1)
        setupButtonClickedListeners()
        setupOnSpinnerItemSelectedListeners()
    }

    private fun setupOnSpinnerItemSelectedListeners() = with(binding) {
        val listener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>, view: View?, position: Int, id: Long
            ) {
                if (calculateExchangeFromSpinner.selectedItemPosition !=
                    calculateExchangeToSpinner.selectedItemPosition
                ) {
                    return
                }
                context?.showShortToast(
                    getString(R.string.error_cannot_convert_same_currency)
                )
                val nextPosition = (position + 1) % parent.count
                calculateExchangeToSpinner.setSelection(nextPosition)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) = Unit
        }

        calculateExchangeFromSpinner.onItemSelectedListener = listener
        calculateExchangeToSpinner.onItemSelectedListener = listener
    }

    private fun setupButtonClickedListeners() = with(binding) {
        calculateExchangeButton.setOnClickListener {
            calculateExchange()
        }
    }

    private fun setupSpinnerAdapters() = with(binding) {
        val adapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.currencies_array,
            android.R.layout.simple_spinner_item
        ).also {
            it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }

        calculateExchangeFromSpinner.adapter = adapter
        calculateExchangeToSpinner.adapter = adapter
    }

    private fun calculateExchange() = with(binding) {
        val exchangeAmount = calculateExchangeAmountEditText.text.toString().toDoubleOrNull()
        if (exchangeAmount == null) {
            Toast.makeText(context, "Enter amount", Toast.LENGTH_SHORT).show()
            return@with
        }

        val fromUnit = calculateExchangeFromSpinner.selectedItem.toString()
        val toUnit = calculateExchangeToSpinner.selectedItem.toString()
        val exchangedAmount = viewModel.exchangeMoney(
            amount = exchangeAmount,
            fromUnit = fromUnit,
            toUnit = toUnit
        )

        val fromUnitText = when (fromUnit) {
            "USD" -> getString(R.string.usd_f, exchangeAmount)
            "EUR" -> getString(R.string.eur_f, exchangeAmount)
            "GBP" -> getString(R.string.gbp_f, exchangeAmount)
            else -> error("")
        }
        val toUnitText = when (toUnit) {
            "USD" -> getString(R.string.usd_f, exchangedAmount)
            "EUR" -> getString(R.string.eur_f, exchangedAmount)
            "GBP" -> getString(R.string.gbp_f, exchangedAmount)
            else -> error("")
        }
        calculateExchangeDisplayTextView.text = String.format(
            "%s = %s",
            fromUnitText, toUnitText
        )
    }
}
