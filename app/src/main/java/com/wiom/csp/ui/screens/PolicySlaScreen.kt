package com.wiom.csp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wiom.csp.ui.components.*
import com.wiom.csp.ui.theme.*
import com.wiom.csp.util.t

// ═══════════════════════════════════════════════════════════════════════════
// Screen 10: Policy & SLA
// ═══════════════════════════════════════════════════════════════════════════

@Composable
fun PolicySlaScreen(onNext: () -> Unit, onBack: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize().background(WiomSurface)) {
        AppHeader(
            title = t("ज़रूरी शर्तें", "Important Terms"),
            onBack = onBack,
            rightText = t("स्टेप 1/5", "Step 1/5"),
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Subheading
            Text(
                "\uD83D\uDCCB ${t("Wiom नीति और सर्विस लेवल एग्रीमेंट", "Wiom's Policy and Service Level Agreement")}",
                fontSize = 18.sp, fontWeight = FontWeight.Bold, color = WiomText,
            )
            Spacer(Modifier.height(4.dp))
            Text(
                t("कृपया नीचे दी गई शर्तें ध्यान से पढ़ें", "Please read the terms below carefully"),
                fontSize = 13.sp, color = WiomTextSec, lineHeight = 18.sp,
            )
            Spacer(Modifier.height(14.dp))

            // Commission card
            WiomCard {
                Text(t("कमीशन", "COMMISSION"), fontSize = 12.sp, fontWeight = FontWeight.Bold, color = WiomTextSec, letterSpacing = 0.5.sp)
                Spacer(Modifier.height(8.dp))
                FeeRow(t("नया कनेक्शन", "New Connection"), "₹300", valueColor = WiomPositive)
                HorizontalDivider(color = WiomBorder)
                FeeRow(t("रीचार्ज", "Recharge"), "₹300", valueColor = WiomPositive)
            }
            Spacer(Modifier.height(8.dp))

            // Payout card
            WiomCard {
                Text(t("भुगतान", "PAYOUT"), fontSize = 12.sp, fontWeight = FontWeight.Bold, color = WiomTextSec, letterSpacing = 0.5.sp)
                Spacer(Modifier.height(8.dp))
                Text(
                    t("हर सोमवार, सुबह 10 बजे तक", "Every Monday by 10 AM"),
                    fontSize = 14.sp, color = WiomTextSec,
                )
            }
            Spacer(Modifier.height(8.dp))

            // Service Levels card
            WiomCard {
                Text(t("सर्विस लेवल बनाए रखें", "SERVICE LEVELS TO BE MAINTAINED"), fontSize = 12.sp, fontWeight = FontWeight.Bold, color = WiomTextSec, letterSpacing = 0.5.sp)
                Spacer(Modifier.height(8.dp))
                Text(
                    "• ${t("शिकायत: 4 घंटे", "Complaints: 4hr")}\n" +
                    "• ${t("अपटाइम: 95%+", "Uptime: 95%+")}\n" +
                    "• ${t("इक्विपमेंट देखभाल", "Equipment care")}\n" +
                    "• ${t("ब्रांड कंप्लायंस", "Brand compliance")}",
                    fontSize = 14.sp, color = WiomTextSec, lineHeight = 22.sp,
                )
            }
        }
        BottomBar {
            WiomButton(t("समझ गया, आगे बढ़ें", "Understood, proceed"), onClick = onNext)
        }
    }
}

// ═══════════════════════════════════════════════════════════════════════════
// Helper: Fee Row (used in Phase 3 screens)
// ═══════════════════════════════════════════════════════════════════════════

@Composable
internal fun FeeRow(label: String, amount: String, isBold: Boolean = false, valueColor: Color = WiomText) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(label, fontSize = 14.sp, color = if (isBold) WiomText else WiomTextSec, fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal)
        Text(amount, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = valueColor)
    }
}
