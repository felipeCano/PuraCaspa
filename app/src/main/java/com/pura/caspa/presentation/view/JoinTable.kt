package com.pura.caspa.presentation.view

import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.pura.caspa.R
import com.pura.caspa.compose.AdmobBanner
import com.pura.caspa.compose.PuraCaspaButton
import com.pura.caspa.compose.TitleFrame
import com.pura.caspa.compose.textFieldColors
import com.pura.caspa.data.util.Resource
import com.pura.caspa.presentation.viewmodel.JoinTableViewModel

@Composable
fun JoinTable(
    onNavigateJoinToPuraCaspaGameView: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: JoinTableViewModel = hiltViewModel(),
    initialTableId: String? = null
) {
    var roomIdInput by remember { mutableStateOf(initialTableId ?: "") }
    val state by viewModel.joinState.collectAsState()
    val userName by viewModel.getName.collectAsState()
    val nameState by viewModel.nameState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(state) {
        if (state is Resource.Success) {
            onNavigateJoinToPuraCaspaGameView(roomIdInput)
            viewModel.resetState()
        }
    }
    LaunchedEffect(Unit) {
        viewModel.getName()
    }

    TitleFrame(stringResource(R.string.join_the_room)) {paddongValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddongValues)
        ) {
            AdmobBanner(
                adUnitId = viewModel.bannerAdUnitId,
                modifier = Modifier
                    .fillMaxWidth().align(Alignment.TopCenter).padding(top = 10.dp)
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .offset(y = (-50).dp)
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                if (userName.isEmpty()) {
                    Text(
                        text = stringResource(id = R.string.name_player),
                        color = Color.LightGray,
                        modifier = Modifier
                            .align(Alignment.Start)
                            .padding(start = 8.dp)
                    )
                    TextField(
                        value = nameState,
                        onValueChange = { input ->
                            viewModel.onNameChange(input)
                        },
                        placeholder = {
                            Text(
                                text = stringResource(id = R.string.name_suggestion),
                                color = Color.Gray
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .border(
                                1.dp,
                                colorResource(id = R.color.gold_border),
                                RoundedCornerShape(12.dp)
                            ),
                        colors = textFieldColors(),
                        shape = RoundedCornerShape(12.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
                Text(
                    text = stringResource(id = R.string.enter_the_room_name),
                    color = Color.LightGray,
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(start = 8.dp)
                )

                TextField(
                    value = roomIdInput,
                    onValueChange = { input ->
                        val cleanText = input.filter { char ->
                            char.isLetterOrDigit() ||
                                    char == '-' || char == '_'
                        }
                        roomIdInput = cleanText
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .border(
                            1.dp,
                            colorResource(id = R.color.gold_border),
                            RoundedCornerShape(12.dp)
                        ),
                    colors = textFieldColors(),
                    shape = RoundedCornerShape(12.dp)
                )
                state?.let { currentState ->
                    when (currentState) {
                        is Resource.Loading -> {
                            CircularProgressIndicator(
                                color = colorResource(R.color.blue)
                            )
                        }
                        is Resource.Error -> {
                            val errorText = currentState.message?.asString(context) ?: ""
                            Text(errorText.ifEmpty { "Error desconocido" }, color = Color.Red)
                        }

                        is Resource.Success -> {
                            Text(stringResource(R.string.room_found), color = Color.Green)
                        }

                        is Resource.Idle -> {}
                    }
                }
            }
            Column(
                modifier = modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .padding(bottom = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                PuraCaspaButton(
                    text = stringResource(id = R.string.join_table),
                    enabled = true,
                    onClick = {
                        val showError = R.string.field_cant_be_empty
                        val finalName = if (userName.isEmpty()) nameState else userName
                        if (finalName.isBlank() || roomIdInput.isBlank()) {
                            Toast.makeText(
                                context,
                                showError,
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            if (userName.isEmpty()) {
                                viewModel.saveName()
                            }
                            viewModel.joinToRoom(roomIdInput)
                        }
                    }
                )
            }
        }
    }

}