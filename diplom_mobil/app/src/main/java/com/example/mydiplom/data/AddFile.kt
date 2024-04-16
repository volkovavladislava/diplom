package com.example.mydiplom.data

import okhttp3.MultipartBody
import java.io.File

data class AddFile (
//    val userId: Int,
//    val link: String,
    val mime_type: String,
    val date: String,
    val name: String,
    val comment: String
)