package de.fklappan.app.workoutlog.ui.searchworkout

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
//

@Composable
fun GreetingSection(
    name: String = "Philipp"
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Good morning, $name",
            )
            Text(
                text = "We wish you have a good day!",
            )
        }
    }
}
//
//@Composable
//fun ChipSection(
//    chips: List<String>
//) {
//    var selectedChipIndex by remember {
//        mutableStateOf(0)
//    }
//    LazyRow {
//        items(chips.size) {
//            Box(
//                contentAlignment = Alignment.Center,
//                modifier = Modifier
//                    .padding(start = 15.dp, top = 15.dp, bottom = 15.dp)
//                    .clickable {
//                        selectedChipIndex = it
//                    }
//                    .clip(RoundedCornerShape(10.dp))
//                    .background(
//                        if (selectedChipIndex == it) ButtonBlue
//                        else DarkerButtonBlue
//                    )
//                    .padding(15.dp)
//            ) {
//                Text(text = chips[it], color = TextWhite)
//            }
//        }
//    }
//}