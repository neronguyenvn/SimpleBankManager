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
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.currencies_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.calculateExchangeFromSpinner.adapter = adapter
            binding.calculateExchangeToSpinner.adapter = adapter
        }

        binding.calculateExchangeToSpinner.setSelection(1)
        binding.calculateExchangeButton.setOnClickListener {
            calculateExchange()
        }

        binding.calculateExchangeFromSpinner.onItemSelectedListener =
            object : OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val toSelectedIndex = binding.calculateExchangeToSpinner.selectedItemPosition

                    if (position == toSelectedIndex) {
                        Toast.makeText(
                            context,
                            "Cannot convert to same currency",
                            Toast.LENGTH_SHORT
                        ).show()

                        val nextToSelectedIndex =
                            if (toSelectedIndex + 1 > binding.calculateExchangeToSpinner.adapter.count) 0
                            else toSelectedIndex + 1

                        binding.calculateExchangeToSpinner.setSelection(nextToSelectedIndex)
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) = Unit
            }

        binding.calculateExchangeToSpinner.onItemSelectedListener =
            object : OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val fromSelectedIndex =
                        binding.calculateExchangeFromSpinner.selectedItemPosition

                    if (position == fromSelectedIndex) {
                        Toast.makeText(
                            context,
                            "Cannot convert to same currency",
                            Toast.LENGTH_SHORT
                        ).show()

                        val nextToSelectedIndex =
                            if (position + 1 >= binding.calculateExchangeToSpinner.adapter.count) 0
                            else position + 1

                        binding.calculateExchangeToSpinner.setSelection(nextToSelectedIndex)
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) = Unit
            }
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
