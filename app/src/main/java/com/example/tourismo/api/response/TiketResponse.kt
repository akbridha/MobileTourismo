package com.example.tourismo.api.response

class TiketResponse (
        val maskapai: Airlines,
        val tanggalBeliEkonomi: String,
        val tanggalBeliBisnis: String
    )

    data class Airlines(
        val bisnis: List<Flight>,
        val ekonomi: List<Flight>
    )

    data class Flight(
        val departure: String,
        val arrival: String,
        val day: Int,
        val airlines: String,
        val urlGambar: String
    )