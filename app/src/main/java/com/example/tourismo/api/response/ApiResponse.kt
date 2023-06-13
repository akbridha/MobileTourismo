package com.example.tourismo.api.response

data class ApiResponse(


    val uid: String,
    val email: String,
    val emailVerified: Boolean,
    val isAnonymous: Boolean,
    val providerData: List<ProviderData>,
    val stsTokenManager: StsTokenManager,
    val createdAt: String,
    val lastLoginAt: String,
    val apiKey: String,
    val appName: String,
    val errorCode : String
)
//data class ErrorResponse(
//    val error: String
//tidak terpakai karena API tidak main di response code
//)
