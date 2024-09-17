package com.me1rel3s.check_in.domain.list.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.me1rel3s.check_in.R
import com.me1rel3s.check_in.common.database.GuestRepository
import com.me1rel3s.check_in.databinding.ItemGuestBinding

class GuestAdapter(
    private val context: Context,
    private val onGuestClick: (GuestRepository.Guest, View) -> Unit
) : RecyclerView.Adapter<GuestAdapter.GuestViewHolder>() {

    private var guestList = listOf<GuestRepository.Guest>()

    fun submitList(guests: List<GuestRepository.Guest>) {
        guestList = guests
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GuestViewHolder {
        val binding = ItemGuestBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GuestViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GuestViewHolder, position: Int) {
        val guest = guestList[position]

        holder.binding.tvName.text = guest.name
        holder.binding.tvPhone.text = guest.phone
        holder.binding.tvEmail.text = guest.email
        holder.binding.icUser.setImageBitmap(guest.photo)
        holder.binding.icEnd.setImageBitmap(guest.qrCode)

        // Alterar o fundo com base no status usando drawable
        val backgroundResource = if (guest.status == "checked") {
            R.drawable.checked_in
        } else {
            R.drawable.not_checked
        }
        holder.binding.root.setBackgroundResource(backgroundResource)

        // Configurar o clique do item
        holder.binding.root.setOnClickListener {
            onGuestClick(guest, holder.binding.root)
        }
    }

    override fun getItemCount(): Int = guestList.size

    class GuestViewHolder(val binding: ItemGuestBinding) : RecyclerView.ViewHolder(binding.root)
}