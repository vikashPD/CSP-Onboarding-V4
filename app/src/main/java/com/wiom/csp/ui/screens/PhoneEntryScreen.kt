package com.wiom.csp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import android.content.Intent
import android.net.Uri
import com.wiom.csp.data.OnboardingState
import com.wiom.csp.data.Scenario
import com.wiom.csp.ui.components.*
import com.wiom.csp.ui.theme.*
import com.wiom.csp.util.Lang
import com.wiom.csp.util.t
import com.wiom.csp.ui.viewmodel.*

// Screen 0: Phone Entry
@Composable
fun PhoneEntryScreen(viewModel: PhoneViewModel, onNext: () -> Unit) {
    val scenario = OnboardingState.activeScenario
    var tncAccepted by remember { mutableStateOf(true) }
    val context = LocalContext.current

    Column(modifier = Modifier.fillMaxSize().background(WiomSurface)) {
        AppHeader(title = "Wiom Partner+")

        if (scenario == Scenario.PHONE_DUPLICATE) {
            // ─── PHONE_DUPLICATE error screen ───
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Spacer(Modifier.height(32.dp))
                Text("\uD83D\uDCF1", fontSize = 40.sp)
                Spacer(Modifier.height(16.dp))
                Text(
                    t("यह नंबर पहले से रजिस्टर्ड है", "This number is already registered"),
                    fontSize = 20.sp, fontWeight = FontWeight.Bold, color = WiomNegative,
                    textAlign = TextAlign.Center,
                )
                Spacer(Modifier.height(16.dp))
                ErrorCard(
                    icon = "ℹ️",
                    titleHi = "अकाउंट मौजूद है",
                    titleEn = "Account Exists",
                    messageHi = "इस नंबर से पहले से एक अकाउंट बना हुआ है। आप लॉगिन कर सकते हैं या नए नंबर से रजिस्टर कर सकते हैं।",
                    messageEn = "An account already exists with this number. You can login or register with a new number.",
                    type = "info",
                )
                Spacer(Modifier.height(12.dp))
                InfoBox("\uD83D\uDD12", t("आपका पुराना डेटा सुरक्षित है", "Your existing data is safe"))
                Spacer(Modifier.height(16.dp))
                WiomButton(t("नए नंबर से OTP भेजें", "Send OTP with new number"), onClick = { OnboardingState.clearScenario(); OnboardingState.phoneNumber = "" })
                Spacer(Modifier.height(8.dp))
                WiomButton(t("लॉगिन करें", "Login"), onClick = { OnboardingState.clearScenario() }, isSecondary = true)
            }
        } else {
            // ─── Normal happy path ───
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                Spacer(Modifier.height(24.dp))
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text("\uD83E\uDD1D", fontSize = 32.sp)
                    Spacer(Modifier.height(8.dp))
                    Text(
                        t("Wiom पार्टनर बनें", "Become a Wiom Partner"),
                        fontSize = 24.sp, fontWeight = FontWeight.Bold,
                        color = WiomText, textAlign = TextAlign.Center,
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        t("Wiom के साथ अपना बिज़नेस शुरू करें", "Start your business with Wiom"),
                        fontSize = 14.sp, color = WiomTextSec, textAlign = TextAlign.Center,
                    )
                }
                Spacer(Modifier.height(24.dp))
                FieldLabel(t("मोबाइल नंबर", "Mobile Number"))
                // Phone input with +91 prefix
                Row(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Box(
                        modifier = Modifier
                            .height(56.dp)
                            .clip(RoundedCornerShape(topStart = 12.dp, bottomStart = 12.dp))
                            .background(WiomBgSec)
                            .border(1.dp, WiomBorderInput, RoundedCornerShape(topStart = 12.dp, bottomStart = 12.dp))
                            .padding(horizontal = 12.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text("+91", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = WiomTextSec)
                    }
                    OutlinedTextField(
                        value = OnboardingState.phoneNumber,
                        onValueChange = { newValue ->
                            val filtered = newValue.filter { it.isDigit() }.take(10)
                            OnboardingState.phoneNumber = filtered
                        },
                        modifier = Modifier.weight(1f).height(56.dp),
                        shape = RoundedCornerShape(topEnd = 12.dp, bottomEnd = 12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedBorderColor = WiomBorderInput,
                            focusedBorderColor = WiomBorderFocus,
                            unfocusedContainerColor = Color.White,
                        ),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        placeholder = { Text(t("10 अंकों का नंबर", "10 digit number"), color = WiomHint) }
                    )
                }
                Spacer(Modifier.height(8.dp))
                InfoBox("\uD83D\uDD12", t("OTP डाले गये नंबर पर भेजा जाएगा", "OTP will be sent to your number"))
            }
            BottomBar {
                // ─── T&C Checkbox just above CTA (default checked) ───
                val tncTag = "TNC_LINK"
                val annotatedText = if (Lang.isHindi) {
                    buildAnnotatedString {
                        append("आगे बढ़कर, मैं ")
                        pushStringAnnotation(tag = tncTag, annotation = "https://wiom.in/terms")
                        withStyle(SpanStyle(color = WiomPrimary, textDecoration = TextDecoration.Underline, fontWeight = FontWeight.SemiBold)) {
                            append("नियम व शर्तें")
                        }
                        pop()
                        append(" स्वीकार करता/करती हूँ")
                    }
                } else {
                    buildAnnotatedString {
                        append("By continuing, I accept the ")
                        pushStringAnnotation(tag = tncTag, annotation = "https://wiom.in/terms")
                        withStyle(SpanStyle(color = WiomPrimary, textDecoration = TextDecoration.Underline, fontWeight = FontWeight.SemiBold)) {
                            append("Terms & Conditions")
                        }
                        pop()
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Checkbox(
                        checked = tncAccepted,
                        onCheckedChange = { tncAccepted = it },
                        colors = CheckboxDefaults.colors(
                            checkedColor = WiomPrimary,
                            uncheckedColor = WiomBorderInput,
                        ),
                        modifier = Modifier.size(18.dp),
                    )
                    Spacer(Modifier.width(6.dp))
                    ClickableText(
                        text = annotatedText,
                        style = TextStyle(fontSize = 11.sp, color = WiomTextSec, lineHeight = 14.sp),
                        onClick = { offset ->
                            annotatedText.getStringAnnotations(tag = tncTag, start = offset, end = offset)
                                .firstOrNull()?.let { annotation ->
                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(annotation.item))
                                    context.startActivity(intent)
                                }
                        },
                    )
                }
                WiomButton(
                    t("OTP भेजें", "Send OTP"),
                    onClick = onNext,
                    enabled = OnboardingState.phoneNumber.length == 10 && tncAccepted,
                )
            }
        }
    }
}
