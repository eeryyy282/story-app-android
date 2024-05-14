package com.example.storyappjuzairi.data.retrofit

import com.example.storyappjuzairi.data.response.AddNewStoryResponse
import com.example.storyappjuzairi.data.response.DetailStoryResponse
import com.example.storyappjuzairi.data.response.LoginResponse
import com.example.storyappjuzairi.data.response.RegisterResponse
import com.example.storyappjuzairi.data.response.StoryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface ApiService {
    @FormUrlEncoded
    @POST("register")
    suspend fun registerUser(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): RegisterResponse

    @FormUrlEncoded
    @POST("login")
    suspend fun loginUser(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResponse

    @GET("stories")
    suspend fun getStories(): StoryResponse

    @GET("stories/{id}")
    suspend fun getDetailStories(
        @Path("id") id: String
    ): DetailStoryResponse

    @Multipart
    @POST("stories")
    suspend fun addStories(
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
    ): AddNewStoryResponse
}