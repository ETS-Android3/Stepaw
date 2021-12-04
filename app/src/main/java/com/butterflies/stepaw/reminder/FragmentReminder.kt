package com.butterflies.stepaw.reminder

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.butterflies.stepaw.adapters.ItemAdapter
import com.butterflies.stepaw.databinding.FragmentReminderBinding
import com.butterflies.stepaw.reminder.RecyclerTouchListener.OnRowClickListener
import com.butterflies.stepaw.reminder.RecyclerTouchListener.OnSwipeOptionsClickListener
import com.google.android.material.bottomsheet.BottomSheetBehavior


class FragmentReminder : Fragment() {
    private lateinit var binding: FragmentReminderBinding
    private lateinit var reminder: ReminderService
    private lateinit var toast:Toast

    interface ReminderService {
        fun setReminder(hour: String, minute: String, extraData: String, vararg days: Int)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        reminder = context as ReminderService
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentReminderBinding.inflate(layoutInflater, container, false)
        val standardBottomSheetBehavior =
            BottomSheetBehavior.from(requireActivity().findViewById(com.butterflies.stepaw.R.id.bottom_sheet_reminder))
        binding.imageView2.setOnClickListener {
            addReminder()
        }
        binding.textView3.setOnClickListener {
            addReminder()
        }
        binding.saveReminder.setOnClickListener {
            val timePicker = binding.timepicker
            val d=binding.reminderLabel.text.toString()
            reminder.setReminder(
                timePicker.hour.toString(),
                timePicker.minute.toString(),
                extraData = d,
                1,
                2,
                3,
                4,
                5,
                6,
                7,
            )
            binding.newreminder.visibility = View.GONE
            standardBottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }
        binding.cancelReminder.setOnClickListener {
            binding.newreminder.visibility = View.GONE
            standardBottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }
//        setUpRecyclerView()
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val root=FragmentReminderBinding.bind(binding.root)
        val recyclerView=root.recyclerReminder


        binding.recyclerReminder.adapter=ItemAdapter(requireContext(), listOf("1","2","3"))


        val touchswiper=RecyclerTouchListener(context as Activity?,recyclerView)
        touchswiper
            .setClickable(object : OnRowClickListener {
                override fun onRowClicked(position: Int) {
//                    Toast.makeText(
//                        ApplicationProvider.getApplicationContext(),
//                        taskList.get(position).getName(),
//                        Toast.LENGTH_SHORT
//                    ).show()
                }

                override fun onIndependentViewClicked(independentViewID: Int, position: Int) {}
            })
            .setSwipeOptionViews(com.butterflies.stepaw.R.id.delete_task, com.butterflies.stepaw.R.id.edit_task)
            .setSwipeable(
                com.butterflies.stepaw.R.id.rowFG, com.butterflies.stepaw.R.id.rowBG,
                OnSwipeOptionsClickListener { viewID, position ->
                    when (viewID) {
                        com.butterflies.stepaw.R.id.delete_task -> {
//                            taskList.remove(position)
//                            recyclerviewAdapter.setTaskList(taskList)
                        }
                        com.butterflies.stepaw.R.id.edit_task->{

                        }
                    }
                })
        recyclerView.addOnItemTouchListener(touchswiper)
    }

    fun setupR(){

    }



    private fun addReminder() {
        if (binding.newreminder.visibility == View.VISIBLE) {
            binding.newreminder.visibility = View.GONE
        } else {
            binding.newreminder.visibility = View.VISIBLE
        }
    }
}