package com.example.speertechassessment.data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path


//Holds user data
data class GitHubUser(
    val login: String,
    val avatar_url: String,
    val name: String?,
    val bio: String?,
    val followers: Int,
    val following: Int

)


//API interface with retrofit
interface GitHubApi{

    @GET("users/{username}")    //return Github user object from query
    suspend fun getUser(@Path("username") username:String) : GitHubUser

    @GET("users/{username}/followers")    //return list of Github followers users object from query
    suspend fun getFollowers(@Path("username") username:String) : List<GitHubUser>

    @GET("users/{username}/following")    //return list of Github followers users object from query
    suspend fun getFollowing(@Path("username") username:String) : List<GitHubUser>

}


/* Retrofit Setup */

object RetrofitInstance {
    val api: GitHubApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GitHubApi::class.java)
    }
}