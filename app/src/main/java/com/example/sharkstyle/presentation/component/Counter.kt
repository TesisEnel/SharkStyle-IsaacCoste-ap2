package com.example.sharkstyle.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon

@Composable
fun CartCounter(
    modifier: Modifier = Modifier,
    initialCount: Int = 3,
    onItemChanged: () -> Unit
) {
    var count by remember { mutableStateOf(initialCount) }

    ConstraintLayout(
        modifier = modifier
            .width(100.dp)
            .background(Color.LightGray, shape = RoundedCornerShape(10.dp))
    ) {
        val (plusCartBtn, minusCartBtn, numberItemTxt) = createRefs()


        Text(
            text = count.toString(),
            color = Color.Black,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.constrainAs(numberItemTxt) {
                end.linkTo(parent.end)
                start.linkTo(parent.start)
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
            }
        )

        Box(
            modifier = Modifier
                .padding(2.dp)
                .size(28.dp)
                .background(Color.Black, shape = RoundedCornerShape(10.dp))
                .constrainAs(plusCartBtn) {
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                }
                .clickable {
                    count++
                    onItemChanged()
                }
        ) {
            Text(
                text = "+",
                color = Color.White,
                modifier = Modifier.align(Alignment.Center),
                textAlign = TextAlign.Center
            )
        }

        Box(
            modifier = Modifier
                .padding(2.dp)
                .size(28.dp)
                .background(
                    Color.White,
                    shape = RoundedCornerShape(10.dp)
                )
                .constrainAs(minusCartBtn) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                }
                .clickable {
                    if (count > 1) {
                        count--
                    } else {

                        count = 0
                    }
                    onItemChanged()
                }
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.align(Alignment.Center)
            ) {
                if (count == 1) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = null,
                        tint = Color.Black
                    )
                } else {
                    Text(
                        text = "-",
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

