package com.example.englishdictionary.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.englishdictionary.R
import com.example.englishdictionary.database.models.History
import com.example.englishdictionary.databinding.WordRvItemBinding
import androidx.cardview.widget.CardView


class WordsRvAdapter : RecyclerView.Adapter<WordsRvAdapter.Vh>() {
    lateinit var list: List<History>
    var pos = 0
    var onpress: onPress? = null

    fun setAdapter(list: List<History>, pos: Int) {
        this.list = list
        this.pos = pos
    }

    inner class Vh(var itemview: WordRvItemBinding) : RecyclerView.ViewHolder(itemview.root) {
        fun bind(history: History) {
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
            if (pos == 2) {
                itemview.item.layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
            }else{
                itemview.item.layoutParams.width = 770
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(WordRvItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: WordsRvAdapter.Vh, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size

    interface onPress {
        fun onclick(history: History)
    }
}