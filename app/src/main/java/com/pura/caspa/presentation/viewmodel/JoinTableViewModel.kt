package com.pura.caspa.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pura.caspa.R
import com.pura.caspa.data.util.Resource
import com.pura.caspa.data.util.UiText
import com.pura.caspa.domain.repository.PuraCaspaRepository
import com.pura.caspa.domain.usecase.GetUserNameUseCase
import com.pura.caspa.domain.usecase.JoinPartyUseCase
import com.pura.caspa.domain.usecase.SaveUserNameUseCase
import com.pura.caspa.presentation.util.toUiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class JoinTableViewModel @Inject constructor(
    private val joinPartyUseCase: JoinPartyUseCase,
    private val getUserNameUseCase: GetUserNameUseCase,
    private val saveUserNameUseCase: SaveUserNameUseCase,
    private val repository: PuraCaspaRepository
) : ViewModel() {

    private val _joinState = MutableStateFlow<Resource<Unit>?>(null)
    val joinState = _joinState.asStateFlow()

    private val _getName = MutableStateFlow("")
    val getName: StateFlow<String> = _getName.asStateFlow()

    private val _nameState = MutableStateFlow(repository.getUserName())
    val nameState: StateFlow<String> = _nameState.asStateFlow()

    init {
        getName()
    }

    fun joinToRoom(roomId: String) {
        if (roomId.isBlank()) {
            _joinState.value = Resource.Error(null, UiText.StringResource(R.string.party_cant_be_empty))
            return
        }

        viewModelScope.launch {
            _joinState.value = Resource.Loading()
            val result = joinPartyUseCase(roomId)
            _joinState.value = when(result){
                is Resource.Error -> {
                    val finalMessage = result.partyError?.toUiText(args = roomId)
                        ?: result.message
                        ?: UiText.StringResource(R.string.unknown_error)

                    Resource.Error(message = finalMessage)
                }
                else -> result
            }
        }
    }

    fun resetState() {
        _joinState.value = Resource.Idle()
    }

    fun getName(){
        viewModelScope.launch {
            _getName.value = getUserNameUseCase()
        }
    }

    fun onNameChange(newName: String) {
        _nameState.value = newName
    }

    fun saveName() {
        saveUserNameUseCase(_nameState.value)
    }
}