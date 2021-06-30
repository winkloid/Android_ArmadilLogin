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
 * The adapter manages the RecyclerView [CredentialViewHolder]s and binds the values from the retrieved list to the holders.
 * If a holder is currently (re)usable, the current value is bound to it.
 * If no holder is (re)usable, a new one is created and the current value is then bound to it.
 * As soon as the user scrolls further and a Holder leaves the screen, it is no longer needed to display its current content and can be reused by binding a new content to it.
 *
 * @property onDeleteClicked Will be triggered when the delete button is clicked. In this case, a new instance of DeleteConfirmationFragment will be created to show an AlterDialog in which the user has to confirm the deletion.
 * @property lifecycleOwner Is necessary to set an observer to dyslexicFont value in [armadilloViewModel]. This is needed to enable and disable dyslexic font mode immediately.
 * @property armadilloViewModel Is necessary because the dyslexicFont value is saved there. This value represents whether dyslexic font mode is enabled or not.
 */
class CredentialAdapter(
    private val onDeleteClicked: (String) -> Unit,
    private val lifecycleOwner: LifecycleOwner,
    private val armadilloViewModel: ArmadilloViewModel
) : ListAdapter<Credential, CredentialAdapter.CredentialViewHolder>(DiffCallback) {

    /**
     * ViewHolder class used by [CredentialAdapter].
     *
     * Shows content of one [Credential] item from the credential list.
     * It also includes a button that can be used to delete the item.
     * For this purpose a listener is set to the button.
     *
     * @property binding Binding object instance. Refers to item_credential.xml
     * @property onDeleteClicked If the delete button is clicked a new instance of DeleteConfirmationFragment will be created to show an AlterDialog in which the user has to confirm the deletion.
     */
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
     * Allows RecyclerView to determine whether [Credential] items have changed in an update of the [Credential] list.
     *
     * Has methods to determine whether content or [Credential] ID of items have changed.
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
     * If there is no (re)usable [CredentialViewHolder]: create a new one.
     *
     * Create a new [CredentialViewHolder] by using the layout of item_credential.xml and calling [LayoutInflater] to instantiate this layout.
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
     * If there is a (re)usable [CredentialViewHolder]: change its content.
     *
     * Change the content of the [CredentialViewHolder] by resetting the credential variable of the view holder instance.
     * Also there is a observer to dyslexicFont value in [armadilloViewModel]. If the dyslexicFont value changes the font of every text resource in this ViewHolder instance will be changed immediately.
     */
    override fun onBindViewHolder(holder: CredentialViewHolder, position: Int) {
        holder.binding.credential = getItem(position)

        // implement dyslexicFont feature
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
    }
}