package com.pura.caspa.presentation.viewmodel

import android.app.Application
import android.net.ConnectivityManager
import androidx.lifecycle.viewModelScope
import android.content.Context
import android.net.NetworkCapabilities
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import com.pura.caspa.data.model.Words
import com.pura.caspa.data.util.Resource
import com.pura.caspa.domain.usecase.GetWordsToPlayUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class PuraCaspaViewModel @Inject constructor(
    private val app: Application,
    val getWordsToPlayUseCase: GetWordsToPlayUseCase
) : AndroidViewModel(app) {
//    private val _state = MutableStateFlow<Resource<APIResponse>>(Resource.Loading())
//    val state: StateFlow<Resource<APIResponse>> = _state.asStateFlow()
//
//    init {
//        getWords()
//    }
//    fun getWords() = viewModelScope.launch(Dispatchers.IO) {
//        _state.value = Resource.Loading()
//        try {
//            if (isNetworkAvailable(app)) {
//                val result = getWordsToPlayUseCase.execute()
//                _state.value = result
//            } else {
//                _state.value = Resource.Error("Internet is not available")
//            }
//        } catch (e:Exception){
//            _state.value = Resource.Error(e.message ?: "Unknown Error")
//        }
//    }

    val state: StateFlow<Resource<Words>> = getWordsToPlayUseCase.execute()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = Resource.Loading()
        )

    private fun isNetworkAvailable(context: Context?): Boolean {
        if (context == null) return false
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?

        if (connectivityManager == null) return false

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork
            val capabilities = connectivityManager.getNetworkCapabilities(network)
            capabilities?.run {
                when {
                    hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                    hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                    hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                    else -> false
                }
            } ?: false
        } else {
            @Suppress("DEPRECATION")
            connectivityManager.activeNetworkInfo?.run {
                isConnected && (type == ConnectivityManager.TYPE_WIFI || type == ConnectivityManager.TYPE_MOBILE)
            } ?: false
        }
    }
}