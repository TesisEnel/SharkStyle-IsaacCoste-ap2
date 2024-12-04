package com.example.sharkstyle.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.sharkstyle.presentation.carrito.CartShopping
import com.example.sharkstyle.presentation.categoria.ProductoCategoriaListScreen
import com.example.sharkstyle.presentation.component.NavigationBarWithAnimatedIcons
import com.example.sharkstyle.presentation.home.HomeScreen
import com.example.sharkstyle.presentation.login.AuthViewModel
import com.example.sharkstyle.presentation.login.LoginPage
import com.example.sharkstyle.presentation.login.SignupPage
import com.example.sharkstyle.presentation.producto.ProductoListScreen
import com.example.sharkstyle.presentation.producto.ProductoScreen
import com.example.sharkstyle.presentation.usuario.UsuarioListScreen
import com.example.sharkstyle.presentation.usuario.UsuarioScreen


@Composable
fun PruebaSharkStyleNavHost(
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel,
    navController: NavHostController
){
    val scope = rememberCoroutineScope()

    NavHost(
        navController = navController,
        startDestination = Screen.Login,
    ) {
        composable<Screen.Login> {
            LoginPage(modifier, navController, authViewModel,
                goToHomePage = {
                    navController.navigate(Screen.Signup)
                },
                goToSignupPage = {
                    navController.navigate(Screen.Signup)
                }
            )
        }
        composable<Screen.Signup> {
            SignupPage(modifier, navController,
                goToHomePage = {
                    navController.navigate(Screen.Login)
                }
            )
        }
        composable<Screen.NavBar> {
            NavigationBarWithAnimatedIcons(
                gotoHome = {
                    navController.navigate(Screen.Home)
                },
                gotoCart = {
                    navController.navigate(Screen.Cart)
                },
                gotoUser = {
                    navController.navigate(Screen.Cart)
                }
            )
        }
        composable<Screen.Home> {
            HomeScreen(
                scope = scope,
                gotoHome = {
                    navController.navigate(Screen.Home)
                },
                gotoCart = {
                    navController.navigate(Screen.Cart)
                },
                gotoUser = {
                    navController.navigate(Screen.User(it))
                },
                onProductoClick = {
                    navController.navigate(Screen.Producto(it))
                },
                onCategoriaClick = {
                    navController.navigate(Screen.Categoria(it))
                }
            )
        }
        composable<Screen.Cart> {
            CartShopping(
                gotoHome = {
                    navController.navigate(Screen.Home)
                },
                gotoCart = {
                    navController.navigate(Screen.Cart)
                },
                gotoUser = {
                    navController.navigate(Screen.Cart)
                }
            )
        }
        composable<Screen.UsuarioList> {
            UsuarioListScreen(
                gotoUsuario = {
                    navController.navigate(Screen.User(it))
                }
            )
        }
        composable<Screen.User> {
            val args = it.toRoute<Screen.User>()
            UsuarioScreen(
                userId = args.userId,
                onLogout = {
                    navController.navigate(Screen.Login)
                }
            )
        }
        composable<Screen.Categoria> { it ->
            val args = it.toRoute<Screen.Categoria>()
            ProductoCategoriaListScreen(
                gotoHome = {
                    navController.navigate(Screen.Home)
                },
                gotoCart = {
                    navController.navigate(Screen.Cart)
                },
                gotoUser = {
                    navController.navigate(Screen.Cart)
                },
                onProductoClick = {
                    navController.navigate(Screen.Producto(it))
                },
                categoriaId = args.categoriaId
            )
        }

        composable<Screen.ProductoList>{
            ProductoListScreen(
                gotoHome = {
                    navController.navigate(Screen.Home)
                },
                gotoCart = {
                    navController.navigate(Screen.Cart)
                },
                gotoUser = {
                    navController.navigate(Screen.Cart)
                },
                onProductoClick = {
                    navController.navigate(Screen.Producto(it))
                },
                onCategoriaClick = {
                    navController.navigate(Screen.CategoriaList)
                }
            )
        }
        composable<Screen.Producto> {
            val args = it.toRoute<Screen.Producto>()
            ProductoScreen(
                productoId = args.productoId,
                gotoHome = {
                    navController.navigate(Screen.Home)
                },
                gotoCart = {
                    navController.navigate(Screen.Cart)
                },
                gotoUser = {
                    navController.navigate(Screen.Cart)
                },
                onBackPressed = {
                    navController.popBackStack()
                },
                onNavigateToCarrito = { event ->
                    navController.navigate(Screen.Cart)
                }
            )
        }
    }
}