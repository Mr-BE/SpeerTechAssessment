package com.example.speertechassessment.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.example.speertechassessment.data.AppDimenVal
import com.example.speertechassessment.nav.NavigationItem
import com.example.speertechassessment.ui.effect.shimmerLoading
import com.example.speertechassessment.viewmodel.AppViewModel
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FollowersScreen(
    username:String,
    viewModel: AppViewModel = viewModel(),
    navController: NavController,
//    onUserClicked: (String) -> Unit,
    onBack: () -> Unit
){

    var isLoading by remember { mutableStateOf(true) }




    val followers by viewModel.followers.collectAsState()


    LaunchedEffect(isLoading) { //simulate long load time
        if (isLoading) {
            delay(3000)
            isLoading = false

            viewModel.getFollowers(username)
        }
    }



    PullToRefreshBox(
        modifier = Modifier.fillMaxSize(),
        isRefreshing = isLoading,
        onRefresh = {isLoading = true}
    )  {
       if (isLoading){
           Column(modifier = Modifier.shimmerLoading()) {
               ListItem(

                   headlineContent = { Text("     ", modifier = Modifier.shimmerLoading()) },
                   leadingContent = {
                       AsyncImage(
                           model = "user.avatar_url", contentDescription = null,
                           modifier = Modifier
                               .size(AppDimenVal.L.value)
                               .clip(CircleShape)
                               .shimmerLoading()
                       )
           })
           }
       }
        else{
            Column {
                TopAppBar(title = { Text("Followers of $username") }, navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                })

                LazyColumn {
                    items(followers) { user ->
                        //                UserItem(user, onItemClick = {
                        //                        selectedUser -> viewModel.getGitHubUser(selectedUser.login)
                        //                    navController.navigate(NavigationItem.Profile)
                        //                })

                        ListItem(
                            headlineContent = { Text(user.login) },
                            leadingContent = {
                                AsyncImage(
                                    model = user.avatar_url, contentDescription = null,
                                    modifier = Modifier
                                        .size(AppDimenVal.L.value)
                                        .clip(CircleShape)
                                )
                            },
                            modifier = Modifier.clickable {
                                navController.navigate("${NavigationItem.Profile.route}/${user.login}")

                            }
                        )
                    }
                }
            }
        }
    }
    }

