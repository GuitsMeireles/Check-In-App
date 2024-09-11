package com.me1rel3s.check_in.domain.list.adapter

import android.os.Parcel
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.me1rel3s.check_in.common.database.GuestRepository
import com.me1rel3s.check_in.databinding.ItemGuestBinding

class GuestAdapter() : ListAdapter<GuestRepository.Guest, GuestAdapter.GuestViewHolder>(
    GuestDiffCallback()
),
    Parcelable {

    constructor(parcel: Parcel) : this() {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GuestViewHolder {
        val binding = ItemGuestBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GuestViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GuestViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class GuestViewHolder(private val binding: ItemGuestBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(guest: GuestRepository.Guest) {
            binding.tvName.text = guest.name
            binding.tvPhone.text = guest.phone
            binding.tvEmail.text = guest.email
            binding.icUser.setImageBitmap(guest.photo) // Foto do convidado
            binding.icEnd.setImageBitmap(guest.qrCode)  // QR Code
        }
    }

    class GuestDiffCallback : DiffUtil.ItemCallback<GuestRepository.Guest>() {
        override fun areItemsTheSame(oldItem: GuestRepository.Guest, newItem: GuestRepository.Guest): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: GuestRepository.Guest, newItem: GuestRepository.Guest): Boolean {
            return oldItem == newItem
        }
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<GuestAdapter> {
        override fun createFromParcel(parcel: Parcel): GuestAdapter {
            return GuestAdapter(parcel)
        }

        override fun newArray(size: Int): Array<GuestAdapter?> {
            return arrayOfNulls(size)
        }
    }
}