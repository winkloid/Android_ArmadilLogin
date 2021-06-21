package de.tuchemnitz.armadillogin.model

import android.app.Application
import android.util.JsonWriter
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

class StudyUserDataViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        private const val LOG_TAG = "StudyUserDataModel"
        private const val STUDY_DB_URL = "https://www-user.tu-chemnitz.de/~owin/armadillogin/api"
        private val JSON = "application/json".toMediaTypeOrNull()
    }

    // private val studyDbApi = StudyDbApi.getInstance()

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


    private val client = OkHttpClient.Builder()
        .addInterceptor(AddHeaderInterceptor())
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(40, TimeUnit.SECONDS)
        .connectTimeout(40, TimeUnit.SECONDS)
        .build()

    /**
     * Implements functionality to send user study data to Database at TU Chemnitz URZ.
     * While the function is running, the value of [_sendingStudyData] is true.
     * After the function successfully finished sending data, the value of [_sentStudyData] becomes true.
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
            jsonWriter.name("experience").value(technicalExperience.value)
            jsonWriter.name("time").value(userTimeInSeconds)
            jsonWriter.name("nanotime").value(userTime)
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