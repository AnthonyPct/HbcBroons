package com.foyer.hbc.presentation.dashboard.carousel

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.foyer.hbc.databinding.CarouselCardItemBinding
import com.foyer.hbc.domain.data.dashboard.CarouselCellType

class CarouselAdapter : RecyclerView.Adapter<CarouselViewHolder>() {

    ///////////////////////////////////////////////////////////////////////////
    // DATA
    ///////////////////////////////////////////////////////////////////////////

    private var cells: List<CarouselCellType> = emptyList()

    ///////////////////////////////////////////////////////////////////////////
    // PUBLIC API
    ///////////////////////////////////////////////////////////////////////////

    fun setData(cells: List<CarouselCellType>) {
        this.cells = cells
        notifyDataSetChanged()
    }

    ///////////////////////////////////////////////////////////////////////////
    // SPECIALIZATION
    ///////////////////////////////////////////////////////////////////////////

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarouselViewHolder {
        val binding =
            CarouselCardItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CarouselViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CarouselViewHolder, position: Int) {
        holder.bind(cells[position])
    }

    override fun getItemCount(): Int {
        return cells.size
    }
}
