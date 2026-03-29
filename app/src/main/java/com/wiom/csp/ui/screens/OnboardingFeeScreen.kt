package com.wiom.csp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wiom.csp.data.OnboardingState
import com.wiom.csp.data.Scenario
import com.wiom.csp.ui.components.*
import com.wiom.csp.ui.theme.*
import com.wiom.csp.ui.viewmodel.*
import com.wiom.csp.util.t
import kotlinx.coroutines.delay

// ═══════════════════════════════════════════════════════════════════════════
// Screen 12: Onboarding Fee ₹20,000 (now AFTER Technical Assessment)
// ═══════════════════════════════════════════════════════════════════════════

@Composable
fun OnboardingFeeScreen(viewModel: PaymentViewModel, onNext: () -> Unit, onBack: (() -> Unit)? = null) {
    val scenario = OnboardingState.activeScenario
    val context = LocalContext.current
    var showSuccess by remember { mutableStateOf(false) }

    // Auto-progress after success
    if (showSuccess) {
        LaunchedEffect(Unit) {
            delay(3000)
            showSuccess = false
            onNext()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize().background(WiomSurface)) {
            AppHeader(
                title = t("एक्टिवेशन", "Activation"),
                onBack = onBack,
                rightText = t("स्टेप 3/5", "Step 3/5"),
            )

            when {
                showSuccess -> {
                    // ─── Payment Success ───
                    Column(
                        modifier = Modifier.weight(1f).fillMaxWidth().background(WiomSurface).padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                    ) {
                        Text("\uD83C\uDF89", fontSize = 64.sp)
                        Spacer(Modifier.height(16.dp))
                        Text(
                            t("पेमेंट सक्सेसफ़ुल!", "Payment Successful!"),
                            fontSize = 24.sp, fontWeight = FontWeight.Bold, color = WiomPositive,
                        )
                        Text("₹20,000", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = WiomText)
                        Spacer(Modifier.height(4.dp))
                        Text(
                            t("ऑनबोर्डिंग फ़ीस मिल गई", "Onboarding Fee received"),
                            fontSize = 14.sp, color = WiomTextSec,
                        )
                        Spacer(Modifier.height(32.dp))
                        Text(
                            t("अगले चरण पर ले जा रहे हैं...", "Moving to next step..."),
                            fontSize = 13.sp, color = WiomTextSec,
                        )
                        Spacer(Modifier.height(12.dp))
                        CircularProgressIndicator(color = WiomPositive, modifier = Modifier.size(24.dp), strokeWidth = 2.dp)
                    }
                }

                scenario == Scenario.ONBOARDFEE_FAILED -> {
                    // ─── Payment Failed ───
                    Column(
                        modifier = Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Spacer(Modifier.height(16.dp))
                        Text("\uD83D\uDE1F", fontSize = 40.sp)
                        Spacer(Modifier.height(16.dp))
                        Text(
                            t("पेमेंट नहीं हो पाया", "Payment failed"),
                            fontSize = 20.sp, fontWeight = FontWeight.Bold, color = WiomNegative,
                            textAlign = TextAlign.Center,
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(
                            t("चिंता न करें — आपका पैसा सुरक्षित है", "Don't worry — your money is safe"),
                            fontSize = 14.sp, color = WiomTextSec, textAlign = TextAlign.Center,
                        )
                        Spacer(Modifier.height(16.dp))
                        WiomCard(borderColor = WiomPositive300, backgroundColor = WiomPositive100) {
                            Text(
                                "\uD83D\uDEE1\uFE0F ${t("पैसा कटा नहीं है", "No money was deducted")}",
                                fontWeight = FontWeight.Bold, fontSize = 12.sp, color = WiomPositive,
                            )
                            Spacer(Modifier.height(4.dp))
                            Text(
                                t("बैंक या UPI ऐप से कनेक्शन में दिक़्क़त हुई — आपकी कोई गलती नहीं है", "There was a connection issue with the bank or UPI app — this is not your fault"),
                                fontSize = 14.sp, color = WiomText, lineHeight = 20.sp,
                            )
                        }
                    }
                    BottomBar {
                        WiomButton(t("दोबारा पेमेंट करें", "Retry Payment"), onClick = { OnboardingState.clearScenario() })
                        Spacer(Modifier.height(8.dp))
                        WiomButton(t("हमसे बात करें", "Talk to Us"), onClick = { val intent = android.content.Intent(android.content.Intent.ACTION_DIAL, android.net.Uri.parse("tel:7836811111")); context.startActivity(intent) }, isSecondary = true)
                    }
                }

                scenario == Scenario.ONBOARDFEE_TIMEOUT -> {
                    // ─── Payment Timeout ───
                    Column(
                        modifier = Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Spacer(Modifier.height(16.dp))
                        Text("⏳", fontSize = 40.sp)
                        Spacer(Modifier.height(16.dp))
                        Text(
                            t("आपका पेमेंट अभी बाकी है", "Payment is pending"),
                            fontSize = 20.sp, fontWeight = FontWeight.Bold, color = WiomWarning700,
                            textAlign = TextAlign.Center,
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(
                            t("बैंक से जवाब देर से आ रहा है। कभी-कभी 2-5 मिनट लग सकते हैं।", "Bank response is delayed. Sometimes it can take 2-5 minutes."),
                            fontSize = 14.sp, color = WiomTextSec, textAlign = TextAlign.Center, lineHeight = 20.sp,
                        )
                        Spacer(Modifier.height(16.dp))
                        WiomCard(borderColor = WiomBorderInput, backgroundColor = Color.White) {
                            Text(t("ट्रांज़ैक्शन डिटेल्स", "Transaction Details"), fontSize = 14.sp, fontWeight = FontWeight.Bold, color = WiomText)
                            Spacer(Modifier.height(10.dp))
                            FeeRow(t("अमाउंट", "Amount"), "₹20,000")
                            HorizontalDivider(color = WiomBorder)
                            FeeRow("UPI Ref", "UPI83746251")
                        }
                    }
                    BottomBar {
                        WiomButton(t("Status Refresh करें", "Refresh Status"), onClick = { OnboardingState.clearScenario() }, backgroundColor = WiomInfo)
                        Spacer(Modifier.height(8.dp))
                        WiomButton(t("हमसे बात करें", "Talk to Us"), onClick = { val intent = android.content.Intent(android.content.Intent.ACTION_DIAL, android.net.Uri.parse("tel:7836811111")); context.startActivity(intent) }, isSecondary = true)
                    }
                }

                else -> {
                    // ─── Normal: Pay ₹20,000 ───
                    Column(
                        modifier = Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(16.dp)
                    ) {
                        Text(
                            "\uD83D\uDCB3 ${t("ऑनबोर्डिंग फ़ीस", "Onboarding Fee")}",
                            fontSize = 18.sp, fontWeight = FontWeight.Bold, color = WiomText,
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            t("यह अमाउंट आपके बिज़नेस लोकेशन पर WiFi इक्विपमेंट भेजने के लिए ज़रूरी है", "This amount is required to send WiFi devices to your business location"),
                            fontSize = 13.sp, color = WiomTextSec, lineHeight = 18.sp,
                        )
                        Spacer(Modifier.height(14.dp))
                        AmountBox("₹20,000", t("अभी पेमेंट करें", "Pay Now"))
                        Spacer(Modifier.height(12.dp))

                        // Investment Summary
                        WiomCard {
                            Text(t("इन्वेस्टमेंट समरी", "INVESTMENT SUMMARY"), fontSize = 12.sp, fontWeight = FontWeight.Bold, color = WiomTextSec, letterSpacing = 0.5.sp)
                            Spacer(Modifier.height(8.dp))
                            Row(Modifier.fillMaxWidth().padding(vertical = 6.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text(t("रजिस्ट्रेशन फ़ीस", "Reg Fee"), fontSize = 13.sp, color = WiomTextSec)
                                Text("₹2,000 ✓ ${t("पेमेंट हो गया", "Paid")}", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = WiomPositive)
                            }
                            HorizontalDivider(color = WiomBorder)
                            Row(Modifier.fillMaxWidth().padding(vertical = 6.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text(t("ऑनबोर्डिंग फ़ीस", "Onboarding Fee"), fontSize = 13.sp, color = WiomTextSec)
                                Text("₹20,000 ${t("बाकी", "Due")}", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = WiomPrimary)
                            }
                            HorizontalDivider(color = WiomBorder)
                            Row(Modifier.fillMaxWidth().padding(vertical = 8.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text(t("टोटल इन्वेस्टमेंट", "Total Investment"), fontSize = 14.sp, fontWeight = FontWeight.Bold, color = WiomText)
                                Text("₹22,000", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = WiomText)
                            }
                        }
                    }
                    BottomBar {
                        WiomButton(t("₹20,000 अभी पेमेंट करें", "Pay ₹20,000 Now"), onClick = { showSuccess = true })
                    }
                }
            }
        }
    }
}
