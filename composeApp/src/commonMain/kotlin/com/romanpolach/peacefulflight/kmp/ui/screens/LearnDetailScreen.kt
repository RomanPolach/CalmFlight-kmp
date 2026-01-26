package com.romanpolach.peacefulflight.kmp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.romanpolach.peacefulflight.kmp.data.AppContent
import com.romanpolach.peacefulflight.kmp.ui.components.ContentCard
import com.romanpolach.peacefulflight.kmp.ui.components.ImageWithTitle
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import peacefulflight.composeapp.generated.resources.Res
import peacefulflight.composeapp.generated.resources.img_takeoff

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LearnDetailScreen(
    itemId: String,
    onBack: () -> Unit
) {
    val item = remember(itemId) { AppContent.getLearnItemById(itemId) }

    if (item == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Article not found")
        }
        return
    }

    val scrollState = rememberScrollState()
    val density = LocalDensity.current

    val headerHeight = 300.dp
    val headerHeightPx = with(density) { headerHeight.toPx() }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // LAYER 1: THE HEADER IMAGE (Parallax)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(headerHeight)
                .graphicsLayer {
                    translationY = -scrollState.value * 0.5f
                    alpha = (1f - (scrollState.value / headerHeightPx)).coerceIn(0f, 1f)
                }
        ) {
            Image(
                painter = painterResource(Res.drawable.img_takeoff),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            // Gradient Overlay
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                MaterialTheme.colorScheme.background
                            ),
                            startY = 0f
                        )
                    )
            )

            Text(
                text = stringResource(item.questionRes),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp)
            )
        }

        // LAYER 2: THE SCROLLABLE CONTENT
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            Spacer(Modifier.height(headerHeight))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(horizontal = 16.dp)
            ) {
                Spacer(modifier = Modifier.height(24.dp))

                ContentCard(text = stringResource(item.answerRes))

                if (item.imageRes != null && item.imageTitleRes != null) {
                    Spacer(modifier = Modifier.height(24.dp))
                    ImageWithTitle(
                        painter = painterResource(item.imageRes),
                        title = stringResource(item.imageTitleRes)
                    )
                }

                Spacer(modifier = Modifier.height(100.dp))
            }
        }

        // LAYER 3: THE TOP BAR
        val toolbarAlpha by remember {
            derivedStateOf {
                (scrollState.value / (headerHeightPx * 0.7f)).coerceIn(0f, 1f)
            }
        }

        TopAppBar(
            title = {
                if (scrollState.value > headerHeightPx - 100) {
                    Text(
                        text = stringResource(item.questionRes),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.background.copy(alpha = toolbarAlpha),
                scrolledContainerColor = MaterialTheme.colorScheme.background
            )
        )
    }
}
