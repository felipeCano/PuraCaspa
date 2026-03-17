package com.pura.caspa.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pura.caspa.R
import com.pura.caspa.data.util.Resource
import com.pura.caspa.data.util.UiText
import com.pura.caspa.domain.repository.AdProvider
import com.pura.caspa.domain.usecase.CreatePartyUseCase
import com.pura.caspa.presentation.util.toUiText
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class ConfirmTableCreationViewModel @Inject constructor(
    private val createPartyUseCase: CreatePartyUseCase,
    private val adProvider: AdProvider
) : ViewModel() {
    private val _createPartyState = MutableStateFlow<Resource<Unit>?>(null)
    val createPartyState = _createPartyState.asStateFlow()

    val bannerAdUnitId: String = adProvider.getBannerAdUnitId()

    init {
        adProvider.loadAd()
    }
    fun createNewRoom(roomName: String) {
        _createPartyState.value = Resource.Loading()
        if (roomName.isBlank()) {
            _createPartyState.value = Resource.Error(null, UiText.StringResource(R.string.table_name_not_blank))
            return
        }

        viewModelScope.launch {
            _createPartyState.value = Resource.Loading()
            val result = createPartyUseCase(roomName)
            _createPartyState.value = when(result){
                is Resource.Error -> {
                    val finalMessage = result.partyError?.toUiText(args = roomName)
                        ?: result.message
                        ?: UiText.StringResource(R.string.unknown_error)

                    Resource.Error(message = finalMessage)
                }
                else -> result
            }
        }
    }

    fun resetState() {
        _createPartyState.value = Resource.Idle()
    }
}