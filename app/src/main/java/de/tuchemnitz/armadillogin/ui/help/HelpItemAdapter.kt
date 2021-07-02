package de.tuchemnitz.armadillogin.ui.help

import android.content.Context
import android.text.Html
import android.text.Html.FROM_HTML_MODE_LEGACY
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import de.tuchemnitz.armadillogin.R
import de.tuchemnitz.armadillogin.data.HelpData
import de.tuchemnitz.armadillogin.fido2api.Credential
import de.tuchemnitz.armadillogin.model.ArmadilloViewModel
import de.tuchemnitz.armadillogin.ui.user.CredentialAdapter
import de.tuchemnitz.armadillogin.ui.user.CredentialAdapter.CredentialViewHolder

/**
 * [RecyclerView] adapter used to display the list of help resources for the current Fragment.
 *
 * The adapter manages the RecyclerView [HelpItemViewHolder]s and binds the values from the help resource list to the holders.
 * If a holder is currently (re)usable, the current help resource is bound to it.
 * If no holder is (re)usable, a new one is created and the current help resource is then bound to it.
 * As soon as the user scrolls further and a Holder leaves the screen, it is no longer needed to display its current content and can be reused by binding a new content to it.
 *
 * @property context necessary to be able to convert resource IDs from [data] to resources.
 * @property data retrieved list of help resources from [HelpViewModel] which was parsed from /assets/helpdata.xml first and then resource ID [String]s were converted to resource IDs in [HelpViewModel].
 * @property lifecycleOwner Is necessary to set an observer to dyslexicFont value in [armadilloViewModel]. This is needed to enable and disable dyslexic font mode immediately.
 * @property armadilloViewModel Is necessary because the dyslexicFont value is saved there. This value represents whether dyslexic font mode is enabled or not.
 */
class HelpItemAdapter(
    private val context: Context,
    private val data: List<HelpData>,
    private val armadilloViewModel: ArmadilloViewModel,
    private val lifecycleOwner: LifecycleOwner
) : RecyclerView.Adapter<HelpItemAdapter.HelpItemViewHolder>() {

    /**
     * ViewHolder class used by [HelpItemAdapter].
     *
     * Contains content of one help item from [data].
     *
     * @property view binding object instance, refers to item_help.xml
     */
    class HelpItemViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.item_help_title_image)
        val headlineView: TextView = view.findViewById(R.id.item_help_title)
        val textView: TextView = view.findViewById(R.id.item_help_text)
    }

    /**
     * If there is no (re)usable [HelpItemViewHolder]: create a new one.
     *
     * Create a new [HelpItemViewHolder] by using the layout of item_help.xml and calling [LayoutInflater] to instantiate this layout.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HelpItemViewHolder {
        // create a new view
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_help, parent, false)

        return HelpItemViewHolder(adapterLayout)
    }

    /**
     * Count the items from the parsed help data list.
     */
    override fun getItemCount(): Int {
        return data.size
    }

    /**
     * If there is a (re)usable [HelpItemViewHolder]: change its content.
     *
     * Change the content of the [HelpItemViewHolder] by retrieving the help item of the current position from the [data] list and bind them to this [HelpItemViewHolder].
     * Also there is a observer to dyslexicFont value in [armadilloViewModel]. If the dyslexicFont value changes, the font of every text resource in this ViewHolder instance will be changed immediately.
     */
    override fun onBindViewHolder(holder: HelpItemViewHolder, position: Int) {
        val helpItem = data[position]

        // get the right string and set it as value for the text of the textview inside the itemViewHolder
        val stringRes = context.resources.getString(helpItem.stringResourceId)

        // use HtmlCompat to get HTML formatting in help item's text
        holder.textView.text = HtmlCompat.fromHtml(stringRes, HtmlCompat.FROM_HTML_MODE_LEGACY)
        holder.textView.movementMethod = LinkMovementMethod.getInstance()
        holder.headlineView.text = context.resources.getString(helpItem.titleResourceId)

        if (helpItem.imageResourceId != null) {
            holder.imageView.visibility = VISIBLE
            holder.imageView.setImageResource(helpItem.imageResourceId)
        } else {
            holder.imageView.visibility = GONE
        }

        armadilloViewModel.dyslexicFont.observe(lifecycleOwner) { dyslexicEnabled ->
            if (dyslexicEnabled) {
                holder.headlineView.setTextAppearance(R.style.TextAppearance_DyslexicTypographyStyles_Headline4)
                holder.textView.setTextAppearance(R.style.TextAppearance_DyslexicTypographyStyles_Body1)
            } else {
                holder.headlineView.setTextAppearance(R.style.TextAppearance_StandardTypographyStyles_Headline4)
                holder.textView.setTextAppearance(R.style.TextAppearance_StandardTypographyStyles_Body1)
            }
        }
    }
}