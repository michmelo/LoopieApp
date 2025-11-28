package com.example.loopieapp.Components

import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarHalf
import androidx.compose.material.icons.filled.StarOutline
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import kotlin.math.ceil
import kotlin.math.floor

@Composable
fun RatingBar(
    modifier: Modifier = Modifier,
    rating: Float,
    stars: Int = 5,
    starsColor: Color = Color(0xFFFFC107),
) {
    val filledStars = floor(rating).toInt()
    val unfilledStars = (stars - ceil(rating)).toInt()
    val halfStar = !(rating.rem(1).equals(0.0f))

    Row(modifier = modifier) {
        repeat(filledStars) {
            Icon(imageVector = Icons.Filled.Star, contentDescription = null, tint = starsColor)
        }
        if (halfStar) {
            Icon(imageVector = Icons.Filled.StarHalf, contentDescription = null, tint = starsColor)
        }
        repeat(unfilledStars) {
            Icon(imageVector = Icons.Filled.StarOutline, contentDescription = null, tint = starsColor)
        }
    }
}