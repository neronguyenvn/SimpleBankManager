package org.hyperskill.simplebankmanager.util

import android.content.Context
import android.widget.Toast
import org.hyperskill.simplebankmanager.R

fun Context.showShortToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Context.getFormattedCurrencyText(unit: String, amount: Double): String? {
    return when (unit) {
        "USD" -> getString(R.string.usd_f, amount)
        "EUR" -> getString(R.string.eur_f, amount)
        "GBP" -> getString(R.string.gbp_f, amount)
        else -> null // Return null for unsupported units
    }
}
