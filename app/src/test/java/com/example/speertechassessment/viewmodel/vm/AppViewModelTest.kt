package com.example.speertechassessment.viewmodel.vm

import com.example.speertechassessment.data.GitHubApi
import com.example.speertechassessment.data.GitHubUser
import com.example.speertechassessment.data.Repository
import com.example.speertechassessment.testutil.httpError
import com.example.speertechassessment.testutil.user
import com.example.speertechassessment.viewmodel.AppViewModel
import com.example.speertechassessment.viewmodel.UiState
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.io.IOException


// FIX: these tests used to call the real GitHub API (slow, flaky, rate limited); they now use a mock
@OptIn(ExperimentalCoroutinesApi::class)
class AppViewModelTest {

    // FIX: was a top-level `@get:Rule val` (not a rule, and shared across test classes)
    private val dispatcher = UnconfinedTestDispatcher()
    private val api = mockk<GitHubApi>()
    private lateinit var viewModel: AppViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
        viewModel = AppViewModel(Repository(api))
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }


    @Test
    fun testInitialAppStateIsIdle() {
        assertEquals(UiState.Idle, viewModel.uiState.value)
    }


    @Test
    fun testGetGitHubUserStateTrimsInput() {
        coEvery { api.getUser("Mr-BE") } returns user("Mr-BE")

        viewModel.getGitHubUser("  Mr-BE ")

        assertEquals(UiState.Success(user("Mr-BE")), viewModel.uiState.value)
    }

    @Test
    fun testBlankSearchIsIgnored() {
        viewModel.getGitHubUser("   ")

        assertEquals(UiState.Idle, viewModel.uiState.value)
        coVerify(exactly = 0) { api.getUser(any()) }
    }

    @Test
    fun test404IsNotFound() {
        coEvery { api.getUser("ghost") } throws httpError(404)

        viewModel.getGitHubUser("ghost")

        assertEquals(UiState.NotFound, viewModel.uiState.value)
    }

    @Test
    fun testNetworkFailureIsErrorNotNotFound() {
        coEvery { api.getUser("octocat") } throws IOException("offline")

        viewModel.getGitHubUser("octocat")

        val state = viewModel.uiState.value
        assertTrue("was $state", state is UiState.Error && "Network" in state.message)
    }

    @Test
    fun testRateLimitIsError() {
        coEvery { api.getUser("octocat") } throws httpError(403)

        viewModel.getGitHubUser("octocat")

        val state = viewModel.uiState.value
        assertTrue("was $state", state is UiState.Error && "rate limit" in state.message)
    }

    @Test
    fun testNewerSearchWinsOverSlowerOlderOne() {
        val slowResponse = CompletableDeferred<GitHubUser>()
        coEvery { api.getUser("slow") } coAnswers { slowResponse.await() }
        coEvery { api.getUser("fast") } returns user("fast")

        viewModel.getGitHubUser("slow")
        viewModel.getGitHubUser("fast")
        slowResponse.complete(user("slow")) // arrives last and must not overwrite "fast"

        assertEquals(UiState.Success(user("fast")), viewModel.uiState.value)
    }

    @Test
    fun testClearSearchResultResetsToIdle() {
        coEvery { api.getUser("octocat") } returns user("octocat")
        viewModel.getGitHubUser("octocat")

        viewModel.clearSearchResult()

        assertEquals(UiState.Idle, viewModel.uiState.value)
    }

    @Test
    fun testFollowersLoadOncePerUserUnlessRefreshed() {
        val followers = listOf(user("a"), user("b"))
        coEvery { api.getFollowers("octocat", any()) } returns followers

        viewModel.getFollowers("octocat")
        viewModel.getFollowers("octocat") // e.g. the screen re-entered after Back
        assertEquals(followers, viewModel.followers.value.users)
        coVerify(exactly = 1) { api.getFollowers("octocat", any()) }

        viewModel.getFollowers("octocat", refresh = true)
        coVerify(exactly = 2) { api.getFollowers("octocat", any()) }
    }

    @Test
    fun testFollowingErrorIsExposedThenRetried() {
        coEvery { api.getFollowing("octocat", any()) } throws IOException("offline")

        viewModel.getFollowing("octocat")
        val failed = viewModel.following.value
        assertTrue("was $failed", !failed.isLoading && failed.error != null && failed.users.isEmpty())

        coEvery { api.getFollowing("octocat", any()) } returns listOf(user("a"))
        viewModel.getFollowing("octocat") // a failed load is retried on the next call

        assertEquals(listOf(user("a")), viewModel.following.value.users)
        assertNull(viewModel.following.value.error)
    }
}
