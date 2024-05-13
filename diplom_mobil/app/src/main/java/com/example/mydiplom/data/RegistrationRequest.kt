package com.example.mydiplom.data

data class RegistrationRequest(
    val login: String,
    val password: String,
    val name : String,
    val height: Int,
    val weight: Int,
    val date_birth : String,
    val gender: Int,
)
