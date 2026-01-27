package com.pura.caspa.presentation.view

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.pura.caspa.R
import com.pura.caspa.compose.PuraCaspaButton
import com.pura.caspa.data.util.Resource
import com.pura.caspa.presentation.viewmodel.JoinTableViewModel

@Composable
fun JoinTable(
    onNavigateJoinToPuraCaspaGameView: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: JoinTableViewModel = hiltViewModel()
) {
    var roomIdInput by remember { mutableStateOf("") }
    val state by viewModel.joinState.collectAsState()

    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            TextField(
                value = roomIdInput,
                onValueChange = { roomIdInput = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .border(
                        1.dp,
                        colorResource(id = R.color.gold_border),
                        RoundedCornerShape(12.dp)
                    ),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = colorResource(id = R.color.input_background),
                    unfocusedContainerColor = colorResource(id = R.color.input_background),
                    disabledContainerColor = colorResource(id = R.color.input_background),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            Spacer(modifier = Modifier.height(24.dp))
            PuraCaspaButton(
                text = stringResource(id = R.string.join_table),
                enabled = true,
                onClick = {
                    viewModel.joinToRoom(roomIdInput)
                }
            )

            if (state.message != null || state.data != null) {
                //Handle state
                when (state) {
                    is Resource.Loading -> {
                        CircularProgressIndicator()
                    }

                    is Resource.Error -> Text(state.message ?: "Error", color = Color.Red)
                    is Resource.Success -> {
                        Text("Sala '${state.data}' creada con éxito", color = Color.Green)
                        onNavigateJoinToPuraCaspaGameView(roomIdInput)
                    }

                    else -> {}
                }
            }
        }
    }
}