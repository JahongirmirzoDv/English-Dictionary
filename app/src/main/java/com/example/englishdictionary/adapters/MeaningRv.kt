package com.example.englishdictionary.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.englishdictionary.database.models.History
import com.example.englishdictionary.databinding.MeaningItemBinding
import com.example.englishdictionary.models.Definition

class MeaningRv : RecyclerView.Adapter<MeaningRv.Vh>() {
    lateinit var list: List<Definition>

    fun setAdapter(list: List<Definition>) {
        this.list = list
    }

    inner class Vh(var itemview: MeaningItemBinding) : RecyclerView.ViewHolder(itemview.root) {
        @SuppressLint("SetTextI18n")
        fun bind(definition: Definition, position: Int) {
            itemview.number.text = "${position.plus(1)}."
            itemview.meaning.text = definition.definition
            itemview.meaningTitle.text = definition.example
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MeaningRv.Vh {
        return Vh(MeaningItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: MeaningRv.Vh, position: Int) {
        holder.bind(list[position], position)
    }

    override fun getItemCount(): Int = list.size
}