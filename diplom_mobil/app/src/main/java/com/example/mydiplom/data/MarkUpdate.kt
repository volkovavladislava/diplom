package com.example.mydiplom.data

data class MarkUpdate(
    val user_id: Int,
    val kind_of_mark_id: Int,
    val date: String,
    val value_number:Double?,
    val value_string:String?,
    val value_enum:Int?
)
