package com.me1rel3s.check_in.domain.register

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter
import com.me1rel3s.check_in.common.database.GuestRepository
import com.me1rel3s.check_in.common.security.SecurityUtil
import com.me1rel3s.check_in.databinding.ActivityRegisterBinding
import javax.crypto.SecretKey

class RegisterActivity : AppCompatActivity() {

    private val binding: ActivityRegisterBinding by lazy {
        ActivityRegisterBinding.inflate(layoutInflater)
    }
    private lateinit var guestRepository: GuestRepository

    private lateinit var cameraLauncher: ActivityResultLauncher<Intent>
    private lateinit var galleryLauncher: ActivityResultLauncher<Intent>

    private lateinit var secretKey: SecretKey

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        guestRepository = GuestRepository(this)

        // Recupera a chave secreta do Keystore
        secretKey = SecurityUtil.getSecretKey()

        setupActivityResultLaunchers()
        setupListeners()
    }

    private fun setupActivityResultLaunchers() {
        cameraLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val bitmap = result.data?.extras?.get("data") as Bitmap
                binding.iPhotograph.setImageBitmap(bitmap)
            }
        }

        galleryLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val imageUri: Uri? = result.data?.data
                binding.iPhotograph.setImageURI(imageUri)
            }
        }
    }

    private fun setupListeners() {
        binding.topAppBar.setNavigationOnClickListener {
            finish()
        }

        binding.btCamera.setOnClickListener {
            if (checkPermission(Manifest.permission.CAMERA)) {
                openCamera()
            } else {
                requestPermission(Manifest.permission.CAMERA, CAMERA_REQUEST_CODE)
            }
        }

        binding.btGallery.setOnClickListener {
            if (checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                openGallery()
            } else {
                requestPermission(Manifest.permission.READ_EXTERNAL_STORAGE, GALLERY_REQUEST_CODE)
            }
        }

        binding.elevatedButton.setOnClickListener {
            val name = binding.textField.editText?.text.toString()
            val phone = binding.tfPhone.editText?.text.toString()
            val email = binding.tfEmail.editText?.text.toString()
            val photo = getBitmapFromImageView(binding.iPhotograph)

            if (name.isEmpty() || phone.isEmpty() || email.isEmpty() || photo == null) {
                Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
            } else {
                // Criar JSON com as informações do convidado
                val guestData = "{\"name\":\"$name\", \"phone\":\"$phone\", \"email\":\"$email\"}"

                // Criptografar as informações do convidado
                val (encryptedData, iv) = SecurityUtil.encryptData(guestData, secretKey)

                // Gerar QR Code com os dados criptografados
                val qrCodeBitmap = generateQRCode(encryptedData, iv)

                // Salvar convidado no banco de dados com o QR code
                guestRepository.addGuest(name, phone, email, photo, qrCodeBitmap)

                Toast.makeText(this, "Cadastro realizado com sucesso", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun checkPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission(permission: String, requestCode: Int) {
        ActivityCompat.requestPermissions(this, arrayOf(permission), requestCode)
    }

    private fun openCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraLauncher.launch(cameraIntent)
    }

    private fun openGallery() {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        galleryLauncher.launch(galleryIntent)
    }

    private fun getBitmapFromImageView(imageView: ImageView): Bitmap? {
        imageView.isDrawingCacheEnabled = true
        imageView.buildDrawingCache()
        return (imageView.drawable as? BitmapDrawable)?.bitmap
    }

    private fun generateQRCode(encryptedData: ByteArray, iv: ByteArray): Bitmap {
        // Combine o IV e os dados criptografados para que possam ser descriptografados posteriormente
        val combined = iv + encryptedData
        val encodedData = Base64.encodeToString(combined, Base64.DEFAULT)

        val writer = QRCodeWriter()
        val bitMatrix = writer.encode(encodedData, BarcodeFormat.QR_CODE, 512, 512)
        val width = bitMatrix.width
        val height = bitMatrix.height
        val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
        for (x in 0 until width) {
            for (y in 0 until height) {
                bmp.setPixel(x, y, if (bitMatrix.get(x, y)) 0xFF000000.toInt() else 0xFFFFFFFF.toInt())
            }
        }
        return bmp
    }

    companion object {
        private const val CAMERA_REQUEST_CODE = 100
        private const val GALLERY_REQUEST_CODE = 101
    }
}