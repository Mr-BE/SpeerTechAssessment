package com.example.speertechassessment.ui.screens

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.example.speertechassessment.nav.NavigationItem
import com.example.speertechassessment.ui.effect.shimmerLoading
import com.example.speertechassessment.viewmodel.AppViewModel
import com.example.speertechassessment.viewmodel.UiState

@Composable
fun SearchScreen(
    navController: NavHostController,
                 viewModel: AppViewModel
) {
    var username by remember { mutableStateOf("") }
    val uiState by viewModel.uiState.collectAsState()


    //screen content
    Column(modifier = Modifier.padding(AppDimenVal.R.value)) {
        OutlinedTextField(
            value = username,
            onValueChange = {username = it},
            label = { Text("Enter username") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(AppDimenVal.R.value))

        Button(
            onClick = { viewModel.getGitHubUser(username) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Search",
                style = MaterialTheme.typography.bodyMedium)
        }

        when (val state = uiState) {
            is UiState.Loading -> Column(modifier = Modifier.shimmerLoading()) { //shimmer load
                Column(modifier = Modifier.padding(AppDimenVal.R.value)) {

                    Row(horizontalArrangement = Arrangement.Center) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(AppDimenVal.XL.value))
                                .padding(AppDimenVal.R.value)
                                .background(color = Color.LightGray)
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
                                    model = "user?.avatar_url",
                                    contentDescription = null,
                                )
                                Spacer(modifier = Modifier.height(AppDimenVal.R.value))
                                Text(
                                     "         ",
                                    fontWeight = FontWeight.Bold,
                                    color = Color.LightGray
                                )

                                    Text(
                                        "      ",
                                        color = Color.LightGray
                                    )


                                Spacer(modifier = Modifier.height(AppDimenVal.S.value))

                                Row(horizontalArrangement = Arrangement.Center) {

                                    Column(

                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {//Followers
                                        Text(
                                            "       ",
                                            color = Color.LightGray,
                                            fontWeight = FontWeight.Bold,
                                        )
                                        Text(
                                            "       ",
                                            color = Color.LightGray
                                        )
                                    }

                                    Spacer(modifier = Modifier.width(AppDimenVal.L.value))

                                    Column(horizontalAlignment = Alignment.CenterHorizontally,
                                       ) {//Followers
                                        //Followers
                                        Text(
                                            "     ",
                                            color = Color.LightGray,
                                            fontWeight = FontWeight.Bold,
                                        )
                                        Text(
                                            "      ",
                                            color = Color.LightGray
                                        )
                                    }
                                }

                                Spacer(Modifier.height(AppDimenVal.L.value))

                                Row {
                                    Text(
                                         "                       ",
                                        textAlign = TextAlign.Center,
                                        color = Color.LightGray
                                    )
                                }

                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(AppDimenVal.R.value))


                }
            }
            is UiState.Success -> LaunchedEffect(state) {
                navController.navigate("${NavigationItem.Profile.route}/$username"){
                    launchSingleTop
                }
            }
            is UiState.NotFound -> Text("User not found \uD83D\uDE14", color = MaterialTheme.colorScheme.error)
            else -> {}
        }
    }




}