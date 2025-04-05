package com.example.speertechassessment.data

data class UserResponse(
    val users: List<GitHubUser>
)

class Repository (private val api: GitHubApi) {
    suspend fun getUser(username:String): GitHubUser = api.getUser(username)

    suspend fun getFollowers(username: String): List<GitHubUser> = api.getFollowers(username)

    suspend fun getFollowing(username: String): List<GitHubUser> = api.getFollowing(username)
}