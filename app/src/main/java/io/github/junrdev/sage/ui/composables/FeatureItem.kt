package io.github.junrdev.sage.ui.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.junrdev.sage.R
import io.github.junrdev.sage.ui.theme.SageTheme


@Preview
@Composable
private fun FeaturesPrev() {
    SageTheme {
        FeatureItem()
    }
}


@Composable
fun FeatureItem(modifier: Modifier = Modifier, featureItemModel: FeatureItemModel = FeatureItemModel()) {

    Column(
        modifier = Modifier
            .width(150.dp)
            .height(150.dp)
            .padding(8.dp)
            .background(
                shape = RoundedCornerShape(8.dp),
                color = Color.LightGray
            )
            .padding(12.dp)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Physics", style = MaterialTheme.typography.titleMedium)
            IconButton(onClick = {}) {
                Icon(imageVector = Icons.Rounded.ArrowForward, contentDescription = null)
            }
        }


        Image(
            painter = painterResource(R.drawable.physics_illustration),
            modifier = Modifier
                .weight(2f)
                .align(Alignment.End),
            contentDescription = null
        )

    }

}

data class FeatureItemModel(
    val title : String = "Physic"
)