package nl.pdik.level5.task1

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import nl.pdik.level5.task1.ui.screens.CreateProfileScreen
import nl.pdik.level5.task1.ui.screens.ProfileScreen
import nl.pdik.level5.task1.ui.screens.Screens
import nl.pdik.level5.task1.ui.theme.Task1Theme
import nl.pdik.level5.task1.viewModel.ProfileViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Task1Theme {
                val context: Context = this
                setContent {
                    ProfileApp(context)
                }
            }
        }
    }
}

@Composable
fun ProfileApp(context: Context) {
    val navController = rememberNavController()
    Scaffold { innerPadding ->
        ProfileNavHost(context, navController, modifier = Modifier.padding(innerPadding))
    }
}

@Composable
fun ProfileNavHost(
    context: Context,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {

    val viewModel: ProfileViewModel = viewModel();
    NavHost(
        navController = navController,
        startDestination = Screens.ProfileScreen.route,
        modifier = modifier
    ) {
        composable(Screens.ProfileScreen.route) {
            ProfileScreen(viewModel)
        }
        composable(Screens.CreateProfile.route) {
            CreateProfileScreen(navController = navController, viewModel = viewModel)
        }

    }
}
