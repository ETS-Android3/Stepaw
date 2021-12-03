package com.butterflies.stepaw.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.butterflies.stepaw.R

class ItemAdapter(context: Context,private val dataset:List<String>):RecyclerView.Adapter<ItemAdapter.ItemViewHolder>(){

    class ItemViewHolder(view: View):RecyclerView.ViewHolder(view){
        val textView: TextView =view.findViewById<TextView>(R.id.recycler_textview);
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val adapterLayout=LayoutInflater.from(parent.context).inflate(R.layout.recycler_item,parent,false)
        return ItemViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item=dataset[position]
        holder.textView.text=item
    }

    override fun getItemCount(): Int {
        return dataset.size
    }
}