package org.hyperskill.simplebankmanager.util

import android.app.AlertDialog
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

fun Context.showAlertDialog(
    title: String,
    message: String,
    positiveButtonText: String,
    negativeButtonText: String? = null,
    onPositiveButtonClick: (() -> Unit)? = null
) {
    AlertDialog.Builder(this).apply {
        setTitle(title)
        setMessage(message)
        setPositiveButton(positiveButtonText) { _, _ ->
            onPositiveButtonClick?.invoke()
        }
        setNegativeButton(negativeButtonText) { _, _ -> }
        show()
    }
}

