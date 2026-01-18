package com.pura.caspa.presentation.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.pura.caspa.presentation.viewmodel.UserNameViewModel

@Composable
fun CreateOrJoinTable(
    onNavigateToCreate: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: UserNameViewModel = hiltViewModel()
){
    val nameState by viewModel.nameState.collectAsState()
    Column(modifier = modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        TextField(
            value = nameState,
            onValueChange = { newText: String ->
                viewModel.onNameChange(newText)
            },
            label = { Text(text = "Nombre del jugador") },
            placeholder = { Text(text = "Ej. Juan Pérez") }
        )
        Button(modifier = modifier, onClick = {
            viewModel.saveName()
            onNavigateToCreate()
        }) {
            Text("Crear Sala")
        }
        Button(modifier = modifier, onClick = {

        }) {
            Text("Unirse a la sala")
        }
    }

}