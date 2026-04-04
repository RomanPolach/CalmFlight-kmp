package com.romanpolach.peacefulflight.kmp.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.dp
import com.romanpolach.peacefulflight.kmp.ui.theme.TealSoft
import com.romanpolach.peacefulflight.kmp.utils.GForceStatus
import com.romanpolach.peacefulflight.kmp.viewmodel.GForceUiState
import org.jetbrains.compose.resources.stringResource
import peacefulflight.composeapp.generated.resources.Res
import peacefulflight.composeapp.generated.resources.g_force_monitor
import peacefulflight.composeapp.generated.resources.gforce_current_label
import peacefulflight.composeapp.generated.resources.gforce_explanation
import peacefulflight.composeapp.generated.resources.gforce_explanation_title
import peacefulflight.composeapp.generated.resources.gforce_max_label
import peacefulflight.composeapp.generated.resources.gforce_min_label
import peacefulflight.composeapp.generated.resources.gforce_safe_range
import peacefulflight.composeapp.generated.resources.gforce_unavailable_message
import peacefulflight.composeapp.generated.resources.gforce_unavailable_title
import peacefulflight.composeapp.generated.resources.perfectly_safe
import peacefulflight.composeapp.generated.resources.safe_operating_zone
import peacefulflight.composeapp.generated.resources.status_bumpy
import peacefulflight.composeapp.generated.resources.status_light_bumps
import peacefulflight.composeapp.generated.resources.status_moderate
import peacefulflight.composeapp.generated.resources.status_smooth

