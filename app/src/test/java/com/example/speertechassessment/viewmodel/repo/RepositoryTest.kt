package com.example.speertechassessment.viewmodel.repo

import com.example.speertechassessment.data.GitHubApi
import com.example.speertechassessment.data.GitHubUser
import com.example.speertechassessment.data.Repository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertEquals

@OptIn (ExperimentalCoroutinesApi::class)
class RepositoryTest {

    private val mockApi = mockk<GitHubApi>()
    private val repo = Repository(mockApi)

    @Test
    fun testGetUserReturnsCorrectResponse() = runTest {
        val user = GitHubUser("login","avi","name", "bio",  10, 5)
        coEvery { mockApi.getUser("name") } returns user

        val result = repo.getUser("name")
        assertEquals(user, result)
    }

    @Test
    fun testGetFollowerReturnsList() = runTest {
        val list = listOf(GitHubUser("", "avatar", "follower1", "", 0, 1))
        coEvery { mockApi.getFollowers("name") } returns list

        val result = repo.getFollowers("name")
        assertEquals(list,result)
    }

    @Test
    fun testGetFollowingReturnsList() = runTest {
        val list = listOf(GitHubUser("", "avatar", "following1", "", 0, 1))
        coEvery { mockApi.getFollowing("name") } returns list

        val result = repo.getFollowing("name")
        assertEquals(list,result)

}
}