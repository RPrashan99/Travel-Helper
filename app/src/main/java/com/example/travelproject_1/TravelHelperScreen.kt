package com.example.travelproject_1

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.travelproject_1.ui.FinderScreen
import com.example.travelproject_1.ui.HomePage
import com.example.travelproject_1.ui.LoginScreen
import com.example.travelproject_1.ui.MemoryScreen
import com.example.travelproject_1.ui.NavigatorScreen
import com.example.travelproject_1.ui.PlannerScreen
import com.example.travelproject_1.ui.RegistrationScreen
import com.example.travelproject_1.ui.StartScreen
import com.example.travelproject_1.ui.UserScreen

enum class TravelHelperScreen(@StringRes val title: Int){
    Start(title = R.string.start),
    SignUp(title = R.string.signup),
    SignIn(title = R.string.signin),
    Home(title = R.string.app_name),
    Planner(title = R.string.planner),
    Finder(title = R.string.finder),
    Memory(title = R.string.memory),
    Navigator(title = R.string.navigator),
    UserDetails(title = R.string.userDetails)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TravelHelperAppBar(
    currentScreen: TravelHelperScreen,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    navigateToUserDetails: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = {
            Text(
                text = stringResource(currentScreen.title),
                style = TextStyle(
                    fontSize = 36.sp,
                    fontFamily = FontFamily(Font(R.font.poppins)),
                    fontWeight = FontWeight(400)),
                color = Color(R.color.white),
                modifier = Modifier
                    .padding(50.dp)
            )
                },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = Color(R.color.primaryColor),
            navigationIconContentColor = Color(R.color.white)
        ),
        modifier = modifier,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back_button)
                    )
                }
            }
        },
        actions = {
            IconButton(onClick = {
                navigateToUserDetails()
            }) {
                Icon(
                    imageVector = Icons.Filled.AccountCircle,
                    contentDescription = stringResource(R.string.user_pic),
                    modifier = Modifier)
            }
        }
    )
}

@Composable
fun TravelHelperApp(
    navController: NavHostController = rememberNavController()
){
    val backStackEntry by navController.currentBackStackEntryAsState()

    val currentScreen = TravelHelperScreen.valueOf(
        backStackEntry?.destination?.route?: TravelHelperScreen.Start.name // start use as default value
    )

    val screenList = listOf<Int>(
        R.string.planner,
        R.string.memory,
        R.string.finder,
        R.string.navigator
    )

    Scaffold(
        topBar = {
            if(currentScreen.title in screenList){
                TravelHelperAppBar(
                    currentScreen = currentScreen,
                    canNavigateBack = true,
                    navigateUp = { navController.navigateUp() },
                    navigateToUserDetails = {
                        navController.navigate(TravelHelperScreen.UserDetails.name)
                    })
            }
        }
    ) {
        innerPadding ->

        NavHost(
            navController = navController,
            startDestination = TravelHelperScreen.Start.name,
            modifier = Modifier.padding(innerPadding)){

            composable(route = TravelHelperScreen.Start.name){
                StartScreen(onStartedButtonClicked = {
                    navController.navigate(TravelHelperScreen.SignIn.name)
                })
            }

            composable(route = TravelHelperScreen.SignUp.name){
                RegistrationScreen(onSignInButtonTap = {
                    navController.navigate(TravelHelperScreen.SignIn.name)
                },
                    onRegisterButtonTap = {
                        navController.navigate(TravelHelperScreen.SignIn.name)
                    },)
            }

            composable(route = TravelHelperScreen.SignIn.name){
                LoginScreen(onSignupButtonTap = {
                    navController.navigate(TravelHelperScreen.SignUp.name)
                },
                    onLoginSuccess = {
                        navController.navigate(TravelHelperScreen.Home.name)
                    })
            }

            composable(route = TravelHelperScreen.Home.name){
                HomePage(
                    onMainButtonClicked = {
                        when (it) {
                            R.string.planner -> navController.navigate(TravelHelperScreen.Planner.name)
                            R.string.finder -> navController.navigate(TravelHelperScreen.Finder.name)
                            R.string.memory -> navController.navigate(TravelHelperScreen.Memory.name)
                            R.string.navigator -> navController.navigate(TravelHelperScreen.Navigator.name)
                            else -> navController.navigate(TravelHelperScreen.Start.name)
                        }
                    }
                )
            }

            composable(route = TravelHelperScreen.Finder.name){
                FinderScreen()
            }

            composable(route = TravelHelperScreen.Navigator.name){
                NavigatorScreen()
            }

            composable(route = TravelHelperScreen.Memory.name){
                MemoryScreen()
            }

            composable(route = TravelHelperScreen.Planner.name){
                PlannerScreen(
                    navigateToMemory = {
                        navController.navigate(TravelHelperScreen.Memory.name)
                    }
                )
            }

            composable(route = TravelHelperScreen.UserDetails.name){
                UserScreen()
            }
        }
    }

}