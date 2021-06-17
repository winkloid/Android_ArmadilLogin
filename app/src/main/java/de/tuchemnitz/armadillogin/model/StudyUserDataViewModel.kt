package de.tuchemnitz.armadillogin.model

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import de.tuchemnitz.armadillogin.R
import kotlin.math.round

class StudyUserDataViewModel(application: Application): AndroidViewModel(application) {

    companion object {
        private const val LOG_TAG = "StudyUserDataModel"
    }
    // time measurement feature
    var userStartTime: Long = 0
    var userFinishedTime: Long = 0
    var userTime: Long = 0
    var userTimeInSeconds: Double = 0.0

    fun calculateUserTime() {
        userTime = userFinishedTime - userStartTime
        // round time in seconds to 2 digits after comma
        userTimeInSeconds = round((userTime / 1000000000.0) * 100) / 100.0
    }

    // personal user data for study and statistics
    /**
     * Represents the age of the user.
     * null - not specified
     * else - the actual age
     */
    private var _age = MutableLiveData<Int?>(null)
    val age: LiveData<Int?> = _age

    fun setAge(ageInput: Int?) {
        _age.value = ageInput
    }

    /**
     * Represents the gender of the user.
     * 0 - not specified
     * 1 - Female
     * 2 - Male
     * 3 - Diverse
     */
    private var _gender = MutableLiveData<Int>(0)
    val gender: LiveData<Int> = _gender

    /**
     * Sets the gender specified by the user immediately when user selects it from radio group in UserDataFragment.
     */
    fun setGender(genderInput: Int) {
        _gender.value = genderInput
    }

    /**
     * Represents technical experience of the user.
     * 0 - not specified
     * 1 - none
     * 2 - little
     * 3 - average
     * 4 - much
     * 5 - expert level
     */
    private var _technicalExperience = MutableLiveData<Int>(0)
    val technicalExperience: LiveData<Int> = _technicalExperience

    /**
     * Sets the gender specified by the user immediately when user selects it from radio group in UserDataFragment.
     */
    fun setTechnicalExperience(technicalExperienceInput: Int) {
        _technicalExperience.value = technicalExperienceInput
    }

    /**
     * Is true while data are being sent to Firestore Database.
     */
    private var _sendingStudyData = MutableLiveData(false)
    val sendingStudyData: LiveData<Boolean> = _sendingStudyData

    /**
     * Is true if all study data has been sent successfully.
     */
    private var _sentStudyData = MutableLiveData(false)
    val sentStudyData: LiveData<Boolean> = _sentStudyData

    /**
     * Implements functionality to send user study data to Firestore Database.
     * While the function is running, the value of [_sendingStudyData] is true.
     * After the function successfully finished sending data, the value of [_sentStudyData] becomes true.
     */
    fun sendData() {
        _sendingStudyData.value = true
        val studyDataBase = Firebase.firestore
        val user = hashMapOf(
            "age" to age.value,
            "gender" to gender.value,
            "technicalExperience" to technicalExperience.value,
            "timeNeeded" to userTime
        )

        studyDataBase.collection("userData")
            .add(user)
            .addOnSuccessListener { documentAddress ->
                Log.d(LOG_TAG, "DocumentSnapshot added with ID: ${documentAddress.id}")
                Toast.makeText(getApplication(), getApplication<Application>().getString(R.string.user_overview_data_sent_success), Toast.LENGTH_SHORT).show()
                this._sentStudyData.value = true
            }
            .addOnFailureListener { error ->
                Log.w(LOG_TAG, "Error adding document", error)
                Toast.makeText(getApplication(), getApplication<Application>().getString(R.string.user_overview_data_sent_error), Toast.LENGTH_SHORT).show()
            }
        _sendingStudyData.value = false
    }
}