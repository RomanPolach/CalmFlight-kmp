package com.romanpolach.peacefulflight.kmp.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.romanpolach.peacefulflight.kmp.utils.GForceStatus
import com.romanpolach.peacefulflight.kmp.viewmodel.GForceUiState
import org.jetbrains.compose.resources.stringResource
import peacefulflight.composeapp.generated.resources.*

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
            if (!isCompact) {
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
                            text = "Current: ",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                        Text(
                            text = "${"%.2f".format(uiState.displayedGForce)} G",
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
                            text = "Min: ",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                        )
                        Text(
                            text = "${"%.2f".format(uiState.minReading)} G",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f),
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Max: ",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                        )
                        Text(
                            text = "${"%.2f".format(uiState.maxReading)} G",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
            }

            Box(
                modifier = Modifier
                    .height(if (isCompact) 150.dp else 300.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.Black.copy(alpha = 0.3f))
            ) {
                // Real-time Graph
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val width = size.width
                    val height = size.height
                    val graphBackground = Color(0xFF1A1C1E) // Match original app deep dark

                    // Map Y axis: 0.0G -> Height, 3.0G -> 0
                    val mapY = { g: Float ->
                        val clamped = g.coerceIn(0f, 3.0f)
                        height - (clamped / 3.0f * height)
                    }

                    // Safe Zone (0 to 2.5G)
                    drawRect(
                        color = graphBackground,
                        topLeft = Offset(0f, mapY(2.5f)),
                        size = Size(width, mapY(0f) - mapY(2.5f))
                    )

                    // Unsafe Zone (Above 2.5G)
                    drawRect(
                        color = Color(0xFFFF6B6B).copy(alpha = 0.15f),
                        topLeft = Offset(0f, mapY(3.0f)),
                        size = Size(width, mapY(2.5f) - mapY(3.0f))
                    )

                    // Normal Zone Grid (0.8 to 1.2G)
                    drawRect(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.White.copy(alpha = 0.05f),
                                Color.White.copy(alpha = 0.05f),
                                Color.Transparent
                            ),
                            startY = mapY(1.2f),
                            endY = mapY(0.8f)
                        )
                    )

                    // Reference Lines
                    drawLine(
                        color = Color(0xFFFF6B6B).copy(alpha = 0.6f),
                        start = Offset(0f, mapY(2.5f)),
                        end = Offset(width, mapY(2.5f)),
                        strokeWidth = 2.dp.toPx()
                    )

                    drawLine(
                        color = Color(0xFF00BDD6).copy(alpha = 0.3f),
                        start = Offset(0f, mapY(1.0f)),
                        end = Offset(width, mapY(1.0f)),
                        strokeWidth = 1.dp.toPx()
                    )

                    // History Plot
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
                            color = Color(0xFF00BDD6), // Teal
                            style = Stroke(width = 2.dp.toPx())
                        )
                    }
                }

                Text(
                    text = stringResource(Res.string.safe_operating_zone),
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f),
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Status text
            Text(
                text = statusText,
                style = MaterialTheme.typography.headlineSmall,
                color = statusColor,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Message
            Text(
                text = if (uiState.status == GForceStatus.SMOOTH)
                    stringResource(Res.string.perfectly_safe)
                else
                    "Movement is normal and safe.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}

// Extension to format floats in multiplatform
private fun Float.format(digits: Int): String {
    val multiplier = kotlin.math.pow(10.0, digits.toDouble())
    return (kotlin.math.round(this * multiplier) / multiplier).toString()
}
