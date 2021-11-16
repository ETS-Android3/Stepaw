package com.butterflies.stepaw.userActions

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.butterflies.stepaw.R
import com.butterflies.stepaw.databinding.ActivityContactusBinding

class Contactus : AppCompatActivity() {
    private lateinit var binding: ActivityContactusBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContactusBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(findViewById(R.id.my_toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.title="Contact us"

        binding.sendMail.setOnClickListener {
            val name = binding.contactName.text.toString()
            val email = binding.contactEmail.text.toString()
            val body = binding.contactBody.text.toString()

            val emailIntent = Intent(Intent.ACTION_SENDTO)
            emailIntent.data = Uri.parse("mailto:$email")
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Stepaw-query")
            emailIntent.putExtra(Intent.EXTRA_TEXT, body)

            try {
                startActivity(Intent.createChooser(emailIntent, "Send email using..."))
            } catch (ex: ActivityNotFoundException) {
                Toast.makeText(this@Contactus, "No email clients installed.", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        super.onBackPressed()
        return true
    }
}