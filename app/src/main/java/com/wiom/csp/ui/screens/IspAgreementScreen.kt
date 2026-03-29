package com.wiom.csp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wiom.csp.ui.components.*
import com.wiom.csp.ui.theme.*
import com.wiom.csp.ui.viewmodel.*
import com.wiom.csp.util.t

// ═══════════════════════════════════════════════════════════════════════════
// Screen 7: ISP Agreement Upload
// ═══════════════════════════════════════════════════════════════════════════

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun IspAgreementScreen(viewModel: IspAgreementViewModel, onNext: () -> Unit, onBack: () -> Unit) {
    val state by viewModel.uiState.collectAsState()
    var showSampleDoc by remember { mutableStateOf(false) }
    var showUploadSheet by remember { mutableStateOf(false) }
    val sampleSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val uploadSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    // Upload Bottom Sheet (matching HTML: PDF, Camera, Gallery + Pro Tips)
    if (showUploadSheet) {
        ModalBottomSheet(
            onDismissRequest = { showUploadSheet = false },
            sheetState = uploadSheetState,
            containerColor = Color.White,
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        ) {
            Column(Modifier.fillMaxWidth().padding(horizontal = 20.dp).padding(bottom = 24.dp)) {
                Text(
                    t("ISP एग्रीमेंट अपलोड करें", "Upload ISP Agreement"),
                    fontSize = 16.sp, fontWeight = FontWeight.Bold, color = WiomText,
                    textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth(),
                )
                Spacer(Modifier.height(16.dp))
                // PDF option
                Row(
                    Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp))
                        .border(1.dp, WiomBorder, RoundedCornerShape(12.dp))
                        .clickable { showUploadSheet = false; viewModel.uploadPdf() }
                        .padding(14.dp, 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text("\uD83D\uDCCE", fontSize = 24.sp)
                    Column {
                        Text(t("PDF अपलोड करें", "Upload PDF"), fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = WiomText)
                        Text(t("PDF फ़ाइल चुनें", "Choose a PDF file"), fontSize = 12.sp, color = WiomTextSec)
                    }
                }
                Spacer(Modifier.height(8.dp))
                // Camera option
                Row(
                    Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp))
                        .border(1.dp, WiomBorder, RoundedCornerShape(12.dp))
                        .clickable { showUploadSheet = false; viewModel.uploadPdf() }
                        .padding(14.dp, 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text("\uD83D\uDCF8", fontSize = 24.sp)
                    Column {
                        Text(t("कैमरा से फ़ोटो लें", "Take Photo from Camera"), fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = WiomText)
                        Text(t("अधिकतम 7 पेज", "Up to 7 pages"), fontSize = 12.sp, color = WiomTextSec)
                    }
                }
                Spacer(Modifier.height(8.dp))
                // Gallery option
                Row(
                    Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp))
                        .border(1.dp, WiomBorder, RoundedCornerShape(12.dp))
                        .clickable { showUploadSheet = false; viewModel.uploadPdf() }
                        .padding(14.dp, 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text("\uD83D\uDDBC\uFE0F", fontSize = 24.sp)
                    Column {
                        Text(t("गैलरी से अपलोड करें", "Upload from Gallery"), fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = WiomText)
                        Text(t("अधिकतम 7 पेज", "Up to 7 pages"), fontSize = 12.sp, color = WiomTextSec)
                    }
                }
                Spacer(Modifier.height(12.dp))
                // Pro Tips
                Surface(shape = RoundedCornerShape(8.dp), color = WiomBgSec, modifier = Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(10.dp, 12.dp)) {
                        Text("\uD83D\uDCA1 ${t("प्रो टिप्स", "Pro Tips")}", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = WiomText)
                        Spacer(Modifier.height(6.dp))
                        listOf(
                            t("दस्तावेज़ साफ़ दिखना चाहिए", "Document should be clearly visible"),
                            t("सभी बिज़नेस डिटेल्स मैच होनी चाहिए", "All business details should match"),
                            t("दस्तावेज़ की एक्सपायर नहीं होना चाहिए", "Document should not be expired"),
                        ).forEach {
                            Row(Modifier.padding(bottom = 4.dp)) {
                                Text("✓ ", fontSize = 11.sp, color = WiomPositive)
                                Text(it, fontSize = 11.sp, color = WiomTextSec, lineHeight = 16.sp)
                            }
                        }
                    }
                }
                Spacer(Modifier.height(8.dp))
                // Cancel
                Text(
                    t("रद्द करें", "Cancel"),
                    fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = WiomTextSec,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth().clickable { showUploadSheet = false }.padding(10.dp),
                )
            }
        }
    }

    // Sample Document Bottom Sheet
    if (showSampleDoc) {
        ModalBottomSheet(
            onDismissRequest = { showSampleDoc = false },
            sheetState = sampleSheetState,
            containerColor = Color.White,
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        ) {
            Column(
                Modifier.fillMaxWidth().padding(horizontal = 20.dp).padding(bottom = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(t("सैंपल ISP एग्रीमेंट", "Sample ISP Agreement"), fontSize = 15.sp, fontWeight = FontWeight.Bold, color = WiomText, textAlign = TextAlign.Center)
                Spacer(Modifier.height(12.dp))
                androidx.compose.foundation.Image(
                    painter = androidx.compose.ui.res.painterResource(id = com.wiom.csp.R.drawable.sample_isp),
                    contentDescription = "Sample ISP Agreement",
                    modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(8.dp)).border(1.dp, WiomBorder, RoundedCornerShape(8.dp)),
                    contentScale = androidx.compose.ui.layout.ContentScale.FillWidth,
                )
                Spacer(Modifier.height(16.dp))
                WiomButton(t("समझ गया", "Got it"), onClick = { showSampleDoc = false })
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize().background(WiomSurface)) {
        AppHeader(
            title = t("वेरिफिकेशन", "Verification"),
            onBack = onBack,
            rightText = t("स्टेप 3/5", "Step 3/5"),
        )
        Column(
            modifier = Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(16.dp)
        ) {
            Text("\uD83D\uDCC4 ${t("ISP एग्रीमेंट अपलोड करें", "Upload ISP Agreement")}", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = WiomText)
            Spacer(Modifier.height(4.dp))
            Text(
                t("दूरसंचार विभाग (DOT) अनुपालन जांच के लिए हमें आपके इंटरनेट सर्विस प्रोवाइडर के साथ आपके कानूनी अनुबंध की ज़रूरीता है", "We need your legal agreement with your Internet Service Provider for Department of Telecommunication compliance check"),
                fontSize = 13.sp, color = WiomTextSec, lineHeight = 18.sp,
            )
            Spacer(Modifier.height(14.dp))

            // Upload row
            Phase2UploadRow(
                icon = "\uD83D\uDCC4",
                label = t("ISP एग्रीमेंट", "ISP Agreement"),
                isUploaded = state.isUploaded,
                onUpload = { if (state.isUploaded) viewModel.resetUpload() else showUploadSheet = true },
            )
            Spacer(Modifier.height(8.dp))

            // View sample document link
            Text(
                "\uD83D\uDCCB ${t("सैंपल दस्तावेज़ देखें", "View sample document")}",
                fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = WiomPrimary,
                textDecoration = androidx.compose.ui.text.style.TextDecoration.Underline,
                modifier = Modifier.clickable { showSampleDoc = true },
            )
            Spacer(Modifier.height(12.dp))

            // Mandatory details
            Surface(shape = RoundedCornerShape(8.dp), color = WiomBgSec, modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(12.dp, 14.dp)) {
                    Text(t("ISP एग्रीमेंट में ज़रूरी डिटेल्स:", "Mandatory details required in ISP Agreement:"), fontSize = 12.sp, fontWeight = FontWeight.Bold, color = WiomText)
                    Spacer(Modifier.height(6.dp))
                    listOf(
                        t("ISP कंपनी का नाम", "ISP Company Name"),
                        t("LCO / पार्टनर का नाम", "LCO / Partner Name"),
                        t("एग्रीमेंट की तारीख़", "Agreement Date"),
                        t("एग्रीमेंट वैलिड होना चाहिए (एक्सपायर नहीं)", "Agreement should be Valid (Not Expired)"),
                        t("लाइसेंस नंबर", "License Number"),
                        t("संपर्क / साइनकर्ता का नाम", "Contact / Signatory Names"),
                        t("पार्टनर और ISP की मुहर और साइन", "Partner and ISP stamp and signature"),
                    ).forEach {
                        Text("• $it", fontSize = 12.sp, color = WiomText, lineHeight = 20.sp)
                    }
                }
            }
        }
        BottomBar {
            WiomButton(
                if (state.isUploaded) t("आगे बढ़ें", "Proceed") else t("ISP एग्रीमेंट अपलोड करें", "Upload ISP Agreement"),
                onClick = if (state.isUploaded) onNext else { {} },
                enabled = state.isUploaded,
            )
        }
    }
}
