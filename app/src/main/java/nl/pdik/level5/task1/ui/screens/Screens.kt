package nl.pdik.level5.task1.ui.screens

sealed class Screens(
    val route: String
) {
    object CreateProfile: Screens("create_profile_screen")
    object ProfileScreen: Screens("profile_screen")
}
