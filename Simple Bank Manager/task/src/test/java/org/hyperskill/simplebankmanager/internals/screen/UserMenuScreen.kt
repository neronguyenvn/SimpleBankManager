package org.hyperskill.simplebankmanager.internals.screen

import android.app.Activity
import android.widget.Button
import android.widget.TextView
import org.hyperskill.simplebankmanager.internals.SimpleBankManagerUnitTest
import org.junit.Assert.assertEquals

// version 1.4
class UserMenuScreen<T: Activity>(private val test: SimpleBankManagerUnitTest<T>) {

    val userMenuWelcomeTextView : TextView = with(test) {
        activity.findViewByString("userMenuWelcomeTextView")
    }
    val userMenuViewBalanceButton : Button = with(test) {
        val idString = "userMenuViewBalanceButton"
        val expectedText = "view balance"
        activity.findViewByString<Button>(idString).apply {
            assertButtonText(idString, expectedText)
        }
    }
    val userMenuTransferFundsButton : Button = with(test) {
        val idString = "userMenuTransferFundsButton"
        val expectedText = "transfer funds"
        activity.findViewByString<Button>(idString).apply {
            assertButtonText(idString, expectedText)
        }
    }
    val userMenuExchangeCalculatorButton : Button = with(test) {
        val idString = "userMenuExchangeCalculatorButton"
        val expectedText = "calculate exchange"
        activity.findViewByString<Button>(idString).apply {
            assertButtonText(idString, expectedText)
        }
    }
    val userMenuPayBillsButton : Button = with(test) {
        val idString = "userMenuPayBillsButton"
        val expectedText = "pay bills"
        activity.findViewByString<Button>(idString).apply {
            assertButtonText(idString, expectedText)
        }
    }

    fun assertWelcomeTitle(username: String = "Lara", messageWelcomeTextError: String) {
        val expectedText = "Welcome $username"
        val actualText = userMenuWelcomeTextView.text.toString()
        assertEquals(messageWelcomeTextError, expectedText, actualText)
    }
}