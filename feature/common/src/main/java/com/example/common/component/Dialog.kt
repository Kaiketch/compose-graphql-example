package com.example.common.component

import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*

@Composable
fun ErrorDialog(errors: List<Throwable>, onDismissRequest: (() -> Unit)? = null) {
    var isShow by remember {
        mutableStateOf(true)
    }
    if (isShow) {
        AlertDialog(
            onDismissRequest = {
                onDismissRequest?.invoke()
                isShow = false
            },
            title = {
                Text(text = "エラー")
            },
            text = {
                Text(text = errors.joinToString("\n") { it.message.orEmpty() })
            },
            buttons = {
                Button(onClick = {
                    onDismissRequest?.invoke()
                    isShow = false
                }) {
                    Text(text = "とじる")
                }
            },
        )
    }
}