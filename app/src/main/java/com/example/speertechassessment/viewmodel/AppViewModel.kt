package com.example.speertechassessment.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.speertechassessment.data.GitHubUser
import com.example.speertechassessment.data.Repository
import com.example.speertechassessment.data.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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

    fun getGitHubUser(username: String) {
        viewModelScope.launch {
            _uiState.value  = UiState.Loading

            try {
                val user = Repository(RetrofitInstance.api).getUser(username)
                Log.d("ViewModel", "retrieved user is ${user}")
                _uiState.value = UiState.Success(user)
            } catch (e:Exception){
                _uiState.value = UiState.NotFound
            }
        }
    }
}