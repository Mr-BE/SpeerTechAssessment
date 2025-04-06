package com.example.speertechassessment.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.speertechassessment.data.AppDimenVal
import com.example.speertechassessment.nav.NavigationItem
import com.example.speertechassessment.ui.effect.shimmerLoading

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SkeletonProfile (){
    Column(modifier = Modifier.shimmerLoading()) {
        TopAppBar(title = { Text("             ") }, navigationIcon = {
            IconButton(onClick = { }, Modifier.shimmerLoading()) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "     ", Modifier.shimmerLoading())
            }
        })
        Column(modifier = Modifier.padding(AppDimenVal.R.value)) {

            Row(horizontalArrangement = Arrangement.Center) {
                Card(
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .padding(16.dp)
                        .shimmerLoading()
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
                        ).shimmerLoading(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        AsyncImage(
                            modifier = Modifier.clip(CircleShape).shimmerLoading(),
                            model = "user?.avatar_url",
                            contentDescription = null,
                        )
                        Spacer(modifier = Modifier.height(AppDimenVal.R.value))

                                Text("        ", style = MaterialTheme.typography.bodyLarge.copy(color = Color.Black, fontWeight = FontWeight.Bold), modifier = Modifier.shimmerLoading())


                            Text("    ", style = MaterialTheme.typography.bodyMedium.copy(color = Color.Black), modifier = Modifier.shimmerLoading())

                            Spacer(modifier = Modifier.height(AppDimenVal.R.value))

                            Row(horizontalArrangement = Arrangement.Center) { //Followers and following row

                                Column(
                                    modifier = Modifier.shimmerLoading(),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {//Followers
                                    Text(
                                        "              ",
                                        style = MaterialTheme.typography.bodyLarge.copy(color = Color.Black, fontWeight = FontWeight.Bold)
                                    , modifier = Modifier.shimmerLoading())
                                    Text(
                                        "             ",
                                        style = MaterialTheme.typography.bodyMedium.copy(color = Color.Black),modifier = Modifier.shimmerLoading())

                                }

                                Spacer(modifier = Modifier.width(AppDimenVal.XL.value))

                                Column(horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier.shimmerLoading(),) {//Followers
                                    //Followers
                                    Text(
                                        "         ",
                                        style = MaterialTheme.typography.bodyLarge.copy(color = Color.Black, fontWeight = FontWeight.Bold)
,modifier = Modifier.shimmerLoading()
                                    )
                                    Text(
                                        "             ",
                                        style = MaterialTheme.typography.bodyMedium.copy(color = Color.Black)
                                    ,modifier = Modifier.shimmerLoading())
                                }
                            }

                            Spacer(Modifier.height(AppDimenVal.L.value))

                            Row {
                                Text(
                                    "     ",
                                    style = MaterialTheme.typography.bodyMedium.copy(color = Color.Black),
                                modifier = Modifier.shimmerLoading())
                            }


                    }
                }

                Spacer(modifier = Modifier.height(AppDimenVal.R.value))


            }
        }

    }
}
