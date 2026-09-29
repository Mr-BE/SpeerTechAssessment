package com.example.speertechassessment.data

import kotlin.time.Duration.Companion.seconds
import kotlin.time.TimeMark
import kotlin.time.TimeSource

// OPT: removed the unused `UserResponse` wrapper

class Repository (
    private val api: GitHubApi,
    private val timeSource: TimeSource = TimeSource.Monotonic, // injectable so tests can move time
) {
    // OPT: the profile screen asks for the user the search just fetched; caching briefly saves that request
    // (GitHub allows 60/hour unauthenticated) and a second loading skeleton. Keyed by lowercase login.
    private val userCache = object : LinkedHashMap<String, CachedUser>(16, 0.75f, true) {
        override fun removeEldestEntry(eldest: MutableMap.MutableEntry<String, CachedUser>?) =
            size > MAX_CACHED_USERS
    }

    suspend fun getUser(username:String): GitHubUser {
        val key = username.lowercase() // GitHub logins are case-insensitive
        synchronized(userCache) { userCache[key] }
            ?.takeIf { it.cachedAt.elapsedNow() < CACHE_TTL }
            ?.let { return it.user }
        return api.getUser(username).also { user ->
            synchronized(userCache) { userCache[key] = CachedUser(user, timeSource.markNow()) }
        }
    }

    suspend fun getFollowers(username: String): List<GitHubUser> = api.getFollowers(username)

    suspend fun getFollowing(username: String): List<GitHubUser> = api.getFollowing(username)

    private class CachedUser(val user: GitHubUser, val cachedAt: TimeMark)

    companion object {
        private const val MAX_CACHED_USERS = 50
        private val CACHE_TTL = 60.seconds // same freshness window as GitHub's Cache-Control

        // OPT: one shared instance so every screen uses the same cache (was re-created on every call)
        val default: Repository by lazy { Repository(RetrofitInstance.api) }
    }
}
