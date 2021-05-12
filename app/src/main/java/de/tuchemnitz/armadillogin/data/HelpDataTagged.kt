package de.tuchemnitz.armadillogin.data

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import de.tuchemnitz.armadillogin.model.FragmentStatus

/**
 * Data class for storing [stringResourceId] and [imageResourceId] of help resources and their category tags which are stored in a [tagList].
 * All these data will be retrieved from an XML file containing these IDs and tags which describe when the corresponding resource should be used.
 */
data class HelpDataTagged (
    /**
     * [stringResourceId] and [imageResourceId] contain the IDs of the corresponding string and image resources which will be visible to the user
     * if there is a suitable tag contained in the [tagList]
     */
    val stringResourceId: String,

    /**
     * [stringResourceId] and [imageResourceId] contain the IDs of the corresponding string and image resources which will be visible to the user
     * if there is a suitable tag included in the [tagList]
     */
    val imageResourceId: String? = null,

    /**
     * [tagList] contains all tags of the corresponding resources. These tags describe in which fragments the resources can be used.
     * Every tag is assigned to a specific fragment. If the help tab is called, only resources which are categorized by the assigned tag shall be displayed.
     */
    val tagList: List<FragmentStatus>,
)