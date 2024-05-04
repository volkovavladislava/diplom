package com.example.mydiplom.data

data class AddMark(
//    val id: Int,
    val userId: Int,
    val kind_of_mark_id: Int,
    val date: String,
    val value_number:Double?,
    val value_string:String?,
    val value_enum:Int?

)
