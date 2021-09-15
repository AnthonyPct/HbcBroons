package com.foyer.hbc.presentation.users.payment

import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.foyer.hbc.R
import com.foyer.hbc.common.getCompleteDayFormat
import com.foyer.hbc.common.getHourMinutesFormat
import com.foyer.hbc.common.setDrawableColor
import com.foyer.hbc.common.withCurrency
import com.foyer.hbc.databinding.PaymentItemBinding
import com.foyer.hbc.domain.model.PaymentEntity
import com.foyer.hbc.domain.model.PaymentType

class PaymentViewHolder(private val binding: PaymentItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    ///////////////////////////////////////////////////////////////////////////
    // PUBLIC API
    ///////////////////////////////////////////////////////////////////////////

    fun bind(payment: PaymentEntity) {
        with(binding) {
            paymentHour.text = payment.date.getHourMinutesFormat()
            paymentAmount.text = payment.price.withCurrency()
            paymentAmount.setDrawableColor(R.color.black)
            paymentDay.text = payment.date.getCompleteDayFormat()
            if (payment.mode == PaymentType.CB) {
                paymentMode.setImageDrawable(
                    ContextCompat.getDrawable(
                        binding.root.context,
                        R.drawable.ic_credit_card,
                    )
                )
            }
        }
    }
}