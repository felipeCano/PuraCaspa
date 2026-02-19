package com.pura.caspa.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pura.caspa.R
import com.pura.caspa.data.model.PartyData
import com.pura.caspa.data.util.Resource
import com.pura.caspa.data.util.UiText
import com.pura.caspa.domain.usecase.GetInstallationIdUseCase
import com.pura.caspa.domain.usecase.GetPartyDataUseCase
import com.pura.caspa.domain.usecase.GetUserNameUseCase
import com.pura.caspa.domain.usecase.SharePartyIDUseCase
import com.pura.caspa.domain.usecase.StartGameUseCase
import com.pura.caspa.domain.usecase.UpdateToVotingUseCase
import com.pura.caspa.presentation.util.toUiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PuraCaspaGameViewModel @Inject constructor(
    private val getUserNameUseCase: GetUserNameUseCase,
    private val getPartyDataUseCase: GetPartyDataUseCase,
    private val getInstallationIdUseCase: GetInstallationIdUseCase,
    private val startGameUseCase: StartGameUseCase,
    private val sharePartyIDUseCase: SharePartyIDUseCase,
    private val updateToVotingUseCase: UpdateToVotingUseCase
) : ViewModel() {

    private val _partyData = MutableStateFlow<Resource<PartyData>>(Resource.Loading())
    val partyData = _partyData.asStateFlow()

    private val _currentUserName = MutableStateFlow("")

    private val _myId = MutableStateFlow<String>("")
    val myId: StateFlow<String> = _myId.asStateFlow()

    private val _shareMessage = MutableStateFlow("")
    val shareMessage = _shareMessage.asStateFlow()

    init {
        loadMyInstallationId()
        loadUserName()
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
            }
        }
    }

    fun onStartGameClicked(roomId: String) {
        //1. We get the current value of our StateFlow
        val currentRoomState = _partyData.value

        //2. Just proceed if the state is Success (we have room data)
        if (currentRoomState is Resource.Success) {
            val roomData = currentRoomState.data

            viewModelScope.launch {
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
}