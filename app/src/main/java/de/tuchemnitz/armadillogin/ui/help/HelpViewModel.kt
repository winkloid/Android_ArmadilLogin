package de.tuchemnitz.armadillogin.ui.help

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import de.tuchemnitz.armadillogin.R
import de.tuchemnitz.armadillogin.model.FragmentStatus
import de.tuchemnitz.armadillogin.data.HelpData
import java.io.InputStream

class HelpViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        private const val LOG_TAG = "HELP_VIEWMODEL"
    }

    /**
     * Contains /assets/helpdata.xml's content converted to [InputStream].
     *
     * This conversion is necessary to parse through it using [HelpDataXmlParser]
     */
    private var parseInputStream: InputStream = application.assets.open("helpdata.xml")

    /**
     * List of help item resources.
     *
     * In this state, the resource IDs are encoded as resource ID [String]s. These have to be converted to resource IDs.
     */
    private val parsedHelpList = HelpDataXmlParser().parse(parseInputStream)

    /**
     * Context of the application.
     *
     * This is necessary to work with resources that are linked to the application and to convert resource ID [String]s to resource ID values of the application.
     */
    private val appContext = application

    /**
     * Iterates through [parsedHelpList] which was parsed from [assets/helpdata.xml].
     *
     * Converts strings which are included in the elements of [parsedHelpList] to resource id values, brings them in a new list and returns this list.
     *
     * @param status Identifier of the current Fragment in Login tab.
     * @return List of [HelpData] that only contains resources that match the identifier stored in [status] variable. This list no longer contains resource IDs encoded in strings, but actual resource IDs.
     */
    fun loadHelpData(status: FragmentStatus?): List<HelpData> {
        val helpItems = mutableListOf<HelpData>()

        for (helpItem in parsedHelpList) {
            if (helpItem.tagList.contains(status) || helpItem.tagList.contains(FragmentStatus.DEFAULT)) {
                var titleResourceId: Int?
                var stringResourceId: Int?
                var imageResourceId: Int?

                val xmlTitleResourceId = helpItem.titleResourceId
                val xmlStringResourceId = helpItem.stringResourceId
                val xmlImageResourceId = helpItem.imageResourceId

                // check if xmlTitleResourceId is null - if not, convert string to resource id; if it is null, assign default value to stringResourceId
                if (!xmlStringResourceId.isNullOrBlank()) {
                    titleResourceId = appContext.resources.getIdentifier(
                        xmlTitleResourceId,
                        "string",
                        appContext.packageName
                    )
                } else {
                    titleResourceId = R.string.help_not_found
                }

                // check if xmlStringResourceId is null - if not, convert string to resource id; if it is null, assign default value to stringResourceId
                if (!xmlStringResourceId.isNullOrBlank()) {
                    stringResourceId = appContext.resources.getIdentifier(
                        xmlStringResourceId,
                        "string",
                        appContext.packageName
                    )
                } else {
                    stringResourceId = R.string.help_not_found
                }

                // check if xmlImageResourceId is null - if not, convert string to resource id; if it is null, assign null to imageResourceId
                if (!xmlImageResourceId.isNullOrBlank()) {
                    imageResourceId = appContext.resources.getIdentifier(
                        xmlImageResourceId,
                        "drawable",
                        appContext.packageName
                    )
                } else {
                    imageResourceId = null
                }
                helpItems.add(HelpData(titleResourceId, stringResourceId, imageResourceId))
            }
        }
        return helpItems
    }
}