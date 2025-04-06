package com.example.speertechassessment.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.example.speertechassessment.data.AppDimenVal
import com.example.speertechassessment.nav.NavigationItem
import com.example.speertechassessment.viewmodel.AppViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FollowingScreen(
    username:String,
    viewModel: AppViewModel = viewModel(),
    navController: NavController,
//    onUserClicked: (String) -> Unit,
    onBack: () -> Unit
){


    val following by viewModel.following.collectAsState()


    LaunchedEffect(username) {
        viewModel.getFollowing(username)
    }


    Column{
        TopAppBar(title = { Text("$username's Following") }, navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }
        })

        LazyColumn {
            items(following) { user ->

                ListItem(
                    headlineContent = { Text(user.login) },
                    leadingContent = {
                        AsyncImage(model = user.avatar_url, contentDescription = null,
                            modifier = Modifier
                                .size(AppDimenVal.L.value)
                                .clip(CircleShape))
                    },
                    modifier = Modifier.clickable {
                        navController.navigate("${NavigationItem.Profile.route}/${user.login}")

                    },
                    shadowElevation = AppDimenVal.XS.value
                )
            }
        }
    }
}

