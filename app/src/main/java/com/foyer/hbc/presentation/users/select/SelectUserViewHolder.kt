package com.foyer.hbc.presentation.users.select

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.foyer.hbc.R
import com.foyer.hbc.databinding.SelectUserItemBinding
import com.foyer.hbc.domain.model.UserEntity
import com.foyer.hbc.domain.model.getUrlImages
import com.google.firebase.storage.FirebaseStorage

class SelectUserViewHolder(private val binding: SelectUserItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    ///////////////////////////////////////////////////////////////////////////
    // PUBLIC API
    ///////////////////////////////////////////////////////////////////////////

    fun bind(user: UserEntity, listener: ISelectUserContract.ViewEvent) {
        binding.userName.text = user.nom
        binding.userBalance.text =
            itemView.context.getString(R.string.order_select_user_balance, user.balance.toString())
        binding.userCell.setOnClickListener {
            listener.onClickUser(user)
        }

        val storage = FirebaseStorage.getInstance()

        // Create a reference to a file from a Google Cloud Storage URI
        val gsReference =
            storage.getReferenceFromUrl("gs://hbcbroons.appspot.com/images/${user.getUrlImages()}")

        Glide.with(binding.root)
            .load(gsReference)
            .circleCrop()
            .into(binding.userImage)
    }
}
