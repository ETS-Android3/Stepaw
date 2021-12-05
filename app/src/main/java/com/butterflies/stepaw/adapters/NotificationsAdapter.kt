package com.butterflies.stepaw.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.butterflies.stepaw.R
import com.butterflies.stepaw.userActions.NotificationsDataModel
import org.jetbrains.anko.backgroundColor

class NotificationsAdapter(
    private val context: Context,
    var dataset: MutableList<NotificationsDataModel>
) :
    RecyclerView.Adapter<NotificationsAdapter.NotificationsViewHolder>() {

    class NotificationsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val date: TextView = view.findViewById<TextView>(R.id.notifications_date)
        val user: TextView = view.findViewById<TextView>(R.id.notifications_user)
        val text1: TextView = view.findViewById<TextView>(R.id.notifications_text1)
        val text2: TextView = view.findViewById<TextView>(R.id.notifications_text2)
        val card: CardView = view.findViewById<CardView>(R.id.notifications_card)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationsViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.notifications_recycler_item, parent, false)
        return NotificationsViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: NotificationsViewHolder, position: Int) {
        val item = dataset[position]
        Log.d("notificationsdata",item.date)
        holder.date.text = item.date
        holder.user.text = item.userName
        holder.text1.text = item.text1
        holder.text2.text = item.text2

        if (position % 2 != 0) {
            holder.card.backgroundColor =
                ContextCompat.getColor(context, R.color.notifications_alternate)
        } else {
            holder.card.backgroundColor =
                ContextCompat.getColor(context, R.color.color_primary_variant_2)
        }

    }

    override fun getItemCount(): Int {
        return dataset.size
    }
fun updateData(dataset:MutableList<NotificationsDataModel>){
    this.dataset=dataset
    notifyDataSetChanged()
}
}