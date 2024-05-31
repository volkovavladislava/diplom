package com.example.mydiplom.data

data class MarkDavlenie(
    val id1: Int,
    val id2: Int,
    val user_id: Int,
    val kind_of_mark_id: Int,
    val date: String,
    val situation: Int?,
    val value_number1:Double?,
    val value_number2:Double?,
    val value_string:String?,
    val value_enum:Int?,
    val value: String?
)
