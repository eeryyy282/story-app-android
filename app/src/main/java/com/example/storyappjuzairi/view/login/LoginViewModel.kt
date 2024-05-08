package com.example.storyappjuzairi.view.login

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.storyappjuzairi.data.repository.LoginRepository
import com.example.storyappjuzairi.data.response.LoginResponse
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException

class LoginViewModel(
    application: Application,
    private val loginRepository: LoginRepository
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

    private val _token = MutableLiveData<String?>()
    val token: MutableLiveData<String?>
        get() = _token

    private val _username = MutableLiveData<String?>()
    val username: MutableLiveData<String?>
        get() = _username

    fun loginUser(email: String, password: String) {
        _loading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = loginRepository.login(email, password)
                val loginResult = response.loginResult
                val token = loginResult?.token
                val username = loginResult?.name
                _token.postValue(token)
                _username.postValue(username)
                _loading.postValue(false)
                _showSuccessDialog.postValue("Login berhasil")
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

}