package com.example.speertechassessment.viewmodel.activity

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.navigation.compose.rememberNavController
import com.example.speertechassessment.data.GitHubApi
import com.example.speertechassessment.data.GitHubUser
import com.example.speertechassessment.data.Repository
import com.example.speertechassessment.nav.AppNavHost
import com.example.speertechassessment.testutil.httpError
import com.example.speertechassessment.testutil.user
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.io.IOException

@RunWith(RobolectricTestRunner::class)
class SearchScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    // FIX: the old test didn't compile (SearchScreen needs a ViewModel) and would have hit the real API.
    // Fake API: one known user, "offline" fails like a dropped connection, everyone else is a 404.
    private val fakeApi = object : GitHubApi {
        override suspend fun getUser(username: String): GitHubUser = when (username) {
            "Mr-BE" -> user("Mr-BE")
            "offline" -> throw IOException("offline")
            else -> throw httpError(404)
        }
        override suspend fun getFollowers(username: String, perPage: Int) = emptyList<GitHubUser>()
        override suspend fun getFollowing(username: String, perPage: Int) = emptyList<GitHubUser>()
    }

    private fun launchApp() {
        composeTestRule.setContent {
            AppNavHost(navController = rememberNavController(), repository = Repository(fakeApi))
        }
    }

    private fun search(username: String) {
        composeTestRule.onNodeWithText("Enter username").performTextInput(username)
        composeTestRule.onNodeWithText("Search").assertIsEnabled().performClick()
    }

    @Test
    fun testSearchButtonDisabledUntilInput() {
        launchApp()
        composeTestRule.onNodeWithText("Search").assertIsNotEnabled()
    }

    // Regression: Back from a found profile used to land on the error screen with no way out
    @Test
    fun testBackFromProfileReturnsToSearch() {
        launchApp()
        search("Mr-BE")
        composeTestRule.onNodeWithText("@Mr-BE").assertIsDisplayed()

        composeTestRule.onNodeWithContentDescription("Back").performClick()

        composeTestRule.onNodeWithText("Search").assertIsDisplayed()
        composeTestRule.onNodeWithText("@Mr-BE").assertDoesNotExist()
        composeTestRule.onNodeWithText("Sorry", substring = true).assertDoesNotExist()
    }

    @Test
    fun testUnknownUserShowsNotFoundInline() {
        launchApp()
        search("nobody")
        composeTestRule.onNodeWithText("User not found", substring = true).assertIsDisplayed()
    }

    @Test
    fun testNetworkErrorIsNotReportedAsNotFound() {
        launchApp()
        search("offline")
        composeTestRule.onNodeWithText("Network error", substring = true).assertIsDisplayed()
        composeTestRule.onNodeWithText("User not found", substring = true).assertDoesNotExist()
    }
}
