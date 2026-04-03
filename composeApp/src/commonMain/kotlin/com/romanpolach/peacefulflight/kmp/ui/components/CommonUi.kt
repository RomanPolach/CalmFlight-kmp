package com.romanpolach.peacefulflight.kmp.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.roundToInt

@Composable
fun ImageWithTitle(
    painter: androidx.compose.ui.graphics.painter.Painter,
    title: String,
    modifier: Modifier = Modifier,
    titleColor: Color = MaterialTheme.colorScheme.onSurface,
    containerColor: Color = MaterialTheme.colorScheme.surfaceContainer
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = containerColor),
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            // Image with 16:9 aspect ratio
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f)
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
            ) {
                Image(
                    painter = painter,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            // Title text
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = titleColor,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

/**
 * Standard top app bar used across screens
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StandardTopBar(
    title: String,
    onBackClick: (() -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {},
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = {
            ScreenTitle(
                text = title,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Start,
                modifier = Modifier.fillMaxWidth()
            )
        },
        navigationIcon = {
            if (onBackClick != null) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        },
        actions = actions,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        modifier = modifier
    )
}

/**
 * Screen title text
 */
@Composable
fun ScreenTitle(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary,
    textAlign: TextAlign = TextAlign.Center
) {
    Text(
        text = text,
        style = MaterialTheme.typography.headlineMedium,
        color = color,
        fontWeight = FontWeight.Bold,
        textAlign = textAlign,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        modifier = modifier
    )
}

/**
 * Content card for displaying text content
 */
@Composable
fun ContentCard(
    text: String,
    modifier: Modifier = Modifier
) {
    val containsHtml = remember(text) {
        text.contains("<b>") || text.contains("<i>") || text.contains("<br>") || text.contains("</")
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surfaceContainer.copy(alpha = 0.5f))
            .padding(12.dp)
    ) {
        Text(
            text = if (containsHtml) htmlToAnnotatedString(text) else AnnotatedString(text),
            style = MaterialTheme.typography.bodyLarge.copy(
                fontSize = 20.sp,
                lineHeight = 30.sp
            ),
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Start
        )
    }
}

@Composable
private fun htmlToAnnotatedString(html: String): AnnotatedString {
    val headingColor = MaterialTheme.colorScheme.primary

    return remember(html, headingColor) {
        buildAnnotatedString {
            var index = 0
            var boldDepth = 0
            var italicDepth = 0

            while (index < html.length) {
                if (html[index] == '<') {
                    val tagEnd = html.indexOf('>', startIndex = index + 1)
                    if (tagEnd == -1) {
                        appendStyledSegment(
                            text = html.substring(index),
                            isBold = boldDepth > 0,
                            isItalic = italicDepth > 0,
                            headingColor = headingColor
                        )
                        break
                    }

                    val tag = html.substring(index + 1, tagEnd).trim().lowercase()
                    when (tag) {
                        "b", "strong" -> boldDepth++
                        "/b", "/strong" -> boldDepth = (boldDepth - 1).coerceAtLeast(0)
                        "i", "em" -> italicDepth++
                        "/i", "/em" -> italicDepth = (italicDepth - 1).coerceAtLeast(0)
                        "br", "br/" -> append("\n")
                        "p", "p dir=\"ltr\"" -> Unit
                        "/p" -> {
                            val currentText = toAnnotatedString().text
                            if (currentText.isNotEmpty() && !currentText.endsWith("\n")) {
                                append("\n\n")
                            }
                        }
                    }

                    index = tagEnd + 1
                    continue
                }

                val nextTag = html.indexOf('<', startIndex = index).let { if (it == -1) html.length else it }
                appendStyledSegment(
                    text = html.substring(index, nextTag),
                    isBold = boldDepth > 0,
                    isItalic = italicDepth > 0,
                    headingColor = headingColor
                )
                index = nextTag
            }
        }
    }
}

private fun AnnotatedString.Builder.appendStyledSegment(
    text: String,
    isBold: Boolean,
    isItalic: Boolean,
    headingColor: Color
) {
    if (text.isEmpty()) return

    val start = length
    append(text)
    val end = length

    if (!isBold && !isItalic) return

    addStyle(
        style = SpanStyle(
            fontWeight = if (isBold) FontWeight.Bold else null,
            fontStyle = if (isItalic) FontStyle.Italic else null,
            color = if (isBold) headingColor else Color.Unspecified
        ),
        start = start,
        end = end
    )
}

/**
 * Primary action button
 */
@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        ),
        modifier = modifier,
        shape = RoundedCornerShape(12.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
    }
}

/**
 * Secondary action button
 */
@Composable
fun SecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f),
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        modifier = modifier,
        shape = RoundedCornerShape(12.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
    }
}

/**
 * Anxiety level rating bar with slider
 */
@Composable
fun AnxietyRatingBar(
    rating: Float,
    onRatingChanged: (Float) -> Unit,
    onSubmitRating: () -> Unit,
    feedbackMessage: String? = null,
    modifier: Modifier = Modifier
) {
    val isEnabled = remember { mutableStateOf(true) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Anxiety Level: ${rating.roundToInt()}/10",
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        Slider(
            enabled = isEnabled.value,
            value = rating,
            onValueChange = onRatingChanged,
            valueRange = 1f..10f,
            steps = 8,
            colors = SliderDefaults.colors(
                thumbColor = MaterialTheme.colorScheme.primary,
                activeTrackColor = MaterialTheme.colorScheme.primary,
                inactiveTrackColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
            )
        )

        // Feedback Area
        if (feedbackMessage != null) {
            Text(
                text = feedbackMessage,
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            PrimaryButton(
                text = "Rate",
                onClick = {
                    isEnabled.value = false
                    onSubmitRating()
                },
                modifier = Modifier.weight(1f)
            )
        }
    }
}
