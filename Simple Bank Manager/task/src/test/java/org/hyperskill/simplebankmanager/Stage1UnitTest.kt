package org.hyperskill.simplebankmanager

import android.content.Intent
import org.hyperskill.simplebankmanager.internals.SimpleBankManagerUnitTest
import org.hyperskill.simplebankmanager.internals.screen.LoginScreen
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters
import org.robolectric.RobolectricTestRunner

// version 1.4
@RunWith(RobolectricTestRunner::class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class Stage1UnitTest : SimpleBankManagerUnitTest<MainActivity>(MainActivity::class.java){


    @Test
    fun test00_checkLoginFragmentHasViews() {
        testActivity {
            LoginScreen(this)
        }
    }

    @Test
    fun test01_checkLoginWithDefaultValuesSucceed() {
        testActivity {
            LoginScreen(this).apply {
                assertLogin(
                    caseDescription = "default values"
                )
            }
        }
    }

    @Test
    fun test02_checkLoginWithDefaultValuesFailWithWrongName() {
        testActivity {
            LoginScreen(this).apply {
                assertLogin(
                    caseDescription = "wrong username for default values",
                    username = "John",
                    isSucceeded = false
                )
            }
        }
    }

    @Test
    fun test03_checkLoginWithDefaultValuesFailWithWrongPass() {
        testActivity {
            LoginScreen(this).apply {
                assertLogin(
                    caseDescription = "wrong password for default values",
                    password = "1111",
                    isSucceeded = false
                )
            }
        }
    }

    @Test
    fun test04_checkLoginWithCustomValuesSucceed() {
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
        }
    }

    @Test
    fun test05_checkLoginWithCustomValuesFailWithWrongName() {
        val username = "Stella"
        val password = "0000"

        val args = Intent().apply {
            putExtra("username", username)
            putExtra("password", password)
        }

        testActivity(arguments = args) {
            LoginScreen(this).apply {
                assertLogin(
                    caseDescription = "wrong username for custom values",
                    username = "John",
                    password = password,
                    isSucceeded = false
                )
            }
        }
    }

    @Test
    fun test06_checkLoginWithCustomValuesFailWithWrongPass() {
        val username = "Stella"
        val password = "0000"

        val args = Intent().apply {
            putExtra("username", username)
            putExtra("password", password)
        }

        testActivity(arguments = args) {
            LoginScreen(this).apply {
                assertLogin(
                    caseDescription = "wrong password for custom values",
                    username = username,
                    password = "9876",
                    isSucceeded = false
                )
            }
        }
    }
}