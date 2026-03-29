package com.wiom.csp.ui.screens

import androidx.compose.foundation.background
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
import com.wiom.csp.ui.components.*
import com.wiom.csp.ui.theme.*
import com.wiom.csp.ui.viewmodel.*
import com.wiom.csp.util.t

// ═══════════════════════════════════════════════════════════════════════════
// Screen 9: Verification Status
// ═══════════════════════════════════════════════════════════════════════════

@Composable
fun VerificationScreen(viewModel: VerificationViewModel, onNext: () -> Unit) {
    val state by viewModel.uiState.collectAsState()
    val rejected = OnboardingState.verificationRejected

    Column(modifier = Modifier.fillMaxSize().background(WiomSurface)) {
        AppHeader(
            title = t("वेरिफिकेशन", "Verification"),
            rightText = t("स्टेप 5/5", "Step 5/5"),
        )

        if (rejected || state.status == VerificationState.REJECTED) {
            // ─── Rejected: auto refund, no re-upload ───
            Column(
                modifier = Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Spacer(Modifier.height(16.dp))
                Text("\uD83D\uDE14", fontSize = 40.sp)
                Spacer(Modifier.height(16.dp))
                Text(t("वेरिफिकेशन रिजेक्ट", "Verification Rejected"), fontSize = 20.sp, fontWeight = FontWeight.Bold, color = WiomNegative, textAlign = TextAlign.Center)
                Spacer(Modifier.height(8.dp))
                Text(t("चिंता न करें — आपका पैसा सुरक्षित है", "Don't worry — your money is safe"), fontSize = 14.sp, color = WiomTextSec, textAlign = TextAlign.Center)
                Spacer(Modifier.height(20.dp))
                WiomCard(borderColor = WiomPositive, backgroundColor = WiomPositive100) {
                    Text("\uD83D\uDD12 ${t("रिफंड शुरू हो गया", "Refund initiated")}", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = WiomPositive)
                    Spacer(Modifier.height(4.dp))
                    Text(t("₹2,000 पूरा रिफंड 5-6 वर्किंग डेज़ों में आपके खाते में आ जाएगा", "₹2,000 full refund will be credited to your account in 5-6 working days"), fontSize = 14.sp, color = WiomText, lineHeight = 20.sp)
                }
            }
            BottomBar {
                WiomButton(t("रिफंड स्टेटस देखें", "Check Refund Status"), onClick = { viewModel.checkRefundStatus() })
            }
        } else {
            // ─── Pending / All documents submitted ───
            Column(
                modifier = Modifier.weight(1f).verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Column(modifier = Modifier.fillMaxWidth().padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Spacer(Modifier.height(16.dp))
                    Text("✅", fontSize = 40.sp)
                    Spacer(Modifier.height(16.dp))
                    Text(t("सभी डॉक्यूमेंट्स सबमिट हो गए", "All Documents Submitted"), fontSize = 20.sp, fontWeight = FontWeight.Bold, color = WiomText, textAlign = TextAlign.Center)
                    Spacer(Modifier.height(8.dp))
                    Text(t("वेरिफिकेशन जारी है — सत्यापन टीम आपके दस्तावेज़ों की जांच कर रही है", "Verification in progress — Verification team is reviewing your documents"), fontSize = 14.sp, color = WiomTextSec, textAlign = TextAlign.Center, lineHeight = 20.sp)
                }
                Spacer(Modifier.height(16.dp))
                ChecklistItem(t("KYC दस्तावेज़", "KYC Documents"), isDone = true)
                ChecklistItem(t("बैंक डिटेल्स", "Bank Details"), isDone = true)
                ChecklistItem(t("ISP एग्रीमेंट", "ISP Agreement"), isDone = true)
                ChecklistItem(t("दुकान और इक्विपमेंट फ़ोटो", "Shop & Equipment Photos"), isDone = true)
                ChecklistItem(t("वेरिफिकेशन रिव्यू", "Verification Review"), subtitle = t("रिव्यू जारी...", "Under review..."), isWaiting = true, isLast = true)

                Column(modifier = Modifier.padding(16.dp)) {
                    InfoBox("⏳", t("रिव्यू में 3 वर्किंग डेज़ लग सकते हैं", "Review may take 3 business days"), type = InfoBoxType.WARNING)
                }
            }
            BottomBar {
                WiomButton("✓ ${t("वेरिफिकेशन अप्रूव्ड", "Verification Approved")}", onClick = { viewModel.setApproved(); onNext() }, backgroundColor = WiomPositive)
                Spacer(Modifier.height(8.dp))
                WiomButton("✗ ${t("वेरिफिकेशन रिजेक्ट", "Verification Rejected")}", onClick = { viewModel.setRejected() }, isSecondary = true, textColor = WiomNegative, backgroundColor = WiomNegative100)
            }
        }
    }
}
