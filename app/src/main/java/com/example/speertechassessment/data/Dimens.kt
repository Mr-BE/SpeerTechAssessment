package com.example.speertechassessment.data

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


sealed class AppDimenVal(val value: Dp) {
    object XXS : AppDimenVal(2.dp)
    object XS : AppDimenVal(4.dp)
    object S : AppDimenVal(8.dp)
    object R : AppDimenVal(16.dp)
    object L : AppDimenVal(32.dp)
    object XL : AppDimenVal(64.dp)
    object XXL : AppDimenVal(128.dp)


}