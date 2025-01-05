package org.hyperskill.simplebankmanager

import android.content.Intent
import org.hyperskill.simplebankmanager.internals.SimpleBankManagerUnitTest
import org.hyperskill.simplebankmanager.internals.screen.LoginScreen
import org.hyperskill.simplebankmanager.internals.screen.UserMenuScreen
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters
import org.robolectric.RobolectricTestRunner

// version 1.4
@RunWith(RobolectricTestRunner::class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class Stage2UnitTest : SimpleBankManagerUnitTest<MainActivity>(MainActivity::class.java) {


    @Test
    fun test00_checkLoginWithDefaultValuesSucceed() {
        testActivity {
            LoginScreen(this).apply {
                assertLogin(
                    caseDescription = "default values"
                )
            }

            UserMenuScreen(this).apply {
                assertWelcomeTitle(
                    messageWelcomeTextError = "Wrong welcome message after login with default values"
                )
            }
        }
    }

    @Test
    fun test01_checkLoginWithCustomValuesSucceed() {
        val username = "Stella"
        val password = "0000"

        val args = Intent().apply {
            putExtra("username", username)
            putExtra("password", password)
        }

        testActivity(arguments = args) {
            LoginScreen(this).apply {
                assertLogin(
                    caseDescription = "custom values",
                    username = username,
                    password = password
                )
            }

            UserMenuScreen(this).apply {
                assertWelcomeTitle(
                    username = username,
                    messageWelcomeTextError = "Wrong welcome message after login with custom values"
                )
            }
        }
    }
}
