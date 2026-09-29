package com.example.speertechassessment.ui.screens

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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import com.example.speertechassessment.data.AppDimenVal
import com.example.speertechassessment.ui.effect.shimmerLoading

// OPT: one card-shaped skeleton, reused by the profile and search screens (search had its own copy).
// Shimmer now sits only on leaf placeholders (it was stacked on ~15 nested nodes), and the avatar
// placeholder no longer asks Coil to load the literal string "user?.avatar_url".
@Composable
fun SkeletonProfileCard(modifier: Modifier = Modifier) {
    Card(
        shape = RoundedCornerShape(12.dp),
        modifier = modifier
            .padding(16.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = AppDimenVal.XL.value, vertical = AppDimenVal.R.value),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Placeholder(Modifier.size(AppDimenVal.XXL.value), CircleShape) // avatar
            Spacer(modifier = Modifier.height(AppDimenVal.R.value))
            Placeholder(Modifier.size(width = 140.dp, height = 20.dp)) // name
            Spacer(modifier = Modifier.height(AppDimenVal.XS.value))
            Placeholder(Modifier.size(width = 90.dp, height = 16.dp)) // @login

            Spacer(modifier = Modifier.height(AppDimenVal.R.value))

            Row { //Followers and following row
                Placeholder(Modifier.size(width = 64.dp, height = 40.dp))
                Spacer(modifier = Modifier.width(AppDimenVal.XL.value))
                Placeholder(Modifier.size(width = 64.dp, height = 40.dp))
            }

            Spacer(Modifier.height(AppDimenVal.L.value))

            Placeholder(Modifier.size(width = 180.dp, height = 16.dp)) // bio
        }
    }
}

@Composable
private fun Placeholder(modifier: Modifier, shape: Shape = RoundedCornerShape(AppDimenVal.XS.value)) {
    Box(modifier.clip(shape).shimmerLoading())
}
