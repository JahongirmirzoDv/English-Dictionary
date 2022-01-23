package com.example.englishdictionary.ui

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.englishdictionary.R
import com.example.englishdictionary.adapters.WordsRvAdapter
import com.example.englishdictionary.database.DatabaseBuilder
import com.example.englishdictionary.database.DatabaseHelperImpl
import com.example.englishdictionary.database.models.History
import com.example.englishdictionary.databinding.FragmentSavedBinding
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
 * Use the [SavedFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SavedFragment : Fragment() {
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

    lateinit var binding: FragmentSavedBinding
    lateinit var viewModel: HomeViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSavedBinding.inflate(inflater, container, false)
        setupViewModel()
        loadSaved()

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
    private fun loadSaved() {
        var n = ArrayList<History>()
        val wordsRvAdapter = WordsRvAdapter()
        wordsRvAdapter.setAdapter(emptyList(),2)
        binding.recyclerView.adapter = wordsRvAdapter
        viewModel.getSaved().observe(viewLifecycleOwner, {
            n.clear()
            for (i in it) {
                if (i.saved == true) {
                    n.add(i)
                }
            }
            wordsRvAdapter.setAdapter(n,2)
            wordsRvAdapter.notifyDataSetChanged()
            wordsRvAdapter.onpress = object : WordsRvAdapter.onPress {
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
            }
        })
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SavedFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SavedFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}