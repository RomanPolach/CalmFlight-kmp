package com.romanpolach.peacefulflight.kmp.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.romanpolach.peacefulflight.kmp.ui.components.StandardTopBar
import org.jetbrains.compose.resources.stringResource
import peacefulflight.composeapp.generated.resources.Res
import peacefulflight.composeapp.generated.resources.help_option_am_desc
import peacefulflight.composeapp.generated.resources.help_option_am_title
import peacefulflight.composeapp.generated.resources.help_option_ftf_desc
import peacefulflight.composeapp.generated.resources.help_option_ftf_title
import peacefulflight.composeapp.generated.resources.help_option_gforce_desc
import peacefulflight.composeapp.generated.resources.help_option_gforce_title
import peacefulflight.composeapp.generated.resources.help_option_ptw_desc
import peacefulflight.composeapp.generated.resources.help_option_ptw_title
import peacefulflight.composeapp.generated.resources.help_option_rc_desc
import peacefulflight.composeapp.generated.resources.help_option_rc_title
import peacefulflight.composeapp.generated.resources.help_option_rtw_desc
import peacefulflight.composeapp.generated.resources.help_option_rtw_title
import peacefulflight.composeapp.generated.resources.help_option_sca_desc
import peacefulflight.composeapp.generated.resources.help_option_sca_title
import peacefulflight.composeapp.generated.resources.help_option_sf_desc
import peacefulflight.composeapp.generated.resources.help_option_sf_title
import peacefulflight.composeapp.generated.resources.help_option_wo_desc
import peacefulflight.composeapp.generated.resources.help_option_wo_title
import peacefulflight.composeapp.generated.resources.help_options_title

private data class HelpOption(
    val title: String,
    val description: String,
    val onClick: () -> Unit
)

@Composable
fun HelpOptionsScreen(
    onBackClick: () -> Unit,
    onNavigateToGForce: () -> Unit,
    onNavigateToRidingTheWave: () -> Unit,
    onNavigateToPostponeTheWorry: () -> Unit,
    onNavigateToWorryOlympics: () -> Unit,
    onNavigateToFacingTheFear: () -> Unit,
    onNavigateToRealityCheck: () -> Unit,
    onNavigateToSafetyFacts: () -> Unit,
    onNavigateToAcceptanceMeditation: () -> Unit,
    onNavigateToSelfCompassion: () -> Unit
) {
    val options = listOf(
        HelpOption(
            title = stringResource(Res.string.help_option_gforce_title),
            description = stringResource(Res.string.help_option_gforce_desc),
            onClick = onNavigateToGForce
        ),
        HelpOption(
            title = stringResource(Res.string.help_option_rtw_title),
            description = stringResource(Res.string.help_option_rtw_desc),
            onClick = onNavigateToRidingTheWave
        ),
        HelpOption(
            title = stringResource(Res.string.help_option_ptw_title),
            description = stringResource(Res.string.help_option_ptw_desc),
            onClick = onNavigateToPostponeTheWorry
        ),
        HelpOption(
            title = stringResource(Res.string.help_option_wo_title),
            description = stringResource(Res.string.help_option_wo_desc),
            onClick = onNavigateToWorryOlympics
        ),
        HelpOption(
            title = stringResource(Res.string.help_option_ftf_title),
            description = stringResource(Res.string.help_option_ftf_desc),
            onClick = onNavigateToFacingTheFear
        ),
        HelpOption(
            title = stringResource(Res.string.help_option_rc_title),
            description = stringResource(Res.string.help_option_rc_desc),
            onClick = onNavigateToRealityCheck
        ),
        HelpOption(
            title = stringResource(Res.string.help_option_sf_title),
            description = stringResource(Res.string.help_option_sf_desc),
            onClick = onNavigateToSafetyFacts
        ),
        HelpOption(
            title = stringResource(Res.string.help_option_am_title),
            description = stringResource(Res.string.help_option_am_desc),
            onClick = onNavigateToAcceptanceMeditation
        ),
        HelpOption(
            title = stringResource(Res.string.help_option_sca_title),
            description = stringResource(Res.string.help_option_sca_desc),
            onClick = onNavigateToSelfCompassion
        )
    )

    Scaffold(
        topBar = {
            StandardTopBar(
                title = stringResource(Res.string.help_options_title),
                onBackClick = onBackClick
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            items(options.size) { index ->
                val option = options[index]
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(onClick = option.onClick),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainer
                    )
                ) {
                    androidx.compose.foundation.layout.Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = option.title,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = option.description,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                        )
                    }
                }
            }
        }
    }
}
