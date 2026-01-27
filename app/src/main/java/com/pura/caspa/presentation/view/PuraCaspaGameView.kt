package com.pura.caspa.presentation.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.pura.caspa.compose.PuraCaspaButton
import com.pura.caspa.compose.TitleFrame
import com.pura.caspa.data.util.Resource
import com.pura.caspa.presentation.viewmodel.PuraCaspaGameViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PuraCaspaGameView(
    modifier: Modifier = Modifier,
    nameTable: String = "",
    viewModel: PuraCaspaGameViewModel = hiltViewModel()
) {
    val roomState by viewModel.partyData.collectAsState()
    val myName by viewModel.currentUserName.collectAsState()

    LaunchedEffect(key1 = nameTable) {
        viewModel.listenToRoom(nameTable)
    }

    TitleFrame("Sala: ", nameTable){ paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when (val state = roomState) {
                is Resource.Success -> {
                    val partyData = state.data
                    val integrantes = partyData?.integrantes ?: emptyList()
                    val isHost = partyData?.host_id == myName

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        if (partyData?.stateParty == "jugando") {
                            val isImpostor = partyData.amoung_us == myName

                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(32.dp),
                                elevation = CardDefaults.cardElevation(defaultElevation = 12.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.White)
                            ) {
                                Column(
                                    modifier = Modifier
                                        .padding(vertical = 48.dp, horizontal = 16.dp)
                                        .fillMaxWidth(),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    if (isImpostor) {
                                        Text(
                                            text = "¡ERES EL",
                                            color = Color.Red,
                                            fontSize = 24.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            text = "IMPOSTOR!",
                                            color = Color.Red,
                                            fontSize = 48.sp,
                                            fontWeight = FontWeight.Black,
                                            textAlign = TextAlign.Center
                                        )
                                        Spacer(modifier = Modifier.height(16.dp))
                                        Text(
                                            text = "Miente para sobrevivir",
                                            style = MaterialTheme.typography.bodyLarge,
                                            color = Color.Gray
                                        )
                                    } else {
                                        Text(
                                            text = "Tu palabra es:",
                                            fontSize = 18.sp,
                                            color = Color.Gray,
                                            fontWeight = FontWeight.Medium
                                        )
                                        Spacer(modifier = Modifier.height(12.dp))
                                        Text(
                                            text = partyData.palabra_actual,
                                            fontSize = 55.sp,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = Color(0xFF2E7D32),
                                            textAlign = TextAlign.Center,
                                            lineHeight = 60.sp,
                                            softWrap = true,
                                            maxLines = 2
                                        )
                                    }
                                }
                            }
                        } else {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    "Esperando inicio...",
                                    style = MaterialTheme.typography.headlineSmall,
                                    modifier = Modifier.padding(bottom = 20.dp)
                                )
                                Card(
                                    modifier = Modifier.fillMaxWidth(0.9f),
                                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                                ) {
                                    Column(modifier = Modifier.padding(24.dp)) {
                                        Text("Jugadores (${integrantes.size}):", fontWeight = FontWeight.Bold)
                                        Spacer(modifier = Modifier.height(8.dp))
                                        integrantes.forEach { Text("• $it", style = MaterialTheme.typography.bodyLarge) }
                                    }
                                }
                            }
                        }
                    }
                    if (isHost) {
                        PuraCaspaButton(
                            text = if (partyData.stateParty == "waiting") "INICIAR JUEGO" else "SIGUIENTE PALABRA",
                            onClick = { viewModel.onStartGameClicked(nameTable) },
                            enabled = integrantes.size >= 2,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(64.dp),
                        )
                    }
                }
                is Resource.Loading -> CircularProgressIndicator()
                is Resource.Error -> Text("Error al cargar datos")
            }
        }
    }
}