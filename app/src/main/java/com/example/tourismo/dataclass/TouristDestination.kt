package com.example.tourismo.dataclass

data class TouristDestination(
    val nama: String,
    val deskripsi: String,
    val url: String
)

data class TouristDestinationDetails(
    val nama: String,
    val jamBuka: String,
    val lokasi: String,
    val historiSingkat: String,
    val funFact: String,
    val deskripsi: String,
    val url: String,
)

