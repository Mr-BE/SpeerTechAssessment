package com.example.speertechassessment.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.speertechassessment.data.AppDimenVal
import com.example.speertechassessment.data.GitHubUser
import com.example.speertechassessment.data.sizedAvatarUrl
import com.example.speertechassessment.ui.effect.shimmerLoading
import com.example.speertechassessment.viewmodel.UserListState

// OPT: shared by FollowersScreen/FollowingScreen, which were near-copies (Following had no
// loading, refresh, empty or error states at all).
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserListScreen(
    title: String,
    username: String,
    state: UserListState,
    emptyMessage: String,
    onRefresh: () -> Unit,
    onUserClick: (GitHubUser) -> Unit,
    onBack: () -> Unit,
) {
    // Nothing to show until the first request for this user has finished
    val isInitialLoad = state.username != username || (state.isLoading && state.users.isEmpty())

    Column {
        // FIX: top bar stays visible while loading (it used to be hidden), so Back is always reachable
        TopAppBar(title = { Text(title) }, navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }
        })

        PullToRefreshBox(
            modifier = Modifier.fillMaxSize(),
            isRefreshing = state.isLoading && !isInitialLoad, // FIX: tied to the real request now
            onRefresh = onRefresh,
        ) {
            when {
                isInitialLoad -> SkeletonUserList()
                state.users.isEmpty() && state.error != null -> ListMessage(state.error, onRetry = onRefresh)
                state.users.isEmpty() -> ListMessage(emptyMessage)
                else -> LazyColumn(modifier = Modifier.fillMaxSize()) {
                    state.error?.let { error -> // a failed refresh keeps the old rows and says why
                        item { Text(error, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(AppDimenVal.R.value)) }
                    }
                    // OPT: stable keys let Compose reuse rows instead of recomposing the whole list
                    items(state.users, key = { it.login }) { user ->
                        UserRow(user, onClick = { onUserClick(user) })
                    }
                }
            }
        }
    }
}

@Composable
private fun UserRow(user: GitHubUser, onClick: () -> Unit) {
    val avatarPx = with(LocalDensity.current) { AppDimenVal.L.value.roundToPx() }
    ListItem(
        headlineContent = { Text(user.login) },
        leadingContent = {
            AsyncImage(
                model = user.sizedAvatarUrl(avatarPx), // OPT: 32dp rows no longer download ~460px avatars
                contentDescription = null,
                modifier = Modifier
                    .size(AppDimenVal.L.value)
                    .clip(CircleShape)
            )
        },
        modifier = Modifier.clickable(onClick = onClick),
        shadowElevation = AppDimenVal.XS.value
    )
}

// FIX: skeleton rows instead of one row whose avatar tried to load the literal string "user.avatar_url"
@Composable
private fun SkeletonUserList() {
    Column {
        repeat(8) {
            ListItem(
                headlineContent = {
                    Box(Modifier.size(width = 140.dp, height = 16.dp).clip(RoundedCornerShape(AppDimenVal.XS.value)).shimmerLoading())
                },
                leadingContent = {
                    Box(Modifier.size(AppDimenVal.L.value).clip(CircleShape).shimmerLoading())
                },
            )
        }
    }
}

// Empty/error message; a LazyColumn so pull-to-refresh still works when there are no rows
@Composable
private fun ListMessage(text: String, onRetry: (() -> Unit)? = null) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        item {
            Column(
                modifier = Modifier
                    .fillParentMaxSize()
                    .padding(AppDimenVal.L.value),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text, textAlign = TextAlign.Center, style = MaterialTheme.typography.bodyLarge)
                if (onRetry != null) {
                    Spacer(Modifier.height(AppDimenVal.R.value))
                    OutlinedButton(onClick = onRetry) { Text("Retry") }
                }
            }
        }
    }
}
