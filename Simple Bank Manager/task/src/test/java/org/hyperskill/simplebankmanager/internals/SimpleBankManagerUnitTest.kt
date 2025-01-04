package org.hyperskill.simplebankmanager.internals

import android.app.Activity
import android.app.AlertDialog
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import org.hyperskill.simplebankmanager.internals.screen.UserMenuScreen
import org.junit.Assert.assertEquals
import org.robolectric.Shadows.shadowOf

// version 1.4
open class SimpleBankManagerUnitTest<T : Activity>(clazz: Class<T>) : AbstractUnitTest<T>(clazz) {

    fun Button.assertButtonText(
        idString: String,
        expectedText: String,
        ignoreCase: Boolean = true
    ) {
        assertTextEquals("Wrong text on $idString", expectedText, text, ignoreCase)
    }

    fun EditText.assertHintEditText(
        idString: String,
        expectedHint: String,
        ignoreCase: Boolean = true
    ) {
        assertTextEquals("Wrong hint on $idString", expectedHint, this.hint, ignoreCase)
    }
    fun TextView.assertText(idString: String, expectedText: String, ignoreCase: Boolean = true) {
        assertTextEquals("Wrong text on $idString", expectedText, this.text, ignoreCase)
    }

    fun TextView.assertTextWithCustomErrorMessage(
        errorMessage: String, expectedText: String, ignoreCase: Boolean = true
    ) {
        assertTextEquals(errorMessage, expectedText, this.text, ignoreCase)
    }

    fun EditText.assertEditText(
        idString: String,
        expectedHint: String,
        expectedType: Int,
        typeString: String,
        ignoreCase: Boolean = true
    ) {

        this.assertHintEditText(idString, expectedHint, ignoreCase)
        val actualInputType = this.inputType
        assertEquals(
            "Wrong inputType on $idString should be $typeString",
            expectedType,
            actualInputType
        )
    }

    fun EditText.assertErrorText(errorMessage: String, expectedErrorText: String) {
        val actualErrorText = this.error?.toString()
        assertEquals(errorMessage, expectedErrorText, actualErrorText)
    }

    fun Spinner.assertSpinnerText(
        idString: String,
        expectedDropdown: ArrayList<String>,
        ignoreCase: Boolean = true
    ) {
        val items = ArrayList<String>()
        for (i in 0 until this.adapter.count) {
            items.add(this.adapter.getItem(i).toString())
        }
        val actualDropdownString =
            if (ignoreCase) items.toString().uppercase() else items.toString()
        val expectedDropdownString =
            if(ignoreCase) expectedDropdown.toString().uppercase() else expectedDropdown.toString()
        assertEquals("Wrong text on $idString", expectedDropdownString, actualDropdownString)
    }

    fun AlertDialog.assertDialogTitle(expectedTitle: String, ignoreCase: Boolean = false) {
        val shadowAlertDialog = shadowOf(this)
        val actualTitle = shadowAlertDialog.title

        assertTextEquals(
            "Wrong AlertDialog title", expectedTitle, actualTitle, ignoreCase
        )
    }
    fun AlertDialog.assertDialogMessage(expectedMessage: String, ignoreCase: Boolean = false) {
        val shadowAlertDialog = shadowOf(this)
        val actualMessage = shadowAlertDialog.message
        assertTextEquals("Wrong AlertDialog message", expectedMessage, actualMessage, ignoreCase)
    }
    fun AlertDialog.assertDialogVisibility(caseDescription: String, expectedVisible: Boolean) {
        val actualVisible = this.isShowing
        val messageError = "Dialog should%s be visible: %s".format(
            if(expectedVisible) "" else " not",
            caseDescription
        )
        assertEquals(messageError, expectedVisible, actualVisible)
    }

    fun clickBackButtonAssertNavigateToUserMenuScreen(originScreenName: String) {
        activity.clickBackAndRun()
        try {
            UserMenuScreen(this)
        } catch (error: AssertionError) {
            throw AssertionError(
                "After clicking back button on $originScreenName screen " +
                        "UserMenu screen should be displayed"
            )
        }
    }

    private fun String.normalizeCase(ignoreCase: Boolean): String {
        return if (ignoreCase) this.lowercase() else this
    }

    private fun CharSequence.normalizeCase(ignoreCase: Boolean): String {
        return this.toString().normalizeCase(ignoreCase)
    }

    private fun assertTextEquals(
        errorMessage: String,
        expectedText: CharSequence,
        actualText: CharSequence?,
        ignoreCase: Boolean = true
    )  {
        val (expectedTextNorm, actualTextNorm) = listOf(expectedText, actualText)
            .map { it?.normalizeCase(ignoreCase) }
        assertEquals(errorMessage, expectedTextNorm, actualTextNorm)
    }

    fun String.numberAsCurrencyFormat(currencySymbol: String = "$"): String {
        return this.toDouble().asCurrencyFormat(currencySymbol)
    }

    fun Double.asCurrencyFormat(currencySymbol: String = "$"): String {
        return "$currencySymbol%.2f".format(this)
    }
}