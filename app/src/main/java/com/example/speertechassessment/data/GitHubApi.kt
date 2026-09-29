package com.example.speertechassessment.data

import com.google.gson.annotations.SerializedName
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


//Holds user data
// Note: followers/following endpoints return a shorter user object (name/bio null, counts 0 there)
data class GitHubUser(
    val login: String,
    @SerializedName("avatar_url") val avatarUrl: String, // OPT: idiomatic name; explicit JSON key survives R8
    val name: String?,
    val bio: String?,
    val followers: Int,
    val following: Int

)

// OPT: GitHub avatars accept an `s` (size in px) param; fetch what we draw instead of the ~460px default
fun GitHubUser.sizedAvatarUrl(sizePx: Int): String =
    avatarUrl + (if ('?' in avatarUrl) "&" else "?") + "s=$sizePx"


//API interface with retrofit
interface GitHubApi{

    @GET("users/{username}")    //return Github user object from query
    suspend fun getUser(@Path("username") username:String) : GitHubUser

    // OPT: per_page=100 (GitHub's max); the default of 30 hid most followers. Full paging is a follow-up
    @GET("users/{username}/followers")    //return list of Github followers users object from query
    suspend fun getFollowers(@Path("username") username:String, @Query("per_page") perPage: Int = 100) : List<GitHubUser>

    @GET("users/{username}/following")    //return list of Github following users object from query
    suspend fun getFollowing(@Path("username") username:String, @Query("per_page") perPage: Int = 100) : List<GitHubUser>

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
