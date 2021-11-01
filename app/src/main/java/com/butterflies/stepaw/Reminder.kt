package com.butterflies.stepaw

import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior

class Reminder : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reminder)
        val standardBottomSheetBehavior = BottomSheetBehavior.from(findViewById(R.id.bottomSheet))
//       Disabling Bottom sheet draggable status
        standardBottomSheetBehavior.isDraggable=false
        standardBottomSheetBehavior.state=BottomSheetBehavior.STATE_HALF_EXPANDED

//Callback for slide changes
        val bottomSheetCallback = object : BottomSheetBehavior.BottomSheetCallback() {

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                // Do something for new state.

            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                // Do something for slide offset.
            }
        }

// To add the callback:
        standardBottomSheetBehavior.addBottomSheetCallback(bottomSheetCallback)
    }
}