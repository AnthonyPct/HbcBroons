package com.foyer.hbc.presentation.resume.bills

import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.foyer.hbc.R
import com.foyer.hbc.common.getCompleteDayFormat
import com.foyer.hbc.common.withCurrency
import com.foyer.hbc.databinding.BillItemBinding
import com.foyer.hbc.domain.model.PaymentEntity
import com.foyer.hbc.presentation.resume.ResumeContract

class BillViewHolder(
    private val binding: BillItemBinding,
    val listener: ResumeContract.ViewEvent
) :
    RecyclerView.ViewHolder(binding.root) {

    ///////////////////////////////////////////////////////////////////////////
    // PUBLIC API
    ///////////////////////////////////////////////////////////////////////////

    fun bind(bill: PaymentEntity) {
        binding.paymentAmount.text = bill.price.withCurrency()
        binding.paymentDay.text = bill.date.getCompleteDayFormat()
        binding.paymentFutNumber.text = binding.root.resources.getString(
            R.string.resume_checkout_bills_nb_fut, bill.nbFut.toString()
        )
        binding.paymentSeeBill.visibility = if (bill.factureUrl != null) {
            View.VISIBLE
        } else View.GONE
        if (bill.isPayed) {
            binding.colorBar.setBackgroundColor(
                ContextCompat.getColor(
                    binding.root.context,
                    R.color.tomato
                )
            )
            binding.paymentPayedBill.visibility = View.GONE
        }
        binding.paymentSeeBill.setOnClickListener {
            bill.factureUrl?.let { url -> listener.onClickBill(url) }
        }
        binding.paymentPayedBill.setOnClickListener {
            listener.onClickPayedBill(bill)
        }
    }
}