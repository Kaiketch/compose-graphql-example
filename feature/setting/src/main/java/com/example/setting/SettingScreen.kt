package com.example.setting

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.common.R
import com.example.model.RequestLimit

@Composable
fun SettingScreen(
    settingViewModel: SettingViewModel
) {
    val uiState by settingViewModel.uiState.collectAsState()
    var isShowLimitDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.title_setting),
                    )
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues)
        ) {
            Row(
                modifier = Modifier
                    .clickable {
                        isShowLimitDialog = true
                    }
                    .padding(16.dp)
                    .fillMaxWidth()

            ) {
                Text(text = stringResource(id = R.string.setting_limit))
                Spacer(
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 16.dp)
                )
                Text(
                    text = uiState.limit.toString()
                )
            }
        }
    }

    if (isShowLimitDialog) {
        Dialog(onDismissRequest = { isShowLimitDialog = false }) {
            Surface {
                Column(Modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    RequestLimit.values().forEach {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = it.limit.toString())
                            Spacer(modifier = Modifier.weight(1f))
                            RadioButton(
                                selected = it.limit == uiState.limit,
                                onClick = {
                                    settingViewModel.onLimitSelected(it.limit)
                                    isShowLimitDialog = false
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
