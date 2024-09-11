package com.me1rel3s.check_in.domain.home

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.me1rel3s.check_in.domain.archive.ImportActivity
import com.me1rel3s.check_in.domain.check.CheckActivity
import com.me1rel3s.check_in.databinding.ActivityMainBinding
import com.me1rel3s.check_in.domain.list.ui.GuestListActivity
import com.me1rel3s.check_in.domain.register.RegisterActivity

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupListeners()
    }

    private fun setupListeners() {
        binding.cdCheck.setOnClickListener {
            val intent = Intent(this, CheckActivity::class.java)
            startActivity(intent)
        }

        binding.cdRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        binding.cdImport.setOnClickListener {
            val intent = Intent(this, ImportActivity::class.java)
            startActivity(intent)
        }

        binding.cdList.setOnClickListener {
            val intent = Intent(this, GuestListActivity::class.java)
            startActivity(intent)
        }
    }
}