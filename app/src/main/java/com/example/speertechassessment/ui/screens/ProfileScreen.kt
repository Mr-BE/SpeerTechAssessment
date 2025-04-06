package com.example.speertechassessment.ui.screens

import android.content.res.Resources.Theme
import androidx.compose.foundation.background
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
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
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
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import com.example.speertechassessment.data.AppDimenVal
import com.example.speertechassessment.nav.NavigationItem
import com.example.speertechassessment.viewmodel.AppViewModel
import com.example.speertechassessment.viewmodel.UiState

@Composable
fun ProfileScreen(navController:NavHostController, viewModel: AppViewModel){

    val state by viewModel.uiState.collectAsState()

    val user = if (state is UiState.Success) {
        (state as UiState.Success).user
    } else null

    Column(modifier = Modifier.padding(AppDimenVal.R.value)) {
        Row (horizontalArrangement = Arrangement.Center){
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(AppDimenVal.XL.value))
                    .padding(AppDimenVal.R.value)
                    .background(color = Color.Blue)
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = AppDimenVal.XL.value,
                        vertical = AppDimenVal.R.value),
                    horizontalAlignment = Alignment.CenterHorizontally) {
                    AsyncImage(
                        modifier = Modifier.clip(CircleShape),
                        model = user?.avatar_url,
                        contentDescription = null,
                    )
                    Spacer(modifier = Modifier.height(AppDimenVal.R.value))
                    Text(
                        user?.name ?: "John Doe",
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    if (user != null) {
                        Text(
                            user.login,
                            color = Color.LightGray
                        )
                    }

                    Spacer(modifier = Modifier.height(AppDimenVal.S.value))

                    Row(horizontalArrangement = Arrangement.Center) { //Followers and following row

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {//Followers
                            Text(
                                "${user?.followers}",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                            )
                            Text(
                                "Followers",
                                color = Color.LightGray
                            )
                        }

                        Spacer(modifier = Modifier.width(AppDimenVal.L.value))

                        Column (horizontalAlignment = Alignment.CenterHorizontally) {//Followers
                            //Followers
                            Text(
                                "${user?.following}",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                            )
                            Text(
                                "Following",
                                color = Color.LightGray
                            )
                        }
                    }

                    Spacer(Modifier.height(AppDimenVal.L.value))

                    Row {
                        Text(user?.bio?:"Lorem ipsum dolor sit amet", textAlign = TextAlign.Center, color = Color.White)
                    }

                }
            }
        }

        Spacer(modifier = Modifier.height(AppDimenVal.R.value))


    }

}