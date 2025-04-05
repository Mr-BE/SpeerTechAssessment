package com.example.speertechassessment.viewmodel.activity

import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.navigation.compose.rememberNavController
import com.example.speertechassessment.ui.screens.SearchScreen
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class SearchScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testSearchInputAndButton() {
        composeTestRule.setContent {
            SearchScreen(navController = rememberNavController())
        }
        composeTestRule.onNodeWithText("Enter username").performTextInput("Mr-BE")
        composeTestRule.onNodeWithText("Search").assertIsEnabled().performClick()
    }
}