package com.wiom.csp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wiom.csp.data.BankVerificationStatus
import com.wiom.csp.data.OnboardingState
import com.wiom.csp.ui.components.*
import com.wiom.csp.ui.theme.*
import com.wiom.csp.ui.viewmodel.*
import com.wiom.csp.util.t

// ═══════════════════════════════════════════════════════════════════════════
// Screen 6: Bank Details
// ═══════════════════════════════════════════════════════════════════════════

@Composable
fun BankDetailsScreen(viewModel: BankViewModel, onNext: () -> Unit, onBack: () -> Unit) {
    val state by viewModel.uiState.collectAsState()

    Column(modifier = Modifier.fillMaxSize().background(WiomSurface)) {
        AppHeader(
            title = t("वेरिफिकेशन", "Verification"),
            onBack = onBack,
            rightText = t("स्टेप 2/5", "Step 2/5"),
        )

        when (state.verificationStatus) {
            BankVerificationStatus.VERIFYING -> {
                // Spinner — full opaque background
                Column(
                    modifier = Modifier.weight(1f).fillMaxWidth().background(WiomSurface),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    Text("⏳", fontSize = 48.sp)
                    Spacer(Modifier.height(16.dp))
                    Text(t("बैंक डिटेल्स वेरिफ़ाई हो रही हैं...", "Verifying bank details..."), fontSize = 18.sp, fontWeight = FontWeight.Bold, color = WiomText)
                    Spacer(Modifier.height(8.dp))
                    Text(t("पेनी ड्रॉप वेरिफिकेशन जारी है", "Penny drop verification in progress"), fontSize = 13.sp, color = WiomTextSec)
                    Spacer(Modifier.height(24.dp))
                    CircularProgressIndicator(color = WiomPrimary, modifier = Modifier.size(32.dp), strokeWidth = 3.dp)
                }
            }

            BankVerificationStatus.SUCCESS -> {
                // Verified delight
                Column(
                    modifier = Modifier.weight(1f).padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    Text("✅", fontSize = 48.sp)
                    Spacer(Modifier.height(16.dp))
                    Text(t("बैंक डिटेल्स वेरिफ़ाई!", "Bank Details Verified!"), fontSize = 20.sp, fontWeight = FontWeight.Bold, color = WiomPositive)
                    Spacer(Modifier.height(8.dp))
                    Text(t("पेनी ड्रॉप सफल — नाम मैच कन्फ़र्म", "Penny drop successful — name match confirmed"), fontSize = 13.sp, color = WiomTextSec)
                    Spacer(Modifier.height(20.dp))
                    WiomCard(borderColor = WiomPositive, backgroundColor = WiomPositive100) {
                        FeeDetailRow(t("अकाउंट होल्डर", "Account Holder"), state.bankAccountHolderName ?: "Rajesh Kumar")
                        HorizontalDivider(color = WiomPositive200)
                        FeeDetailRow(t("बैंक", "Bank"), state.bankName ?: "State Bank of India")
                        HorizontalDivider(color = WiomPositive200)
                        FeeDetailRow(t("स्टेटस", "Status"), "✓ ${t("वेरिफ़ाइड", "Verified")}", valueColor = WiomPositive)
                    }
                }
                BottomBar {
                    WiomButton(t("ISP एग्रीमेंट जोड़ें", "Add ISP Agreement"), onClick = onNext)
                }
            }

            else -> {
                // Default entry form
                Column(
                    modifier = Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(16.dp)
                ) {
                    Text("\uD83C\uDFE6 ${t("बैंक डिटेल्स", "Bank Details")}", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = WiomText)
                    Spacer(Modifier.height(4.dp))
                    Text(t("अपने बैंक अकाउंट की जानकारी डालें", "Enter your bank account details"), fontSize = 13.sp, color = WiomTextSec, lineHeight = 18.sp)
                    Spacer(Modifier.height(8.dp))

                    // Ownership hint
                    Surface(shape = RoundedCornerShape(8.dp), color = WiomBgSec, modifier = Modifier.fillMaxWidth().border(1.dp, WiomBorder, RoundedCornerShape(8.dp))) {
                        Text(
                            "ℹ️ ${t("बैंक डिटेल्स", "Bank details should belong to")} ${state.personalName.ifEmpty { "Rajesh Kumar" }} ${t("या", "or")} ${state.tradeName.ifEmpty { "Kumar Electronics" }} ${t("के नाम पर होने चाहिए", "")}",
                            modifier = Modifier.padding(8.dp, 10.dp), fontSize = 12.sp, color = WiomTextSec, lineHeight = 16.sp,
                        )
                    }
                    Spacer(Modifier.height(14.dp))

                    // Account Number
                    FieldLabel(t("बैंक अकाउंट नंबर", "Bank Account Number"))
                    WiomTextField(
                        value = state.accountNumber,
                        onValueChange = { viewModel.onAccountNumberChanged(it) },
                        placeholder = t("बैंक अकाउंट नंबर डालें", "Enter bank account number"),
                        isVerified = state.accountNumber.length >= 9,
                        isError = state.accountNumberError != null,
                        errorMessage = state.accountNumberError,
                        visualTransformation = if (state.isAccountBlurred && state.accountNumber.length >= 9) PasswordVisualTransformation() else VisualTransformation.None,
                        onFocusChanged = { if (!it) viewModel.onAccountNumberBlurred() },
                    )

                    // Re-enter Account Number
                    FieldLabel(t("बैंक अकाउंट नंबर दोबारा डालें", "Re-enter Bank Account Number"))
                    WiomTextField(
                        value = state.accountNumberConfirm,
                        onValueChange = { viewModel.onAccountNumberConfirmChanged(it) },
                        placeholder = t("बैंक अकाउंट नंबर दोबारा डालें", "Re-enter bank account number"),
                        isVerified = state.accountNumberConfirm.isNotEmpty() && state.accountNumberConfirm == state.accountNumber,
                        isError = state.accountNumberConfirmError != null,
                        errorMessage = state.accountNumberConfirmError,
                        onFocusChanged = { if (!it) viewModel.onAccountNumberConfirmBlurred() },
                    )

                    // IFSC Code
                    FieldLabel(t("IFSC कोड", "IFSC Code"))
                    WiomTextField(
                        value = state.ifsc,
                        onValueChange = { viewModel.onIfscChanged(it) },
                        placeholder = t("उदा. SBIN0001234", "e.g. SBIN0001234"),
                        isVerified = state.ifsc.length == 11 && state.ifscError == null,
                        isError = state.ifscError != null,
                        errorMessage = state.ifscError,
                        onFocusChanged = { if (!it) viewModel.onIfscBlurred() },
                    )
                }
                BottomBar {
                    WiomButton(
                        t("बैंक डिटेल्स वेरिफ़ाई करें", "Verify Bank Details"),
                        onClick = { viewModel.verifyBankDetails(onNext) },
                        enabled = state.isFormValid,
                    )
                }
            }
        }

        // Bottom sheet overlays for failure states
        if (state.verificationStatus == BankVerificationStatus.PENNY_DROP_FAIL) {
            BankFailBottomSheet(
                title = t("बैंक अकाउंट वेरिफिकेशन फ़ेल", "Bank Account Verification Failed"),
                subtitle = t("हम आपके बैंक डिटेल्स वेरिफ़ाई नहीं कर पाए", "We could not verify your bank details"),
                icon = "✗",
                iconColor = WiomNegative,
                iconBg = WiomNegative100,
                onChangeBankDetails = { viewModel.onChangeBankDetails() },
                onUploadDoc = { viewModel.onUploadSupportingDoc("pennydrop") },
                showUploadOption = true,
            )
        }

        if (state.verificationStatus == BankVerificationStatus.NAME_MISMATCH) {
            BankMismatchBottomSheet(
                bankName = state.bankAccountHolderName ?: "Rajesh Kumar Sharma",
                personalName = state.personalName.ifEmpty { "Rajesh Kumar" },
                tradeName = state.tradeName.ifEmpty { "Kumar Electronics" },
                onChangeBankDetails = { viewModel.onChangeBankDetails() },
                onUploadDoc = { viewModel.onUploadSupportingDoc("mismatch") },
            )
        }

        if (state.verificationStatus == BankVerificationStatus.DEDUP) {
            BankFailBottomSheet(
                title = t("बैंक अकाउंट पहले से जुड़ा है", "Bank Account Already Linked"),
                subtitle = t("यह बैंक अकाउंट नंबर एक और Wiom अकाउंट से जुड़ा है जिसका मोबाइल नंबर ****${state.duplicateAccountPhone ?: "4567"} है", "This bank account number is linked with another Wiom account number ending with ${state.duplicateAccountPhone ?: "4567"}"),
                icon = "\uD83D\uDD0D",
                iconColor = WiomNegative,
                iconBg = WiomNegative100,
                onChangeBankDetails = { viewModel.onChangeBankDetails() },
                onUploadDoc = null,
                showUploadOption = false,
            )
        }
    }
}

