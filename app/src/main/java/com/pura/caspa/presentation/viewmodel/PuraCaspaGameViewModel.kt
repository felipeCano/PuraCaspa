package com.pura.caspa.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pura.caspa.R
import com.pura.caspa.data.model.PartyData
import com.pura.caspa.data.util.Resource
import com.pura.caspa.data.util.UiText
import com.pura.caspa.domain.repository.AdProvider
import com.pura.caspa.domain.usecase.GetInstallationIdUseCase
import com.pura.caspa.domain.usecase.GetPartyDataUseCase
import com.pura.caspa.domain.usecase.GetUserNameUseCase
import com.pura.caspa.domain.usecase.ResetPlayersVotesUseCase
import com.pura.caspa.domain.usecase.RevealImpostorUseCase
import com.pura.caspa.domain.usecase.SharePartyIDUseCase
import com.pura.caspa.domain.usecase.ShowRewardedAdUseCase
import com.pura.caspa.domain.usecase.StartGameUseCase
import com.pura.caspa.domain.usecase.UpdateToVotingUseCase
import com.pura.caspa.domain.usecase.VoteForPlayerUseCase
import com.pura.caspa.presentation.util.toUiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PuraCaspaGameViewModel @Inject constructor(
    private val getUserNameUseCase: GetUserNameUseCase,
    private val getPartyDataUseCase: GetPartyDataUseCase,
    private val getInstallationIdUseCase: GetInstallationIdUseCase,
    private val startGameUseCase: StartGameUseCase,
    private val sharePartyIDUseCase: SharePartyIDUseCase,
    private val updateToVotingUseCase: UpdateToVotingUseCase,
    private val voteForPlayerUseCase: VoteForPlayerUseCase,
    private val revealImpostorUseCase: RevealImpostorUseCase,
    private val resetPlayersVotesUseCase: ResetPlayersVotesUseCase,
    private val showRewardedAdUseCase: ShowRewardedAdUseCase,
    private val adProvider: AdProvider,
) : ViewModel() {

    private var lastWord = ""

    private val _partyData = MutableStateFlow<Resource<PartyData>>(Resource.Loading())
    val partyData = _partyData.asStateFlow()

    private val _currentUserName = MutableStateFlow("")

    private val _myId = MutableStateFlow<String>("")
    val myId: StateFlow<String> = _myId.asStateFlow()

    private val _shareMessage = MutableStateFlow("")
    val shareMessage = _shareMessage.asStateFlow()

    private val _hasVoted = MutableStateFlow(false)
    val hasVoted: StateFlow<Boolean> = _hasVoted.asStateFlow()

    private val _isChangingWord = MutableStateFlow(false)
    val isChangingWord = _isChangingWord.asStateFlow()

    private var timerJob: Job? = null
    private val _remainingTime = MutableStateFlow(30)
    val remainingTime: StateFlow<Int> = _remainingTime.asStateFlow()

    //Variable to know if the time expired
    private val _isTimerFinished = MutableStateFlow(false)
    val isTimerFinished: StateFlow<Boolean> = _isTimerFinished.asStateFlow()

    val bannerAdUnitId: String = adProvider.getBannerAdUnitId()

    init {
        loadMyInstallationId()
        loadUserName()
        adProvider.loadAd()
    }

    private fun loadMyInstallationId() {
        viewModelScope.launch {
            _myId.value = getInstallationIdUseCase()
        }
    }

    private fun loadUserName() {
        viewModelScope.launch {
            _currentUserName.value = getUserNameUseCase()
        }
    }

    fun listenToRoom(roomId: String) {
        viewModelScope.launch {
            getPartyDataUseCase(roomId).collect { result ->
                _partyData.value = result
                if (result is Resource.Success) {
                    val data = result.data

                    if (data?.stateParty == "voting" && timerJob == null) {
                        startVotingTimer()
                    } else if (data?.stateParty != "voting") {
                        stopVotingTimer()
                    }

                    val meEnLaLista = data?.integrantes?.find { it.id == _myId.value }
                    if (meEnLaLista != null) {
                        _hasVoted.value = meEnLaLista.hasVoted
                    }

                    if (data?.palabra_actual != lastWord) {
                        _isChangingWord.value = false
                    }
                }
            }
        }
    }

    fun onStartGameClicked(roomId: String) {
        //1. We get the current value of our StateFlow
        val currentRoomState = _partyData.value

        //2. Just proceed if the state is Success (we have room data)
        if (currentRoomState is Resource.Success) {
            val roomData = currentRoomState.data
            lastWord = currentRoomState.data?.palabra_actual ?: ""
            viewModelScope.launch {
                _isChangingWord.value = true
                resetPlayersVotesUseCase(roomId)
                revealImpostorUseCase(roomId, false)
                resetVotingState()

                //3. We call the UseCase passing the required parameters
                val result = startGameUseCase(
                    roomId = roomId,
                    integrantes = roomData!!.integrantes,
                    usedWords = roomData.usedWords
                )

                when (result) {
                    is Resource.Error -> {
                        val finalMessage = result.partyError?.toUiText(args = roomId)
                            ?: result.message
                            ?: UiText.StringResource(R.string.unknown_error)

                        _partyData.value = Resource.Error(message = finalMessage)
                        _isChangingWord.value = false
                    }
                    is Resource.Success -> {
                    }
                    is Resource.Loading -> {}
                    is Resource.Idle -> {}
                }
            }
        }
    }

    fun onShareClicked(roomId: String) {
        val message = sharePartyIDUseCase(partyId = roomId)
        _shareMessage.value = message
    }

    fun onShareDone() {
        _shareMessage.value = ""
    }

    //updated PartyState
    fun changeStatusToVoting(roomId: String) {
        viewModelScope.launch {
            val result = updateToVotingUseCase(roomId)

            when (result) {
                is Resource.Error -> {
                    val finalMessage = result.partyError?.toUiText(args = roomId)
                        ?: result.message
                        ?: UiText.StringResource(R.string.unknown_error)

                    _partyData.value = Resource.Error(message = finalMessage)
                }
                is Resource.Success -> {}
                is Resource.Loading -> { }
                is Resource.Idle -> {}
            }
        }
    }

    //Voting
    fun onVoteClicked(roomId: String, playerVotedId: String) {
        if (_hasVoted.value) return

        viewModelScope.launch {
            val myPlayerId = _myId.value

            val result = voteForPlayerUseCase(roomId, playerVotedId, myPlayerId)

            if (result is Resource.Error) {
                _hasVoted.value = true
            }
        }
    }

    //Fun to reset voting state when a new round starts
    fun resetVotingState() {
        _hasVoted.value = false
    }

    //Reveal Imposter
    fun onRevealImpostorClicked(roomId: String) {
        viewModelScope.launch {
            revealImpostorUseCase(roomId, true)
        }
    }

    //Counter Voted
    //This variable is BOOLEAN to enable the button
    val isVotingComplete: StateFlow<Boolean> = _partyData.map { resource ->
        if (resource is Resource.Success) {
            val data = resource.data
            val votosRondaActual = data?.votos_en_esta_ronda ?: 0
            votosRondaActual >= data?.integrantes!!.size
        } else false
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    //This variable is STRING to show the counter
    val votingProgress: StateFlow<String> = _partyData.map { resource ->
        if (resource is Resource.Success) {
            val data = resource.data
            val actuales = data?.votos_en_esta_ronda ?: 0
            val total = data?.integrantes?.size ?: 0

            "$actuales / $total"
        } else "0 / 0"
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "0 / 0")

    fun onShowResultsClicked(activity: android.app.Activity,onComplete: (Boolean) -> Unit) {
        showRewardedAdUseCase(activity) { earned ->
            onComplete(earned)
        }
    }

    private fun startVotingTimer() {
        _isTimerFinished.value = false
        _remainingTime.value = 30
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (_remainingTime.value > 0) {
                delay(1000)
                _remainingTime.value -= 1
            }
            _isTimerFinished.value = true
        }
    }

    private fun stopVotingTimer() {
        timerJob?.cancel()
        timerJob = null
        _remainingTime.value = 30
        _isTimerFinished.value = false
    }

    //We modified the validation to be: (Voting complete) OR (Time expired)
    val canHostMoveForward: StateFlow<Boolean> = combine(
        _partyData,
        _isTimerFinished
    ) { resource, timerDone ->
        if (resource is Resource.Success) {
            val data = resource.data
            val integrantesCount = data?.integrantes?.size ?: 0
            val votosCount = data?.votos_en_esta_ronda ?: 0

            //Available if: all voted OR timer finished
            (votosCount >= integrantesCount && integrantesCount > 0) || timerDone
        } else false
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)
}