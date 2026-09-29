package com.example.speertechassessment.testutil

import com.example.speertechassessment.data.GitHubUser
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.HttpException
import retrofit2.Response

fun user(login: String, followers: Int = 0, following: Int = 0) =
    GitHubUser(login, avatarUrl = "", name = "Name of $login", bio = null, followers = followers, following = following)

// What Retrofit throws for a non-2xx response (404 not found, 403 rate limited, ...)
fun httpError(code: Int) =
    HttpException(Response.error<Any>(code, "{}".toResponseBody("application/json".toMediaType())))