@Composable
private fun BankFailBottomSheet(
    title: String, subtitle: String, icon: String,
    iconColor: Color, iconBg: Color,
    onChangeBankDetails: () -> Unit,
    onUploadDoc: (() -> Unit)?,
    showUploadOption: Boolean,
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.Black.copy(alpha = 0.5f),
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
            Surface(
                shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
                color = Color.White,
            ) {
                Column(modifier = Modifier.padding(28.dp, 28.dp, 28.dp, 24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    // Drag handle
                    Box(Modifier.width(40.dp).height(4.dp).clip(RoundedCornerShape(2.dp)).background(WiomBorderInput))
                    Spacer(Modifier.height(20.dp))
                    // Icon circle
                    Box(
                        modifier = Modifier.size(56.dp).clip(CircleShape).background(iconBg),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(icon, fontSize = 28.sp, color = iconColor)
                    }
                    Spacer(Modifier.height(14.dp))
                    Text(title, fontSize = 17.sp, fontWeight = FontWeight.Bold, color = WiomText, textAlign = TextAlign.Center)
                    Spacer(Modifier.height(8.dp))
                    Text(subtitle, fontSize = 13.sp, color = WiomTextSec, textAlign = TextAlign.Center, lineHeight = 18.sp)
                    Spacer(Modifier.height(24.dp))
                    WiomButton(t("बैंक डिटेल्स बदलें", "Change Bank Details"), onClick = onChangeBankDetails)
                    if (showUploadOption && onUploadDoc != null) {
                        Spacer(Modifier.height(10.dp))
                        WiomButton(t("बैंक दस्तावेज़ अपलोड करें", "Upload Bank Document"), onClick = onUploadDoc, isSecondary = true)
                    }
                }
            }
        }
    }
}

