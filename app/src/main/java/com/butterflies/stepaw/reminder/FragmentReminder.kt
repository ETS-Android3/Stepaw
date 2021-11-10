package com.butterflies.stepaw.reminder

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.butterflies.stepaw.databinding.FragmentReminderBinding

import com.google.android.material.bottomsheet.BottomSheetBehavior


class FragmentReminder : Fragment() {
    private lateinit var binding: FragmentReminderBinding
    private lateinit var reminder: ReminderService
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    interface ReminderService {
        fun setReminder(hour: String, minute: String)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        reminder = context as ReminderService
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentReminderBinding.inflate(layoutInflater, container, false)
        val standardBottomSheetBehavior =
            BottomSheetBehavior.from(requireActivity().findViewById(com.butterflies.stepaw.R.id.bottom_sheet_reminder))
        binding.imageView2.setOnClickListener {
            if (binding.newreminder.visibility == View.VISIBLE) {
                binding.newreminder.visibility = View.GONE
            } else {
                binding.newreminder.visibility = View.VISIBLE
            }
        }
        binding.saveReminder.setOnClickListener {
            val time_picker = binding.timepicker
            val hour = time_picker.hour
            reminder.setReminder(time_picker.hour.toString(), time_picker.minute.toString())
            binding.newreminder.visibility = View.GONE
            standardBottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }
        binding.cancelReminder.setOnClickListener {
            binding.newreminder.visibility = View.GONE
            standardBottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        return binding.root
    }

}