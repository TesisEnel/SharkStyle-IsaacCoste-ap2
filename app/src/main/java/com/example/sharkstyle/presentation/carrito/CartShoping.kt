package com.example.sharkstyle.presentation.carrito

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.rememberAsyncImagePainter
import com.example.pruebasharkstyle.R
import com.example.sharkstyle.presentation.component.CartCounter

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun CartShopping(
    carritoViewModel: CarritoViewModel = hiltViewModel(),
    gotoHome: () -> Unit,
    gotoCart: () -> Unit,
    gotoUser: () -> Unit
) {
    val uiState by carritoViewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                actions = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 24.dp, end = 20.dp)
                    ) {
                        Image(
                            painter = painterResource(R.drawable.back),
                            contentDescription = "Back",
                            modifier = Modifier
                                .size(45.dp)
                                .background(Color.White, shape = RoundedCornerShape(50.dp))
                        )
                        Box(
                            modifier = Modifier.weight(1f),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Cart",
                                style = MaterialTheme.typography.titleLarge,
                                color = Color.Black
                            )
                        }
                        Image(
                            painter = painterResource(R.drawable.search_icon),
                            contentDescription = "Search",
                            modifier = Modifier
                                .size(45.dp)
                                .background(Color.White, shape = RoundedCornerShape(50.dp))
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colorResource(R.color.white)
                )
            )
        },
        bottomBar = {
            CartSummary(
                itemTotal = uiState.detallesCarrito.sumOf { (it.precio ?: 0.0) * (it.cantidad ?: 0) },
                delivery = 10.0
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            if (uiState.detallesCarrito.isEmpty()) {
                Text(
                    text = "Cart Is Empty",
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            } else {
                CartList(
                    cartItems = uiState.detallesCarrito.map { detalle ->
                        CartItem(
                            title = detalle.productoId.toString() ?: "Producto",
                            price = detalle.precio ?: 0.0,
                            numberInCart = detalle.cantidad ?: 1,
                            picUrl = detalle.cantidad.toString()?: ""
                        )
                    }
                ) {
                }
            }

        }
    }
}


class ManagmentCart {
    private val itemsInCart = mutableListOf<CartItem>()


    fun getListCart(): List<CartItem> = itemsInCart
    fun getTotal(): Double = itemsInCart.sumOf { it.price * it.numberInCart }
    fun plusItem(index: Int) {
        itemsInCart[index] = itemsInCart[index].copy(numberInCart = itemsInCart[index].numberInCart + 1)
    }

    fun minusItem(index: Int) {
        val item = itemsInCart[index]
        if (item.numberInCart > 1) {
            itemsInCart[index] = item.copy(numberInCart = item.numberInCart - 1)
        }
    }
}

@Composable
fun CartSummary(itemTotal: Double, delivery: Double) {
    val total = itemTotal + delivery

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(
                RoundedCornerShape(
                    topStart = 20.dp,
                    topEnd = 20.dp
                )
            ) // Solo redondea la parte superior
            .background(colorResource(R.color.black)) // Fondo púrpura para la caja
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, start = 16.dp, end = 16.dp, bottom = 60.dp)
        ) {
            // Total
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            ) {
                Text(
                    text = "Total",
                    Modifier.weight(1f),
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "$$total",
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            // Botón de "Check Out"
            Button(
                onClick = { /* Handle Checkout */ },
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(Color.White),
                modifier = Modifier
                    .padding(top = 16.dp)
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(text = "Check Out", color = Color.Black, fontSize = 18.sp)
            }
        }
    }
}

fun calculatorCar(managmentCart: ManagmentCart, tax: MutableState<Double>) {
    val percentTax = 0.02
    tax.value = Math.round((managmentCart.getTotal() * percentTax) * 100) / 100.0
}

@Composable
fun CartList(cartItems: List<CartItem>, onItemChanged: () -> Unit) {
    LazyColumn(Modifier.padding(top = 16.dp)) {
        items(cartItems) { item ->
            CartItem(cartItem = item, onItemChanged = onItemChanged)
        }
    }
}

@Composable
fun CartItem(
    cartItem: CartItem,
    onItemChanged: () -> Unit
) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        val (pic, titleTxt, feeEachTime, totalEachItem, quantity) = createRefs()

        Image(
            painter = rememberAsyncImagePainter(cartItem.picUrl),
            contentDescription = null,
            modifier = Modifier
                .size(90.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(colorResource(R.color.lightgray), shape = RoundedCornerShape(10.dp))
                .constrainAs(pic) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                }
        )

        Text(
            text = cartItem.title,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .constrainAs(titleTxt) {
                    start.linkTo(pic.end)
                    top.linkTo(parent.top)
                }
                .padding(start = 8.dp, end = 8.dp)
        )

        Text(
            text = "$${cartItem.price}",
            color = colorResource(R.color.lightgray),
            modifier = Modifier
                .constrainAs(feeEachTime) {
                    start.linkTo(titleTxt.start)
                    top.linkTo(titleTxt.bottom)
                }
                .padding(start = 8.dp, end = 8.dp)
        )

        Text(
            text = "$${cartItem.numberInCart * cartItem.price}",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .constrainAs(totalEachItem) {
                    start.linkTo(titleTxt.start)
                    bottom.linkTo(pic.bottom)
                }
                .padding(start = 8.dp, end = 8.dp, bottom = 8.dp)
        )

        Box(
            modifier = Modifier
                .constrainAs(quantity) {
                    end.linkTo(parent.end)
                    bottom.linkTo(pic.bottom)
                }
        ) {
            CartCounter(
                onItemChanged = onItemChanged
            )
        }
    }
}

data class CartItem(
    val title: String,
    val price: Double,
    val numberInCart: Int,
    val picUrl: String
)
