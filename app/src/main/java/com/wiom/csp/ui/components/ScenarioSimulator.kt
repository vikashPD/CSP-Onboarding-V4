package com.wiom.csp.ui.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wiom.csp.data.OnboardingState
import com.wiom.csp.data.Scenario
import com.wiom.csp.data.scenarioMeta
import com.wiom.csp.ui.theme.*
import com.wiom.csp.util.t

// ─── Scenario Simulator Panel ───────────────────────────────────
@Composable
fun ScenarioSimulatorPanel(currentScreen: Int) {
    val expanded = OnboardingState.simulatorExpanded
    val active = OnboardingState.activeScenario
    val activeMeta = scenarioMeta[active]
    val panelBg = Color(0xFF1A1025)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(panelBg)
            .animateContentSize(),
    ) {
        // Header row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { OnboardingState.simulatorExpanded = !expanded }
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text("\uD83E\uDDEA", fontSize = 14.sp)
                Text(
                    "Scenario Simulator",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                )
                if (active != Scenario.NONE && activeMeta != null) {
                    Text(
                        "${activeMeta.icon} ${t(activeMeta.labelHi, activeMeta.labelEn)}",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier
                            .clip(RoundedCornerShape(888.dp))
                            .background(WiomPrimary)
                            .padding(horizontal = 8.dp, vertical = 2.dp),
                    )
                }
            }
            Icon(
                if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                contentDescription = "Toggle",
                tint = Color.White,
                modifier = Modifier.size(20.dp),
            )
        }

        // Expanded body
        if (expanded) {
            val screenScenarios = OnboardingState.scenariosForScreen(currentScreen)
            val allScenarios = Scenario.entries.filter { it != Scenario.NONE }
            var showAll by remember { mutableStateOf(false) }
            val displayList = if (showAll) allScenarios else screenScenarios

            Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)) {
                // Toggle: this screen / all
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(bottom = 8.dp),
                ) {
                    Text(
                        t("\u0907\u0938 \u0938\u094D\u0915\u094D\u0930\u0940\u0928", "This Screen"),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (!showAll) WiomPrimary else WiomHint,
                        modifier = Modifier
                            .clip(RoundedCornerShape(888.dp))
                            .background(if (!showAll) WiomPrimaryLight else Color.Transparent)
                            .clickable { showAll = false }
                            .padding(horizontal = 10.dp, vertical = 4.dp),
                    )
                    Text(
                        t("\u0938\u092C \u0926\u0947\u0916\u0947\u0902", "Show All"),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (showAll) WiomPrimary else WiomHint,
                        modifier = Modifier
                            .clip(RoundedCornerShape(888.dp))
                            .background(if (showAll) WiomPrimaryLight else Color.Transparent)
                            .clickable { showAll = true }
                            .padding(horizontal = 10.dp, vertical = 4.dp),
                    )
                    Spacer(Modifier.weight(1f))
                    if (active != Scenario.NONE) {
                        Text(
                            "Clear",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = WiomNegative,
                            modifier = Modifier
                                .clip(RoundedCornerShape(888.dp))
                                .background(WiomNegative100)
                                .clickable { OnboardingState.clearScenario() }
                                .padding(horizontal = 10.dp, vertical = 4.dp),
                        )
                    }
                }

                if (displayList.isEmpty()) {
                    Text(
                        t("\u0907\u0938 \u0938\u094D\u0915\u094D\u0930\u0940\u0928 \u0915\u0947 \u0932\u093F\u090F \u0915\u094B\u0908 scenario \u0928\u0939\u0940\u0902", "No scenarios for this screen"),
                        fontSize = 12.sp,
                        color = WiomHint,
                        modifier = Modifier.padding(vertical = 8.dp),
                    )
                } else {
                    // Group by category
                    val grouped = displayList.groupBy { scenarioMeta[it]?.category ?: "" }
                    grouped.forEach { (category, scenarios) ->
                        Text(
                            category.uppercase(),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = WiomHint,
                            letterSpacing = 0.5.sp,
                            modifier = Modifier.padding(top = 4.dp, bottom = 4.dp),
                        )
                        // Buttons in a flow
                        @OptIn(ExperimentalLayoutApi::class)
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp),
                        ) {
                            scenarios.forEach { scenario ->
                                val meta = scenarioMeta[scenario]!!
                                val isActive = active == scenario
                                Text(
                                    "${meta.icon} ${t(meta.labelHi, meta.labelEn)}",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = if (isActive) Color.White else Color.White.copy(alpha = 0.8f),
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(
                                            if (isActive) WiomNegative else Color.White.copy(alpha = 0.1f)
                                        )
                                        .clickable { OnboardingState.triggerScenario(scenario) }
                                        .padding(horizontal = 10.dp, vertical = 6.dp),
                                )
                            }
                        }
                    }
                }
                Spacer(Modifier.height(8.dp))
            }
        }
    }
}
