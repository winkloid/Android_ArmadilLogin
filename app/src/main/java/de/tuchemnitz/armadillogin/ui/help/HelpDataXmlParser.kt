package de.tuchemnitz.armadillogin.ui.help

import android.util.Log
import android.util.Xml
import de.tuchemnitz.armadillogin.data.HelpDataTagged
import de.tuchemnitz.armadillogin.model.FragmentStatus
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import java.io.IOException
import java.io.InputStream

/**
 * This class will help to parse the helpdata.xml which is located in /assets folder.
 *
 * It will use an [XmlPullParser] instance to parse the file. This file includes links to all help resources which will be accessible via the help tab.
 * The file has the following structure:
 * <resources>                          // Indicate that this file contains resource links. However, this tag is necessary for android studio to not show any errors.
 *     <helpitem>                       // Indicates the beginning of a help resource item
 *          <tag>...</tag>              // One helpitem can contain many <tag> tags. Every <tag> contains a tag of a fragment for which the help resource will be shown in help tab.
 *          <titleres>...</titleres>    // Contains resource name of the text resource that contains the title of this particular help item.
 *          <textres>...</textres>      // Contains resource name of the text resource that contains the text for this particular help resource.
 *          <imgres>...</imgres>        // Contains resource name of the image resource that contains the image for this particular help resource.
 *     </helpitem>                      // marks the end of the helpitem
 * </resources>                         // marks end of file
 */
class HelpDataXmlParser {

    companion object {
        private const val LOG_TAG = "XML_PARSER"
    }

    /**
     * Indicates that I won't use XML namespaces in this class.
     */
    private val namespace: String? = null


    /**
     * Initializes [XmlPullParser], starts parsing process and invokes [readXml]
     *
     * @param inputStream is the /assets/helpdata.xml converted to inputStream. This is necessary to apply [XmlPullParser] on it.
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
     *
     * Searches all helpitem tags and calls functions to process nested tags.
     * Calls [readHelpItem] when helpitem tag is found to read and process everything which is inside the opening and closing helpitem tags
     * Calls [skip] to skip all other (unknown) tags.
     *
     * @param parser is the [XmlPullParser] instance that was created in [parse] method.
     */
    @Throws(XmlPullParserException::class, IOException::class)
    private fun readXml(parser: XmlPullParser): List<HelpDataTagged> {
        val helpItems = mutableListOf<HelpDataTagged>()

        //start parsing when start tag named "resources" has been found
        parser.require(XmlPullParser.START_TAG, namespace, "resources")

        // continue if opening resources tag has not been reached yet
        // if parser finds a helpItem tag: process its contents and add it to helpItems list
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.eventType != XmlPullParser.START_TAG) {
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
     *
     * If a <tag>, <textres> or <imgres> tag is found, corresponding read methods are called.
     * Returns [HelpDataTagged] instance.
     *
     * @param parser is the [XmlPullParser] instance that was created in [parse] method.
     */
    @Throws(XmlPullParserException::class, IOException::class)
    private fun readHelpItem(parser: XmlPullParser): HelpDataTagged {
        parser.require(XmlPullParser.START_TAG, namespace, "helpitem")

        var titleResId: String? = null
        var stringResourceId: String? = null
        var imageResourceId: String? = null
        val tagList = mutableListOf<FragmentStatus>()

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.eventType != XmlPullParser.START_TAG) {
                continue
            }
            when (parser.name) {
                "tag" -> tagList.add(readTagList(parser))
                "titleres" -> titleResId = readTitleRes(parser)
                "textres" -> stringResourceId = readTextRes(parser)
                "imgres" -> imageResourceId = readImgRes(parser)
                else -> skip(parser)
            }

            Log.d(LOG_TAG, "imgResource: $imageResourceId")
        }
        return HelpDataTagged(titleResId, stringResourceId, imageResourceId, tagList)
    }

    /**
     * Parse a <tag> tag.
     *
     * Convert the parsed [String] to [FragmentStatus] values.
     *
     * @param parser is the [XmlPullParser] instance that was created in [parse] method.
     * @return The [FragmentStatus] value of one fragment for which the help resource will be shown in help tab.
     */
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
            "LOGIN_USERNAME" -> FragmentStatus.LOGIN
            "LOGIN_KEY" -> FragmentStatus.LOGIN_KEY
            "USER_OVERVIEW" -> FragmentStatus.USER_OVERVIEW
            else -> FragmentStatus.DEFAULT
        }
    }

    /**
     * Parse the resource name of the help item's titile.
     *
     * Uses [readText] to read text between <titleres> tags.
     *
     * @param parser is the [XmlPullParser] instance that was created in [parse] method.
     * @return [String] that contains the resource name of the help item's title.
     */
    @Throws(XmlPullParserException::class, IOException::class)
    private fun readTitleRes(parser: XmlPullParser): String {
        parser.require(XmlPullParser.START_TAG, namespace, "titleres")
        val titleResAsString = readText(parser)
        parser.require(XmlPullParser.END_TAG, namespace, "titleres")

        return titleResAsString
    }

    /**
     * Parse the resource name of the help item's help text.
     *
     * Uses [readText] to read text between <textres> tags.
     *
     * @param parser is the [XmlPullParser] instance that was created in [parse] method.
     * @return [String] that contains the resource name of the help item's text.
     */
    @Throws(XmlPullParserException::class, IOException::class)
    private fun readTextRes(parser: XmlPullParser): String {
        parser.require(XmlPullParser.START_TAG, namespace, "textres")
        val textResAsString = readText(parser)
        parser.require(XmlPullParser.END_TAG, namespace, "textres")

        return textResAsString
    }

    /**
     * Parse the resource name of the help item's title image.
     *
     * Uses [readText] to read text between <imgres> tags.
     *
     * @param parser is the [XmlPullParser] instance that was created in [parse] method.
     * @return [String] that contains the resource name of the help item's title image.
     */
    @Throws(XmlPullParserException::class, IOException::class)
    private fun readImgRes(parser: XmlPullParser): String {
        parser.require(XmlPullParser.START_TAG, namespace, "imgres")
        val imgResAsString = readText(parser)
        parser.require(XmlPullParser.END_TAG, namespace, "imgres")

        return imgResAsString
    }

    /**
     * Reads the text between opening and closing XML tags.
     *
     * @param parser is the [XmlPullParser] instance that was created in [parse] method.
     * @return an empty [String] ("") if there is no text between the XML tags and the text if there is any text between the tags.
     */
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
     *
     * Tracks depth to skip also nested tags.
     *
     * @param parser is the [XmlPullParser] instance that was created in [parse] method.
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