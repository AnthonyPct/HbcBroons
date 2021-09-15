package com.foyer.hbc.presentation.resume.bills

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.foyer.hbc.databinding.BillItemBinding
import com.foyer.hbc.domain.model.PaymentEntity
import com.foyer.hbc.presentation.resume.ResumeContract

class BillsAdapter(val listener: ResumeContract.ViewEvent) :
    RecyclerView.Adapter<BillViewHolder>() {

    ///////////////////////////////////////////////////////////////////////////
    // DATA
    ///////////////////////////////////////////////////////////////////////////

    private var bills = emptyList<PaymentEntity>()

    ///////////////////////////////////////////////////////////////////////////
    // PUBLIC API
    ///////////////////////////////////////////////////////////////////////////

    fun setData(bills: List<PaymentEntity>) {
        this.bills = bills
        notifyDataSetChanged()
    }

    ///////////////////////////////////////////////////////////////////////////
    // SPECIALIZATION
    ///////////////////////////////////////////////////////////////////////////

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BillViewHolder {
        val binding =
            BillItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BillViewHolder(binding, listener)
    }

    override fun onBindViewHolder(holder: BillViewHolder, position: Int) {
        holder.bind(bills[position])
    }

    override fun getItemCount(): Int {
        return bills.size
    }
}