package com.example.englishdictionary.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.englishdictionary.databinding.SplashItemBinding

class SplashPagerAdapter(var list: HashMap<String, String>) :
    RecyclerView.Adapter<SplashPagerAdapter.Vh>() {
    inner class Vh(var itemview: SplashItemBinding) : RecyclerView.ViewHolder(itemview.root) {
        fun bind(position: Int) {
            val toList = list.toList()
            itemview.action.text = toList[position].first
            itemview.title.text = toList[position].second
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(SplashItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = list.size
}