package com.pura.caspa.presentation.view

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.pura.caspa.R
import com.pura.caspa.compose.PuraCaspaButton
import com.pura.caspa.compose.TitleFrame
import com.pura.caspa.compose.UserRow
import com.pura.caspa.data.util.Resource
import com.pura.caspa.presentation.viewmodel.PuraCaspaGameViewModel
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.ui.Alignment
import com.pura.caspa.compose.HeaderSection
import com.pura.caspa.compose.RacerResultRow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PuraCaspaGameView(
    modifier: Modifier = Modifier,
    nameTable: String = "",
    viewModel: PuraCaspaGameViewModel = hiltViewModel(),
    onReturnToHome: () -> Unit = {}
) {
    val roomState by viewModel.partyData.collectAsState()
    val myId by viewModel.myId.collectAsState()
    val context = LocalContext.current
    val messageToShare by viewModel.shareMessage.collectAsState()
    var selectedPlayerId by remember { mutableStateOf("") }
    val hasVoted by viewModel.hasVoted.collectAsState()
    val stateParty = (roomState as? Resource.Success)?.data?.stateParty
    val isVotingComplete by viewModel.isVotingComplete.collectAsState()
    val votingProgress by viewModel.votingProgress.collectAsState()
    val isChangingWord by viewModel.isChangingWord.collectAsState()
    var showResults by remember { mutableStateOf(false) }
    var isAdLoading by remember { mutableStateOf(false) }
    val remainingTime by viewModel.remainingTime.collectAsState()
    val canMoveForward by viewModel.canHostMoveForward.collectAsState()

    LaunchedEffect(key1 = nameTable) {
        viewModel.listenToRoom(nameTable)
    }

    LaunchedEffect(messageToShare) {
        if (messageToShare.isNotEmpty()) {
            val sendIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, messageToShare)
                type = "text/plain"
            }
            val shareIntent = Intent.createChooser(sendIntent, null)
            context.startActivity(shareIntent)
            viewModel.onShareDone()
        }
    }

    LaunchedEffect(key1 = stateParty) {
        if (stateParty == "voting") {
            selectedPlayerId = ""
        }
    }

    LaunchedEffect(key1 = stateParty) {
        if (stateParty != "voting" && stateParty != null) {
            viewModel.resetVotingState()
            selectedPlayerId = ""
        }
    }


    TitleFrame(stringResource(R.string.party), nameTable) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (isChangingWord) {
                CircularProgressIndicator()
            } else {
                when (val state = roomState) {
                    is Resource.Success -> {
                        val partyData = state.data
                        val integrantes = partyData?.integrantes ?: emptyList()
                        val isHost = partyData?.host_id == myId
                        var revealImpostor = partyData?.showImpostor ?: false

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            contentAlignment = Alignment.Center
                        ) {
                            if (partyData?.stateParty == "jugando") {
                                val isImpostor = partyData.amoung_us == myId

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
                                                text = stringResource(R.string.you_are_the),
                                                color = Color.Red,
                                                fontSize = 24.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                            Text(
                                                text = stringResource(R.string.imposter),
                                                color = Color.Red,
                                                fontSize = 48.sp,
                                                fontWeight = FontWeight.Black,
                                                textAlign = TextAlign.Center
                                            )
                                            Spacer(modifier = Modifier.height(16.dp))
                                            Text(
                                                text = stringResource(R.string.lie_to_survive),
                                                style = MaterialTheme.typography.bodyLarge,
                                                color = Color.Gray
                                            )
                                        } else {
                                            Text(
                                                text = stringResource(R.string.your_word_is),
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
                            } else if (partyData?.stateParty == "voting") {
                                if (!revealImpostor) {
                                    LazyColumn(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.spacedBy(12.dp),
                                        contentPadding = PaddingValues(vertical = 16.dp)
                                    ) {
                                        item {
                                            Text(
                                                text = if (remainingTime > 0) "Tiempo de votacion restante: $remainingTime s" else "¡Tiempo agotado!",
                                                color = if (remainingTime <= 5) Color.Red else Color.Gray,
                                                fontWeight = FontWeight.Bold,
                                                modifier = Modifier.padding(bottom = 8.dp)
                                            )
                                        }
                                        items(
                                            items = integrantes,
                                            { player -> player.id }
                                        ) { integrante ->
                                            val isSelected =
                                                integrante.id == selectedPlayerId

                                            Box(modifier = Modifier.clickable(enabled = !hasVoted) { // Si ya votó, no puede cambiar selección
                                                selectedPlayerId = integrante.id
                                            }) {
                                                UserRow(
                                                    name = "${integrante.name} ",
                                                    isSelected = isSelected
                                                )
                                            }
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(13.dp))
                                } else {
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
                                            val imposter =
                                                integrantes.find { it.id == partyData?.amoung_us }
                                            val imposterNameDisplay = imposter?.name ?: ""
                                            Text(
                                                text = imposterNameDisplay,
                                                color = Color.Red,
                                                fontSize = 48.sp,
                                                fontWeight = FontWeight.Black,
                                                textAlign = TextAlign.Center
                                            )
                                        }
                                    }
                                }

                            } else if (partyData?.stateParty == "gameOver") {
                                if (!showResults) {
                                    PuraCaspaButton(
                                        text = if (isAdLoading) "CARGANDO VIDEO..." else "VER RESULTADOS (VIDEO)",
                                        enabled = !isAdLoading,
                                        onClick = {
                                            val activity = context as? android.app.Activity
                                            isAdLoading = true
                                            viewModel.onShowResultsClicked(activity!!) { earned ->
                                                isAdLoading = false
                                                if (earned) {
                                                    showResults = true
                                                } else {
                                                    Toast.makeText(
                                                        context,
                                                        "El video no está listo, intenta en un momento",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                }
                                            }
                                        }, modifier = Modifier
                                            .fillMaxWidth()
                                            .height(64.dp)
                                    )
                                } else {
                                    val winners = remember(partyData.integrantes) {
                                        partyData.integrantes.sortedBy { it.votes }
                                    }

                                    Column(modifier = Modifier.fillMaxSize()) {
                                        HeaderSection()
                                        LazyColumn(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .weight(1f),
                                            contentPadding = PaddingValues(bottom = 80.dp),
                                            verticalArrangement = Arrangement.spacedBy(10.dp)
                                        ) {
                                            item {

                                            }

                                            itemsIndexed(winners) { index, player ->
                                                RacerResultRow(
                                                    player = player,
                                                    position = index + 1
                                                )
                                            }
                                        }
                                    }
                                }

                            } else {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        stringResource(R.string.waiting_to_start),
                                        style = MaterialTheme.typography.headlineSmall,
                                        modifier = Modifier.padding(bottom = 20.dp)
                                    )
                                    Card(
                                        modifier = Modifier.fillMaxWidth(0.9f),
                                        colors = CardDefaults.cardColors(
                                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                                        )
                                    ) {
                                        Column(modifier = Modifier.padding(24.dp)) {
                                            Text(
                                                stringResource(
                                                    R.string.players_waiting
                                                ) + " (${integrantes.size}):",
                                                fontWeight = FontWeight.Bold
                                            )
                                            Spacer(modifier = Modifier.height(8.dp))
                                            integrantes.forEach {
                                                Text(
                                                    "• ${it.name}",
                                                    style = MaterialTheme.typography.bodyLarge
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        Spacer(modifier = Modifier.weight(0.2f))
                        if (partyData?.stateParty == "voting" && !revealImpostor) {
                            PuraCaspaButton(
                                text = "VOTAR",
                                onClick = {
                                    viewModel.onVoteClicked(
                                        nameTable,
                                        selectedPlayerId
                                    )
                                },
                                enabled = !hasVoted && selectedPlayerId.isNotEmpty(),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(64.dp)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                        if (partyData!!.stateParty == "gameOver") {
                            PuraCaspaButton(
                                text = "Volver a jugar",
                                enabled = true,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(64.dp)
                            ) {
                                onReturnToHome()
                            }
                        }
                        if (isHost) {
                            if (partyData?.stateParty == "waiting") {
                                Column {
                                }
                                PuraCaspaButton(
                                    text = stringResource(R.string.share_room),
                                    onClick = {
                                        viewModel.onShareClicked(nameTable)
                                    },
                                    enabled = true,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(64.dp)
                                )
                            }
                            if (partyData!!.stateParty == "jugando") {
                                Column {
                                    PuraCaspaButton(
                                        text = "INICIAR VOTACION",
                                        onClick = {
                                            viewModel.changeStatusToVoting(nameTable)
                                        },
                                        enabled = true,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(64.dp)
                                    )
                                }
                            }
                            if (partyData?.stateParty == "voting" && !revealImpostor) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    PuraCaspaButton(
                                        text = "MOSTRAR IMPOSTOR",
                                        onClick = { viewModel.onRevealImpostorClicked(nameTable) },
                                        enabled = canMoveForward,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(64.dp)
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = if (isVotingComplete) "¡Votación terminada!" else "Votos recibidos: $votingProgress",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = if (isVotingComplete) Color(0xFF2E7D32) else Color.Gray
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            PuraCaspaButton(
                                text = if (partyData.stateParty == "waiting") stringResource(
                                    R.string.start_game
                                ) else stringResource(
                                    R.string.next_word
                                ),
                                onClick = {
                                    viewModel.onStartGameClicked(nameTable)
                                },
                                enabled = integrantes.size >= 2 && (
                                        partyData.stateParty == "waiting" || // Permitir si apenas van a empezar
                                                (partyData.stateParty == "voting" && canMoveForward)),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(64.dp),
                            )
                        }
                    }

                    is Resource.Loading -> CircularProgressIndicator()
                    is Resource.Error -> {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            contentAlignment = Alignment.Center
                        ) {
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
                                    val errorText = state.message?.asString(context) ?: ""
                                    Text(
                                        text = errorText.ifEmpty { "Error Desconocido" },
                                        color = Color.Red,
                                        fontSize = 28.sp,
                                        lineHeight = 34.sp,
                                        textAlign = TextAlign.Center,
                                        fontWeight = FontWeight.Black
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.weight(0.2f))
                    }

                    is Resource.Idle -> {}
                }
            }
        }
    }
}