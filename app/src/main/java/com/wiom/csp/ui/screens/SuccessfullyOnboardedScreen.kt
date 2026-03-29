package com.wiom.csp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.background
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.wiom.csp.util.t

// ═══════════════════════════════════════════════════════════════════════════
// Screen 14: Successfully Onboarded
// ═══════════════════════════════════════════════════════════════════════════

@Composable
fun SuccessfullyOnboardedScreen() {
    val context = LocalContext.current
    Column(modifier = Modifier.fillMaxSize().background(WiomSurface)) {
        AppHeader(
            title = t("एक्टिवेशन", "Activation"),
            rightText = t("स्टेप 5/5", "Step 5/5"),
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Celebration
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Spacer(Modifier.height(16.dp))
                Text("\uD83C\uDF89", fontSize = 48.sp)
                Spacer(Modifier.height(12.dp))
                Text(
                    t("बधाई हो, राजेश!", "Congratulations, Rajesh!"),
                    fontSize = 24.sp, fontWeight = FontWeight.Bold, color = WiomText,
                    textAlign = TextAlign.Center,
                )
                Spacer(Modifier.height(6.dp))
                Text(
                    t("अब आप व्योम के सर्विस प्रोवाइडर हैं", "You are now a Wiom Connection Service Provider"),
                    fontSize = 14.sp, color = WiomTextSec, textAlign = TextAlign.Center, lineHeight = 20.sp,
                )
            }

            Spacer(Modifier.height(20.dp))

            // Next Steps
            Text(
                t("अगले कदम", "Next Steps"),
                fontSize = 14.sp, fontWeight = FontWeight.Bold, color = WiomText,
            )
            Spacer(Modifier.height(8.dp))
            WiomCard(borderColor = WiomPrimary, backgroundColor = WiomPrimaryLight) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text("\uD83D\uDCF2", fontSize = 32.sp)
                    Spacer(Modifier.height(8.dp))
                    Text(
                        t("Wiom Partner Plus ऐप डाउनलोड करें", "Download Wiom Partner Plus App"),
                        fontSize = 16.sp, fontWeight = FontWeight.Bold, color = WiomText,
                        textAlign = TextAlign.Center,
                    )
                    Spacer(Modifier.height(10.dp))
                    OutlinedButton(
                        onClick = { val intent = android.content.Intent(android.content.Intent.ACTION_VIEW, android.net.Uri.parse("https://play.google.com/store")); context.startActivity(intent) },
                        border = ButtonDefaults.outlinedButtonBorder.copy(width = 2.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = WiomPrimary),
                    ) {
                        Text(
                            t("अभी इंस्टॉल करें", "Install Now"),
                            fontWeight = FontWeight.Bold,
                        )
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            // Important Instructions
            Text(
                t("ज़रूरी बातें", "Important Instructions"),
                fontSize = 14.sp, fontWeight = FontWeight.Bold, color = WiomText,
            )
            Spacer(Modifier.height(8.dp))
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                color = WiomBgSec,
            ) {
                Column(modifier = Modifier.padding(12.dp, 14.dp)) {
                    Text(
                        "1. ${t("Wiom Partner Plus ऐप में लॉगिन करें", "Login to Wiom Partner Plus app")}",
                        fontSize = 13.sp, color = WiomText, lineHeight = 22.sp,
                    )
                    Text(
                        "2. ${t("सभी ज़रूरी परमिशन दें", "Allow all required permissions")}",
                        fontSize = 13.sp, color = WiomText, lineHeight = 22.sp,
                    )
                    Text(
                        "3. ${t("ज़रूरी Wiom ट्रेनिंग पूरा करें", "Complete Mandatory Wiom Training")}",
                        fontSize = 13.sp, color = WiomText, lineHeight = 22.sp,
                    )
                }
            }
        }
    }
}