@Composable
fun GForceMonitorCard(
    uiState: GForceUiState,
    isCompact: Boolean = false,
    onClick: (() -> Unit)? = null
) {
    val statusText = when (uiState.status) {
        GForceStatus.SMOOTH -> stringResource(Res.string.status_smooth)
        GForceStatus.LIGHT_BUMPS -> stringResource(Res.string.status_light_bumps)
        GForceStatus.MODERATE -> stringResource(Res.string.status_moderate)
        GForceStatus.BUMPY -> stringResource(Res.string.status_bumpy)
    }

    val statusColor = when (uiState.status) {
        GForceStatus.SMOOTH, GForceStatus.LIGHT_BUMPS -> MaterialTheme.colorScheme.primary
        else -> MaterialTheme.colorScheme.error
    }

    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = onClick != null) { onClick?.invoke() }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            if (!isCompact && uiState.isSensorAvailable) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(Res.string.g_force_monitor),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = stringResource(Res.string.gforce_current_label) + ": ",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                        Text(
                            text = "${uiState.displayedGForce.formatGForce()} G",
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Min/Max readings row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = stringResource(Res.string.gforce_min_label) + ": ",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                        )
                        Text(
                            text = "${uiState.minReading.formatGForce()} G",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f),
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = stringResource(Res.string.gforce_max_label) + ": ",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                        )
                        Text(
                            text = "${uiState.maxReading.formatGForce()} G",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
            }

            if (!isCompact && !uiState.isSensorAvailable) {
                Text(
                    text = stringResource(Res.string.g_force_monitor),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )

                Spacer(modifier = Modifier.height(12.dp))
            }

            if (uiState.isSensorAvailable) {
                BoxWithConstraints(
                    modifier = Modifier
                        .height(if (isCompact) 150.dp else 300.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.Black.copy(alpha = 0.3f))
                ) {
                    val graphHeight = if (isCompact) 150.dp else 300.dp
                    val line25Top = graphHeight / 6
                    val label25Top = (line25Top - 18.dp).coerceAtLeast(6.dp)

                    val graphBackground = MaterialTheme.colorScheme.onSecondaryContainer

                    // Real-time Graph
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val width = size.width
                        val height = size.height

                        // Map Y axis: 0.0G -> Height, 3.0G -> 0
                        val mapY = { g: Float ->
                            val clamped = g.coerceIn(0f, 3.0f)
                            height - (clamped / 3.0f * height)
                        }

                        val unsafeZoneTop = mapY(3.0f)
                        val unsafeZoneBottom = mapY(2.5f)
                        drawRect(
                            color = Color(0xFFFF6B6B).copy(alpha = 0.15f),
                            topLeft = Offset(0f, unsafeZoneTop),
                            size = Size(width, unsafeZoneBottom - unsafeZoneTop)
                        )

                        val safeZoneTop = mapY(2.5f)
                        val safeZoneBottom = mapY(0f)
                        drawRect(
                            color = graphBackground,
                            topLeft = Offset(0f, safeZoneTop),
                            size = Size(width, safeZoneBottom - safeZoneTop)
                        )

                        drawRect(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    graphBackground.copy(alpha = 0.1f),
                                    graphBackground.copy(alpha = 0.3f),
                                    graphBackground.copy(alpha = 0.3f),
                                    graphBackground.copy(alpha = 0.1f)
                                ),
                                startY = mapY(1.2f),
                                endY = mapY(0.8f)
                            )
                        )

                        drawLine(
                            color = Color(0xFFFF6B6B).copy(alpha = 0.6f),
                            start = Offset(0f, mapY(2.5f)),
                            end = Offset(width, mapY(2.5f)),
                            strokeWidth = 3.dp.toPx()
                        )

                        drawLine(
                            color = TealSoft.copy(alpha = 0.5f),
                            start = Offset(0f, mapY(1.0f)),
                            end = Offset(width, mapY(1.0f)),
                            strokeWidth = 2.dp.toPx()
                        )

                        if (uiState.history.isNotEmpty()) {
                            val path = Path()
                            val stepX = width / 300f

                            uiState.history.forEachIndexed { index, g ->
                                val x = width - ((uiState.history.size - 1 - index) * stepX)
                                val y = mapY(g)

                                if (index == 0) {
                                    path.moveTo(x, y)
                                } else {
                                    path.lineTo(x, y)
                                }
                            }

                            drawPath(
                                path = path,
                                color = TealSoft,
                                style = Stroke(width = 3.dp.toPx())
                            )
                        }
                    }

                    if (!isCompact) {
                        Text(
                            text = "3.0G",
                            color = Color(0xFFC71313).copy(alpha = 0.8f),
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .align(Alignment.TopStart)
                                .padding(start = 10.dp, top = 8.dp)
                        )
                    }

                    Text(
                        text = "2.5G",
                        color = Color(0xFFC71313).copy(alpha = 0.6f),
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Clip,
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(start = 10.dp, top = label25Top)
                            .background(graphBackground.copy(alpha = 0.85f), RoundedCornerShape(4.dp))
                            .padding(horizontal = 4.dp, vertical = 1.dp)
                    )

                    Text(
                        text = stringResource(Res.string.safe_operating_zone),
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = statusText,
                    style = MaterialTheme.typography.headlineSmall,
                    color = statusColor,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = stringResource(Res.string.gforce_safe_range),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            } else {
                GForceUnavailableState(isCompact = isCompact)
            }
        }
    }
}

@Composable
private fun GForceUnavailableState(isCompact: Boolean) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(if (isCompact) 150.dp else 220.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Color.Black.copy(alpha = 0.3f))
            .padding(20.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = stringResource(Res.string.gforce_unavailable_title),
                style = if (isCompact) MaterialTheme.typography.titleMedium else MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Text(
                text = stringResource(Res.string.gforce_unavailable_message),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun GForceExplanationCard() {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer.copy(alpha = 0.7f)
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = stringResource(Res.string.gforce_explanation_title),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = stringResource(Res.string.gforce_explanation),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.9f),
                lineHeight = 24.sp
            )
        }
    }
}

// Extension to format floats in multiplatform
private fun Float.formatGForce(): String {
    val rounded = (kotlin.math.round(this * 100) / 100.0)
    return rounded.toString()
}
