package com.example.speertechassessment.viewmodel.repo

import com.example.speertechassessment.data.GitHubApi
import com.example.speertechassessment.data.GitHubUser
import com.example.speertechassessment.data.Repository
import com.example.speertechassessment.testutil.httpError
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.time.Duration.Companion.seconds
import kotlin.time.TestTimeSource

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

    // NEW: cache behaviour
    @Test
    fun testGetUserIsCachedCaseInsensitively() = runTest {
        coEvery { mockApi.getUser(any()) } returns GitHubUser("octocat", "avi", null, null, 1, 1)

        repo.getUser("Octocat")
        repo.getUser("octocat")

        coVerify(exactly = 1) { mockApi.getUser(any()) }
    }

    @Test
    fun testCachedUserExpires() = runTest {
        val time = TestTimeSource()
        val repo = Repository(mockApi, time)
        coEvery { mockApi.getUser("octocat") } returns GitHubUser("octocat", "avi", null, null, 1, 1)

        repo.getUser("octocat")
        time += 61.seconds
        repo.getUser("octocat")

        coVerify(exactly = 2) { mockApi.getUser("octocat") }
    }

    @Test
    fun testFailedLookupsAreNotCached() = runTest {
        coEvery { mockApi.getUser("ghost") } throws httpError(404)

        repeat(2) { runCatching { repo.getUser("ghost") } }

        coVerify(exactly = 2) { mockApi.getUser("ghost") }
    }
}
