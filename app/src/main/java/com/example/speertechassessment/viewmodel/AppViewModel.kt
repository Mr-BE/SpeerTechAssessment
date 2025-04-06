package com.example.speertechassessment.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.speertechassessment.data.GitHubUser
import com.example.speertechassessment.data.Repository
import com.example.speertechassessment.data.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class UiState { //cover all possibilities
    object Idle: UiState()
    object Loading: UiState()
    data class Success (val user: GitHubUser): UiState()
    object  NotFound:UiState()
}

class AppViewModel: ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Idle) //innit (internal)
    val uiState:StateFlow<UiState> = _uiState

    private val _followers = MutableStateFlow<List<GitHubUser>>(emptyList())
    val followers: StateFlow<List<GitHubUser>> = _followers.asStateFlow()

    private val _following = MutableStateFlow<List<GitHubUser>>(emptyList())
    val following: StateFlow<List<GitHubUser>> = _following.asStateFlow()

    fun getGitHubUser(username: String) {
        viewModelScope.launch {
            _uiState.value  = UiState.Loading

            try {
                val user = Repository(RetrofitInstance.api).getUser(username)


                _uiState.value = UiState.Success(user)
            } catch (e:Exception){
                _uiState.value = UiState.NotFound
            }
        }
    }


    fun getFollowers(username: String) {
        viewModelScope.launch {
            try {
                _followers.value = Repository(RetrofitInstance.api).getFollowers(username)
            } catch (e: Exception) {
                _followers.value = emptyList()
            }
        }
    }

    fun getFollowing(username: String) {
        viewModelScope.launch {
            try {
                _following.value = Repository(RetrofitInstance.api).getFollowing(username)
            } catch (e: Exception) {
                _following.value = emptyList()
            }
        }
    }
}