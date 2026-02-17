package com.pura.caspa.presentation.util

import com.pura.caspa.R
import com.pura.caspa.data.util.PartyError
import com.pura.caspa.data.util.UiText

fun PartyError.toUiText(args: Any? = null): UiText {
    return when(this) {
        PartyError.ALREADY_EXISTS -> UiText.StringResource(R.string.party_already_exist, args ?: "")
        PartyError.FIREBASE_ERROR -> UiText.StringResource(R.string.firebase_error)
        PartyError.WORDS_NOT_FOUND -> UiText.StringResource(R.string.words_not_found)
        PartyError.WITHOUT_WORDS -> UiText.StringResource(R.string.without_words)
        PartyError.ERROR_TO_LISTEN_PARTY -> UiText.StringResource(R.string.error_to_listen_party)
    }
}