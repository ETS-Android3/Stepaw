package com.butterflies.stepaw.adapters

import android.content.Context
import android.os.SystemClock
import android.view.*
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.butterflies.stepaw.R
import com.butterflies.stepaw.roomORM.ReminderDB
import com.butterflies.stepaw.roomORM.ReminderEntity
import org.jetbrains.anko.backgroundColor

class ItemAdapter(private val context: Context, private var dataset: MutableList<ReminderEntity>) :
    RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {


    class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val t = view.findViewById<TextView>(R.id.timeText)
        val schedtext = view.findViewById<TextView>(R.id.task_name)
        val latext = view.findViewById<TextView>(R.id.task_desc)
        val delete = view.findViewById<ImageView>(R.id.img_delete)
        val edit = view.findViewById<ImageView>(R.id.img_edit)
        val card = view.findViewById<LinearLayout>(R.id.rowFG)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val adapterLayout =
            LayoutInflater.from(parent.context).inflate(R.layout.reminder_item, parent, false)
        return ItemViewHolder(adapterLayout)
    }


    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = dataset[position]
        holder.t.text = item.hour + ":" + item.minute
        holder.schedtext.text = "DAILY"
        holder.latext.text = item.label
        if (position % 2 != 0) {
            holder.card.backgroundColor =
                ContextCompat.getColor(context, R.color.white)
        } else {
            holder.card.backgroundColor =
                ContextCompat.getColor(context, R.color.color_primary_variant_2)
        }
    }

    override fun getItemCount(): Int {
        return dataset.size
    }


    fun updateData(data: MutableList<ReminderEntity>) {
        this.dataset = data
        notifyDataSetChanged()
    }

    fun clearData() {
        dataset.clear() // clear list
        notifyDataSetChanged() // let your adapter know about the changes and reload view.
    }

    fun getDataset(): List<ReminderEntity> {
        return this.dataset
    }
}