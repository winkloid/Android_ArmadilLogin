package de.tuchemnitz.armadillogin.model

import android.app.Application
import android.util.JsonWriter
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import de.tuchemnitz.armadillogin.R
import de.tuchemnitz.armadillogin.fido2api.AddHeaderInterceptor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import java.io.StringWriter
import java.util.concurrent.TimeUnit
import kotlin.math.round

/**
 * Stores all data collected in the study and includes functionality to send the data.
 *
 * Stores the time the user needs to complete the registration and login process and the personal data the user provided for the study.
 * Once the study is finished the data will be sent to an PHP script which is connected to an MySQL database to store the data permanently.
 */
class StudyUserDataViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        private const val LOG_TAG = "StudyUserDataModel"
        private const val STUDY_DB_URL = "https://www-user.tu-chemnitz.de/~owin/armadillogin/api"
        private val JSON = "application/json".toMediaTypeOrNull()
    }

    // variables for time measurement feature
    /**
     * Stores the system start time of the study.
     *
     * Stores the system time when "Studie beginnen" button is clicked.
     */
    var userStartTime: Long? = null
    /**
     * Stores the system time when login process was completed successfully.
     */
    var userFinishedTime: Long? = null

    /**
     * Stores the difference between [userFinishedTime] and [userStartTime].
     *
     * In nanoseconds.
     */
    var userTime: Long = 0

    /**
     * Stores [userTime] converted from nanoseconds into seconds.
     */
    var userTimeInSeconds: Double = 0.0

    /**
     * Stores system time when FIDO2 for Android API Intent for registering key is called.
     */
    var userRegisterStartTime: Long? = null

    /**
     * Stores system time when FIDO2 for Android API Intent for registering key was completed successfully.
     */
    var userRegisterFinishedTime: Long? = null

    /**
     * Stores difference between [userRegisterFinishedTime] and [userRegisterStartTime]
     */
    var userRegisterTime: Long = 0

    /**
     * Stores [userRegisterTime] in seconds.
     */
    var userRegisterTimeInSeconds: Double = 0.0

    /**
     * Stores system time when FIDO2 for Android API Intent for login with key is called.
     */
    var userLoginStartTime: Long? = null

    /**
     * Stores difference between [userFinishedTime] and [userLoginStartTime]
     */
    var userLoginTime: Long = 0

    /**
     * Stores [userLoginTime] in seconds.
     */
    var userLoginTimeInSeconds: Double = 0.0

    /**
     * Calculate time differences between finished and start times and convert results into seconds.
     *
     * Call [convertNanoToSec] to convert unit of time values from nanoseconds to seconds.
     */
    fun calculateUserTime() {

        // calculate time the user need to complete the whole registration and login process
        // there are null values as standard values to make sure that the time is not measured again after a failed login or register attempt
        if(userFinishedTime != null && userStartTime != null) {
            userTime = userFinishedTime!! - userStartTime!!
        }

        if(userRegisterFinishedTime != null && userRegisterStartTime != null) {
            userRegisterTime = userRegisterFinishedTime!! - userRegisterStartTime!!
        }

        if(userLoginStartTime != null && userFinishedTime != null) {
            userLoginTime = userFinishedTime!! - userLoginStartTime!!
        }

        // convert time from nanoseconds to seconds rounded to 2 digits after comma
        userTimeInSeconds = convertNanoToSec(userTime)
        userRegisterTimeInSeconds = convertNanoToSec(userRegisterTime)
        userLoginTimeInSeconds = convertNanoToSec(userLoginTime)
    }

    /**
     * Convert given [time] from nanoseconds to seconds.
     *
     * To get a value that has been rounded to two decimal places using the [round] method, multiply it by 100 first and divide it by 100 after the [round] method was called.
     */
    private fun convertNanoToSec(time: Long): Double {
        return round((time / 1000000000.0) * 100) / 100.0
    }

    // personal user data for study and statistics
    /**
     * Represents the age of the user.
     *
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
     *
     * 0 - not specified
     * 1 - Female
     * 2 - Male
     * 3 - Diverse
     */
    private var _gender = MutableLiveData<Int>(0)
    val gender: LiveData<Int> = _gender

    /**
     * Sets the gender specified by the user immediately when user selects it from radio group in [UserDataFragment][de.tuchemnitz.armadillogin.ui.welcome.UserDataFragment].
     */
    fun setGender(genderInput: Int) {
        _gender.value = genderInput
    }

    /**
     * Represents usage frequency of smartphones by the user.
     *
     * 0 - not specified
     * 1 - I don't have a smartphone
     * 2 - I use it once a week or less
     * 3 - I use it a couple times a week
     * 4 - I use it every day
     * 5 - I use it multilpe times per day
     */
    private var _technicalExperienceFrequency = MutableLiveData<Int>(0)
    val technicalExperienceFrequency: LiveData<Int> = _technicalExperienceFrequency

    /**
     * Sets the usage frequency of technical devices specified by the user immediately when user selects it from radio group in UserDataFragment.
     */
    fun setTechnicalExperienceFrequency(technicalExperienceInput: Int) {
        _technicalExperienceFrequency.value = technicalExperienceInput
    }


    /**
     * Represents usage frequency of technical devices of the user.
     *
     * 0 - not specified
     * 1 - I don't use apps, and I'm not familiar with typical app user interfaces.
     * 5 - I use apps for a wide variety of application purposes, know at least most of the settings, and often find my way around new apps on my own without problems
     */
    private var _technicalExperienceVariety = MutableLiveData(0)
    val technicalExperienceVariety: LiveData<Int> = _technicalExperienceVariety

    fun setTechnicalExperienceVariety(technicalExperienceInput: Int) {
        _technicalExperienceVariety.value = technicalExperienceInput
    }

    /**
     * Stores the Participation code of the participant.
     */
    private var participationNumber = "PART000"

    /**
     * Receives participant code of the participant from [UserDataFragment][de.tuchemnitz.armadillogin.ui.welcome.UserDataFragment].
     */
    fun setParticipationNumber(partNumber: String) {
        participationNumber = partNumber
    }

    /**
     * Is true while data are being sent to MySQL Database via PHP script.
     */
    private var _sendingStudyData = MutableLiveData(false)
    val sendingStudyData: LiveData<Boolean> = _sendingStudyData

    /**
     * Is true if all study data has been sent successfully.
     */
    private var _sentStudyData = MutableLiveData(false)
    val sentStudyData: LiveData<Boolean> = _sentStudyData


    /**
     * HTTP client.
     *
     * This client will be used to connect to the PHP script to which the study data will be sent.
     * This script will then send the data to my study database.
     */
    private val client = OkHttpClient.Builder()
        .addInterceptor(AddHeaderInterceptor())
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(40, TimeUnit.SECONDS)
        .connectTimeout(40, TimeUnit.SECONDS)
        .build()

    /**
     * Implements functionality to send user study data to Database.
     *
     * While the function is running, the value of [_sendingStudyData] is true.
     * After the function successfully finished sending data, the value of [_sentStudyData] will be set true.
     */
    fun sendData() {
        _sendingStudyData.value = true

        /**
         * Build JSON string which will be part of the request that will be sent to the server.
         * It includes all data that is important for the study. The server will take this data and transfer it into an database which is hosted in tu chemnitz datacenter.
         */
        val output = StringWriter()
        JsonWriter(output).use { jsonWriter ->
            jsonWriter.beginObject()
            jsonWriter.name("age").value(age.value)
            jsonWriter.name("gender").value(gender.value)
            jsonWriter.name("experienceFrequency").value(technicalExperienceFrequency.value)
            jsonWriter.name("experienceVariety").value(technicalExperienceVariety.value)
            jsonWriter.name("time").value(userTimeInSeconds)
            jsonWriter.name("nanotime").value(userTime)
            jsonWriter.name("userRegisterTime").value(userRegisterTimeInSeconds)
            jsonWriter.name("userLoginTime").value(userLoginTimeInSeconds)
            jsonWriter.name("participationNumber").value(participationNumber)
            jsonWriter.endObject()
        }

        /**
         * Build HTTP request which will be sent to server.
         */
        val call = client.newCall(
            Request.Builder()
                .url("${STUDY_DB_URL}/create.php")
                .method("POST", output.toString().toRequestBody(JSON))
                .build()
        )

        /**
         * Execute the built HTTP request.
         *
         * If this is not successful show an appropriate toast message.
         */
        val response: Response
        runBlocking {
            response = withContext(Dispatchers.Default) {
                call.execute()
            }
        }
        if (!response.isSuccessful) {
            Toast.makeText(
                getApplication(),
                getApplication<Application>().getString(R.string.user_overview_data_sent_error),
                Toast.LENGTH_SHORT
            ).show()
        } else {
            Toast.makeText(
                getApplication(),
                getApplication<Application>().getString(R.string.user_overview_data_sent_success),
                Toast.LENGTH_SHORT
            ).show()
            _sentStudyData.value = true
        }
        _sendingStudyData.value = false
    }
}