package com.pura.caspa.data.model

data class PartyData(
    val id: String = "",
    val host_id: String = "",
    val estado: String = "esperando", // "esperando" o "jugando"
    val integrantes: List<String> = emptyList(),
    val palabra_actual: String = "",
    val amoung_us : String = ""
)