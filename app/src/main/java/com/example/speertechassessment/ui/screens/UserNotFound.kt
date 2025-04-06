package com.example.speertechassessment.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import com.example.speertechassessment.data.AppDimenVal
import com.example.speertechassessment.nav.NavigationItem
import com.example.speertechassessment.viewmodel.AppViewModel

@Composable
fun UserNotFoundScreen (navHostController: NavHostController){ //For unanticipated errors

    Column (modifier = Modifier
        .fillMaxSize()
        .padding(AppDimenVal.L.value), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally){

        Text("Sorry, it's not you, it's us 🫣", style = MaterialTheme.typography.headlineMedium.copy(textAlign = TextAlign.Center))


        Spacer(Modifier.height(AppDimenVal.XL.value))


        Text("We couldn't find that profile now but we'll try to later 🥲", style = MaterialTheme.typography.bodyLarge.copy(textAlign = TextAlign.Center))
        Spacer(Modifier.height(AppDimenVal.L.value))
        Row (modifier = Modifier.padding(AppDimenVal.R.value)){
            Button(
                onClick = {
                    navHostController.popBackStack(route = NavigationItem.Search.route, inclusive = false)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    "Go to Search",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }


}