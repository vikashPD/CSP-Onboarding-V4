package com.wiom.csp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
// Screen 13: Account Setup (auto-progress, no CTA)
// ═══════════════════════════════════════════════════════════════════════════

@Composable
fun AccountSetupScreen(viewModel: AccountSetupViewModel, onNext: () -> Unit) {
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    // Auto-start setup on first composition
    LaunchedEffect(Unit) {
        viewModel.startSetup(onNext)
    }

    Column(modifier = Modifier.fillMaxSize().background(WiomSurface)) {
        AppHeader(
            title = t("एक्टिवेशन", "Activation"),
            rightText = t("स्टेप 4/5", "Step 4/5"),
        )

        when (state.state) {
            AccountSetupState.LOADING -> {
                // Loading spinner — full opaque background
                Column(
                    modifier = Modifier.weight(1f).fillMaxWidth().background(WiomSurface),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    Text("⚙\uFE0F", fontSize = 48.sp)
                    Spacer(Modifier.height(16.dp))
                    Text(
                        t("अकाउंट सेटअप जारी है", "Account Setup in Progress"),
                        fontSize = 18.sp, fontWeight = FontWeight.Bold, color = WiomText,
                        textAlign = TextAlign.Center,
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        "${state.businessName} ${t("के लिए अकाउंट तैयार किया जा रहा है", "")}",
                        fontSize = 14.sp, color = WiomTextSec,
                        textAlign = TextAlign.Center,
                    )
                    Spacer(Modifier.height(24.dp))
                    CircularProgressIndicator(color = WiomPrimary, modifier = Modifier.size(32.dp), strokeWidth = 3.dp)
                }
            }

            AccountSetupState.FAILED -> {
                Column(
                    modifier = Modifier.weight(1f).padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    Text("❌", fontSize = 48.sp)
                    Spacer(Modifier.height(16.dp))
                    Text(
                        t("अकाउंट सेटअप फ़ेल", "Account Setup Failed"),
                        fontSize = 20.sp, fontWeight = FontWeight.Bold, color = WiomNegative,
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        t("टेक्निकल प्रॉब्लम के कारण अकाउंट सेटअप नहीं हो पाया", "Account setup failed due to a technical issue"),
                        fontSize = 14.sp, color = WiomTextSec, textAlign = TextAlign.Center, lineHeight = 20.sp,
                    )
                }
                BottomBar {
                    WiomButton(t("दोबारा कोशिश करें", "Retry"), onClick = { viewModel.retry(onNext) })
                    Spacer(Modifier.height(8.dp))
                    WiomButton(t("हमसे बात करें", "Talk to Us"), onClick = { val intent = android.content.Intent(android.content.Intent.ACTION_DIAL, android.net.Uri.parse("tel:7836811111")); context.startActivity(intent) }, isSecondary = true)
                }
            }

            AccountSetupState.PENDING -> {
                Column(
                    modifier = Modifier.weight(1f).padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    Text("⏳", fontSize = 48.sp)
                    Spacer(Modifier.height(16.dp))
                    Text(
                        t("अकाउंट सेटअप अभी बाकी है", "Account Setup Pending"),
                        fontSize = 20.sp, fontWeight = FontWeight.Bold, color = WiomWarning,
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        t("अकाउंट सेटअप अभी प्रोसेस हो रहा है", "Account setup is being processed"),
                        fontSize = 14.sp, color = WiomTextSec, textAlign = TextAlign.Center, lineHeight = 20.sp,
                    )
                }
                BottomBar {
                    WiomButton(t("स्टेटस रिफ्रेश करें", "Refresh Status"), onClick = { viewModel.refreshStatus(onNext) }, backgroundColor = WiomInfo)
                    Spacer(Modifier.height(8.dp))
                    WiomButton(t("हमसे बात करें", "Talk to Us"), onClick = { val intent = android.content.Intent(android.content.Intent.ACTION_DIAL, android.net.Uri.parse("tel:7836811111")); context.startActivity(intent) }, isSecondary = true)
                }
            }

            AccountSetupState.COMPLETED -> {
                // Brief success before auto-navigate
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    Text("✅", fontSize = 48.sp)
                    Spacer(Modifier.height(16.dp))
                    Text(
                        t("अकाउंट सेटअप पूरा!", "Account Setup Complete!"),
                        fontSize = 20.sp, fontWeight = FontWeight.Bold, color = WiomPositive,
                    )
                }
            }
        }
    }
}
