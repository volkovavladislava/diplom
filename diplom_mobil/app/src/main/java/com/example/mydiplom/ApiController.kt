package com.example.mydiplom


import com.example.mydiplom.data.AddFavoriteKindOfMark
import com.example.mydiplom.data.AddHandMadeKindOfMark
import com.example.mydiplom.data.AddMark
import com.example.mydiplom.data.AddMarkDavlenie
import com.example.mydiplom.data.AddPrompt
import com.example.mydiplom.data.Blog
import com.example.mydiplom.data.File
import com.example.mydiplom.data.KindOfMark
import com.example.mydiplom.data.KindOfMarkValues
import com.example.mydiplom.data.Mark
import com.example.mydiplom.data.MarkDeleteDavlenie
import com.example.mydiplom.data.MarkUpdate
import com.example.mydiplom.data.MarkUpdateDavlenie
import com.example.mydiplom.data.Prompt
import com.example.mydiplom.data.PromptUpdate
import com.example.mydiplom.data.UpdateOperatingValue
import com.example.mydiplom.data.User
import com.example.mydiplom.data.UserOperatingValue
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

    @GET("/api/listKindOfMarkOfSystem")
    fun getListKindOfMarkOfSystem(): Call<List<KindOfMark>>

    @GET("/api/listKindOfMarkOfHandMade/{userId}")
    fun getListKindOfMarkOfHandMade(@Path("userId") userId: Int): Call<List<KindOfMark>>

    @PUT("/api/addHandMadeKindOfMark/{userId}")
    fun addHandMadeKindOfMark(@Path("userId") userId: Int, @Body addHandMadeKindOfMark: AddHandMadeKindOfMark): Call<Void>

    @PUT("/api/addFavoriteKindOfMark")
    fun addFavoriteKindOfMark( @Body addFavoriteKindOfMark: AddFavoriteKindOfMark): Call<Void>

    @POST("/api/deleteFavoriteKindOfMark")
    fun deleteFavoriteKindOfMark( @Body addFavoriteKindOfMark: AddFavoriteKindOfMark): Call<Void>





    @PUT("/api/updateUser/{userId}")
    fun updateUser(@Path("userId") userId: Int, @Body userUpdate: UserUpdate): Call<Void> //Call<Void>

    @GET("/api/userOpertingValue/{userId}")
    fun getUserOpertingValue(@Path("userId") userId: Int): Call<List<UserOperatingValue>>

    @PUT("/api/updateUserOperatingValue/{userId}")
    fun updateUserOperatingValue(@Path("userId") userId: Int, @Body userOperatingValueUpdate: UpdateOperatingValue): Call<Void> //Call<Void>

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







    @PUT("/api/addMark/{kindOfMarkId}")
    fun addMark(@Path("kindOfMarkId") kindOfMarkId: Int, @Body addMark: AddMark): Call<Void> //Call<Void>

    @PUT("/api/addMarkD")
    fun addMarkDavlenie( @Body addMark: AddMarkDavlenie): Call<Void> //Call<Void>

    @GET("/api/kindOfMarkValues/{kindOfMarkId}")
    fun getKindOfMarkValues(@Path("kindOfMarkId") kindOfMarkId: Int): Call<List<KindOfMarkValues>>


    @GET("/api/marksForUser/userId={userId}/kindOfMarkId={kindOfMarkId}")
    fun marksForUser(@Path("userId") userId: Int, @Path("kindOfMarkId") kindOfMarkId: Int): Call<List<Mark>>


    @GET("/api/marksForUserByDate/userId={userId}/kindOfMarkId={kindOfMarkId}/date1={date1}/date2={date2}")
    fun marksForUserByDate(@Path("userId") userId: Int, @Path("kindOfMarkId") kindOfMarkId: Int, @Path("date1") date1: String, @Path("date2") date2: String): Call<List<Mark>>

    @GET("/api/markById/{markId}")
    fun getMarkById(@Path("markId") markId: Int): Call<Mark>

    @POST("/api/deleteMark/{markId}")
    fun deleteMark(@Path("markId") markId: Int): Call<Void> //Call<Void>

    @PUT("/api/updateMark/{markId}")
    fun updateMark(@Path("markId") markId: Int, @Body markUpdate: MarkUpdate): Call<Void> //Call<Void>

    @PUT("/api/updateMarkDavlenie")
    fun updateMarkDavlenie( @Body markUpdate: MarkUpdateDavlenie): Call<Void> //Call<Void>

    @POST("/api/deleteMarkDavlenie")
    fun deleteMarkDavlenie(@Body markUpdate: MarkDeleteDavlenie): Call<Void>













}
