package dev.mindfuldays.app.ui.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.mindfuldays.app.R
import dev.mindfuldays.app.data.model.MindfulnessAttitude
import dev.mindfuldays.app.ui.theme.MindfulDaysTheme
import dev.mindfuldays.app.ui.viewmodel.MindfulnessViewModel

@Composable
fun HomeScreen(
    viewModel: MindfulnessViewModel,
    onNavigateToSettings: () -> Unit,
    onNavigateToTimer: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    // Se envía a Gemini el nombre ya traducido al idioma del dispositivo
    val localizedTitle = stringResource(uiState.attitude.nameRes)

    HomeScreenContent(
        attitude = uiState.attitude,
        aiReflection = uiState.aiReflection,
        isLoadingAi = uiState.isLoadingAi,
        totalAttitudes = viewModel.totalAttitudes,
        onGenerateAiReflection = { viewModel.generateAiReflection(localizedTitle) },
        onNavigateToSettings = onNavigateToSettings,
        onNavigateToTimer = onNavigateToTimer
    )
}

@Composable
fun HomeScreenContent(
    attitude: MindfulnessAttitude,
    aiReflection: String,
    isLoadingAi: Boolean,
    onGenerateAiReflection: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToTimer: () -> Unit,
    modifier: Modifier = Modifier,
    totalAttitudes: Int = 9
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .padding(horizontal = 24.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Box(modifier = Modifier.fillMaxWidth().padding(top = 16.dp)) {
            Column(
                modifier = Modifier.align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.app_title),
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = stringResource(R.string.attitude_counter, attitude.id, totalAttitudes),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            IconButton(
                onClick = onNavigateToSettings,
                modifier = Modifier.align(Alignment.CenterEnd)
            ) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = stringResource(R.string.cd_settings),
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
        }

        ElevatedCard(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.elevatedCardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp)
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text(
                    text = if (attitude.nameRes != 0) stringResource(attitude.nameRes) else attitude.name,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(Modifier.height(12.dp))
                Text(
                    text = if (attitude.descriptionRes != 0) stringResource(attitude.descriptionRes) else attitude.description,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 16.dp),
                    color = MaterialTheme.colorScheme.outlineVariant
                )
                Text(
                    text = aiReflection.ifBlank { stringResource(R.string.default_reflection) },
                    style = MaterialTheme.typography.bodyMedium,
                    fontStyle = FontStyle.Italic,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(Modifier.height(24.dp))
                Button(
                    onClick = onGenerateAiReflection,
                    enabled = !isLoadingAi,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (isLoadingAi) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(18.dp),
                            strokeWidth = 2.dp
                        )
                    } else {
                        Icon(Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(18.dp))
                    }
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = stringResource(R.string.btn_new_reflection),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        OutlinedButton(
            onClick = onNavigateToTimer,
            modifier = Modifier.fillMaxWidth().height(52.dp),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.primary)
        ) {
            Text(stringResource(R.string.btn_start_timer))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeScreenPreview() {
    MindfulDaysTheme {
        HomeScreenContent(
            attitude = MindfulnessAttitude(3, "Acceptance", "", R.string.attitude_3_name, R.string.attitude_3_desc),
            aiReflection = "",
            isLoadingAi = false,
            onGenerateAiReflection = {},
            onNavigateToSettings = {},
            onNavigateToTimer = {}
        )
    }
}
