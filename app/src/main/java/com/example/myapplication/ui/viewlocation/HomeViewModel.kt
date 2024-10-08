package com.example.myapplication.ui.viewlocation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Getting Current Location"
    }
    val text: LiveData<String> = _text
}