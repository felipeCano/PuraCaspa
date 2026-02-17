package com.pura.caspa.data.util

sealed class Resource<T>(val data: T? = null, val partyError: PartyError? = null, val message: UiText? = null) {
    class Success<T>(data: T) : Resource<T>(data)
    class Loading<T>(data: T? = null) : Resource<T>(data)
    class Error<T>(partyError: PartyError? = null,message: UiText? = null, data: T? = null) : Resource<T>(data, partyError, message)
    class Idle<T> : Resource<T>()
}