package com.example.storyappjuzairi.view.login

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.storyappjuzairi.data.pref.UserPreference
import com.example.storyappjuzairi.data.repository.LoginRepository
import com.example.storyappjuzairi.data.response.LoginResponse
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException

class LoginViewModel(
    application: Application,
    private val loginRepository: LoginRepository,
    private val userPreference: UserPreference
) : AndroidViewModel(application) {

    private val _showSuccessDialog = MutableLiveData<String>()
    val showSuccessDialog: LiveData<String>
        get() = _showSuccessDialog

    private val _showErrorDialog = MutableLiveData<String>()
    val showErrorDialog: LiveData<String>
        get() = _showErrorDialog

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean>
        get() = _loading


    fun loginUser(email: String, password: String) {
        _loading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = loginRepository.login(email, password)
                val loginResult = response.loginResult
                val userId = loginResult?.userId
                val username = loginResult?.name
                val userToken = loginResult?.token
                _loading.postValue(false)
                _showSuccessDialog.postValue("Login berhasil")
                userToken?.let { token ->
                    userId?.let { id ->
                        username?.let { name ->
                            saveUserData(id, name, token)
                        }
                    }
                }
            } catch (e: HttpException) {
                _loading.postValue(false)
                val jsonInString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonInString, LoginResponse::class.java)
                val errorMessage = errorBody.message ?: "Terjadi kesalahan saat login"
                _showErrorDialog.postValue(errorMessage)
            } catch (e: Exception) {
                _loading.postValue(false)
                _showErrorDialog.postValue("Login gagal: ${e.message}")
            }
        }
    }

    private fun saveUserData(userId: String, username: String, userToken: String) {
        viewModelScope.launch {
            userPreference.saveUserData(token = userToken, id = userId, name = username)
        }
    }

}