package de.tuchemnitz.armadillogin.ui.help

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import de.tuchemnitz.armadillogin.R
import de.tuchemnitz.armadillogin.model.FragmentStatus
import de.tuchemnitz.armadillogin.data.HelpData
import java.io.InputStream
import java.nio.channels.AsynchronousFileChannel.open

class HelpViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        private const val LOG_TAG = "HELP_VIEWMODEL"
    }

    var parseInputStream: InputStream = application.assets.open("helpdata.xml")
    private val parsedHelpList = HelpDataXmlParser().parse(parseInputStream)
    private val appContext = application

    fun getCurrentHelp(status: FragmentStatus?): String {
        return when(status) {
            FragmentStatus.WELCOME -> "Willkommen"
            FragmentStatus.LOGIN -> "Einloggen"
            FragmentStatus.REGISTER_LOGIN -> "Registrieren oder einloggen"
            FragmentStatus.REGISTER1 -> "Persönliche Daten eingeben"
            FragmentStatus.REGISTER2 -> "Nutzernamen vergeben"
            FragmentStatus.REGISTER_SUMMARY -> "Bestätige die Richtigkeit der eingegebenen Daten oder passe Sie an."
            FragmentStatus.REGISTER_KEY -> "Registriere deinen Schlüssel."
            else -> "Strange thing..."
        }
    }

    /**
     * Iterates through [parsedHelpList] which was parsed from [assets/helpdata.xml].
     * Converts strings which are included in the elements of [parsedHelpList] to resource id values, brings them in a new list and returns this list.
     */
    fun loadHelpData(status: FragmentStatus?): List<HelpData> {
        Log.d(LOG_TAG, "parsedList: $parsedHelpList")
        val helpItems = mutableListOf<HelpData>()

        for (helpItem in parsedHelpList) {
            if (helpItem.tagList.contains(status) || helpItem.tagList.contains(FragmentStatus.DEFAULT)) {
                var stringResourceId: Int? = null
                var imageResourceId: Int? = null

                val xmlStringResourceId = helpItem.stringResourceId
                val xmlImageResourceId = helpItem.imageResourceId

                // check if xmlStringResourceId is null - if not, convert string to resource id; if it is null, assign default value to stringResourceId
                if (!xmlStringResourceId.isNullOrBlank()) {
                    stringResourceId = appContext.resources.getIdentifier(xmlStringResourceId, "string", appContext.packageName)
                } else {
                    stringResourceId = R.string.help_not_found
                }

                // check if xmlImageResourceId is null - if not, convert string to resource id; if it is null, assign 0 to imageResourceId
                if (!xmlImageResourceId.isNullOrBlank()) {
                    imageResourceId = appContext.resources.getIdentifier(xmlImageResourceId, "drawable", appContext.packageName)
                } else {
                    imageResourceId = null
                }
                helpItems.add(HelpData(stringResourceId, imageResourceId))
            }
        }
        return helpItems
        /*return listOf<HelpData>(
                HelpData(R.string.help1, R.drawable.image1),
                HelpData(R.string.help2),
                HelpData(R.string.help3),
                HelpData(R.string.help4),
                HelpData(R.string.help5, R.drawable.image2),
                HelpData(R.string.help6),
                HelpData(R.string.help7, R.drawable.image3),
                HelpData(R.string.help8),
                HelpData(R.string.help9),
                HelpData(R.string.help10)
        )*/
    }
}