package com.pura.caspa.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.pura.caspa.domain.usecase.GetWordsToPlayUseCase

class PuraCaspaViewModelFactory(
    private val app: Application,
    val getWordsToPlayUseCase: GetWordsToPlayUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PuraCaspaViewModel(
            app,
            getWordsToPlayUseCase
        ) as T
    }
}