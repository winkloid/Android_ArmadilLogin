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

    /**
     * Describes which color mode is to be used.
     * 0 - Light theme
     * 1 - Dark theme
     * 2 - System adapted theme
     */
    private val _colorMode = MutableLiveData<Int>(0)
    val colorMode: LiveData<Int> = _colorMode

    fun setFragmentStatus(fragStatus: FragmentStatus) {
        _status.value = fragStatus
        Log.d("SETSTATUS", "${status.value}")
    }

    fun setDyslexicFont(dyslexicActive: Boolean) {
        _dyslexicFont.value = dyslexicActive
    }

    /**
     * Sets the value of [_colorMode].
     */
    fun setColorMode(mode: Int) {
        _colorMode.value = mode
    }
}