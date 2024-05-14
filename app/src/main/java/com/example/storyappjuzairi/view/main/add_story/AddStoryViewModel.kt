package com.example.storyappjuzairi.view.main.add_story

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AddStoryViewModel : ViewModel() {

    private val _selectImageUri = MutableLiveData<Uri?>()
    val selectImageUri: LiveData<Uri?> = _selectImageUri

    fun setSelectImageUri(uri: Uri?) {
        _selectImageUri.value = uri
    }
}