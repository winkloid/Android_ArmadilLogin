package de.tuchemnitz.armadillogin.ui.help

import android.util.Log
import android.util.Xml
import de.tuchemnitz.armadillogin.data.HelpDataTagged
import de.tuchemnitz.armadillogin.model.FragmentStatus
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import java.io.IOException
import java.io.InputStream

class HelpDataXmlParser {

    companion object {
        private const val LOG_TAG = "XML_PARSER"
    }

    // don't use Xml namespaces
    private val namespace: String? = null


    /**
     * Initializes [XmlPullParser], starts parsing process and invokes [readXml]
     */
    @Throws(XmlPullParserException::class, IOException::class)
    fun parse(inputStream: InputStream?): List<HelpDataTagged> {
        inputStream.use { iS ->
            val parser: XmlPullParser = Xml.newPullParser()
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
            parser.setInput(iS, null)
            parser.nextTag()
            return readXml(parser)
        }
    }

    /**
     * Process the Xml file.
     * Searches all helpitem tags and calls functions to process nested tags.
     * Calls [readHelpItem] when helpitem tag is found to read and process everything inside opening and closing helpitem tag
     * Calls [skip] to skip all other tags
     */
    @Throws(XmlPullParserException::class, IOException::class)
    private fun readXml(parser: XmlPullParser): List<HelpDataTagged> {
        val helpItems = mutableListOf<HelpDataTagged>()

        //start parsing when start tag named "resources" has been found
        parser.require(XmlPullParser.START_TAG, namespace, "resources")

        // continue if opening resources tag has not been reached yet
        // if parser finds a helpItem tag: process its contents and add it to helpItems list
        while (parser.next() !=XmlPullParser.END_TAG) {
            if(parser.eventType != XmlPullParser.START_TAG) {
                continue
            }
            if (parser.name == "helpitem") {
                helpItems.add(readHelpItem(parser))
            } else {
                skip(parser)
            }
        }
        return helpItems
    }

    /**
     * Parses contents of one helpItem.
     * If a tag, textres or imgres tag is found, corresponding read methods are called.
     * Returns [HelpDataTagged] instance.
     */
    @Throws(XmlPullParserException::class, IOException::class)
    private fun readHelpItem(parser: XmlPullParser): HelpDataTagged {
        parser.require(XmlPullParser.START_TAG, namespace, "helpitem")
        var stringResourceId: String? = null
        var imageResourceId: String? = null
        val tagList = mutableListOf<FragmentStatus>()

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.eventType != XmlPullParser.START_TAG) {
                continue
            }
            when (parser.name) {
                "tag" -> tagList.add(readTagList(parser))
                "textres" -> stringResourceId = readTextRes(parser)
                "imgres" -> imageResourceId = readImgRes(parser)
                else -> skip(parser)
            }

            Log.d(LOG_TAG, "imgResource: $imageResourceId")
        }
        return HelpDataTagged(stringResourceId, imageResourceId, tagList)
    }

    @Throws(XmlPullParserException::class, IOException::class)
    private fun readTagList(parser: XmlPullParser): FragmentStatus {
        parser.require(XmlPullParser.START_TAG, namespace, "tag")
        val tagAsString = readText(parser)
        parser.require(XmlPullParser.END_TAG, namespace, "tag")

        return when (tagAsString) {
            "WELCOME" -> FragmentStatus.WELCOME
            "REGISTER_LOGIN" -> FragmentStatus.REGISTER_LOGIN
            "REGISTER1" -> FragmentStatus.REGISTER1
            "REGISTER2" -> FragmentStatus.REGISTER2
            "REGISTER_SUMMARY" -> FragmentStatus.REGISTER_SUMMARY
            "REGISTER_KEY" -> FragmentStatus.REGISTER_KEY
            "REGISTER_FINISHED" -> FragmentStatus.REGISTER_FINISHED
            "REGISTER_ERROR" -> FragmentStatus.REGISTER_ERROR
            "LOGIN" -> FragmentStatus.LOGIN
            "LOGIN_KEY" -> FragmentStatus.LOGIN_KEY
            "USER_OVERVIEW" -> FragmentStatus.USER_OVERVIEW
            else -> FragmentStatus.DEFAULT
        }
    }

    @Throws(XmlPullParserException::class, IOException::class)
    private fun readTextRes(parser: XmlPullParser): String {
        parser.require(XmlPullParser.START_TAG, namespace, "textres")
        val textResAsString = readText(parser)
        parser.require(XmlPullParser.END_TAG, namespace, "textres")

        return textResAsString
    }

    @Throws(XmlPullParserException::class, IOException::class)
    private fun readImgRes(parser: XmlPullParser): String {
        parser.require(XmlPullParser.START_TAG, namespace, "imgres")
        val imgResAsString = readText(parser)
        parser.require(XmlPullParser.END_TAG, namespace, "imgres")

        return imgResAsString
    }

    @Throws(XmlPullParserException::class, IOException::class)
    private fun readText(parser: XmlPullParser): String {
        var result = ""
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.text
            parser.nextTag()
        }
        return result
    }

    /**
     * Skips all xml tags which are not important.
     * Tracks depth to skip also nested tags
     */
    @Throws(XmlPullParserException::class, IOException::class)
    private fun skip(parser: XmlPullParser) {
        if (parser.eventType != XmlPullParser.START_TAG) {
            throw IllegalStateException()
        }
        var depth = 1
        while (depth != 0) {
            when (parser.next()) {
                XmlPullParser.END_TAG -> depth--
                XmlPullParser.START_TAG -> depth++
            }
        }
    }
}