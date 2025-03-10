package org.hyperskill.simplebankmanager

import android.content.Intent
import org.hyperskill.simplebankmanager.internals.SimpleBankManagerUnitTest
import org.hyperskill.simplebankmanager.internals.screen.*
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters
import org.robolectric.RobolectricTestRunner

// version 1.4
@RunWith(RobolectricTestRunner::class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class Stage5UnitTest : SimpleBankManagerUnitTest<MainActivity>(MainActivity::class.java) {

    companion object {
        private const val DIALOG_BILL_PAYMENT_TITLE: String = "Bill info"
        private const val DIALOG_ERROR_TITLE: String = "Error"
        private const val DIALOG_BILL_MESSAGE_ERROR_WRONG_CODE: String = "Wrong code"
        private const val DIALOG_ERROR_MESSAGE_NOT_ENOUGH_FUNDS: String = "Not enough funds"

        private const val DIALOG_BILL_MESSAGE_ELECTRICITY: String =
            "Name: Electricity\nBillCode: ELEC\nAmount: $45.00"
        private const val DIALOG_BILL_MESSAGE_WATER: String = "Name: Water\nBillCode: WTR\nAmount: $25.50"
        private const val DIALOG_BILL_MESSAGE_GAS: String = "Name: Gas\nBillCode: GAS\nAmount: $20.00"

        private const val BILL_CODE_ELECTRICITY: String = "ELEC"
        private const val BILL_CODE_WATER: String = "WTR"
        private const val BILL_CODE_GAS: String = "GAS"
    }



    @Test
    fun test00_checkNavigationOnPayBills() {
        testActivity {
            LoginScreen(this).apply {
                assertLogin(
                    caseDescription = "default values"
                )
            }
            UserMenuScreen(this).apply {
                userMenuPayBillsButton.clickAndRun()
            }
            PayBillsScreen(this).apply {
                clickBackButtonAssertNavigateToUserMenuScreen(
                    originScreenName = "PayBills"
                )
            }
        }
    }

    @Test
    fun test01_checkDialogDataCorrectBillCodeInput() {
        testActivity {
            LoginScreen(this).apply {
                assertLogin(
                    caseDescription = "default values"
                )
            }
            UserMenuScreen(this).apply {
                userMenuPayBillsButton.clickAndRun()
            }

            PayBillsScreen(this).apply {
                inputBillCodeAndClickShowBillInfoButton(
                    billCode = BILL_CODE_ELECTRICITY,
                    expectedDialogTitle = DIALOG_BILL_PAYMENT_TITLE,
                    expectedDialogMessage = DIALOG_BILL_MESSAGE_ELECTRICITY
                )
            }
        }
    }

    @Test
    fun test02_checkDialogDataIncorrectBillCode() {
        testActivity {
            LoginScreen(this).apply {
                assertLogin(
                    caseDescription = "default values"
                )
            }
            UserMenuScreen(this).apply {
                userMenuPayBillsButton.clickAndRun()
            }

            PayBillsScreen(this).apply {
                inputBillCodeAndClickShowBillInfoButton(
                    billCode = "phone",
                    expectedDialogTitle = DIALOG_ERROR_TITLE,
                    expectedDialogMessage = DIALOG_BILL_MESSAGE_ERROR_WRONG_CODE
                )
            }
        }
    }

    @Test
    fun test03_payBillSuccess() {
        testActivity {
            LoginScreen(this).apply {
                assertLogin(
                    caseDescription = "default values"
                )
            }
            UserMenuScreen(this).apply {
                userMenuViewBalanceButton.clickAndRun()
            }
            ViewBalanceScreen(this).apply {
                assertBalanceAmountDisplay(
                    expectedBalance = "\$100.00",
                    caseDescription = "with default initial balance values"
                )
                clickBackButtonAssertNavigateToUserMenuScreen(
                    originScreenName = "ViewBalance"
                )
            }
            UserMenuScreen(this).apply {
                userMenuPayBillsButton.clickAndRun()
            }

            PayBillsScreen(this).apply {

                inputBillCodeAndClickShowBillInfoButton(
                    billCode = BILL_CODE_WATER,
                    expectedDialogTitle = DIALOG_BILL_PAYMENT_TITLE,
                    expectedDialogMessage = DIALOG_BILL_MESSAGE_WATER
                ).acceptBillPaymentAssertSuccessMessage("Water")

                clickBackButtonAssertNavigateToUserMenuScreen(
                    originScreenName = "PayBills"
                )
            }
            UserMenuScreen(this).apply {
                userMenuViewBalanceButton.clickAndRun()
            }
            ViewBalanceScreen(this).apply {
                assertBalanceAmountDisplay(
                    expectedBalance = "\$74.50",
                    caseDescription = "after payment of bill water"
                )
            }
        }
    }

    @Test
    fun test04_checkDialogDataCorrectBillCodeCancelBillPayment() {
        testActivity {
            LoginScreen(this).apply {
                assertLogin(
                    caseDescription = "default values"
                )
            }
            UserMenuScreen(this).apply {
                userMenuPayBillsButton.clickAndRun()
            }

            PayBillsScreen(this).apply {
                inputBillCodeAndClickShowBillInfoButton(
                    billCode = "phone",
                    expectedDialogTitle = DIALOG_ERROR_TITLE,
                    expectedDialogMessage = DIALOG_BILL_MESSAGE_ERROR_WRONG_CODE
                ).declineBillPayment()
            }
        }
    }

    @Test
    fun test05_afterTransactionBillPaymentInsufficientFunds() {
        testActivity {
            LoginScreen(this).apply {
                assertLogin(
                    caseDescription = "default values"
                )
            }
            UserMenuScreen(this).apply {
                userMenuTransferFundsButton.clickAndRun()
            }
            TransferFundsScreen(this).apply {
                transferFundsAmountEditText.setText("90")
                transferFundsAccountEditText.setText("sa9276")
                transferFundsButton.clickAndRun()
            }

            try {
                UserMenuScreen(this)
            } catch (error: AssertionError) {
                throw AssertionError("When transfer is successful user should be automatically redirected to UserMenu screen")
            }.apply {
                userMenuPayBillsButton.clickAndRun()
            }

            PayBillsScreen(this).apply {
                inputBillCodeAndClickShowBillInfoButton(
                    billCode = BILL_CODE_ELECTRICITY,
                    expectedDialogTitle = DIALOG_BILL_PAYMENT_TITLE,
                    expectedDialogMessage = DIALOG_BILL_MESSAGE_ELECTRICITY
                ).acceptBillPaymentAssertFail(
                    titleBillPaymentDialog = DIALOG_BILL_PAYMENT_TITLE,
                    expectedTitleFailDialog = DIALOG_ERROR_TITLE,
                    expectedMessageFailDialog = DIALOG_ERROR_MESSAGE_NOT_ENOUGH_FUNDS
                )
            }
        }
    }

    @Test
    fun test06_withCustomBalanceInsufficientFundsAfterSecondBillPayment() {
        val username = "Elaine"
        val password = "9678"

        val args = Intent().apply {
            putExtra("username", username)
            putExtra("password", password)
            putExtra("balance", 55.32)
        }
        testActivity(arguments = args) {
            LoginScreen(this).apply {
                assertLogin(
                    username = username,
                    password = password,
                    caseDescription = "custom values"
                )
            }
            UserMenuScreen(this).apply {
                userMenuPayBillsButton.clickAndRun()
            }
            PayBillsScreen(this).apply {
                inputBillCodeAndClickShowBillInfoButton(
                    billCode = BILL_CODE_GAS,
                    expectedDialogTitle = DIALOG_BILL_PAYMENT_TITLE,
                    expectedDialogMessage = DIALOG_BILL_MESSAGE_GAS
                ).acceptBillPaymentAssertSuccessMessage(billName = "Gas")

                inputBillCodeAndClickShowBillInfoButton(
                    billCode = BILL_CODE_ELECTRICITY,
                    expectedDialogTitle = DIALOG_BILL_PAYMENT_TITLE,
                    expectedDialogMessage = DIALOG_BILL_MESSAGE_ELECTRICITY
                ).acceptBillPaymentAssertFail(
                    titleBillPaymentDialog = DIALOG_BILL_PAYMENT_TITLE,
                    expectedTitleFailDialog = DIALOG_ERROR_TITLE,
                    expectedMessageFailDialog = DIALOG_ERROR_MESSAGE_NOT_ENOUGH_FUNDS,
                )
            }
        }
    }

    @Test
    fun test07_customBillSuccessfulPayment() {
        val billInfoMap =
            mapOf(
                "PHONE" to Triple("Mobile phone", "PHONE", 80.0)
            )

        val args = Intent().apply {
            putExtra("billInfo", billInfoMap as java.io.Serializable)
            putExtra("balance", 100.00)
        }

        testActivity(arguments = args) {
            LoginScreen(this).apply {
                assertLogin(
                    caseDescription = "default values"
                )
            }
            UserMenuScreen(this).apply {
                userMenuViewBalanceButton.clickAndRun()
            }
            ViewBalanceScreen(this).apply {
                assertBalanceAmountDisplay(
                    expectedBalance = "\$100.00",
                    caseDescription = "with default initial balance values"
                )
                clickBackButtonAssertNavigateToUserMenuScreen(
                    originScreenName = "ViewBalance"
                )
            }
            UserMenuScreen(this).apply {
                userMenuPayBillsButton.clickAndRun()
            }
            PayBillsScreen(this).apply {
                inputBillCodeAndClickShowBillInfoButton(
                    billCode = "PHONE",
                    expectedDialogTitle = DIALOG_BILL_PAYMENT_TITLE,
                    expectedDialogMessage = "Name: Mobile phone\nBillCode: PHONE\nAmount: $80.00"
                ).acceptBillPaymentAssertSuccessMessage("Mobile phone")
            }
        }

    }

    @Test
    fun test08_customBillInsufficientBalance() {
        val billInfoMap =
            mapOf(
                "CARINSURANCE" to Triple("Car insurance", "CARINSURANCE", 120.0)
            )

        val args = Intent().apply {
            putExtra("billInfo", billInfoMap as java.io.Serializable)
            putExtra("balance", 100.00)

        }
        testActivity(arguments = args) {
            LoginScreen(this).apply {
                assertLogin(
                    caseDescription = "default values"
                )
            }
            UserMenuScreen(this).apply {
                userMenuViewBalanceButton.clickAndRun()
            }
            ViewBalanceScreen(this).apply {
                assertBalanceAmountDisplay(
                    expectedBalance = "\$100.00",
                    caseDescription = "with default initial balance values"
                )
                clickBackButtonAssertNavigateToUserMenuScreen(
                    originScreenName = "ViewBalance"
                )
            }
            UserMenuScreen(this).apply {
                userMenuPayBillsButton.clickAndRun()
            }
            PayBillsScreen(this).apply {
                inputBillCodeAndClickShowBillInfoButton(
                    billCode = "CARINSURANCE",
                    expectedDialogTitle = DIALOG_BILL_PAYMENT_TITLE,
                    expectedDialogMessage = "Name: Car insurance\nBillCode: CARINSURANCE\nAmount: $120.00"
                ).acceptBillPaymentAssertFail(
                    titleBillPaymentDialog = DIALOG_BILL_PAYMENT_TITLE,
                    expectedTitleFailDialog = DIALOG_ERROR_TITLE,
                    expectedMessageFailDialog = DIALOG_ERROR_MESSAGE_NOT_ENOUGH_FUNDS
                )
            }
        }
    }
}