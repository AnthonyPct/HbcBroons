package com.foyer.hbc.presentation.stats.graph

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.bumptech.glide.Glide
import com.foyer.hbc.R
import com.foyer.hbc.databinding.PodiumComponentBinding
import com.foyer.hbc.domain.model.UserEntity
import com.foyer.hbc.domain.model.getUrlImages
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class PodiumComponent @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    ///////////////////////////////////////////////////////////////////////////
    // DATA
    ///////////////////////////////////////////////////////////////////////////

    private var binding: PodiumComponentBinding = PodiumComponentBinding.inflate(
        LayoutInflater.from(context),
        this,
        true
    )

    ///////////////////////////////////////////////////////////////////////////
    // PUBLIC API
    ///////////////////////////////////////////////////////////////////////////

    fun initPodium(user: List<UserEntity>) {
        val storage = FirebaseStorage.getInstance()
        loadImage(getReference(storage, user.first()), binding.firstPlaceUser)
        loadImage(getReference(storage, user[1]), binding.secondPlaceUser)
        loadImage(getReference(storage, user[2]), binding.thirdPlaceUser)
        loadImage(getReference(storage, user[3]), binding.fourthPlaceUser)
        loadImage(getReference(storage, user[4]), binding.fivePlaceUser)
        setText(user.first(), binding.firstPlace, "1")
        setText(user[1], binding.secondPlace, "2")
        setText(user[2], binding.thirdPlace, "3")
        setText(user[3], binding.fourthPlace, "4")
        setText(user[4], binding.fivePlace, "5")
    }

    ///////////////////////////////////////////////////////////////////////////
    // HELPER
    ///////////////////////////////////////////////////////////////////////////

    private fun loadImage(reference: StorageReference, imageView: ImageView) {
        Glide.with(binding.root)
            .load(reference)
            .circleCrop()
            .placeholder(R.drawable.ic_icon_avatar)
            .error(R.drawable.ic_icon_avatar)
            .into(imageView)
    }

    private fun getReference(firebase: FirebaseStorage, user: UserEntity): StorageReference {
        return firebase.getReferenceFromUrl("gs://hbcbroons.appspot.com/images/${user.getUrlImages()}")
    }

    private fun setText(user: UserEntity, textView: TextView, position: String) {
        textView.text = "$position \n ${user.nom} : ${user.consumptionsPayed}"
    }
}