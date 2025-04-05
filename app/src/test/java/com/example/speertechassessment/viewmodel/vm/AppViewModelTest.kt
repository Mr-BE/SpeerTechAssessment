package com.example.speertechassessment.viewmodel.vm

import com.example.speertechassessment.viewmodel.AppViewModel
import com.example.speertechassessment.viewmodel.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Test
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule


@OptIn(ExperimentalCoroutinesApi::class)
@get:Rule

val dispatcher = UnconfinedTestDispatcher()
class AppViewModelTest {

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }


    @Test
    fun testInitialAppStateIsIdle() {
        val viewModel = AppViewModel()
        kotlin.run {
            assertEquals(UiState.Idle, viewModel.uiState.value)
        }
    }


    @Test
    fun testGetGitHubUserState() = runTest{
        val viewModel = AppViewModel()

        viewModel.getGitHubUser("Mr-BE")

            assert(viewModel.uiState.value !is UiState.Idle)


    }
}