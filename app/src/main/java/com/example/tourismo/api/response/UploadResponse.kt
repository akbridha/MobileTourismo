package com.example.tourismo.api.response

data class UploadResponse (
    val Lokasi: String,
    val error: String? = null
        )

data class ErrorResponse(
    val error: String
    //tidak terpakai karena API tidak main di response code
)