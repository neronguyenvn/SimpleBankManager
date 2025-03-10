package org.hyperskill.simplebankmanager

import android.content.Intent
import org.hyperskill.simplebankmanager.internals.SimpleBankManagerUnitTest
import org.hyperskill.simplebankmanager.internals.screen.CalculateExchangeScreen
import org.hyperskill.simplebankmanager.internals.screen.CalculateExchangeScreen.Companion.EUR
import org.hyperskill.simplebankmanager.internals.screen.CalculateExchangeScreen.Companion.GBP
import org.hyperskill.simplebankmanager.internals.screen.CalculateExchangeScreen.Companion.USD
import org.hyperskill.simplebankmanager.internals.screen.LoginScreen
import org.hyperskill.simplebankmanager.internals.screen.UserMenuScreen
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters
import org.robolectric.RobolectricTestRunner

// version 1.4.1
@RunWith(RobolectricTestRunner::class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class Stage4UnitTest : SimpleBankManagerUnitTest<MainActivity>(MainActivity::class.java) {


    @Test
    fun test00_checkNavigationOnCalculateExchange() {
        testActivity {
            LoginScreen(this).apply {
                assertLogin(
                    caseDescription = "default values"
                )
            }
            UserMenuScreen(this).apply {
                userMenuExchangeCalculatorButton.clickAndRun()
            }

            CalculateExchangeScreen(this).apply {
                clickBackButtonAssertNavigateToUserMenuScreen(
                    originScreenName = "CalculateExchange"
                )
            }
        }
    }


    @Test
    fun test01_convertEURtoGBP() {
        testActivity {
            LoginScreen(this).apply {
                assertLogin(
                    caseDescription = "default values"
                )
            }
            UserMenuScreen(this).apply {
                userMenuExchangeCalculatorButton.clickAndRun()
            }
            CalculateExchangeScreen(this).apply {
                val amountToConvert = 350.0
                val expectedConvertedAmount = amountToConvert * defaultMap[EUR]!![GBP]!!
                assertDisplayConvertedAmount(
                    amountToConvert,
                    EUR,
                    GBP,
                    expectedConvertedAmount
                ) // conversion is set to 2 decimal points
            }
        }
    }

    @Test
    fun test02_convertUSDtoEUR() {
        testActivity {
            LoginScreen(this).apply {
                assertLogin(
                    caseDescription = "default values"
                )
            }
            UserMenuScreen(this).apply {
                userMenuExchangeCalculatorButton.clickAndRun()
            }
            CalculateExchangeScreen(this).apply {
                val amountToConvert = 100.0
                val expectedConvertedAmount = amountToConvert * defaultMap[USD]!![EUR]!!
                assertDisplayConvertedAmount(
                    amountToConvert,
                    USD,
                    EUR,
                    expectedConvertedAmount
                ) // conversion is set to 2 decimal points
            }
        }
    }

    @Test
    fun test03_convertGBPtoEUR() {
        testActivity {
            LoginScreen(this).apply {
                assertLogin(
                    caseDescription = "default values"
                )
            }
            UserMenuScreen(this).apply {
                userMenuExchangeCalculatorButton.clickAndRun()
            }
            CalculateExchangeScreen(this).apply {
                val amountToConvert = 345.0
                val expectedConvertedAmount = amountToConvert * defaultMap[GBP]!![EUR]!!
                assertDisplayConvertedAmount(
                    amountToConvert,
                    GBP,
                    EUR,
                    expectedConvertedAmount
                ) // conversion is set to 2 decimal points
            }
        }
    }

    @Test
    fun test04_CheckForErrorSameCurrenciesSelected() {
        testActivity {
            LoginScreen(this).apply {
                assertLogin(
                    caseDescription = "default values"
                )
            }
            UserMenuScreen(this).apply {
                userMenuExchangeCalculatorButton.clickAndRun()
            }
            CalculateExchangeScreen(this).apply {
                checkForErrorMessages(isEmptyAmount = true)
            }
        }
    }

    @Test
    fun test05_CheckForErrorEmptyAmount() {
        testActivity {
            LoginScreen(this).apply {
                assertLogin(
                    caseDescription = "default values"
                )
            }
            UserMenuScreen(this).apply {
                userMenuExchangeCalculatorButton.clickAndRun()
            }
            CalculateExchangeScreen(this).apply {
                checkForErrorMessages(isSameCurrencySelected = true)
            }
        }
    }

    @Test
    fun test06_CheckIfSameCurrencyCanBeSelected() {
        testActivity {
            LoginScreen(this).apply {
                assertLogin(
                    caseDescription = "default values"
                )
            }
            UserMenuScreen(this).apply {
                userMenuExchangeCalculatorButton.clickAndRun()
            }
            CalculateExchangeScreen(this).apply {
                setSpinnerCurrencySelection(USD, USD)
                val convertFrom = calculateExchangeConvertFromSpinner.selectedItem
                val convertTo = calculateExchangeConvertToSpinner.selectedItem
                assertTrue("Currencies for \"convert from\"=$convertFrom and \"convert to\"=$convertTo"
                        + " should not be selected as equal",
                    convertFrom != convertTo
                )
            }
        }
    }

    @Test
    fun test07_convertAllDefaultMap() {


        testActivity {
            LoginScreen(this).apply {
                assertLogin(
                    caseDescription = "default values"
                )
            }
            UserMenuScreen(this).apply {
                userMenuExchangeCalculatorButton.clickAndRun()
            }

            CalculateExchangeScreen(this).apply {
                for (from in defaultMap.keys) {
                    val fromMap = defaultMap[from]!!
                    for (to in fromMap.keys) {
                        val rate = fromMap[to]!!
                        val amountToConvert = 100.0
                        val expectedConvertedAmount = amountToConvert * rate

                        assertDisplayConvertedAmount(
                            amountToConvert,
                            from,
                            to,
                            expectedConvertedAmount
                        ) // conversion is set to 2 decimal points
                    }
                }
            }
        }
    }

    @Test
    fun test08_convertAllCustomMap() {

        val exchangeMap: Map<String, Map<String, Double>> = mapOf(
            EUR to mapOf(
                GBP to 0.886,
                USD to 1.074
            ),
            GBP to mapOf(
                EUR to 1.128,
                USD to 1.212
            ),
            USD to mapOf(
                EUR to 0.913,
                GBP to 0.825
            )
        )

        val args = Intent().apply {
            putExtra("exchangeMap", exchangeMap as java.io.Serializable)
        }

        testActivity(arguments = args) {
            LoginScreen(this).apply {
                assertLogin(
                    caseDescription = "default values"
                )
            }
            UserMenuScreen(this).apply {
                userMenuExchangeCalculatorButton.clickAndRun()
            }

            CalculateExchangeScreen(this).apply {
                for (from in exchangeMap.keys) {
                    val fromMap = exchangeMap[from]!!
                    for (to in fromMap.keys) {
                        val rate = fromMap[to]!!
                        val amountToConvert = 100.0
                        val expectedConvertedAmount = amountToConvert * rate

                        assertDisplayConvertedAmount(
                            amountToConvert,
                            from,
                            to,
                            expectedConvertedAmount
                        ) // conversion is set to 2 decimal points
                    }
                }
            }
        }
    }
}