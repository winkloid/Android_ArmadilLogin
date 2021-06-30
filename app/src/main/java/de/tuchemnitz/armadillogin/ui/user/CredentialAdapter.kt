package de.tuchemnitz.armadillogin.ui.user

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import de.tuchemnitz.armadillogin.R
import de.tuchemnitz.armadillogin.databinding.ItemCredentialBinding
import de.tuchemnitz.armadillogin.fido2api.Credential
import de.tuchemnitz.armadillogin.model.ArmadilloViewModel

/**
 * [RecyclerView] adapter used to display the retrieved list of credentials.
 *
 * The adapter manages the RecyclerView holders and binds the values from the retrieved list to the holders.
 * If a holder is currently (re)usable, the current value is bound to it.
 * If no holder is (re)usable, a new one is created and the current value is then bound to it.
 * As soon as the user scrolls further and a Holder leaves the screen, it is no longer needed to display its current content and can be reused by binding a new content to it.
 */
class CredentialAdapter(
    private val onDeleteClicked: (String) -> Unit,
    private val lifecycleOwner: LifecycleOwner,
    private val armadilloViewModel: ArmadilloViewModel
) : ListAdapter<Credential, CredentialAdapter.CredentialViewHolder>(DiffCallback) {

    class CredentialViewHolder(
        val binding: ItemCredentialBinding, onDeleteClicked: (String) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.itemCredentialDelete.setOnClickListener {
                binding.credential?.let { credential ->
                    onDeleteClicked(credential.id)
                }
            }
        }
    }

    /**
     * Allows RecyclerView to determine whether [Credential] items have changed when the [List] of [Credential] gets updated.
     */
    companion object DiffCallback : DiffUtil.ItemCallback<Credential>() {
        override fun areItemsTheSame(oldItem: Credential, newItem: Credential): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Credential, newItem: Credential): Boolean {
            return oldItem == newItem
        }
    }

    /**
     * If there are no free [RecyclerView] item views: create a new one.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CredentialViewHolder {
        return CredentialViewHolder(
            ItemCredentialBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            onDeleteClicked
        )
    }

    /**
     * If there is a free [RecyclerView] item view: change its content
     */
    override fun onBindViewHolder(holder: CredentialViewHolder, position: Int) {
        holder.binding.credential = getItem(position)
        armadilloViewModel.dyslexicFont.observe(lifecycleOwner) { dyslexicEnabled ->
            if (dyslexicEnabled) {
                holder.binding.itemCredentialIdTitle.setTextAppearance(R.style.TextAppearance_DyslexicTypographyStyles_Headline6)
                holder.binding.itemCredentialPublickeyTitle.setTextAppearance(R.style.TextAppearance_DyslexicTypographyStyles_Headline6)
                holder.binding.itemCredentialId.setTextAppearance(R.style.TextAppearance_DyslexicTypographyStyles_Caption)
                holder.binding.itemCredentialPublickey.setTextAppearance(R.style.TextAppearance_DyslexicTypographyStyles_Caption)
            } else {
                holder.binding.itemCredentialIdTitle.setTextAppearance(R.style.TextAppearance_StandardTypographyStyles_Headline6)
                holder.binding.itemCredentialPublickeyTitle.setTextAppearance(R.style.TextAppearance_StandardTypographyStyles_Headline6)
                holder.binding.itemCredentialId.setTextAppearance(R.style.TextAppearance_StandardTypographyStyles_Caption)
                holder.binding.itemCredentialPublickey.setTextAppearance(R.style.TextAppearance_StandardTypographyStyles_Caption)
            }
        }
        Log.d("CredentialAdapter", "${getItem(position)}")
    }
}