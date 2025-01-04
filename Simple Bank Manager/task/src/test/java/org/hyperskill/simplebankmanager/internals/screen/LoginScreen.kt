package org.hyperskill.simplebankmanager.internals.screen


import android.app.Activity
import android.text.InputType
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import org.hyperskill.simplebankmanager.internals.SimpleBankManagerUnitTest
import org.junit.Assert.assertNull
import org.robolectric.shadows.ShadowToast

// version 1.4
class LoginScreen<T: Activity>(private val test: SimpleBankManagerUnitTest<T>) {

    val loginUsername : EditText = with(test) {
        val id = "loginUsername"

        activity.findViewByString<EditText>(id).apply {
            assertEditText(
                idString = id,
                expectedHint = "username",
                expectedType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PERSON_NAME,
                typeString = "textPersonName"
            )
        }
    }

    val loginPassword : EditText = with(test) {
        val id = "loginPassword"
        activity.findViewByString<EditText>(id).apply {
            assertEditText(
                idString = id,
                expectedHint = "password",
                expectedType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_PASSWORD,
                typeString = "numberPassword"
            )
        }
    }

    val loginButton : Button = with(test) {
        val idString = "loginButton"
        val expectedText = "log in"
        activity.findViewByString<Button>(idString).apply {
            assertButtonText(idString, expectedText)
        }
    }



    fun assertLogin(
        caseDescription: String,
        username: String = "Lara",
        password: String = "1234",
        isSucceeded: Boolean = true,
    ) {
        with(test) {
            loginUsername.setText(username)
            loginPassword.setText(password)

            val latestToast: Toast? = ShadowToast.getLatestToast()
            assertNull("There should be no toast messages before loginButton is clicked",
                latestToast)

            loginButton.clickAndRun()

            if(isSucceeded) {
                val message = "Wrong toast message after successful login with $caseDescription"
                assertLastToastMessageEquals(
                    message,
                    "logged in"
                )
            } else {
                val message = "Wrong toast message after unsuccessful login with $caseDescription"
                assertLastToastMessageEquals(
                    message,
                    "invalid credentials"
                )
            }
            ShadowToast.reset()
        }
    }
}