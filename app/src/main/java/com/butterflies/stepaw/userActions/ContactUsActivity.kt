package com.butterflies.stepaw.userActions

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.butterflies.stepaw.R
import com.butterflies.stepaw.databinding.ActivityContactusBinding

class ContactUsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityContactusBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContactusBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(findViewById(R.id.my_toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.custom_back_button)
        val toolbarTitle=findViewById<TextView>(R.id.toolbar_title)
        toolbarTitle.text = "Contact Us"
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
                Toast.makeText(this@ContactUsActivity, "No email clients installed.", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        super.onBackPressed()
        return true
    }
}