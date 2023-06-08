package com.example.tourismo.api.response

data class PostResponse(
    val uid: String,
    val email: String,
    val emailVerified: Boolean,
    val isAnonymous: Boolean,
    val providerData: List<ProviderData>,
    val stsTokenManager: StsTokenManager,
    val createdAt: String,
    val lastLoginAt: String,
    val apiKey: String,
    val appName: String
)

//data class ProviderData(
//    val providerId: String,
//    val uid: String,
//    val displayName: String?,
//    val email: String,
//    val phoneNumber: String?,
//    val photoURL: String?
//)
//
//data class StsTokenManager(
//    val refreshToken: String,
//    val accessToken: String,
//    val expirationTime: Long
//)