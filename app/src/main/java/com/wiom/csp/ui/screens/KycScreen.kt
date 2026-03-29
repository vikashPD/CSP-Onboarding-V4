package com.wiom.csp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wiom.csp.data.OnboardingState
import com.wiom.csp.ui.components.*
import com.wiom.csp.ui.theme.*
import com.wiom.csp.util.t
import com.wiom.csp.ui.viewmodel.*

// Screen 4: KYC Documents (Sub-stages: PAN → Aadhaar → GST — matches HTML prototype)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KycScreen(viewModel: KycViewModel, onNext: () -> Unit, onBack: () -> Unit) {
    // Sub-stage: 0=PAN, 1=Aadhaar, 2=GST
    // Use OnboardingState directly (observable mutableStateOf)
    var kycSub by OnboardingState::kycSubStage
    var panNumber by OnboardingState::panNumber
    var aadhaarNumber by OnboardingState::aadhaarNumber
    var gstNumber by OnboardingState::gstNumber

    val panValid = Regex("^[A-Z]{5}[0-9]{4}[A-Z]$").matches(panNumber)
    val aadhaarClean = aadhaarNumber.replace(" ", "")
    val aadhaarValid = Regex("^[0-9]{12}$").matches(aadhaarClean)
    val gstValid = gstNumber.length == 15 && Regex("^[0-9]{2}[A-Z]{5}[0-9]{4}[A-Z][0-9A-Z]{3}$").matches(gstNumber)

    var sheetTarget by remember { mutableStateOf<String?>(null) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    // Sample document bottom sheet
    var sampleDocType by remember { mutableStateOf<String?>(null) }
    val sampleSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    fun doUpload(target: String) {
        when (target) {
            "pan" -> OnboardingState.panUploaded = true
            "aadhaar_front" -> OnboardingState.aadhaarFrontUploaded = true
            "aadhaar_back" -> OnboardingState.aadhaarBackUploaded = true
            "gst" -> OnboardingState.gstUploaded = true
        }
        sheetTarget = null
    }

    if (sheetTarget != null) {
        val label = when (sheetTarget) {
            "pan" -> t("पैन कार्ड", "PAN Card")
            "aadhaar_front" -> t("आधार — सामने", "Aadhaar — Front")
            "aadhaar_back" -> t("आधार — पीछे", "Aadhaar — Back")
            "gst" -> t("जीएसटी सर्टिफिकेट", "GST Certificate")
            else -> ""
        }
        ModalBottomSheet(
            onDismissRequest = { sheetTarget = null },
            sheetState = sheetState,
            containerColor = Color.White,
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        ) {
            Column(Modifier.fillMaxWidth().padding(horizontal = 20.dp).padding(bottom = 32.dp)) {
                Text(t("$label अपलोड करें", "Upload $label"), fontSize = 18.sp, fontWeight = FontWeight.Bold, color = WiomText)
                Spacer(Modifier.height(16.dp))
                Row(Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp)).background(WiomBgSec).clickable { doUpload(sheetTarget!!) }.padding(16.dp), horizontalArrangement = Arrangement.spacedBy(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Box(Modifier.size(48.dp).clip(RoundedCornerShape(12.dp)).background(WiomPrimaryLight), contentAlignment = Alignment.Center) { Text("\uD83D\uDCF7", fontSize = 24.sp) }
                    Column { Text(t("कैमरा से फ़ोटो लें", "Take Photo"), fontWeight = FontWeight.Bold, fontSize = 15.sp, color = WiomText); Text(t("सीधे कैमरा खोलें", "Open camera"), fontSize = 13.sp, color = WiomTextSec) }
                }
                Spacer(Modifier.height(10.dp))
                Row(Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp)).background(WiomBgSec).clickable { doUpload(sheetTarget!!) }.padding(16.dp), horizontalArrangement = Arrangement.spacedBy(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Box(Modifier.size(48.dp).clip(RoundedCornerShape(12.dp)).background(WiomInfo100), contentAlignment = Alignment.Center) { Text("\uD83D\uDDBC\uFE0F", fontSize = 24.sp) }
                    Column { Text(t("गैलरी से चुनें", "Choose from Gallery"), fontWeight = FontWeight.Bold, fontSize = 15.sp, color = WiomText); Text(t("फ़ोन में सेव फ़ोटो में से चुनें", "Pick from saved photos"), fontSize = 13.sp, color = WiomTextSec) }
                }
            }
        }
    }

    // Sample Document Bottom Sheet
    if (sampleDocType != null) {
        val sampleTitle = when (sampleDocType) {
            "pan" -> t("सैंपल पैन कार्ड", "Sample PAN Card")
            "aadhaar" -> t("सैंपल आधार कार्ड", "Sample Aadhaar Card")
            "gst" -> t("सैंपल जीएसटी सर्टिफिकेट", "Sample GST Certificate")
            else -> ""
        }
        val sampleRes = when (sampleDocType) {
            "pan" -> com.wiom.csp.R.drawable.sample_pan
            "aadhaar" -> com.wiom.csp.R.drawable.sample_aadhaar
            "gst" -> com.wiom.csp.R.drawable.sample_gst
            else -> 0
        }
        ModalBottomSheet(
            onDismissRequest = { sampleDocType = null },
            sheetState = sampleSheetState,
            containerColor = Color.White,
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        ) {
            Column(
                Modifier.fillMaxWidth().padding(horizontal = 20.dp).padding(bottom = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(sampleTitle, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = WiomText, textAlign = TextAlign.Center)
                Spacer(Modifier.height(12.dp))
                if (sampleRes != 0) {
                    androidx.compose.foundation.Image(
                        painter = androidx.compose.ui.res.painterResource(id = sampleRes),
                        contentDescription = sampleTitle,
                        modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(8.dp)).border(1.dp, WiomBorder, RoundedCornerShape(8.dp)),
                        contentScale = androidx.compose.ui.layout.ContentScale.FillWidth,
                    )
                }
                Spacer(Modifier.height(16.dp))
                WiomButton(t("समझ गया", "Got it"), onClick = { sampleDocType = null })
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize().background(WiomSurface)) {
        AppHeader(title = t("वेरिफिकेशन", "Verification"), onBack = { if (kycSub > 0) kycSub-- else onBack() }, rightText = t("स्टेप 1/5", "Step 1/5"))
        Column(modifier = Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(16.dp)) {
            // ─── Stepper ───
            Row(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp), verticalAlignment = Alignment.Top) {
                listOf(Triple("\uD83E\uDEAA", t("पैन", "PAN"), 0), Triple("\uD83D\uDCC4", t("आधार", "Aadhaar"), 1), Triple("\uD83D\uDCCB", "GST", 2)).forEachIndexed { i, (icon, label, _) ->
                    Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(modifier = Modifier.size(32.dp).clip(CircleShape).background(when { i < kycSub -> WiomPositive; i == kycSub -> WiomPrimary; else -> WiomBgSec }), contentAlignment = Alignment.Center) {
                            Text(if (i < kycSub) "✓" else icon, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = if (i <= kycSub) Color.White else WiomHint)
                        }
                        Spacer(Modifier.height(4.dp))
                        Text(label, fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = when { i < kycSub -> WiomPositive; i == kycSub -> WiomPrimary; else -> WiomHint })
                    }
                    if (i < 2) { Box(modifier = Modifier.weight(0.5f).padding(top = 15.dp).height(2.dp).background(if (i < kycSub) WiomPositive else WiomBorder)) }
                }
            }

            when (kycSub) {
                0 -> {
                    Text("\uD83E\uDEAA ${t("पैन विवरण", "PAN Details")}", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = WiomText)
                    Spacer(Modifier.height(4.dp))
                    Text(t("पैन नंबर डालें और पैन कार्ड अपलोड करें", "Enter PAN number and upload PAN card"), fontSize = 13.sp, color = WiomTextSec, lineHeight = 18.sp)
                    Spacer(Modifier.height(14.dp))
                    FieldLabel(t("पैन नंबर", "PAN Number"))
                    WiomTextField(value = panNumber, onValueChange = { panNumber = it.uppercase().filter { c -> c.isLetterOrDigit() }.take(10) }, placeholder = t("उदा. CEVPG6375L", "e.g. CEVPG6375L"), isVerified = panValid, keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Characters))
                    FieldLabel(t("पैन कार्ड अपलोड", "Upload PAN Card"))
                    KycUploadRow(emoji = "\uD83E\uDEAA", label = t("पैन कार्ड", "PAN Card"), isUploaded = OnboardingState.panUploaded, onClick = { sheetTarget = "pan" })
                    Spacer(Modifier.height(8.dp))
                    KycSampleDocLink { sampleDocType = "pan" }
                }
                1 -> {
                    Text("\uD83D\uDCC4 ${t("आधार विवरण", "Aadhaar Details")}", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = WiomText)
                    Spacer(Modifier.height(4.dp))
                    Text(t("आधार नंबर डालें और आधार कार्ड अपलोड करें", "Enter Aadhaar number and upload Aadhaar card"), fontSize = 13.sp, color = WiomTextSec, lineHeight = 18.sp)
                    Spacer(Modifier.height(14.dp))
                    FieldLabel(t("आधार नंबर", "Aadhaar Number"))
                    WiomTextField(value = aadhaarNumber, onValueChange = { val digits = it.replace(" ", "").filter { c -> c.isDigit() }.take(12); aadhaarNumber = digits.chunked(4).joinToString(" ") }, placeholder = t("उदा. 3696 8916 4553", "e.g. 3696 8916 4553"), isVerified = aadhaarValid, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
                    FieldLabel(t("आधार कार्ड अपलोड", "Upload Aadhaar Card"))
                    KycUploadRow(emoji = "\uD83D\uDCC4", label = t("आधार — सामने", "Aadhaar — Front"), isUploaded = OnboardingState.aadhaarFrontUploaded, onClick = { sheetTarget = "aadhaar_front" })
                    Spacer(Modifier.height(6.dp))
                    KycUploadRow(emoji = "\uD83D\uDCC4", label = t("आधार — पीछे", "Aadhaar — Back"), isUploaded = OnboardingState.aadhaarBackUploaded, onClick = { sheetTarget = "aadhaar_back" })
                    Spacer(Modifier.height(8.dp))
                    KycSampleDocLink { sampleDocType = "aadhaar" }
                }
                2 -> {
                    Text("\uD83D\uDCCB ${t("जीएसटी विवरण", "GST Details")}", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = WiomText)
                    Spacer(Modifier.height(4.dp))
                    Text(t("जीएसटी नंबर डालें और जीएसटी सर्टिफिकेट अपलोड करें", "Enter GST number and upload GST certificate"), fontSize = 13.sp, color = WiomTextSec, lineHeight = 18.sp)
                    Spacer(Modifier.height(14.dp))
                    FieldLabel(t("जीएसटी नंबर", "GST Number"))
                    WiomTextField(value = gstNumber, onValueChange = { gstNumber = it.uppercase().filter { c -> c.isLetterOrDigit() }.take(15) }, placeholder = t("उदा. 09${panNumber.ifEmpty { "CEVPG6375L" }}1Z4", "e.g. 09${panNumber.ifEmpty { "CEVPG6375L" }}1Z4"), isVerified = gstValid, keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Characters))
                    FieldLabel(t("जीएसटी सर्टिफिकेट अपलोड", "Upload GST Certificate"))
                    KycUploadRow(emoji = "\uD83D\uDCCB", label = t("जीएसटी सर्टिफिकेट", "GST Certificate"), isUploaded = OnboardingState.gstUploaded, onClick = { sheetTarget = "gst" })
                    Spacer(Modifier.height(8.dp))
                    KycSampleDocLink { sampleDocType = "gst" }
                }
            }
        }
        BottomBar {
            when (kycSub) {
                0 -> { val ok = panValid && OnboardingState.panUploaded; WiomButton(t("पैन विवरण भरें", "Submit PAN Details"), onClick = { if (ok) kycSub = 1 }, enabled = ok) }
                1 -> { val ok = aadhaarValid && OnboardingState.aadhaarFrontUploaded && OnboardingState.aadhaarBackUploaded; WiomButton(t("आधार विवरण भरें", "Submit Aadhaar Details"), onClick = { if (ok) kycSub = 2 }, enabled = ok) }
                2 -> { val ok = gstValid && OnboardingState.gstUploaded; WiomButton(t("अब बैंक का विवरण दें", "Now Add Bank Details"), onClick = { if (ok) { OnboardingState.kycSubStage = 0; onNext() } }, enabled = ok) }
            }
        }
    }
}

