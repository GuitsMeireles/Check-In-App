package com.me1rel3s.check_in.common.code

import android.graphics.Bitmap
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec
import android.util.Base64

class QRCodeUtil {

    // Gerar chave AES
    private val secretKey: SecretKey = generateSecretKey()

    // Gerar chave AES
    private fun generateSecretKey(): SecretKey {
        val keyGen = KeyGenerator.getInstance("AES")
        keyGen.init(256) // Chave AES de 256 bits
        return keyGen.generateKey()
    }

    // Criptografar dados usando AES
    fun encrypt(data: String): String {
        val cipher = Cipher.getInstance("AES")
        cipher.init(Cipher.ENCRYPT_MODE, secretKey)
        val encryptedBytes = cipher.doFinal(data.toByteArray())
        return Base64.encodeToString(encryptedBytes, Base64.DEFAULT)
    }

    // Descriptografar dados usando AES
    fun decrypt(encryptedData: String): String {
        val cipher = Cipher.getInstance("AES")
        cipher.init(Cipher.DECRYPT_MODE, secretKey)
        val decryptedBytes = cipher.doFinal(Base64.decode(encryptedData, Base64.DEFAULT))
        return String(decryptedBytes)
    }

    // Gerar QR Code a partir dos dados criptografados
    fun generateQRCode(data: String): Bitmap {
        val writer = QRCodeWriter()
        val bitMatrix = writer.encode(data, BarcodeFormat.QR_CODE, 512, 512)
        val width = bitMatrix.width
        val height = bitMatrix.height
        val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
        for (x in 0 until width) {
            for (y in 0 until height) {
                bmp.setPixel(x, y, if (bitMatrix.get(x, y)) 0x000000 else 0xFFFFFF)
            }
        }
        return bmp
    }
}