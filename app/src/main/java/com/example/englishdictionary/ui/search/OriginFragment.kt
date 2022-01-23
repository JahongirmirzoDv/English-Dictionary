package com.example.englishdictionary.ui.search

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.englishdictionary.R
import com.example.englishdictionary.databinding.FragmentOriginBinding
import com.example.englishdictionary.models.WordData
import com.example.englishdictionary.utils.SharedPref

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"

/**
 * A simple [Fragment] subclass.
 * Use the [OriginFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class OriginFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    lateinit var binding: FragmentOriginBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentOriginBinding.inflate(inflater, container, false)

        SharedPref.getInstanceDis(requireContext())
        binding.origin.text = if (SharedPref.origin == null) "Malumot yo'q" else SharedPref.origin

        return binding.root
    }
}