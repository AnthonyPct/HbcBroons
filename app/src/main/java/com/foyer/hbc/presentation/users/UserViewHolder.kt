package com.foyer.hbc.presentation.users

import android.content.res.ColorStateList
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.foyer.hbc.R
import com.foyer.hbc.databinding.UserItemBinding
import com.foyer.hbc.domain.model.UserEntity
import com.foyer.hbc.domain.model.getUrlImages
import com.foyer.hbc.presentation.users.select.ISelectUserContract
import com.google.firebase.storage.FirebaseStorage

class UserViewHolder(private val binding: UserItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    ///////////////////////////////////////////////////////////////////////////
    // PUBLIC API
    ///////////////////////////////////////////////////////////////////////////

    fun bind(user: UserEntity, listener: ISelectUserContract.ViewEvent, isSelected: Boolean) {
        binding.userName.text = user.nom
        binding.userBalance.text = user.balance.toString()
        binding.userTeam.text = user.equipe
        binding.userConsumption.text = user.consumptionsPayed.toString()
        binding.userCell.setOnClickListener {
            listener.onClickUser(user)
        }

        if (isSelected) {
            changeSelectedColor(
                binding.userTeamImg,
                binding.root.resources.getColor(R.color.cornflower_blue)
            )
            changeSelectedColor(
                binding.userBalanceImg,
                binding.root.resources.getColor(R.color.tomato)
            )
            changeSelectedColor(
                binding.userConsumptionsImg,
                binding.root.resources.getColor(R.color.yellow)
            )
        }

        val storage = FirebaseStorage.getInstance()
        val gsReference =
            storage.getReferenceFromUrl("gs://hbcbroons.appspot.com/images/${user.getUrlImages()}")

        Glide.with(binding.root)
            .load(gsReference)
            .circleCrop()
            .placeholder(R.drawable.ic_icon_avatar)
            .error(R.drawable.ic_icon_avatar)
            .into(binding.userImage)
    }

    ///////////////////////////////////////////////////////////////////////////
    // HELPER
    ///////////////////////////////////////////////////////////////////////////

    private fun changeSelectedColor(image: AppCompatImageView, color: Int) {
        with(image) {
            ViewCompat.setBackgroundTintList(this, ColorStateList.valueOf(color))
            DrawableCompat.setTint(
                DrawableCompat.wrap(this.drawable),
                binding.root.resources.getColor(R.color.white)
            )
        }
    }
}
