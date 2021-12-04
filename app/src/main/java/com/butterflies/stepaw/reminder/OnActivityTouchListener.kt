package com.butterflies.stepaw.reminder

import android.view.MotionEvent


interface OnActivityTouchListener {
    fun getTouchCoordinates(ev: MotionEvent?)
}