package com.me1rel3s.check_in.domain.list.ui

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
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
        guestAdapter = GuestAdapter(this) { guest, view ->
            shareGuestCard(guest, view)
        }
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

    private fun shareGuestCard(guest: GuestRepository.Guest, view: View) {
        val bitmap = getBitmapFromView(view)
        val path = MediaStore.Images.Media.insertImage(contentResolver, bitmap, "Guest Card", null)
        val uri = Uri.parse(path)

        val intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_STREAM, uri)
            type = "image/png"
        }

        startActivity(Intent.createChooser(intent, "Share guest card"))
    }

    private fun getBitmapFromView(view: View): Bitmap {
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }
}