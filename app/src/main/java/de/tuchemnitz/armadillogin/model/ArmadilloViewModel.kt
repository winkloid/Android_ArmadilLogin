package de.tuchemnitz.armadillogin.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import de.tuchemnitz.armadillogin.R

enum class FragmentStatus {
    WELCOME,
    REGISTER_LOGIN,
    REGISTER1,
    REGISTER2,
    REGISTER_SUMMARY,
    REGISTER_KEY,
    REGISTER_FINISHED,
    REGISTER_ERROR,
    LOGIN,
    LOGIN_KEY,
    USER_OVERVIEW,
    DEFAULT,
}

class ArmadilloViewModel : ViewModel() {
    private val _status = MutableLiveData<FragmentStatus>()
    val status: LiveData<FragmentStatus> = _status

    private val _dyslexicFont = MutableLiveData<Boolean>(false)
    val dyslexicFont: LiveData<Boolean> = _dyslexicFont

    fun setFragmentStatus(fragStatus: FragmentStatus) {
        _status.value = fragStatus
        Log.d("SETSTATUS", "${status.value}")
    }

    fun setDyslexicFont(dyslexicActive: Boolean) {
        _dyslexicFont.value = dyslexicActive
    }
}