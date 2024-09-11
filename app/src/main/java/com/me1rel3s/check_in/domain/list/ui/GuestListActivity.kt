package com.me1rel3s.check_in.domain.list.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.me1rel3s.check_in.common.database.GuestRepository
import com.me1rel3s.check_in.databinding.ActivityGuestListBinding
import com.me1rel3s.check_in.domain.list.adapter.GuestAdapter

class GuestListActivity : AppCompatActivity() {

    private val binding: ActivityGuestListBinding by lazy {
        ActivityGuestListBinding.inflate(layoutInflater)
    }

    private lateinit var guestRepository: GuestRepository
    private lateinit var guestAdapter: GuestAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        guestRepository = GuestRepository(this)
        setupRecyclerView()

        loadGuests()
        setupListeners()
    }

    private fun setupRecyclerView() {
        guestAdapter = GuestAdapter()
        binding.rvLists.apply {
            layoutManager = LinearLayoutManager(this@GuestListActivity)
            adapter = guestAdapter
        }
    }

    private fun loadGuests() {
        val guests: List<GuestRepository.Guest> = guestRepository.getAllGuests()
        guestAdapter.submitList(guests)
    }

    private fun setupListeners() {
        binding.topAppBar.setNavigationOnClickListener {
            finish()
        }
    }
}