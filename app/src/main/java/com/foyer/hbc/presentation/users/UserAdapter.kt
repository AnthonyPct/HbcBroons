package com.foyer.hbc.presentation.users

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.foyer.hbc.databinding.UserItemBinding
import com.foyer.hbc.domain.model.UserEntity
import com.foyer.hbc.presentation.users.select.ISelectUserContract

class UserAdapter(val listener: ISelectUserContract.ViewEvent) :
    RecyclerView.Adapter<UserViewHolder>() {

    ///////////////////////////////////////////////////////////////////////////
    // DATA
    ///////////////////////////////////////////////////////////////////////////

    private var users: List<UserEntity> = emptyList()
    private var selectedPosition = -1

    ///////////////////////////////////////////////////////////////////////////
    // PUBLIC API
    ///////////////////////////////////////////////////////////////////////////

    fun setData(users: List<UserEntity>) {
        this.users = users
        notifyDataSetChanged()
    }

    fun updateSelectedUser(user: UserEntity) {
        this.selectedPosition = users.indexOf(user)
        notifyDataSetChanged()
    }

    fun unSelectUser() {
        this.selectedPosition = -1
        notifyDataSetChanged()
    }

    ///////////////////////////////////////////////////////////////////////////
    // SPECIALIZATION
    ///////////////////////////////////////////////////////////////////////////

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding =
            UserItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holderSelect: UserViewHolder, position: Int) {
        holderSelect.bind(users[position], listener, selectedPosition == position)
    }

    override fun getItemCount(): Int {
        return users.size
    }
}
