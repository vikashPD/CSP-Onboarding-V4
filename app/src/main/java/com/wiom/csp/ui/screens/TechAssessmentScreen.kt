package com.wiom.csp.ui.screens

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
import com.wiom.csp.ui.components.*
import com.wiom.csp.ui.theme.*
import com.wiom.csp.ui.viewmodel.*
import com.wiom.csp.util.t

// ═══════════════════════════════════════════════════════════════════════════
// Screen 11: Technical Assessment (now BEFORE Onboarding Fee)
// ═══════════════════════════════════════════════════════════════════════════

@Composable
fun TechAssessmentScreen(viewModel: TechAssessmentViewModel, onNext: () -> Unit) {
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    Column(modifier = Modifier.fillMaxSize().background(WiomSurface)) {
        AppHeader(
            title = t("टेक्निकल असेसमेंट", "Technical Assessment"),
            rightText = t("स्टेप 2/5", "Step 2/5"),
        )

        if (state.status == TechAssessmentStatus.REJECTED) {
            // ─── Rejected state ───
            Column(
                modifier = Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Spacer(Modifier.height(16.dp))
                Text("\uD83D\uDE14", fontSize = 40.sp)
                Spacer(Modifier.height(16.dp))
                Text(
                    t("प्रोफ़ाइल अभी अप्रूव नहीं हुई", "Profile not accepted yet"),
                    fontSize = 20.sp, fontWeight = FontWeight.Bold, color = WiomNegative,
                    textAlign = TextAlign.Center,
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    t("टेक्निकल असेसमेंट पास नहीं हो पाया", "Technical assessment could not be passed"),
                    fontSize = 14.sp, color = WiomTextSec, textAlign = TextAlign.Center, lineHeight = 20.sp,
                )
                Spacer(Modifier.height(16.dp))
                // Reason card
                WiomCard(borderColor = WiomNegative, backgroundColor = WiomNegative100) {
                    Text(t("कारण", "Reason"), fontSize = 12.sp, fontWeight = FontWeight.Bold, color = WiomNegative)
                    Spacer(Modifier.height(4.dp))
                    Text(
                        state.rejectionReason.ifEmpty { t("इंफ्रास्ट्रक्चर तैयार नहीं", "Infrastructure not ready") },
                        fontSize = 14.sp, color = WiomText,
                    )
                }
                Spacer(Modifier.height(8.dp))
                // No refund notice
                WiomCard(borderColor = WiomWarning, backgroundColor = WiomWarning200) {
                    Text(
                        t("अभी कोई रिफंड नहीं मिलेगा", "No refund will be done at this moment"),
                        fontSize = 13.sp, color = WiomText, lineHeight = 18.sp,
                    )
                }
            }
            BottomBar {
                WiomButton(t("हमसे बात करें", "Talk to Us"), onClick = {
                    val intent = android.content.Intent(android.content.Intent.ACTION_DIAL, android.net.Uri.parse("tel:7836811111"))
                    context.startActivity(intent)
                })
            }
        } else {
            // ─── Pending/In progress state ───
            Column(
                modifier = Modifier.weight(1f).verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Spacer(Modifier.height(16.dp))
                    Text("\uD83D\uDD27", fontSize = 40.sp)
                    Spacer(Modifier.height(16.dp))
                    Text(
                        t("टेक्निकल असेसमेंट जारी है", "Technical Assessment in progress"),
                        fontSize = 20.sp, fontWeight = FontWeight.Bold, color = WiomText,
                        textAlign = TextAlign.Center,
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        t("हमारी तकनीकी टीम जांच कर रही है", "Our technical team is reviewing"),
                        fontSize = 14.sp, color = WiomTextSec, textAlign = TextAlign.Center,
                    )
                }
                Spacer(Modifier.height(16.dp))
                // Checklist
                ChecklistItem(t("इंफ्रास्ट्रक्चर", "Infrastructure Review"), isWaiting = true)
                ChecklistItem(t("नेटवर्क", "Network Readiness"), isWaiting = true)
                ChecklistItem(t("स्थान", "Location Feasibility"), isWaiting = true, isLast = true)

                Column(modifier = Modifier.padding(16.dp)) {
                    InfoBox("⏳", t("4-5 वर्किंग डेज़", "May take 4-5 business days"), type = InfoBoxType.WARNING)
                    Spacer(Modifier.height(8.dp))
                    InfoBox("\uD83D\uDCDE", t("अगले चरणों के लिए हमारी नेटवर्क क्वालिटी टीम आपको कॉल भी करेगी", "You will also receive a call from our Network Quality team for next steps"))
                }
            }
            BottomBar {
                WiomButton(
                    "✓ ${t("असेसमेंट पास", "Assessment Passed")}",
                    onClick = { viewModel.setApproved(); onNext() },
                    backgroundColor = WiomPositive,
                )
                Spacer(Modifier.height(8.dp))
                WiomButton(
                    "✗ ${t("असेसमेंट रिजेक्ट", "Assessment Rejected")}",
                    onClick = { viewModel.setRejected() },
                    isSecondary = true,
                    textColor = WiomNegative,
                    backgroundColor = WiomNegative100,
                )
            }
        }
    }
}
