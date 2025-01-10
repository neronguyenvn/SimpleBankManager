package org.hyperskill.simplebankmanager.internals.screen

import android.app.Activity
import android.text.InputType
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import org.hyperskill.simplebankmanager.internals.SimpleBankManagerUnitTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.robolectric.shadows.ShadowToast

// version 1.4
class CalculateExchangeScreen<T : Activity>(private val test: SimpleBankManagerUnitTest<T>) {

    companion object {
        const val EUR = "EUR"
        const val GBP = "GBP"
        const val USD = "USD"
        val expectedDropdownText = arrayListOf(EUR, GBP, USD)
    }

    val defaultMap: Map<String, Map<String, Double>> = mapOf(
        EUR to mapOf(
            GBP to 0.5,
            USD to 2.0
        ),
        GBP to mapOf(
            EUR to 2.0,
            USD to 4.0
        ),
        USD to mapOf(
            EUR to 0.5,
            GBP to 0.25
        )
    )

    val calculateExchangeLabelFromTextView = with(test) {
        val idString = "calculateExchangeLabelFromTextView"
        val expectedText = "convert from"
        activity.findViewByString<TextView>(idString).apply {
            assertText(idString, expectedText)
        }
    }

    val calculateExchangeLabelToTextView = with(test) {
        val idString = "calculateExchangeLabelToTextView"
        val expectedText = "convert to"
        activity.findViewByString<TextView>(idString).apply {
            assertText(idString, expectedText)
        }
    }

    val calculateExchangeConvertFromSpinner = with(test) {
        val idString = "calculateExchangeFromSpinner"
        activity.findViewByString<Spinner>(idString).apply {
            assertSpinnerText(idString, expectedDropdownText)
        }
    }

    val calculateExchangeConvertToSpinner = with(test) {
        val idString = "calculateExchangeToSpinner"
        activity.findViewByString<Spinner>(idString).apply {
            assertSpinnerText(idString, expectedDropdownText)
        }
    }

    val calculateExchangeDisplayTextView = with(test) {
        val idString = "calculateExchangeDisplayTextView"
        activity.findViewByString<TextView>(idString)
    }

    val calculateExchangeAmountEditText = with(test) {
        val idString = "calculateExchangeAmountEditText"
        val expectedType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
        val typeString = "numberDecimal"
        activity.findViewByString<EditText>(idString).apply {
            assertEditText(idString, "Enter amount", expectedType, typeString)
        }
    }

    val calculateExchangeButton = with(test) {
        val idString = "calculateExchangeButton"
        val expectedText = "calculate"
        activity.findViewByString<Button>(idString).apply {
            assertText(idString, expectedText)
        }
    }


    fun assertDisplayConvertedAmount(
        amountToConvert: Double,
        convertFromText: String,
        convertToText: String,
        expectedConvertedAmount: Double
    ) = with(test) {

        setSpinnerCurrencySelection(convertFromText, convertToText)
        calculateExchangeAmountEditText.setText(amountToConvert.toString())

        calculateExchangeButton.clickAndRun()

        val fromCurrencySymbol = currencySymbol(convertFromText)
        val toCurrencySymbol = currencySymbol(convertToText)

        val expectedAmountToConvertFormat = amountToConvert.asCurrencyFormat(fromCurrencySymbol)
        val expectedConvertedAmountFormat =
            expectedConvertedAmount.asCurrencyFormat(toCurrencySymbol)

        calculateExchangeDisplayTextView.apply {
            val expectedText =
                "$expectedAmountToConvertFormat = $expectedConvertedAmountFormat"

            val actualText = this.text.toString().uppercase()
            val messageDisplayError = "calculateExchangeDisplayTextView has wrong text " +
                    "on conversion from $convertFromText to $convertToText,"
            assertEquals(messageDisplayError, expectedText, actualText)
        }
    }

    fun checkForErrorMessages(
        isEmptyAmount: Boolean = false, isSameCurrencySelected: Boolean = false
    ) = with(test) {

        if (isEmptyAmount) {
            calculateExchangeButton.clickAndRun()
            val expectedToastMessage = "Enter amount"
            assertLastToastMessageEquals(
                "Wrong Toast message for empty EditText at CalculateExchange",
                expectedToastMessage
            )
            ShadowToast.reset()
        }
        if (isSameCurrencySelected) {

            val countFrom = calculateExchangeConvertFromSpinner.adapter.count
            val countTo = calculateExchangeConvertToSpinner.adapter.count

            assertTrue(
                "Both spinners at CalculateExchange should have same length",
                countFrom == countTo
            )

            for (i in 0 until countFrom) {
                calculateExchangeConvertFromSpinner.setSelection(i)
                calculateExchangeConvertToSpinner.setSelection(i)
                calculateExchangeAmountEditText.setText("321")

                try {
                    calculateExchangeButton.clickAndRun()
                } catch (e: Exception) {
                    throw AssertionError(
                        "Exception, when same currency selected at CalculateExchange " +
                                "test failed on activity execution with $e", e
                    )
                }

                val expectedToastMessage = "Cannot convert to same currency"

                assertLastToastMessageEquals(
                    "Wrong Toast message for same currency selected at CalculateExchange",
                    expectedToastMessage
                )
                ShadowToast.reset()
            }
        }
    }

    fun setSpinnerCurrencySelection(convertFromText: String, convertToText: String) {

        val convertFrom = when (convertFromText.uppercase()) {
            EUR -> 0
            GBP -> 1
            USD -> 2
            else -> throw Exception("Wrong currency selected or not found")
        }

        calculateExchangeConvertFromSpinner.setSelection(convertFrom)

        val convertTo = when (convertToText.uppercase()) {
            EUR -> 0
            GBP -> 1
            USD -> 2
            else -> throw Exception("Wrong currency selected or not found")
        }
        calculateExchangeConvertToSpinner.setSelection(convertTo)
    }

    fun currencySymbol(countryCode: String): String {
        return when (countryCode) {
            "EUR" -> "€"
            "GBP" -> "£"
            "USD" -> "$"
            else -> throw IllegalArgumentException(
                "only EUR, GBP and USD are accepted as countryCode, but was $countryCode"
            )
        }
    }
}