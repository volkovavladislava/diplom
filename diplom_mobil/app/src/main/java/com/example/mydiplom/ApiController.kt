package com.example.mydiplom


import com.example.mydiplom.data.AddPrompt
import com.example.mydiplom.data.Blog
import com.example.mydiplom.data.KindOfMark
import com.example.mydiplom.data.Prompt
import com.example.mydiplom.data.User
import com.example.mydiplom.data.UserUpdate
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiController {

    @GET("/api/listKindOfMark")
    fun getListKindOfMark(): Call<List<KindOfMark>>







    @PUT("/api/updateUser/{userId}")
    fun updateUser(@Path("userId") userId: Int, @Body userUpdate: UserUpdate): Call<Void> //Call<Void>

    @GET("/api/user/{userId}")
    fun getUser(@Path("userId") userId: Int): Call<User>






    @GET("/api/listBlog")
    fun getlistBlog(): Call<List<Blog>>

    @GET("/api/blog/{blogId}")
    fun getBlog(@Path("blogId") blogId: Int): Call<Blog>




    @PUT("/api/addPrompt/{userId}")
    fun addPrompt(@Path("userId") userId: Int, @Body addPrompt: AddPrompt): Call<Void> //Call<Void>

    @GET("/api/prompt/{userId}")
    fun getPrompt(@Path("userId") userId: Int): Call<List<Prompt>>
}