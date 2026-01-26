package com.romanpolach.peacefulflight.kmp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.MonitorHeart
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Waves
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.romanpolach.peacefulflight.kmp.ui.components.StandardTopBar
import org.jetbrains.compose.resources.stringResource
import peacefulflight.composeapp.generated.resources.Res
import peacefulflight.composeapp.generated.resources.nav_tools
import peacefulflight.composeapp.generated.resources.tool_acceptance
import peacefulflight.composeapp.generated.resources.tool_acceptance_desc
import peacefulflight.composeapp.generated.resources.tool_catastrophic
import peacefulflight.composeapp.generated.resources.tool_catastrophic_desc
import peacefulflight.composeapp.generated.resources.tool_facing_fear
import peacefulflight.composeapp.generated.resources.tool_facing_fear_desc
import peacefulflight.composeapp.generated.resources.tool_gforce
import peacefulflight.composeapp.generated.resources.tool_gforce_desc
import peacefulflight.composeapp.generated.resources.tool_postpone_worry
import peacefulflight.composeapp.generated.resources.tool_postpone_worry_desc
import peacefulflight.composeapp.generated.resources.tool_reality_check
import peacefulflight.composeapp.generated.resources.tool_reality_check_desc
import peacefulflight.composeapp.generated.resources.tool_riding_wave
import peacefulflight.composeapp.generated.resources.tool_riding_wave_desc
import peacefulflight.composeapp.generated.resources.tool_safety_facts
import peacefulflight.composeapp.generated.resources.tool_safety_facts_desc
import peacefulflight.composeapp.generated.resources.tool_self_compassion
import peacefulflight.composeapp.generated.resources.tool_self_compassion_desc
import peacefulflight.composeapp.generated.resources.tool_voice
import peacefulflight.composeapp.generated.resources.tool_voice_desc
import peacefulflight.composeapp.generated.resources.tool_worry_olympics
import peacefulflight.composeapp.generated.resources.tool_worry_olympics_desc

/**
 * Tool data class for UI
 */
data class ToolDisplay(
    val id: String,
    val name: String,
    val description: String,
    val iconName: String
)

/**
 * Tools screen showing all available anxiety management tools
 */
@Composable
fun ToolsScreen(
    onNavigateToTool: (String) -> Unit = {}
) {
    // Use Compose Resources for localized tool names
    val tools = listOf(
        ToolDisplay(
            id = "3",
            name = stringResource(Res.string.tool_gforce),
            description = stringResource(Res.string.tool_gforce_desc),
            iconName = "Graph"
        ),
        ToolDisplay(
            id = "5",
            name = stringResource(Res.string.tool_riding_wave),
            description = stringResource(Res.string.tool_riding_wave_desc),
            iconName = "Wave"
        ),
        ToolDisplay(
            id = "6",
            name = stringResource(Res.string.tool_postpone_worry),
            description = stringResource(Res.string.tool_postpone_worry_desc),
            iconName = "Clock"
        ),
        ToolDisplay(
            id = "7",
            name = stringResource(Res.string.tool_worry_olympics),
            description = stringResource(Res.string.tool_worry_olympics_desc),
            iconName = "Trophy"
        ),
        ToolDisplay(
            id = "8",
            name = stringResource(Res.string.tool_facing_fear),
            description = stringResource(Res.string.tool_facing_fear_desc),
            iconName = "Cloud"
        ),
        ToolDisplay(
            id = "9",
            name = stringResource(Res.string.tool_reality_check),
            description = stringResource(Res.string.tool_reality_check_desc),
            iconName = "Chart"
        ),
        ToolDisplay(
            id = "10",
            name = stringResource(Res.string.tool_safety_facts),
            description = stringResource(Res.string.tool_safety_facts_desc),
            iconName = "Shield"
        ),
        ToolDisplay(
            id = "11",
            name = stringResource(Res.string.tool_acceptance),
            description = stringResource(Res.string.tool_acceptance_desc),
            iconName = "Meditation"
        ),
        ToolDisplay(
            id = "12",
            name = stringResource(Res.string.tool_self_compassion),
            description = stringResource(Res.string.tool_self_compassion_desc),
            iconName = "Heart"
        ),
        ToolDisplay(
            id = "13",
            name = stringResource(Res.string.tool_catastrophic),
            description = stringResource(Res.string.tool_catastrophic_desc),
            iconName = "Brain"
        ),
        ToolDisplay(
            id = "14",
            name = stringResource(Res.string.tool_voice),
            description = stringResource(Res.string.tool_voice_desc),
            iconName = "Voice"
        )
    )

    val navTools = stringResource(Res.string.nav_tools)

    Scaffold(
        topBar = {
            StandardTopBar(
                title = navTools,
                onBackClick = null
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(
                top = 16.dp,
                bottom = 88.dp,
                start = 16.dp,
                end = 16.dp
            ),
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            items(tools) { tool ->
                ToolCard(tool, onClick = { onNavigateToTool(tool.id) })
            }
        }
    }
}

/**
 * Individual tool card
 */
@Composable
fun ToolCard(
    tool: ToolDisplay,
    onClick: () -> Unit
) {
    ElevatedCard(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        ),
        modifier = Modifier
            .height(160.dp)
            .clickable { onClick() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = getToolIcon(tool.iconName),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                textAlign = TextAlign.Center,
                text = tool.name,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = tool.description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                textAlign = TextAlign.Center
            )
        }
    }
}

/**
 * Get the appropriate icon for a tool based on its iconName
 */
@Composable
private fun getToolIcon(iconName: String): ImageVector = when (iconName) {
    "Breathing" -> Icons.Default.Air
    "Headphones" -> Icons.Default.Headphones
    "Graph" -> Icons.Default.MonitorHeart
    "Wave" -> Icons.Default.Waves
    "Clock" -> Icons.Default.AccessTime
    "Trophy" -> Icons.Default.EmojiEvents
    "Cloud" -> Icons.Default.Cloud
    "Chart" -> Icons.Default.DateRange
    "Shield" -> Icons.Default.Shield
    "Meditation" -> Icons.Default.SelfImprovement
    "Heart" -> Icons.Default.Favorite
    "Brain" -> Icons.Default.Psychology
    "Voice" -> Icons.Default.RecordVoiceOver
    else -> Icons.Default.QuestionMark
}
