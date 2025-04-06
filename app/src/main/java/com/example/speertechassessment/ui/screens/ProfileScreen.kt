package com.example.speertechassessment.ui.screens

import android.content.res.Resources.Theme
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import com.example.speertechassessment.data.AppDimenVal
import com.example.speertechassessment.data.GitHubUser
import com.example.speertechassessment.nav.NavigationItem
import com.example.speertechassessment.viewmodel.AppViewModel
import com.example.speertechassessment.viewmodel.UiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(username:String,navController:NavHostController){

val vm: AppViewModel = viewModel()
    LaunchedEffect  (username){
        vm.getGitHubUser(username)
    }

    val state by vm.uiState.collectAsState()


    val user = if (state is UiState.Success) {
        (state as UiState.Success).user
    } else null

    Column {
        TopAppBar(title = { Text("${user?.login}")}, navigationIcon = {
            IconButton(onClick = { navController.navigateUp() }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }
        })
        Column(modifier = Modifier.padding(AppDimenVal.R.value)) {

            Row(horizontalArrangement = Arrangement.Center) {
                Card(
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                    )

                ) {
                    Column(
                        modifier = Modifier.padding(
                            horizontal = AppDimenVal.XL.value,
                            vertical = AppDimenVal.R.value
                        ),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        AsyncImage(
                            modifier = Modifier.clip(CircleShape),
                            model = user?.avatar_url,
                            contentDescription = null,
                        )
                        Spacer(modifier = Modifier.height(AppDimenVal.R.value))
                        if (user != null) {
                            user.name?.let {
                                Text(it, style = MaterialTheme.typography.bodyLarge.copy(color = Color.Black, fontWeight = FontWeight.Bold))
                            }
                        }
                        if (user != null) {
                            Text("@${user.login}", style = MaterialTheme.typography.bodyMedium.copy(color = Color.Black))

                        Spacer(modifier = Modifier.height(AppDimenVal.R.value))

                        Row(horizontalArrangement = Arrangement.Center) { //Followers and following row

                            Column(
                                modifier = Modifier.clickable {
                                    navController.navigate("${NavigationItem.Followers.route}/${user?.login}")
                                },
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {//Followers
                                Text(
                                    "${user.followers}",
                                    style = MaterialTheme.typography.bodyLarge.copy(color = Color.Black, fontWeight = FontWeight.Bold)
                                )
                                Text(
                                    "Followers",
                                    style = MaterialTheme.typography.bodyMedium.copy(color = Color.Black))

                            }

                            Spacer(modifier = Modifier.width(AppDimenVal.XL.value))

                            Column(horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.clickable {
                                    navController.navigate("${NavigationItem.Following.route}/${user?.login}")
                                },) {//Followers
                                //Followers
                                Text(
                                    "${user?.following}",
                                    style = MaterialTheme.typography.bodyLarge.copy(color = Color.Black, fontWeight = FontWeight.Bold)

                                )
                                Text(
                                    "Following",
                                    style = MaterialTheme.typography.bodyMedium.copy(color = Color.Black)
                                )
                            }
                        }

                        Spacer(Modifier.height(AppDimenVal.L.value))

                        Row {
                            Text(
                                user.bio ?: "No Bio Available",
                                style = MaterialTheme.typography.bodyMedium.copy(color = Color.Black))
                        }

                    }
                }
            }

            Spacer(modifier = Modifier.height(AppDimenVal.R.value))


        }
    }

}}