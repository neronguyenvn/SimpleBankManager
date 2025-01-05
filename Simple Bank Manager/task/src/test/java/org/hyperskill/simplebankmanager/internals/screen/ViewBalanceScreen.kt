package org.hyperskill.simplebankmanager.internals.screen

import android.app.Activity
import android.widget.TextView
import org.hyperskill.simplebankmanager.internals.SimpleBankManagerUnitTest
import org.junit.Assert.assertEquals

// version 1.4
class ViewBalanceScreen<T : Activity>(private val test: SimpleBankManagerUnitTest<T>) {

    val viewBalanceLabelTextView: TextView = with(test) {
        val idString = "viewBalanceLabelTextView"
        activity.findViewByString<TextView>(idString).apply {
            assertText(idString, "balance:")
        }
    }

    val viewBalanceShowBalanceTextView: TextView = with(test) {
        activity.findViewByString("viewBalanceAmountTextView")
    }


    fun assertBalanceAmountDisplay(expectedBalance: String, caseDescription: String = "") {
        val actualBalance = viewBalanceShowBalanceTextView.text.toString()
        assertEquals(
            "Wrong balance on viewBalanceAmountTextView, ${caseDescription}",
            expectedBalance,
            actualBalance
        )
    }
}