package com.pura.caspa.data.model

data class PartyData(
    val id: String = "",
    val host_id: String = "",
    val stateParty: String = "",
    val integrantes: List<Player> = emptyList(),
    val palabra_actual: String = "",
    val amoung_us : String = "",
    val usedWords: List<String> = emptyList(),
    val showImpostor: Boolean = false,
    val votos_en_esta_ronda : Int = 0
)