@Composable
private fun BankMismatchBottomSheet(
    bankName: String, personalName: String, tradeName: String,
    onChangeBankDetails: () -> Unit, onUploadDoc: () -> Unit,
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.Black.copy(alpha = 0.5f),
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
            Surface(
                shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
                color = Color.White,
            ) {
                Column(modifier = Modifier.padding(28.dp, 28.dp, 28.dp, 24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(Modifier.width(40.dp).height(4.dp).clip(RoundedCornerShape(2.dp)).background(WiomBorderInput))
                    Spacer(Modifier.height(20.dp))
                    Box(modifier = Modifier.size(56.dp).clip(CircleShape).background(WiomWarning200), contentAlignment = Alignment.Center) {
                        Text("⚠", fontSize = 28.sp, color = WiomWarning700)
                    }
                    Spacer(Modifier.height(14.dp))
                    Text(t("बैंक अकाउंट का नाम मैच नहीं हो रहा", "Bank Account Name Mismatch"), fontSize = 17.sp, fontWeight = FontWeight.Bold, color = WiomText, textAlign = TextAlign.Center)
                    Spacer(Modifier.height(16.dp))
                    // Name comparison
                    Surface(shape = RoundedCornerShape(8.dp), color = WiomBgSec, modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(10.dp, 12.dp)) {
                            FeeDetailRow(t("बैंक में नाम", "Name in Bank"), bankName)
                            HorizontalDivider(color = WiomBorder)
                            FeeDetailRow(t("पर्सनल नाम", "Personal Name"), personalName)
                            HorizontalDivider(color = WiomBorder)
                            FeeDetailRow(t("बिज़नेस नाम", "Business Name"), tradeName)
                        }
                    }
                    Spacer(Modifier.height(20.dp))
                    WiomButton(t("बैंक डिटेल्स बदलें", "Change Bank Details"), onClick = onChangeBankDetails)
                    Spacer(Modifier.height(10.dp))
                    WiomButton(t("बैंक दस्तावेज़ अपलोड करें", "Upload Bank Document"), onClick = onUploadDoc, isSecondary = true)
                }
            }
        }
    }
}
