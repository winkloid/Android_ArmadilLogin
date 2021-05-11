package de.tuchemnitz.armadillogin.ui.user

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import de.tuchemnitz.armadillogin.databinding.ItemCredentialBinding
import de.tuchemnitz.armadillogin.fido2api.Credential

class CredentialAdapter(
    private val onDeleteClicked: (String) -> Unit
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

        fun bind(credentialItem : Credential) {
            binding.credential = credentialItem
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
        Log.d("CredentialAdapter", "${getItem(position)}")
    }

}