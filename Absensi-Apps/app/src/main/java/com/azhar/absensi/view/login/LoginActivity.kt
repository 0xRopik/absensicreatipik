package com.azhar.absensi.view.login

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.azhar.absensi.R
import com.azhar.absensi.utils.SessionLogin
import com.azhar.absensi.view.main.MainActivity
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private lateinit var session: SessionLogin
    private val REQ_PERMISSION = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        setPermission()
        setInitLayout()
    }

    private fun setPermission() {
        // Mengecek apakah izin lokasi sudah diberikan atau belum
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            // Meminta izin lokasi ke pengguna secara bersamaan
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                REQ_PERMISSION
            )
        }
    }

    private fun setInitLayout() {
        session = SessionLogin(applicationContext)

        // Cek apakah user sudah login sebelumnya
        if (session.isLoggedIn()) {
            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
            finish()
            return // Mencegah kode di bawahnya ikut dieksekusi jika sudah pindah halaman
        }

        btnLogin.setOnClickListener {
            // Gunakan variabel lokal (val) dan tambahkan .trim() untuk menghilangkan spasi tidak sengaja
            val strNama = inputNama.text.toString().trim()
            val strPassword = inputPassword.text.toString().trim()

            if (strNama.isEmpty() || strPassword.isEmpty()) {
                Toast.makeText(this@LoginActivity, "Form tidak boleh kosong!", Toast.LENGTH_SHORT).show()
            } else {
                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                startActivity(intent)
                session.createLoginSession(strNama)
                finish() // Tambahkan finish() agar user tidak bisa kembali ke halaman login saat menekan tombol "Back" di HP
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == REQ_PERMISSION) {
            for (grantResult in grantResults) {
                if (grantResult == PackageManager.PERMISSION_GRANTED) {
                    // Refresh halaman jika izin diberikan
                    val intent = intent
                    finish()
                    startActivity(intent)
                    break // Menghentikan perulangan jika izin sudah beres
                }
            }
        }
    }
}