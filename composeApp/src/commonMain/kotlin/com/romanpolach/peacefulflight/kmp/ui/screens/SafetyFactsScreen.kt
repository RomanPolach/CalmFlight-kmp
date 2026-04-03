package com.romanpolach.peacefulflight.kmp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.romanpolach.peacefulflight.kmp.ui.components.StandardTopBar
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import peacefulflight.composeapp.generated.resources.Res
import peacefulflight.composeapp.generated.resources.safety_bottom_message
import peacefulflight.composeapp.generated.resources.safety_comparison_accidents
import peacefulflight.composeapp.generated.resources.safety_comparison_flights
import peacefulflight.composeapp.generated.resources.safety_comparison_title
import peacefulflight.composeapp.generated.resources.safety_stat_accident_rate_desc
import peacefulflight.composeapp.generated.resources.safety_stat_accident_rate_title
import peacefulflight.composeapp.generated.resources.safety_stat_daily_desc
import peacefulflight.composeapp.generated.resources.safety_stat_daily_title
import peacefulflight.composeapp.generated.resources.safety_stat_fatal_desc
import peacefulflight.composeapp.generated.resources.safety_stat_fatal_title
import peacefulflight.composeapp.generated.resources.safety_stat_flights_desc
import peacefulflight.composeapp.generated.resources.safety_stat_flights_title
import peacefulflight.composeapp.generated.resources.safety_stat_improvement_desc
import peacefulflight.composeapp.generated.resources.safety_stat_improvement_title
import peacefulflight.composeapp.generated.resources.safety_stat_passengers_desc
import peacefulflight.composeapp.generated.resources.safety_stat_passengers_title
import peacefulflight.composeapp.generated.resources.safety_subtitle
import peacefulflight.composeapp.generated.resources.safety_title
import peacefulflight.composeapp.generated.resources.safety_years_desc
import peacefulflight.composeapp.generated.resources.safety_years_title

private data class SafetyStat(
    val titleRes: StringResource,
    val descriptionRes: StringResource,
    val color: Color
)

@Composable
fun SafetyFactsScreen(
    onBackClick: () -> Unit
) {
    val stats = listOf(
        SafetyStat(
            titleRes = Res.string.safety_stat_passengers_title,
            descriptionRes = Res.string.safety_stat_passengers_desc,
            color = MaterialTheme.colorScheme.primary
        ),
        SafetyStat(
            titleRes = Res.string.safety_stat_flights_title,
            descriptionRes = Res.string.safety_stat_flights_desc,
            color = MaterialTheme.colorScheme.error
        ),
        SafetyStat(
            titleRes = Res.string.safety_stat_daily_title,
            descriptionRes = Res.string.safety_stat_daily_desc,
            color = Color(0xFF0EA5E9)
        ),
        SafetyStat(
            titleRes = Res.string.safety_stat_accident_rate_title,
            descriptionRes = Res.string.safety_stat_accident_rate_desc,
            color = Color(0xFF16A34A)
        ),
        SafetyStat(
            titleRes = Res.string.safety_stat_fatal_title,
            descriptionRes = Res.string.safety_stat_fatal_desc,
            color = Color(0xFFF97316)
        ),
        SafetyStat(
            titleRes = Res.string.safety_stat_improvement_title,
            descriptionRes = Res.string.safety_stat_improvement_desc,
            color = Color(0xFF2563EB)
        )
    )

    Scaffold(
        topBar = {
            StandardTopBar(
                title = stringResource(Res.string.safety_title),
                onBackClick = onBackClick
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = stringResource(Res.string.safety_subtitle),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.9f),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            stats.chunked(2).forEach { row ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    row.forEach { stat ->
                        SafetyStatCard(
                            stat = stat,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    if (row.size == 1) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }

            SafetyComparisonCard()
            SafetyLongOddsCard()

            Text(
                text = stringResource(Res.string.safety_bottom_message),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Medium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun SafetyStatCard(
    stat: SafetyStat,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = stat.color.copy(alpha = 0.15f)),
        modifier = modifier.height(140.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(stat.titleRes),
                style = MaterialTheme.typography.headlineMedium,
                color = stat.color,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(stat.descriptionRes),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun SafetyComparisonCard() {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = stringResource(Res.string.safety_comparison_title),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold
            )

            ComparisonBar(
                label = stringResource(Res.string.safety_comparison_flights),
                progress = 1f,
                color = MaterialTheme.colorScheme.primary
            )

            ComparisonBar(
                label = stringResource(Res.string.safety_comparison_accidents),
                progress = 0.03f,
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}

@Composable
private fun ComparisonBar(
    label: String,
    progress: Float,
    color: Color
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(22.dp)
                .clip(RoundedCornerShape(999.dp))
                .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(progress.coerceIn(0.02f, 1f))
                    .height(22.dp)
                    .clip(RoundedCornerShape(999.dp))
                    .background(color)
            )
        }
    }
}

@Composable
private fun SafetyLongOddsCard() {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = stringResource(Res.string.safety_years_title),
                style = MaterialTheme.typography.displayMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Text(
                text = stringResource(Res.string.safety_years_desc),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )
        }
    }
}
