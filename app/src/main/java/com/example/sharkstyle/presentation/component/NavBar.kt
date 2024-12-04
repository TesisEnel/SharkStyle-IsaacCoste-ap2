package com.example.sharkstyle.presentation.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun NavigationBarWithAnimatedIcons(
    gotoHome: () -> Unit,
    gotoCart: () -> Unit,
    gotoUser: () -> Unit
) {
    var selectedItem by remember { mutableIntStateOf(0) }
    val items = listOf("Inicio", "Carrito", "Usuario")
    val selectedIcons = listOf(Icons.Filled.Home, Icons.Filled.ShoppingCart, Icons.Filled.Person)
    val unselectedIcons =
        listOf(Icons.Outlined.Home, Icons.Outlined.ShoppingCart, Icons.Outlined.Person)

    NavigationBar {
        items.forEachIndexed { index, item ->
            val isSelected = selectedItem == index

            val iconTint by animateColorAsState(targetValue = if (isSelected) Color.Black else Color.Gray)
            val iconSize by animateFloatAsState(targetValue = if (isSelected) 30f else 24f)

            NavigationBarItem(
                icon = {
                    Icon(
                        if (isSelected) selectedIcons[index] else unselectedIcons[index],
                        contentDescription = item,
                        tint = iconTint,
                        modifier = Modifier.size(iconSize.dp)
                    )
                },
                label = { Text(item) },
                selected = isSelected,
                onClick = {
                    selectedItem = index
                    when (index) {
                        0 -> gotoHome()
                        1 -> gotoCart()
                        2 -> gotoUser()
                    }
                },
                alwaysShowLabel = false
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NavigationBarWithAnimatedIconsPreview() {
    NavigationBarWithAnimatedIcons(
        gotoHome = {},
        gotoCart = {},
        gotoUser = {}
    )
}