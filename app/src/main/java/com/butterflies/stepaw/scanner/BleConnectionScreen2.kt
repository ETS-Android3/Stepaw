package com.butterflies.stepaw.scanner

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.butterflies.stepaw.R
import android.graphics.drawable.Drawable

import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.DrawableImageViewTarget
import com.bumptech.glide.request.target.Target
import com.butterflies.stepaw.databinding.ActivityBleConnectionScreen2Binding


//private lateinit var binding: ActivityBleConnectionScreen2Binding

class BleConnectionScreen2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityBleConnectionScreen2Binding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val imageViewTarget = DrawableImageViewTarget(binding.bleStatusGif)
        Glide.with(this).load(R.raw.bluetooth_status).into<Target<Drawable>>(imageViewTarget)

    }
}