package com.pura.caspa.data.model

import com.google.gson.annotations.SerializedName

data class Words(
    @SerializedName("words")
    val words: List<String> = emptyList()
)
