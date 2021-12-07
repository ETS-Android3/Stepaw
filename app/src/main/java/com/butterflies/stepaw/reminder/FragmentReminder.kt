package com.butterflies.stepaw.reminder

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.butterflies.stepaw.adapters.ItemAdapter
import com.butterflies.stepaw.databinding.FragmentReminderBinding
import com.butterflies.stepaw.reminder.RecyclerTouchListener.OnRowClickListener
import com.butterflies.stepaw.reminder.RecyclerTouchListener.OnSwipeOptionsClickListener
import com.butterflies.stepaw.roomORM.ReminderDB
import com.butterflies.stepaw.roomORM.ReminderDao
import com.butterflies.stepaw.roomORM.ReminderEntity
import com.butterflies.stepaw.utils.StepawUtils
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class FragmentReminder : Fragment() {
    private lateinit var binding: FragmentReminderBinding
    private lateinit var reminder: ReminderService
    private lateinit var toast: Toast
    private lateinit var db: ReminderDB
    private lateinit var dataset: MutableList<ReminderEntity>
    private lateinit var recycleradapter: ItemAdapter

    interface ReminderService {
        fun setReminder(
            hour: String,
            minute: String,
            extraData: String,
            unique: Int,
            vararg days: Int
        )
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
        db = Room.databaseBuilder(requireContext(), ReminderDB::class.java, "remindersDB").build()
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
            var labelText = "Stepaw"
            if (binding.reminderLabel.text.isNotEmpty()) {
                labelText = binding.reminderLabel.text.toString()
            }

            val utils = StepawUtils()

            reminder.setReminder(
                timePicker.hour.toString(),
                timePicker.minute.toString(),
                extraData = labelText,
                unique = utils.getUniqueID(),
                days = intArrayOf(1, 2, 3)
            )
            val unique = utils.getUniqueID()
///            Write data to database
            val job = lifecycleScope.launch(Dispatchers.IO) {
                addDataDB(
                    unique,
                    timePicker.hour.toString(),
                    timePicker.minute.toString(),
                    label = labelText,
                    utils.getDate()
                )
            }
            job.start()
//
            if (this::dataset.isInitialized) {

                requireActivity().runOnUiThread {

                    dataset.add(
                        ReminderEntity(
                            unique,
                            timePicker.hour.toString(),
                            timePicker.minute.toString(),
                            labelText,
                            utils.getDate()
                        )
                    )
                    recycleradapter.updateData(dataset)

                }

            }
            //
            binding.newreminder.visibility = View.GONE
            binding.recyclerReminder.visibility = View.VISIBLE
            standardBottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }
        binding.cancelReminder.setOnClickListener {
            binding.newreminder.visibility = View.GONE
            standardBottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        return binding.root
    }


    private suspend fun addDataDB(
        unique: Int,
        hour: String,
        minute: String,
        label: String,
        date: String
    ) {
        withContext(Dispatchers.IO) {
            val dao = db.reminderdao()
            dao.insertAll(ReminderEntity(unique, hour, minute, label, date))
        }

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("redrawn", "Redrawing")
        val root = FragmentReminderBinding.bind(binding.root)
        val recyclerView = root.recyclerReminder
        recyclerView.layoutManager = LinearLayoutManager(context)
        val dao = db.reminderdao()
        recycleradapter = ItemAdapter(requireContext(), mutableListOf())
        Thread {
            dataset = dao.getAll()
            if (dataset.isNotEmpty()) {
                recycleradapter.updateData(dataset)
                recyclerView.adapter = recycleradapter
            }
        }.start()
        val touchswiper = RecyclerTouchListener(context as Activity?, recyclerView)
        touchswiper
            .setClickable(object : OnRowClickListener {
                override fun onRowClicked(position: Int) {

                }

                override fun onIndependentViewClicked(independentViewID: Int, position: Int) {

                }
            })
            .setSwipeOptionViews(
                com.butterflies.stepaw.R.id.delete_task,
                com.butterflies.stepaw.R.id.edit_task
            )
            .setSwipeable(
                com.butterflies.stepaw.R.id.rowFG, com.butterflies.stepaw.R.id.rowBG,
                OnSwipeOptionsClickListener { viewID, position ->
                    when (viewID) {
                        com.butterflies.stepaw.R.id.delete_task -> {
                            val removeID = dataset.removeAt(position)
                            recycleradapter.updateData(dataset)
                            Thread {
                                dao.deleteByID(removeID.id)
                            }.start()
                        }
                        com.butterflies.stepaw.R.id.edit_task -> {
                            addReminder()
                        }
                    }
                })
        recyclerView.addOnItemTouchListener(touchswiper)
    }


    private fun addReminder() {
        if (binding.newreminder.visibility == View.VISIBLE) {
            binding.newreminder.visibility = View.GONE
            binding.recyclerReminder.visibility = View.VISIBLE
        } else {
            binding.newreminder.visibility = View.VISIBLE
            binding.recyclerReminder.visibility = View.GONE
        }

    }



}