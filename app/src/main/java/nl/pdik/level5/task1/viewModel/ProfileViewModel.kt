package nl.pdik.level5.task1.viewModel

import android.app.Application
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import nl.pdik.level5.task1.model.Profile
import nl.pdik.level5.task1.repository.ProfileRepository

class ProfileViewModel(application: Application) : AndroidViewModel(application) {
    private val TAG = "FIRESTORE"
    private val profileRepository: ProfileRepository = ProfileRepository()

    //Student
    var imageUri by mutableStateOf<Uri?>(null)
    val bitmap = mutableStateOf<Bitmap?>(null)

    val profile: LiveData<Profile> = profileRepository.profile

    val createSuccess: LiveData<Boolean> = profileRepository.createSuccess

    private val _errorText: MutableLiveData<String> = MutableLiveData()
    val errorText: LiveData<String>
        get() = _errorText

    fun getProfile() {
        viewModelScope.launch {
            try {
                profileRepository.getProfile()
            } catch (ex: ProfileRepository.ProfileRetrievalError) {
                val errorMsg = "Something went wrong while retrieving profile"
                Log.e(TAG, ex.message ?: errorMsg)
                _errorText.value = errorMsg
            }
        }
    }

    fun createProfile(firstName: String, lastName: String, description: String, imageUri: String) {
        // persist data to firestore
        val profile = Profile(firstName, lastName, description, imageUri)

        viewModelScope.launch {
            try {
                profileRepository.createProfile(profile)
            } catch (ex: ProfileRepository.ProfileSaveError) {
                val errorMsg = "Something went wrong while saving the profile"
                Log.e(TAG, ex.message ?: errorMsg)
                _errorText.value = errorMsg
            }
        }
    }

    fun reset() {
        _errorText.value = ""
        profileRepository.reset()
    }
}