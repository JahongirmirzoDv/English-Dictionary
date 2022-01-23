package com.example.englishdictionary.ui

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.englishdictionary.R
import com.example.englishdictionary.adapters.HistoryRv
import com.example.englishdictionary.adapters.WordsRvAdapter
import com.example.englishdictionary.database.DatabaseBuilder
import com.example.englishdictionary.database.DatabaseHelperImpl
import com.example.englishdictionary.database.models.History
import com.example.englishdictionary.databinding.FragmentHistoryBinding
import com.example.englishdictionary.retrofit.ApiHelperImpl
import com.example.englishdictionary.retrofit.RetrofitClient
import com.example.englishdictionary.ui.viewmodels.HomeViewModel
import com.example.englishdictionary.utils.ViewModelFactory
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HistoryFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HistoryFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    lateinit var binding: FragmentHistoryBinding
    lateinit var viewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHistoryBinding.inflate(inflater, container, false)
        setupViewModel()
        loadHistory()



        return binding.root
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(
                ApiHelperImpl(RetrofitClient.apiService),
                DatabaseHelperImpl(DatabaseBuilder.getInstance(requireContext()))
            )
        )[HomeViewModel::class.java]
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun loadHistory() {
        val wordsRvAdapter = HistoryRv()
        wordsRvAdapter.setAdapter(emptyList())
        binding.rv.adapter = wordsRvAdapter
        viewModel.getLatest().observe(viewLifecycleOwner, {
            if (it.isNotEmpty()) {
                wordsRvAdapter.setAdapter(it.reversed())
                wordsRvAdapter.notifyDataSetChanged()
                wordsRvAdapter.onpress = object : HistoryRv.onPress {
                    override fun onclick(history: History) {
                        GlobalScope.launch {
                            if (history.saved == false) {
                                viewModel.updateHistroy(
                                    History(
                                        id = history.id,
                                        word = history.word.toString(),
                                        origin = history.origin,
                                        saved = true,
                                        audio = history.audio
                                    )
                                )
                            } else {
                                viewModel.updateHistroy(
                                    History(
                                        id = history.id,
                                        word = history.word.toString(),
                                        origin = history.origin,
                                        saved = false,
                                        audio = history.audio
                                    )
                                )
                            }
                        }
                    }

                    override fun onDelete(history: History, position: Int) {
                        viewModel.findHistory(history.word.toString())
                        Toast.makeText(requireContext(), "O'chirildi", Toast.LENGTH_SHORT).show()
                        wordsRvAdapter.notifyItemRemoved(position)
                        viewModel.getLatest().observe(viewLifecycleOwner, {
                            wordsRvAdapter.setAdapter(it.reversed())
                        })
                    }
                }
            }
        })
    }
}