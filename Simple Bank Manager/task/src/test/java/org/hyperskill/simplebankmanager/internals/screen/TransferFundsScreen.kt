package org.hyperskill.simplebankmanager.internals.screen

import android.app.Activity
import android.text.InputType
import android.widget.Button
import android.widget.EditText
import org.hyperskill.simplebankmanager.internals.SimpleBankManagerUnitTest

// version 1.4
class TransferFundsScreen<T : Activity>(private val test : SimpleBankManagerUnitTest<T>) {

    val transferFundsAccountEditText = with(test) {
        val idString = "transferFundsAccountEditText"
        val expectedType = InputType.TYPE_CLASS_TEXT
        val typeString = "text"

        activity.findViewByString<EditText>(idString).apply {
            assertEditText(idString, "Account number", expectedType, typeString)
        }
    }
    val transferFundsAmountEditText = with(test) {
        val idString = "transferFundsAmountEditText"
        val expectedType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
        val typeString = "numberDecimal"

        activity.findViewByString<EditText>(idString).apply {
            assertEditText(idString, "Enter amount", expectedType, typeString)
        }
    }
    val transferFundsButton = with(test) {
        val idString = "transferFundsButton"
        val expectedText = "transfer"
        activity.findViewByString<Button>(idString).apply {
            assertButtonText(idString, expectedText)
        }
    }
}