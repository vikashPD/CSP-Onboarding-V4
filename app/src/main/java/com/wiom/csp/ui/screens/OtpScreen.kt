package com.wiom.csp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wiom.csp.data.OnboardingState
import com.wiom.csp.data.Scenario
import com.wiom.csp.ui.components.*
import com.wiom.csp.ui.theme.*
import com.wiom.csp.util.t
import com.wiom.csp.ui.viewmodel.*
import kotlinx.coroutines.delay

// Screen 1: OTP Verification
@Composable
fun OtpTncScreen(viewModel: OtpViewModel, onNext: () -> Unit, onBack: () -> Unit) {
    val scenario = OnboardingState.activeScenario

    // Timer state
    var timerSeconds by remember { mutableIntStateOf(30) }
    var timerExpired by remember { mutableStateOf(false) }

    // Countdown timer
    LaunchedEffect(timerExpired) {
        if (!timerExpired) {
            while (timerSeconds > 0) {
                delay(1000)
                timerSeconds--
            }
            timerExpired = true
        }
    }

    // Format phone display
    val phoneDisplay = if (OnboardingState.phoneNumber.length == 10) {
        val p = OnboardingState.phoneNumber
        "+91 ${p.substring(0, 5)} ${p.substring(5)}"
    } else {
        "+91 98765 43210"
    }

    Column(modifier = Modifier.fillMaxSize().background(WiomSurface)) {
        AppHeader(title = t("OTP वेरिफिकेशन", "Verify OTP"), onBack = onBack)

        if (scenario == Scenario.OTP_WRONG) {
            // ─── OTP_WRONG error screen ───
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Spacer(Modifier.height(16.dp))
                Text(
                    t("OTP डालें", "Enter OTP"),
                    fontSize = 20.sp, fontWeight = FontWeight.Bold, color = WiomText,
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    t("$phoneDisplay पर भेजा गया", "Sent to $phoneDisplay"),
                    fontSize = 14.sp, color = WiomTextSec,
                )
                OtpRow(isError = true)
                ErrorCard(
                    icon = "❌",
                    titleHi = "गलत OTP",
                    titleEn = "Wrong OTP",
                    messageHi = "कृपया दोबारा कोशिश करें — 2 प्रयास बाकी हैं",
                    messageEn = "Please try again — 2 attempts remaining",
                    type = "error",
                )
                Spacer(Modifier.height(12.dp))
                Text(
                    t("OTP दोबारा भेजें", "Resend OTP"),
                    fontSize = 14.sp, color = WiomPrimary, fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.clickable { OnboardingState.clearScenario() },
                )
            }
            BottomBar {
                WiomButton(t("वेरिफ़ाई करें", "Verify"), onClick = { OnboardingState.clearScenario() })
            }
        } else if (scenario == Scenario.OTP_EXPIRED) {
            // ─── OTP_EXPIRED error screen ───
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Spacer(Modifier.height(16.dp))
                Text(
                    t("OTP डालें", "Enter OTP"),
                    fontSize = 20.sp, fontWeight = FontWeight.Bold, color = WiomText,
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    t("$phoneDisplay पर भेजा गया", "Sent to $phoneDisplay"),
                    fontSize = 14.sp, color = WiomTextSec,
                )
                OtpRow(isExpired = true)
                ErrorCard(
                    icon = "⏰",
                    titleHi = "OTP expired हो गया",
                    titleEn = "OTP has expired",
                    messageHi = "चिंता न करें — नया OTP भेजें",
                    messageEn = "Don't worry — send a new OTP",
                    type = "warning",
                )
                Spacer(Modifier.height(16.dp))
                WiomButton(t("नया OTP भेजें", "Send New OTP"), onClick = { OnboardingState.clearScenario() })
                Spacer(Modifier.height(8.dp))
                WiomButton(t("नंबर बदलें", "Change Number"), onClick = { OnboardingState.clearScenario(); onBack() }, isSecondary = true)
            }
        } else {
            // ─── Normal happy path ───
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Spacer(Modifier.height(16.dp))
                Text(
                    t("OTP डालें", "Enter OTP"),
                    fontSize = 20.sp, fontWeight = FontWeight.Bold, color = WiomText,
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    t("$phoneDisplay पर भेजा गया", "Sent to $phoneDisplay"),
                    fontSize = 14.sp, color = WiomTextSec,
                )
                // Interactive OTP input
                OtpInputRow(
                    digits = OnboardingState.otpDigits,
                    onDigitsChange = { OnboardingState.otpDigits = it },
                )

                // Timer / Resend / Change Number
                if (!timerExpired) {
                    Text(
                        t("OTP दोबारा भेजें", "Resend OTP") + " ${timerSeconds}s",
                        fontSize = 12.sp, color = WiomHint,
                    )
                } else {
                    // Timer expired — show resend + change number
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            t("OTP दोबारा भेजें", "Resend OTP"),
                            fontSize = 14.sp,
                            color = WiomPrimary,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.clickable {
                                timerSeconds = 30
                                timerExpired = false
                            },
                        )
                        Text("|", fontSize = 14.sp, color = WiomHint)
                        Text(
                            t("नंबर बदलें", "Change Number"),
                            fontSize = 14.sp,
                            color = WiomTextSec,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.clickable { onBack() },
                        )
                    }
                }
            }
            BottomBar {
                val allFilled = OnboardingState.otpDigits.all { it.isNotEmpty() }
                WiomButton(
                    t("वेरिफ़ाई करें", "Verify"),
                    onClick = onNext,
                    enabled = allFilled,
                )
            }
        }
    }
}
