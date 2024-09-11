package com.me1rel3s.check_in.domain.check

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.me1rel3s.check_in.R
import com.me1rel3s.check_in.databinding.ActivityCheckBinding

class CheckActivity : AppCompatActivity() {

    private val binding: ActivityCheckBinding by lazy { ActivityCheckBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupListeners()
    }


    private fun setupListeners() {
        binding.topAppBar.setNavigationOnClickListener {
            finish()
        }
    }
}