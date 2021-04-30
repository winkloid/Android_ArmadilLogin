package de.tuchemnitz.armadillogin.ui.help

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import de.tuchemnitz.armadillogin.R
import de.tuchemnitz.armadillogin.model.HelpData

class HelpItemAdapter(private val context: Context, private val data: List<HelpData>) : RecyclerView.Adapter<HelpItemAdapter.HelpItemViewHolder>(){

    class HelpItemViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.item_help_title_image)
        val textView: TextView = view.findViewById(R.id.item_help_title)
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
        holder.textView.text = context.resources.getString(helpItem.stringResourceId)
        if(helpItem.imageResourceId != null) {
            holder.imageView.setImageResource(helpItem.imageResourceId)
        }
    }

}