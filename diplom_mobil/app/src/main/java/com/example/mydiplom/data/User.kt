package com.example.mydiplom.data

import java.time.LocalDate

data class User(
    val userId: Int,
    val name: String,
    val height: Int,
    val weight: Int,
    val date_birth: String,
    val gender: Int,
    val password: String
)