@Composable
private fun KycUploadRow(emoji: String, label: String, isUploaded: Boolean, onClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp))
            .background(if (isUploaded) WiomPositive100 else Color.White)
            .border(1.dp, if (isUploaded) WiomPositive300 else WiomBorderInput, RoundedCornerShape(12.dp))
            .clickable { onClick() }.padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically,
    ) {
        Column {
            Text("$emoji $label", fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = WiomText)
            Text(if (isUploaded) t("अपलोड हो गया ✓", "Uploaded ✓") else t("टैप करें", "Tap to Upload"), fontSize = 12.sp, color = if (isUploaded) WiomPositive else WiomHint)
        }
        Text(
            if (isUploaded) t("बदलें", "Update") else t("अपलोड", "Upload"),
            fontSize = 12.sp, fontWeight = FontWeight.Bold, color = if (isUploaded) WiomPositive else WiomPrimary,
            modifier = Modifier.clip(RoundedCornerShape(8.dp)).border(1.dp, if (isUploaded) WiomPositive else WiomPrimary, RoundedCornerShape(8.dp)).padding(horizontal = 10.dp, vertical = 6.dp),
        )
    }
}

@Composable
private fun KycSampleDocLink(onClick: () -> Unit) {
    Text(
        "\uD83D\uDCCB ${t("सैंपल दस्तावेज़ देखें", "View sample document")}",
        fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = WiomPrimary,
        textDecoration = TextDecoration.Underline,
        modifier = Modifier.clickable { onClick() },
    )
}
