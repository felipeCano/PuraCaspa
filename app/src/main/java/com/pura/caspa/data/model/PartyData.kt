package com.pura.caspa.data.model

data class PartyData(
    val id: String = "",
    val host_id: String = "",
    val stateParty: String = "",
    val integrantes: List<String> = emptyList(),
    val palabra_actual: String = "",
    val amoung_us : String = "",
    val usedWords: List<String> = emptyList()
)