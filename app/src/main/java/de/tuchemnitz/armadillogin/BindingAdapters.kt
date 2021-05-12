package de.tuchemnitz.armadillogin

import androidx.databinding.BindingAdapter
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import de.tuchemnitz.armadillogin.data.HelpData
import de.tuchemnitz.armadillogin.ui.help.HelpItemAdapter

@BindingAdapter(value = ["textContent", "viewContext"])
fun bindText(recyclerView: RecyclerView, textContent: List<HelpData>, viewContext: FragmentActivity) {
    recyclerView.adapter = HelpItemAdapter(viewContext, textContent)
    recyclerView.setHasFixedSize(true)
}