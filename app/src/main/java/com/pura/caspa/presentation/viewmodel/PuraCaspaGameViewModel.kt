package com.pura.caspa.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pura.caspa.data.model.PartyData
import com.pura.caspa.data.util.Resource
import com.pura.caspa.domain.usecase.GetPartyDataUseCase
import com.pura.caspa.domain.usecase.GetUserNameUseCase
import com.pura.caspa.domain.usecase.StartGameUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PuraCaspaGameViewModel @Inject constructor(
    private val getUserNameUseCase: GetUserNameUseCase,
    private val getPartyDataUseCase: GetPartyDataUseCase,
    private val startGameUseCase: StartGameUseCase
) : ViewModel() {

    private val _partyData = MutableStateFlow<Resource<PartyData>>(Resource.Loading())
    val partyData = _partyData.asStateFlow()

    private val _currentUserName = MutableStateFlow("")
    val currentUserName = _currentUserName.asStateFlow()

    init {
        loadUserName()
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
                startGameUseCase(
                    roomId = roomId,
                    integrantes = roomData!!.integrantes,
                    usedWords = roomData.usedWords
                )
            }
        }
    }
}