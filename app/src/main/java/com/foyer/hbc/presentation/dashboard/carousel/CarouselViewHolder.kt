package com.foyer.hbc.presentation.dashboard.carousel

import android.content.res.ColorStateList
import android.os.Build
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.foyer.hbc.R
import com.foyer.hbc.common.withCurrency
import com.foyer.hbc.databinding.CarouselCardItemBinding
import com.foyer.hbc.domain.data.dashboard.CarouselCellType
import kotlin.math.absoluteValue

class CarouselViewHolder(private val binding: CarouselCardItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    ///////////////////////////////////////////////////////////////////////////
    // PUBLIC API
    ///////////////////////////////////////////////////////////////////////////

    fun bind(cell: CarouselCellType) {
        bindMainInfo(cell)
        bindMainText(cell)
        bindSubtitle(cell)
    }

    ///////////////////////////////////////////////////////////////////////////
    // HELPERS
    ///////////////////////////////////////////////////////////////////////////

    // Set image with title
    private fun bindMainInfo(cell: CarouselCellType) {
        binding.cardImage.apply {
            setImageResource(cell.info.icon)
            val tint = ColorStateList.valueOf(resources.getColor(cell.info.backgroundIcon))
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                backgroundTintList = tint
            } else {
                ViewCompat.setBackgroundTintList(this, tint)
            }
        }
        binding.cardTitle.setText(cell.info.title)
    }


    // Set main text
    private fun bindMainText(cell: CarouselCellType) {
        binding.cardMainText.text = when (cell) {
            is CarouselCellType.Checkout -> {
                cell.actualAmount.withCurrency()
            }
            is CarouselCellType.UnPayed -> {
                cell.mostUnPayedUser?.nom
            }
            is CarouselCellType.Payed -> {
                cell.mostPayedUser?.nom
            }
            is CarouselCellType.TotalConsumptions -> {
                cell.totalConsumptions.toString()
            }
        }
    }

    // Set subtitle with cell data
    private fun bindSubtitle(cell: CarouselCellType) {
        binding.cardSubtitle.text = when (cell) {
            is CarouselCellType.Checkout -> {
                cell.currentBill?.let {
                    itemView.context.getString(
                        cell.info.subtitle!!,
                        it.withCurrency()
                    )
                } ?: let {
                    itemView.context.getString(R.string.carousel_checkout_subtitle_no_bill)
                }
            }
            is CarouselCellType.UnPayed -> {
                itemView.context.getString(
                    cell.info.subtitle!!,
                    cell.mostUnPayedUser?.balance?.absoluteValue?.withCurrency()
                )
            }
            is CarouselCellType.Payed -> {
                itemView.context.getString(
                    cell.info.subtitle!!,
                    cell.mostPayedUser?.balance?.withCurrency()
                )
            }
            else -> ""
        }
    }
}

