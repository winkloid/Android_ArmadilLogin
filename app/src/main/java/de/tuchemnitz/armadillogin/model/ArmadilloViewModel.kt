package de.tuchemnitz.armadillogin.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * Represents the fragment currently displayed in the login tab.
 *
 * This is used so that the help tab can always display help resources adapted to the current fragment.
 */
enum class FragmentStatus {
    WELCOME,
    USER_DATA,
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
    FINISHED,
    DEFAULT,
}

/**
 * Stores [FragmentStatus] and some settings.
 *
 * When settings are changed in the Settings tab, the changed values are saved in this ViewModel.
 * Observers in the application watching the LiveData stored here help to make the changes effective immediately in the whole application.
 */
class ArmadilloViewModel : ViewModel() {
    /**
     * Stores current [FragmentStatus].
     *
     * When a Fragment is resumed in login tab this value is changed via [setFragmentStatus].
     */
    private val _status = MutableLiveData<FragmentStatus>()

    /**
     * Refers to [_status].
     *
     * This value is public but not mutable. Only the private [_status] variable can be changed via [setFragmentStatus].
     * If the value of [_status] changes, the value of this variable will change, too.
     */
    val status: LiveData<FragmentStatus> = _status

    /**
     * Stores whether dyslexic mode is enabled or not.
     *
     * Is true if dyslexic mode is enabled. This value can be changed by calling [setDyslexicFont].
     */
    private val _dyslexicFont = MutableLiveData<Boolean>(false)

    /**
     * Refers to [_dyslexicFont].
     *
     * Can be observed by an Observer to allow changes to take effect immediately.
     */
    val dyslexicFont: LiveData<Boolean> = _dyslexicFont

    /**
     * Describes which color mode is to be used.
     *
     * 0 - Light theme
     * 1 - Dark theme
     * 2 - System adapted theme
     */
    private val _colorMode = MutableLiveData<Int>(0)
    val colorMode: LiveData<Int> = _colorMode

    /**
     * Set [_status] which is of type [FragmentStatus].
     *
     * Should be called when a fragment is resumed in the login tab.
     */
    fun setFragmentStatus(fragStatus: FragmentStatus) {
        _status.value = fragStatus
    }

    /**
     * Set [_dyslexicFont] value.
     *
     * Called from corresponding switch in [SettingsFragment][de.tuchemnitz.armadillogin.ui.settings.SettingFragment].
     */
    fun setDyslexicFont(dyslexicActive: Boolean) {
        _dyslexicFont.value = dyslexicActive
    }

    /**
     * Sets the value of [_colorMode].
     *
     * Called from corresponding radio buttons in [SettingsFragment][de.tuchemnitz.armadillogin.ui.settings.SettingFragment]
     */
    fun setColorMode(mode: Int) {
        _colorMode.value = mode
    }
}