package com.pura.caspa.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pura.caspa.data.util.Resource
import com.pura.caspa.domain.usecase.CreatePartyUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class ConfirmTableCreationViewModel @Inject constructor(
    private val createPartyUseCase: CreatePartyUseCase
) : ViewModel() {
    private val _createPartyState = MutableStateFlow<Resource<Unit>?>(null)
    val createPartyState = _createPartyState.asStateFlow()

    fun createNewRoom(roomName: String) {
        _createPartyState.value = Resource.Loading()
        if (roomName.isBlank()) {
            _createPartyState.value = Resource.Error("El nombre de la sala no puede estar vacío")
            return
        }

        viewModelScope.launch {
            _createPartyState.value = Resource.Loading()
            _createPartyState.value = createPartyUseCase(roomName)
        }
    }

    fun resetState() {
        _createPartyState.value = Resource.Idle()
    }
}