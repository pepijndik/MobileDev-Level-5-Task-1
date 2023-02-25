package nl.pdik.level5.task1.ui.screens

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import nl.pdik.level5.task1.R
import nl.pdik.level5.task1.viewModel.ProfileViewModel

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ProfileScreen(viewModel: ProfileViewModel) {
    val context = LocalContext.current

    viewModel.getProfile()

    val errorMsg by viewModel.errorText.observeAsState()
    val successfullyCreatedQuiz by viewModel.createSuccess.observeAsState()
    val profile by viewModel.profile.observeAsState()

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
                DisplayImageFromGallery(context, viewModel, profile?.imageUri)
                Text(
                    text = profile?.firstName.toString() + " " + profile?.lastName.toString(),
                    style = MaterialTheme.typography.h4,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                )
                Text(
                    text = profile?.description.toString(),
                    fontStyle = FontStyle.Italic,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                )

            }
        }
        if (!errorMsg.isNullOrEmpty()) {
            Toast.makeText(context, errorMsg, Toast.LENGTH_LONG).show()
            viewModel.reset()
        }
        if (successfullyCreatedQuiz != null) {
            if (successfullyCreatedQuiz!!) {
                Toast.makeText(
                    context,
                    stringResource(id = R.string.successfully_created_profile),
                    Toast.LENGTH_LONG
                ).show()
                viewModel.reset()
            }
        }
    }
}

@Composable
fun DisplayImageFromGallery(
    context: Context,
    viewModel: ProfileViewModel,
    imageUriString: String?
) {

    if (!(imageUriString.isNullOrEmpty() || imageUriString.equals(context.getString(R.string.no_gallery_image)))) {
        val imageUri = Uri.parse(imageUriString) // Conversion String to Uri datatype.
        // https://stackoverflow.com/questions/58903911/how-to-fix-deprecated-issue-in-android-bitmap
        viewModel.bitmap.value = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val src = ImageDecoder.createSource(context.contentResolver, imageUri)
            ImageDecoder.decodeBitmap(src)
        } else {
            MediaStore.Images.Media.getBitmap(context.contentResolver, imageUri)
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
}