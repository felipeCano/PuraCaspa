package com.pura.caspa.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pura.caspa.data.model.PartyData
import com.pura.caspa.data.model.Words
import com.pura.caspa.data.util.Resource
import com.pura.caspa.domain.usecase.GetPartyDataUseCase
import com.pura.caspa.domain.usecase.GetUserNameUseCase
import com.pura.caspa.domain.usecase.GetWordsToPlayUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PuraCaspaGameViewModel @Inject constructor(
    private val getUserNameUseCase: GetUserNameUseCase,
    private val getPartyDataUseCase: GetPartyDataUseCase,
    val getWordsToPlayUseCase: GetWordsToPlayUseCase,
) : ViewModel() {

    private val _partyData = MutableStateFlow<Resource<PartyData>>(Resource.Loading())
    val partyData = _partyData.asStateFlow()

    private val _currentUserName = MutableStateFlow("")
    val currentUserName = _currentUserName.asStateFlow()

    init {
        loadUserName()
    }

    val state: StateFlow<Resource<Words>> = flow {
        emit(getWordsToPlayUseCase.execute())
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = Resource.Loading()
    )

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
}