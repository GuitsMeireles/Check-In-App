package com.me1rel3s.check_in.domain.check

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.zxing.integration.android.IntentIntegrator
import com.me1rel3s.check_in.common.database.GuestRepository
import com.me1rel3s.check_in.databinding.ActivityCheckBinding

class CheckActivity : AppCompatActivity() {

    private val binding: ActivityCheckBinding by lazy { ActivityCheckBinding.inflate(layoutInflater) }
    private lateinit var guestRepository: GuestRepository

    companion object {
        const val CAMERA_PERMISSION_REQUEST_CODE = 1001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        guestRepository = GuestRepository(this)

        setupListeners()
    }

    private fun setupListeners() {
        binding.topAppBar.setNavigationOnClickListener {
            finish()
        }

        binding.btCheck.setOnClickListener {
            // Lógica para check-in manual
        }

        binding.btCode.setOnClickListener {
            requestCameraPermission()
        }
    }

    private fun requestCameraPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                CAMERA_PERMISSION_REQUEST_CODE
            )
        } else {
            openQrScanner()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE && grantResults.isNotEmpty() &&
            grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {
            openQrScanner()
        } else {
            // Mostra uma mensagem ao usuário dizendo que a permissão é necessária
        }
    }

    private fun openQrScanner() {
        val integrator = IntentIntegrator(this)
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
        integrator.setPrompt("Scan QR code")
        integrator.setCameraId(0)
        integrator.setBeepEnabled(true)
        integrator.initiateScan()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents != null) {
                handleQrCode(result.contents)
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun handleQrCode(qrCodeData: String) {
        val guestId = guestRepository.findGuestByQrCode(qrCodeData)
        if (guestId != null) {
            guestRepository.checkInGuest(guestId)
            setResult(RESULT_OK)
            finish()  // Fecha a activity e retorna para a lista de convidados
        } else {
            // Mostrar mensagem de erro
        }
    }
}