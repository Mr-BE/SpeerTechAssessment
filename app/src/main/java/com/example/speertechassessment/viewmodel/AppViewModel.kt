package com.example.speertechassessment.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.speertechassessment.data.GitHubUser
import com.example.speertechassessment.data.Repository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

sealed class UiState { //cover all possibilities
    object Idle: UiState()
    object Loading: UiState()
    data class Success (val user: GitHubUser): UiState()
    object  NotFound:UiState()
    data class Error(val message: String): UiState() // FIX: offline/rate-limit errors were shown as "not found"
}

// FIX: list screens now get real loading/error state, tagged with the user the list belongs to
data class UserListState(
    val username: String? = null,
    val users: List<GitHubUser> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
)

// OPT: repository is injected (tests pass a fake; screens share one cached instance)
class AppViewModel(private val repository: Repository = Repository.default): ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Idle) //innit (internal)
    val uiState:StateFlow<UiState> = _uiState.asStateFlow() // OPT: read-only view, like the other flows

    private val _followers = MutableStateFlow(UserListState())
    val followers: StateFlow<UserListState> = _followers.asStateFlow()

    private val _following = MutableStateFlow(UserListState())
    val following: StateFlow<UserListState> = _following.asStateFlow()

    // FIX: a new request cancels the one in flight, so a slow older response can't overwrite a newer one
    private var userJob: Job? = null
    private var followersJob: Job? = null
    private var followingJob: Job? = null

    fun getGitHubUser(username: String) {
        val query = username.trim() // FIX: keyboards often append a space, which made real users "not found"
        val current = _uiState.value
        // OPT: skip blank input, and the refetch when this user is already shown (e.g. after Back)
        if (query.isEmpty() || (current is UiState.Success && current.user.login.equals(query, ignoreCase = true))) return

        userJob?.cancel()
        userJob = viewModelScope.launch {
            _uiState.value  = UiState.Loading

            _uiState.value = try {
                UiState.Success(repository.getUser(query))
            } catch (e: CancellationException) {
                throw e // FIX: cancellation used to be caught and turned into an error state
            } catch (e: Exception) {
                // FIX: only a 404 means "no such user"
                if (e is HttpException && e.code() == 404) UiState.NotFound else UiState.Error(e.toUserMessage())
            }
        }
    }

    // FIX: search consumes its result after navigating, so coming Back doesn't re-open the profile (it looped)
    fun clearSearchResult() {
        _uiState.value = UiState.Idle
    }


    fun getFollowers(username: String, refresh: Boolean = false) {
        followersJob = loadUsers(_followers, followersJob, username, refresh, repository::getFollowers)
    }

    fun getFollowing(username: String, refresh: Boolean = false) {
        followingJob = loadUsers(_following, followingJob, username, refresh, repository::getFollowing)
    }

    // OPT: shared by followers/following. Screens call this on every entry, so it skips users already
    // loaded/loading unless it's a pull-to-refresh or the last attempt failed.
    private fun loadUsers(
        state: MutableStateFlow<UserListState>,
        inFlight: Job?,
        username: String,
        refresh: Boolean,
        fetch: suspend (String) -> List<GitHubUser>,
    ): Job? {
        val current = state.value
        if (!refresh && current.username == username && current.error == null) return inFlight

        inFlight?.cancel()
        return viewModelScope.launch {
            state.value = UserListState(
                username = username,
                users = if (current.username == username) current.users else emptyList(), // rows stay while refreshing
                isLoading = true,
            )
            state.value = try {
                val users = fetch(username)
                state.value.copy(users = users, isLoading = false)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                state.value.copy(isLoading = false, error = e.toUserMessage())
            }
        }
    }
}

// FIX: user-facing reason for a failed request (was always "User not found")
private fun Exception.toUserMessage(): String = when {
    this is HttpException && (code() == 403 || code() == 429) -> "GitHub's rate limit was reached. Please try again later."
    this is HttpException -> "GitHub returned an error (${code()}). Please try again."
    this is IOException -> "Network error. Check your connection and try again."
    else -> "Something went wrong. Please try again."
}
