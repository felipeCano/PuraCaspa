package com.pura.caspa.presentation.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun PuraCaspaGameView(
    modifier: Modifier = Modifier,
    nameTable: String = ""
) {
    Box(modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        Text(
            text = "Table Name: $nameTable"
        )
    }
}