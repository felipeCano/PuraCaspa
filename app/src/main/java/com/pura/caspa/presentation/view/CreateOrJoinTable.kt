package com.pura.caspa.presentation.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun CreateOrJoinTable(
    onNavigateToCreate: () -> Unit,
    modifier: Modifier = Modifier,
){
    Column(modifier = modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Button(modifier = modifier, onClick = {
            onNavigateToCreate()
        }) {
            Text("Create Table")
        }
        Button(modifier = modifier, onClick = {

        }) {
            Text("Join Table")
        }
    }

}