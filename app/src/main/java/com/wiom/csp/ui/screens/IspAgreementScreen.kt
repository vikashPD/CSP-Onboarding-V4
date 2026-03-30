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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// ═══════════════════════════════════════════════════════════════════════════
// Screen 7: ISP Agreement Upload — Multi-page flow (up to 7 pages)
// ═══════════════════════════════════════════════════════════════════════════

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun IspAgreementScreen(viewModel: IspAgreementViewModel, onNext: () -> Unit, onBack: () -> Unit) {
    val state by viewModel.uiState.collectAsState()
    var showSampleDoc by remember { mutableStateOf(false) }
    var showUploadSheet by remember { mutableStateOf(false) }
    var showPageConfirmSheet by remember { mutableStateOf(false) }
    var uploadSource by remember { mutableStateOf("") }
    var isSimulatingUpload by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val sampleSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val uploadSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val pageConfirmSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    // ─── Multi-page confirm Bottom Sheet (after each page upload) ───
    if (showPageConfirmSheet && state.pageCount > 0) {
        ModalBottomSheet(
            onDismissRequest = { showPageConfirmSheet = false },
            sheetState = pageConfirmSheetState,
            containerColor = Color.White,
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        ) {
            Column(
                Modifier.fillMaxWidth().padding(horizontal = 20.dp).padding(bottom = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    "${t("पेज", "Page")} ${state.pageCount} ${t("अपलोड हो गया", "uploaded")} \u2714",
                    fontSize = 16.sp, fontWeight = FontWeight.Bold, color = WiomText,
                    textAlign = TextAlign.Center,
                )
                Spacer(Modifier.height(6.dp))
                Text(
                    "${t("क्या और पेज जोड़ने हैं?", "Want to add more pages?")} (${t("अधिकतम", "max")} 7)",
                    fontSize = 13.sp, color = WiomTextSec, textAlign = TextAlign.Center,
                )
                Spacer(Modifier.height(20.dp))

                // "Add more pages" button
                Button(
                    onClick = {
                        showPageConfirmSheet = false
                        scope.launch {
                            isSimulatingUpload = true
                            delay(1000)
                            viewModel.addPage()
                            isSimulatingUpload = false
                            if (state.pageCount + 1 >= 7) {
                                viewModel.finishUpload()
                            } else {
                                showPageConfirmSheet = true
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = WiomPrimary),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                ) {
                    Text(
                        "${t("और पेज जोड़ें", "Add More Pages")} (${state.pageCount}/7)",
                        fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color.White,
                    )
                }
                Spacer(Modifier.height(10.dp))

                // "Upload complete" button
                OutlinedButton(
                    onClick = {
                        showPageConfirmSheet = false
                        viewModel.finishUpload()
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = WiomPrimary),
                    border = androidx.compose.foundation.BorderStroke(1.5.dp, WiomPrimary),
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                ) {
                    Text(
                        t("अपलोड पूरा करें", "Finish Upload"),
                        fontSize = 15.sp, fontWeight = FontWeight.Bold, color = WiomPrimary,
                    )
                }
            }
        }
    }

    // ─── Upload source picker Bottom Sheet ───
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

                // PDF option — single file, no multi-page
                UploadSourceOption(
                    icon = "\uD83D\uDCCE",
                    title = t("PDF अपलोड करें", "Upload PDF"),
                    subtitle = t("PDF फ़ाइल चुनें", "Choose a PDF file"),
                    onClick = {
                        showUploadSheet = false
                        viewModel.uploadPdf()
                    },
                )
                Spacer(Modifier.height(8.dp))

                // Camera option — multi-page up to 7
                UploadSourceOption(
                    icon = "\uD83D\uDCF8",
                    title = t("कैमरा से फ़ोटो लें", "Take Photo from Camera"),
                    subtitle = t("अधिकतम 7 पेज", "Up to 7 pages"),
                    onClick = {
                        showUploadSheet = false
                        uploadSource = "camera"
                        viewModel.resetUpload()
                        scope.launch {
                            isSimulatingUpload = true
                            delay(1200)
                            viewModel.addPage()
                            isSimulatingUpload = false
                            showPageConfirmSheet = true
                        }
                    },
                )
                Spacer(Modifier.height(8.dp))

                // Gallery option — multi-page up to 7
                UploadSourceOption(
                    icon = "\uD83D\uDDBC\uFE0F",
                    title = t("गैलरी से अपलोड करें", "Upload from Gallery"),
                    subtitle = t("अधिकतम 7 पेज", "Up to 7 pages"),
                    onClick = {
                        showUploadSheet = false
                        uploadSource = "gallery"
                        viewModel.resetUpload()
                        scope.launch {
                            isSimulatingUpload = true
                            delay(1200)
                            viewModel.addPage()
                            isSimulatingUpload = false
                            showPageConfirmSheet = true
                        }
                    },
                )
                Spacer(Modifier.height(12.dp))

                // Pro Tips
                Surface(shape = RoundedCornerShape(8.dp), color = WiomBgSec, modifier = Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(10.dp, 12.dp)) {
                        Text("\uD83D\uDCA1 ${t("प्रो टिप्स", "Pro Tips")}", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = WiomText)
                        Spacer(Modifier.height(6.dp))
                        listOf(
                            t("डॉक्यूमेंट साफ़ दिखना चाहिए", "Document should be clearly visible"),
                            t("सभी बिज़नेस डिटेल्स मैच होनी चाहिए", "All business details should match"),
                            t("डॉक्यूमेंट एक्सपायर नहीं होना चाहिए", "Document should not be expired"),
                        ).forEach {
                            Row(Modifier.padding(bottom = 4.dp)) {
                                Text("✓ ", fontSize = 11.sp, color = WiomPositive)
                                Text(it, fontSize = 11.sp, color = WiomTextSec, lineHeight = 16.sp)
                            }
                        }
                    }
                }
                Spacer(Modifier.height(8.dp))
                Text(
                    t("रद्द करें", "Cancel"),
                    fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = WiomTextSec,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth().clickable { showUploadSheet = false }.padding(10.dp),
                )
            }
        }
    }

    // ─── Sample Document Bottom Sheet ───
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

    // ─── Uploading overlay ───
    if (isSimulatingUpload) {
        Box(
            Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.4f)),
            contentAlignment = Alignment.Center,
        ) {
            Surface(shape = RoundedCornerShape(16.dp), color = Color.White) {
                Column(
                    Modifier.padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    CircularProgressIndicator(color = WiomPrimary, strokeWidth = 3.dp)
                    Spacer(Modifier.height(12.dp))
                    Text(
                        t("पेज अपलोड हो रहा है...", "Uploading page..."),
                        fontSize = 14.sp, color = WiomText,
                    )
                }
            }
        }
    }

    // ─── Main Screen ───
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
                t("DOT कंप्लायंस चेक के लिए हमें आपके ISP के साथ आपके लीगल एग्रीमेंट की ज़रूरत है", "We need your legal agreement with your ISP for DOT compliance check"),
                fontSize = 13.sp, color = WiomTextSec, lineHeight = 18.sp,
            )
            Spacer(Modifier.height(14.dp))

            // Upload row — show page count if multi-page
            Phase2UploadRow(
                icon = "\uD83D\uDCC4",
                label = if (state.isUploaded && state.pageCount > 1) {
                    "${t("ISP एग्रीमेंट", "ISP Agreement")} (${state.pageCount} ${t("पेज", "pages")})"
                } else {
                    t("ISP एग्रीमेंट", "ISP Agreement")
                },
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
                    Text(t("ISP एग्रीमेंट में ज़रूरी डिटेल्स:", "Mandatory details in ISP Agreement:"), fontSize = 12.sp, fontWeight = FontWeight.Bold, color = WiomText)
                    Spacer(Modifier.height(6.dp))
                    listOf(
                        t("ISP कंपनी का नाम", "ISP Company Name"),
                        t("LCO / पार्टनर का नाम", "LCO / Partner Name"),
                        t("एग्रीमेंट की तारीख़", "Agreement Date"),
                        t("एग्रीमेंट वैलिड होना चाहिए (एक्सपायर नहीं)", "Agreement should be Valid (Not Expired)"),
                        t("लाइसेंस नंबर", "License Number"),
                        t("कॉन्टैक्ट / साइनेटरी का नाम", "Contact / Signatory Names"),
                        t("पार्टनर और ISP की मोहर और साइन", "Partner and ISP stamp and signature"),
                    ).forEach {
                        Text("• $it", fontSize = 12.sp, color = WiomText, lineHeight = 20.sp)
                    }
                }
            }
        }
        BottomBar {
            WiomButton(
                if (state.isUploaded) t("आगे बढ़ें", "Proceed") else t("ISP एग्रीमेंट अपलोड करें", "Upload ISP Agreement"),
                onClick = if (state.isUploaded) onNext else { { showUploadSheet = true } },
                enabled = state.isUploaded,
            )
        }
    }
}

@Composable
private fun UploadSourceOption(icon: String, title: String, subtitle: String, onClick: () -> Unit) {
    Row(
        Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp))
            .border(1.dp, WiomBorder, RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(14.dp, 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(icon, fontSize = 24.sp)
        Column {
            Text(title, fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = WiomText)
            Text(subtitle, fontSize = 12.sp, color = WiomTextSec)
        }
    }
}
