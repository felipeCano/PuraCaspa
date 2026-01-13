package com.pura.caspa.presentation.viewmodel

import android.app.Application
import android.net.ConnectivityManager
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import android.content.Context
import android.net.NetworkCapabilities
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import com.pura.caspa.data.model.APIResponse
import com.pura.caspa.data.util.Resource
import com.pura.caspa.domain.usecase.GetWordsToPlayUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PuraCaspaViewModel(
    private val app: Application,
    val getWordsToPlayUseCase: GetWordsToPlayUseCase
) : AndroidViewModel(app) {
    val puraCaspaGetWords: MutableLiveData<Resource<List<APIResponse>>> = MutableLiveData()

    fun getWords() = viewModelScope.launch(Dispatchers.IO) {
        puraCaspaGetWords.postValue(Resource.Loading())
        try {
            if (isNetworkAvailable(app)) {
                val firestoreResult = getWordsToPlayUseCase.execute()
                puraCaspaGetWords.postValue(firestoreResult)
            } else {
                puraCaspaGetWords.postValue(Resource.Error("Internet is not available"))
            }
        } catch (e:Exception){
            puraCaspaGetWords.postValue(Resource.Error(e.message.toString()))
        }
    }

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