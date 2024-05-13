package com.example.mydiplom.data

data class UserAuth(
    val id: Int,
    val name: String,
    val height: Int,
    val weight: Int,
    val date_birth: String,
    val gender: Int,
    val password:String,
    val login: String,
    val accessToken: String
)
