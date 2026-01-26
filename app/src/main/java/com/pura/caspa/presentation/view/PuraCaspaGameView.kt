package com.pura.caspa.presentation.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Nombre de la Sala: $nameTable",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            when (val state = roomState) {
                is Resource.Success -> {
                    val partyData = state.data
                    val integrantes = partyData?.integrantes ?: emptyList()

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        if (partyData!!.stateParty == "jugando") {
                            // We compare the saved name with the chosen one in Firebase
                            val isImpostor = partyData.amoung_us == myName

                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                if (isImpostor) {
                                    Text("¡ERES EL IMPOSTOR!", color = Color.Red, style = MaterialTheme.typography.bodySmall)
                                    Text("No sabes la palabra. ¡Miente para sobrevivir!")
                                } else {
                                    Text("Tu palabra es:", style = MaterialTheme.typography.bodyMedium)
                                    Text(
                                        text = partyData.palabra_actual,
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = Color.Green
                                    )
                                }
                            }
                        }else{
                            Text(
                                "Jugadores conectados: ${integrantes.size}",
                                style = MaterialTheme.typography.titleMedium
                            )

                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                                )
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    integrantes.forEach { nombre ->
                                        Text("• $nombre", style = MaterialTheme.typography.bodyLarge)
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(20.dp))

                        }

                        // Logic for the leader
                        val isHost = partyData.host_id == myName
                        if (isHost) {
                            if (integrantes.size >= 2) {
                                Button(
                                    onClick = {
                                        viewModel.onStartGameClicked(nameTable)
                                    },
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text("Iniciar Juego")
                                }
                            } else {
                                Text(
                                    "Esperando a más jugadores...",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.secondary
                                )
                            }
                        }
                    }
                }

                is Resource.Loading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                is Resource.Error -> Text(
                    text = state.message ?: "Error",
                    color = Color.Red,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}