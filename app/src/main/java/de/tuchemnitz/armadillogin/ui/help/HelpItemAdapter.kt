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
import de.tuchemnitz.armadillogin.model.ArmadilloViewModel

class HelpItemAdapter(
    private val context: Context,
    private val data: List<HelpData>,
    private val armadilloViewModel: ArmadilloViewModel,
    private val lifecycleOwner: LifecycleOwner
) : RecyclerView.Adapter<HelpItemAdapter.HelpItemViewHolder>() {

    class HelpItemViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.item_help_title_image)
        val headlineView: TextView = view.findViewById(R.id.item_help_title)
        val textView: TextView = view.findViewById(R.id.item_help_text)
    }

    // create new view holders if there are no free view holders any more
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HelpItemViewHolder {
        // create a new view
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_help, parent, false)

        return HelpItemViewHolder(adapterLayout)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    // if there is a view holder: replace contents of list item view inside of it
    override fun onBindViewHolder(holder: HelpItemViewHolder, position: Int) {
        val helpItem = data[position]

        // get the right string and set it as value for the text of the textview inside the itemViewHolder
        val stringRes = context.resources.getString(helpItem.stringResourceId)
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
            if(dyslexicEnabled) {
                holder.headlineView.setTextAppearance(R.style.TextAppearance_DyslexicTypographyStyles_Headline4)
                holder.textView.setTextAppearance(R.style.TextAppearance_DyslexicTypographyStyles_Body1)
            } else {
                holder.headlineView.setTextAppearance(R.style.TextAppearance_StandardTypographyStyles_Headline4)
                holder.textView.setTextAppearance(R.style.TextAppearance_StandardTypographyStyles_Body1)
            }
        }
    }
}