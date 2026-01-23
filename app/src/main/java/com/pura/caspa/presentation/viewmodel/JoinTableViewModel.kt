package com.pura.caspa.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pura.caspa.data.util.Resource
import com.pura.caspa.domain.usecase.JoinPartyUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class JoinTableViewModel @Inject constructor(
    private val joinPartyUseCase: JoinPartyUseCase
) : ViewModel() {

    private val _joinState = MutableStateFlow<Resource<Unit>>(Resource.Loading())
    val joinState = _joinState.asStateFlow()

    fun joinToRoom(roomId: String) {
        if (roomId.isBlank()) {
            _joinState.value = Resource.Error("Debes ingresar un código de sala")
            return
        }

        viewModelScope.launch {
            _joinState.value = Resource.Loading()
            _joinState.value = joinPartyUseCase(roomId)
        }
    }
}