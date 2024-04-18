package com.example.mydiplom


import com.example.mydiplom.data.AddFile
import com.example.mydiplom.data.AddMark
import com.example.mydiplom.data.AddPrompt
import com.example.mydiplom.data.Blog
import com.example.mydiplom.data.File
import com.example.mydiplom.data.KindOfMark
import com.example.mydiplom.data.KindOfMarkValues
import com.example.mydiplom.data.Mark
import com.example.mydiplom.data.Prompt
import com.example.mydiplom.data.PromptUpdate
import com.example.mydiplom.data.User
import com.example.mydiplom.data.UserUpdate
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
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

    @GET("/api/promptByUser/{userId}")
    fun getPrompts(@Path("userId") userId: Int): Call<List<Prompt>>

    @GET("/api/promptById/{promptId}")
    fun getPromptById(@Path("promptId") promptId: Int): Call<Prompt>

    @PUT("/api/updatePrompt/{promptId}")
    fun updatePrompt(@Path("promptId") promptId: Int, @Body promptUpdate: PromptUpdate): Call<Void> //Call<Void>

    @POST("/api/deletePrompt/{promptId}")
    fun deletePrompt(@Path("promptId") promptId: Int): Call<Void> //Call<Void>





    @PUT("/api/addMark/{kindOfMarkId}")
    fun addMark(@Path("kindOfMarkId") kindOfMarkId: Int, @Body addMark: AddMark): Call<Void> //Call<Void>
    @GET("/api/kindOfMarkValues/{kindOfMarkId}")
    fun getKindOfMarkValues(@Path("kindOfMarkId") kindOfMarkId: Int): Call<List<KindOfMarkValues>>




    @GET("/api/fileByUser/{userId}")
    fun getFiles(@Path("userId") userId: Int): Call<List<File>>

    @Multipart
    @POST("/api/addFile/{userId}")
//    fun addFile(@Path("userId") userId: Int, @Part file: MultipartBody.Part, @Part("mime_type") fileType: String, @Part("date") date: String, @Part("name") name: String, @Part("comment") comment: String): Call<Void>
    fun addFile(@Path("userId") userId: Int,
                @Part file: MultipartBody.Part,
                @Part("mime_type") fileType: RequestBody,
                @Part("date") date: RequestBody,
                @Part("name") name: RequestBody,
                @Part("comment") comment: RequestBody): Call<Void>

//    @PUT("/api/addFile/{userId}")
//    fun addFile(@Path("userId") userId: Int, @Body addFile: AddFile): Call<Void> //Call<Void>


    @GET("/api/fileById/{fileId}")
    fun getFileById(@Path("fileId") fileId: Int): Call<File>

    @Multipart
    @POST("/api/updateFile/{fileId}")
    fun updateFile(
        @Path("fileId") fileId: Int,
        @Part file: MultipartBody.Part,
        @Part("mime_type") fileType: RequestBody,
        @Part("date") date: RequestBody,
        @Part("name") name: RequestBody,
        @Part("comment") comment: RequestBody): Call<Void>

    @POST("/api/deleteFile/{fileId}")
    fun deleteFile(@Path("fileId") fileId: Int): Call<Void> //Call<Void>




    @GET("/api/marksForUser/userId={userId}/kindOfMarkId={kindOfMarkId}")
    fun marksForUser(@Path("userId") userId: Int, @Path("kindOfMarkId") kindOfMarkId: Int): Call<List<Mark>>
}
