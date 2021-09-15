package com.foyer.hbc.presentation.users.select

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.foyer.hbc.databinding.SelectUserItemBinding
import com.foyer.hbc.domain.model.UserEntity

class SelectUserAdapter(val listener: ISelectUserContract.ViewEvent) :
    RecyclerView.Adapter<SelectUserViewHolder>() {

    ///////////////////////////////////////////////////////////////////////////
    // DATA
    ///////////////////////////////////////////////////////////////////////////

    private var users: List<UserEntity> = emptyList()

    ///////////////////////////////////////////////////////////////////////////
    // PUBLIC API
    ///////////////////////////////////////////////////////////////////////////

    fun setData(users: List<UserEntity>) {
        this.users = users
        notifyDataSetChanged()
    }

    ///////////////////////////////////////////////////////////////////////////
    // SPECIALIZATION
    ///////////////////////////////////////////////////////////////////////////

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectUserViewHolder {
        val binding =
            SelectUserItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SelectUserViewHolder(binding)
    }

    override fun onBindViewHolder(holderSelect: SelectUserViewHolder, position: Int) {
        holderSelect.bind(users[position], listener)
    }

    override fun getItemCount(): Int {
        return users.size
    }
}
