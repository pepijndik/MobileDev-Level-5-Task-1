package nl.pdik.level5.task1.ui.screens

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.ImageDecoder
import android.os.Build
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import nl.pdik.level5.task1.viewModel.ProfileViewModel
import nl.pdik.level5.task1.R;

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun CreateProfileScreen(navController: NavController, viewModel: ProfileViewModel) {

    val context = LocalContext.current

    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var profileDescription by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = context.getString(R.string.app_name)) },
            )
        },
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                PickImageFromGallery(context, viewModel)
                TextField(
                    value = firstName,
                    // below line is used to add placeholder ("hint") for our text field.
                    placeholder = {
                        Text(text = stringResource(id = R.string.first_name_hint))
                    },
                    onValueChange = { firstName = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = colors.background, // Same color as background
                        focusedIndicatorColor = colors.secondary, // This is green underlined
                        unfocusedIndicatorColor = Color.Gray, // Grey underlined
                        focusedLabelColor = colors.secondary,
                        unfocusedLabelColor = Color.Gray
                    ),
                    label = {
                        Text(
                            text = stringResource(R.string.first_name),
                        )
                    }
                )
                TextField(
                    value = lastName,
                    // below line is used to add placeholder ("hint") for our text field.
                    placeholder = {
                        Text(text = stringResource(id = R.string.last_name_hint))
                    },
                    onValueChange = { lastName = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = colors.background, // Same color as background
                        focusedIndicatorColor = colors.secondary, // This is green underlined
                        unfocusedIndicatorColor = Color.Gray, // Grey underlined
                        focusedLabelColor = colors.secondary,
                        unfocusedLabelColor = Color.Gray
                    ),
                    label = {
                        Text(
                            text = stringResource(R.string.last_name),
                        )
                    }
                )
                TextField(
                    value = profileDescription,
                    // below line is used to add placeholder ("hint") for our text field.
                    placeholder = {
                        Text(text = stringResource(id = R.string.description_hint))
                    },
                    onValueChange = { profileDescription = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = colors.background, // Same color as background
                        focusedIndicatorColor = colors.secondary, // This is green underlined
                        unfocusedIndicatorColor = Color.Gray, // Grey underlined
                        focusedLabelColor = colors.secondary,
                        unfocusedLabelColor = Color.Gray
                    ),
                    label = {
                        Text(
                            text = stringResource(R.string.description),
                        )
                    }
                )
            }
            Row(
                modifier = Modifier
                    .weight(1f, false)
            ) {

                Button(modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        contentColor = colors.onPrimary,
                        backgroundColor = colors.secondary
                    ),
                    onClick = {
                        // CREATE PROFILE IF INPUT NOT EMPTY
                        if (firstName.isEmpty() || lastName.isEmpty() || profileDescription.isEmpty()) {
                            Toast.makeText(
                                context,
                                context.getString(R.string.fields_must_not_be_empty),
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            val uriString: String
                            if (viewModel.imageUri == null) {
                                uriString = context.getString(R.string.no_gallery_image)
                            } else {
                                uriString = viewModel.imageUri.toString()
                            }
                            viewModel.reset()
                            viewModel.createProfile(
                                firstName = firstName,
                                lastName = lastName,
                                description = profileDescription,
                                imageUri = uriString
                            )
                            firstName = ""
                            lastName = ""
                            profileDescription = ""
                            navController.navigate(Screens.ProfileScreen.route)
                        }
                    }) {
                    Text(text = context.getString(R.string.confirm).uppercase())
                }
            }
        }
    }
}

@Composable
fun PickImageFromGallery(context: Context, viewModel: ProfileViewModel) {

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        viewModel.imageUri = uri
    }

    if (viewModel.imageUri != null) {
        // https://stackoverflow.com/questions/58903911/how-to-fix-deprecated-issue-in-android-bitmap
        viewModel.bitmap.value = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val src = ImageDecoder.createSource(context.contentResolver, viewModel.imageUri!!)
            ImageDecoder.decodeBitmap(src)
        } else {
            MediaStore.Images.Media.getBitmap(context.contentResolver, viewModel.imageUri)
        }
        viewModel.bitmap.value?.let { btm ->
            Image(
                bitmap = btm.asImageBitmap(),
                contentDescription = null,
                modifier = Modifier
                    .padding(horizontal = 0.dp, vertical = 40.dp)
                    .width(128.dp)
                    .height(128.dp)
            )
        }
    } else {
        Image(
            painter = painterResource(id = R.drawable.ic_account_box_black),
            contentDescription = "Logo",
            modifier = Modifier
                .padding(horizontal = 0.dp, vertical = 40.dp)
                .width(128.dp)
                .height(128.dp)
        )
    }
    Button(
        onClick = { launcher.launch("image/*") },
        colors = ButtonDefaults.buttonColors(
            contentColor = colors.onPrimary,
            backgroundColor = colors.secondary
        )
    )
    {
        Text(text = context.getString(R.string.open_picture_gallery).uppercase())
    }
}