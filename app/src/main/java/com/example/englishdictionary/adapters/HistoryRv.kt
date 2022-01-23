package com.example.englishdictionary.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.englishdictionary.R
import com.example.englishdictionary.database.models.History
import com.example.englishdictionary.databinding.HistoryRvItemBinding

class HistoryRv : RecyclerView.Adapter<HistoryRv.Vh>() {
    lateinit var list: List<History>
    var onpress: HistoryRv.onPress? = null

    fun setAdapter(list: List<History>) {
        this.list = list
    }

    inner class Vh(var itemview: HistoryRvItemBinding) : RecyclerView.ViewHolder(itemview.root) {
        fun bind(history: History,position: Int) {
            itemview.word.text = history.word
            itemview.trancription.text = history.origin
            if (history.saved == true) {
                itemview.save.setImageResource(R.drawable.benchmark1)
            } else {
                itemview.save.setImageResource(R.drawable.benchmark2)
            }
            itemview.save.setOnClickListener {
                onpress?.onclick(history)
            }
            itemview.close.setOnClickListener {
                onpress?.onDelete(history, position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(HistoryRvItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.bind(list[position],position)
    }

    override fun getItemCount(): Int = list.size

    interface onPress {
        fun onclick(history: History)

        fun onDelete(history: History,position: Int)
    }
}