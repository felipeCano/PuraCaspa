package com.pura.caspa.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.pura.caspa.domain.repository.AdProvider
import com.pura.caspa.domain.repository.PuraCaspaRepository
import com.pura.caspa.domain.usecase.SaveUserNameUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class UserNameViewModel @Inject constructor(
    private val saveUserNameUseCase: SaveUserNameUseCase,
    private val repository: PuraCaspaRepository,
    private val adProvider: AdProvider
) : ViewModel() {
    private val _nameState = MutableStateFlow(repository.getUserName())
    val nameState: StateFlow<String> = _nameState.asStateFlow()

    val bannerAdUnitId: String = adProvider.getBannerAdUnitId()

    init {
        adProvider.loadAd()
    }

    fun onNameChange(newName: String) {
        _nameState.value = newName
    }

    fun saveName() {
        saveUserNameUseCase(_nameState.value)
    }
}