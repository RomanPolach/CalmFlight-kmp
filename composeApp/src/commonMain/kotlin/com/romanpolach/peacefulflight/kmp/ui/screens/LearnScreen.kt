package com.romanpolach.peacefulflight.kmp.ui.screens

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.romanpolach.peacefulflight.kmp.model.LearnItem
import com.romanpolach.peacefulflight.kmp.model.LearnSection
import com.romanpolach.peacefulflight.kmp.ui.components.StandardTopBar
import com.romanpolach.peacefulflight.kmp.viewmodel.LearnViewModel
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import peacefulflight.composeapp.generated.resources.Res
import peacefulflight.composeapp.generated.resources.nav_learn

/**
 * Learn screen with educational content about flying
 */
@OptIn(KoinExperimentalAPI::class)
@Composable
fun LearnScreen(
    onNavigateToDetail: (String) -> Unit = {},
    viewModel: LearnViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val navLearn = stringResource(Res.string.nav_learn)

    Scaffold(
        topBar = {
            StandardTopBar(title = navLearn)
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(top = 8.dp, bottom = 88.dp)
        ) {
            items(uiState.sections) { section ->
                LearnSectionCard(
                    section = section,
                    isExpanded = uiState.expandedSectionId == section.id,
                    onToggle = {
                        viewModel.toggleSection(section.id)
                    },
                    onItemClick = onNavigateToDetail
                )
            }
        }
    }
}


@Composable
fun LearnSectionCard(
    section: LearnSection,
    isExpanded: Boolean,
    onToggle: () -> Unit,
    onItemClick: (String) -> Unit
) {
    val cardColor = if (isExpanded)
        MaterialTheme.colorScheme.primaryContainer
    else
        MaterialTheme.colorScheme.surfaceContainer

    val title = stringResource(section.titleRes)
    val imageRes = section.imageRes

    Card(
        colors = CardDefaults.cardColors(containerColor = cardColor),
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize()
    ) {
        Column {
            // Header with image
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .clickable { onToggle() }
            ) {
                // Background Image
                imageRes?.let { res ->
                    Image(
                        painter = painterResource(res),
                        contentDescription = title,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )

                    // Scrim/Overlay for readability
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        Color.Black.copy(alpha = 0.4f),
                                        Color.Black.copy(alpha = 0.8f)
                                    ),
                                    startY = 100f
                                )
                            )
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.Bottom
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f)
                    )

                    Icon(
                        imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                }
            }

            if (isExpanded) {
                HorizontalDivider(
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                section.items.forEachIndexed { index, item ->
                    if (index > 0) {
                        HorizontalDivider(
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                    }
                    LearnItemRow(item = item, onClick = { onItemClick(item.id) })
                }

                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun LearnItemRow(
    item: LearnItem,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(start = 32.dp, end = 16.dp, top = 16.dp, bottom = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(item.questionRes),
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Normal,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary
        )
    }
}
