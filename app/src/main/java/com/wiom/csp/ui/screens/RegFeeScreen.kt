package com.wiom.csp.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
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

// Screen 5: Registration Fee ₹2,000
@Composable
fun RegFeeScreen(viewModel: PaymentViewModel, onNext: () -> Unit, onBack: () -> Unit) {
    val scenario = OnboardingState.activeScenario
    val context = LocalContext.current

    Column(modifier = Modifier.fillMaxSize().background(WiomSurface)) {
        AppHeader(
            title = t("रजिस्ट्रेशन", "Registration"),
            onBack = onBack,
        )

        when (scenario) {
            Scenario.REGFEE_FAILED -> {
                // ─── REGFEE_FAILED error screen ───
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Spacer(Modifier.height(24.dp))
                    Text("\uD83D\uDE1F", fontSize = 40.sp)
                    Spacer(Modifier.height(16.dp))
                    Text(
                        t("पेमेंट नहीं हो पाया", "Payment could not be processed"),
                        fontSize = 20.sp, fontWeight = FontWeight.Bold, color = WiomNegative,
                        textAlign = TextAlign.Center,
                    )
                    Spacer(Modifier.height(16.dp))
                    WiomCard(borderColor = WiomPositive300, backgroundColor = WiomPositive100) {
                        Text(
                            "\u2705 ${t("पैसा कटा नहीं है", "No money deducted")}",
                            fontWeight = FontWeight.Bold, fontSize = 14.sp, color = WiomPositive,
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            t("चिंता न करें — आपके अकाउंट से कोई पैसा नहीं कटा है।", "Don't worry — no money has been deducted from your account."),
                            fontSize = 14.sp, color = WiomTextSec, lineHeight = 20.sp,
                        )
                    }
                    Spacer(Modifier.height(8.dp))
                    WiomCard {
                        Text(t("ट्रांज़ैक्शन विवरण", "TRANSACTION DETAILS"), fontSize = 12.sp, fontWeight = FontWeight.Bold, color = WiomTextSec, letterSpacing = 0.5.sp)
                        Spacer(Modifier.height(8.dp))
                        Row(Modifier.fillMaxWidth().padding(vertical = 2.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Amount", fontSize = 13.sp, color = WiomTextSec)
                            Text("\u20B92,000", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = WiomText)
                        }
                        Row(Modifier.fillMaxWidth().padding(vertical = 2.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Error", fontSize = 13.sp, color = WiomTextSec)
                            Text("BANK_GATEWAY_TIMEOUT", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = WiomNegative)
                        }
                        Row(Modifier.fillMaxWidth().padding(vertical = 2.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Time", fontSize = 13.sp, color = WiomTextSec)
                            Text("just now", fontSize = 13.sp, color = WiomText)
                        }
                    }
                    Spacer(Modifier.height(8.dp))
                    InfoBox("\uD83D\uDCA1", t("2-3 मिनट बाद दोबारा कोशिश करें", "Try again after 2-3 minutes"), type = InfoBoxType.WARNING)
                    Spacer(Modifier.height(16.dp))
                    WiomButton(t("दोबारा पेमेंट करें", "Retry Payment"), onClick = { OnboardingState.clearScenario() })
                    Spacer(Modifier.height(8.dp))
                    WiomButton(t("बाद में करें", "Pay Later"), onClick = { OnboardingState.clearScenario() }, isSecondary = true)
                }
            }
            Scenario.REGFEE_TIMEOUT -> {
                // ─── REGFEE_TIMEOUT error screen ───
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Spacer(Modifier.height(24.dp))
                    Text("\u23F3", fontSize = 40.sp)
                    Spacer(Modifier.height(16.dp))
                    Text(
                        t("पेमेंट pending है", "Payment is pending"),
                        fontSize = 20.sp, fontWeight = FontWeight.Bold, color = WiomWarning700,
                        textAlign = TextAlign.Center,
                    )
                    Spacer(Modifier.height(16.dp))
                    ErrorCard(
                        icon = "\u23F3",
                        titleHi = "Bank response में देरी",
                        titleEn = "Bank response delayed",
                        messageHi = "Bank से response आने में 2-5 मिनट लग सकते हैं। कृपया थोड़ा इंतज़ार करें।",
                        messageEn = "Bank response may take 2-5 minutes. Please wait.",
                        type = "warning",
                    )
                    Spacer(Modifier.height(8.dp))
                    WiomCard {
                        Text(t("ट्रांज़ैक्शन विवरण", "TRANSACTION DETAILS"), fontSize = 12.sp, fontWeight = FontWeight.Bold, color = WiomTextSec, letterSpacing = 0.5.sp)
                        Spacer(Modifier.height(8.dp))
                        Row(Modifier.fillMaxWidth().padding(vertical = 2.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Amount", fontSize = 13.sp, color = WiomTextSec)
                            Text("\u20B92,000", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = WiomText)
                        }
                        Row(Modifier.fillMaxWidth().padding(vertical = 2.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("UPI Ref", fontSize = 13.sp, color = WiomTextSec)
                            Text("UPI123456789", fontSize = 13.sp, color = WiomText)
                        }
                        Row(Modifier.fillMaxWidth().padding(vertical = 2.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Status", fontSize = 13.sp, color = WiomTextSec)
                            Text("\u23F3 Pending", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = WiomWarning700)
                        }
                    }
                    Spacer(Modifier.height(8.dp))
                    InfoBox("\uD83D\uDD12", t("48 घंटे में auto-refund अगर fail हो", "Auto-refund within 48hrs if failed"))
                    Spacer(Modifier.height(16.dp))
                    WiomButton(t("Status Refresh करें", "Refresh Status"), onClick = { OnboardingState.clearScenario() })
                    Spacer(Modifier.height(8.dp))
                    WiomButton(t("हमसे बात करें", "Talk to us"), onClick = {
                        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:7836811111"))
                        context.startActivity(intent)
                    }, isSecondary = true)
                }
            }
            else -> {
                // ─── Normal happy path ───
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp)
                ) {
                    Text(
                        t("रजिस्ट्रेशन फ़ीस", "Registration Fee"),
                        fontSize = 14.sp, color = WiomTextSec,
                    )
                    Spacer(Modifier.height(12.dp))
                    AmountBox("\u20B92,000", t("रजिस्ट्रेशन फ़ीस", "Registration Fee"))
                    Spacer(Modifier.height(12.dp))
                    WiomCard {
                        Text(
                            "\u2139\uFE0F ${t("जरूरी जानकारी", "Important Information")}",
                            fontWeight = FontWeight.Bold, fontSize = 14.sp, color = WiomText,
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(
                            t(
                                "1. रजिस्ट्रेशन फ़ीस पेमेंट के बाद डॉक्यूमेंट वेरिफिकेशन प्रक्रिया शुरू होगी।",
                                "1. Document Verification process will start after Registration fee payment."
                            ),
                            fontSize = 13.sp, color = WiomText, lineHeight = 20.sp,
                        )
                        Spacer(Modifier.height(6.dp))
                        Text(
                            t(
                                "2. कृपया पेमेंट के 3 दिनों के भीतर सभी ज़रूरी दस्तावेज़ जमा करें ताकि आपका अर्ज़ी एक्टिव रहे।",
                                "2. Please submit your documents within 3 days to keep your application active."
                            ),
                            fontSize = 13.sp, color = WiomText, lineHeight = 20.sp,
                        )
                        Spacer(Modifier.height(12.dp))
                        TrustBadge("\uD83D\uDD12", t("अर्ज़ी रिजेक्ट होने पर पूरा रिफंड", "Full Refund if application rejected"))
                    }
                }
                BottomBar {
                    WiomButton(t("₹2,000 अभी पेमेंट करें", "Pay ₹2,000 Now"), onClick = onNext)
                }
            }
        }
    }
}
