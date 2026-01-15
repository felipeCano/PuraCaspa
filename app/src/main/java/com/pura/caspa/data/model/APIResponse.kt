package com.pura.caspa.data.model

import com.google.gson.annotations.SerializedName

data class APIResponse (
    @SerializedName("words")
    val words: List<String> = emptyList()
)