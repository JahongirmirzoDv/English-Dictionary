package com.example.englishdictionary.ui.search

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.englishdictionary.adapters.MeaningRv
import com.example.englishdictionary.database.DatabaseBuilder
import com.example.englishdictionary.database.DatabaseHelperImpl
import com.example.englishdictionary.databinding.FragmentMeaningBinding
import com.example.englishdictionary.models.Definition
import com.example.englishdictionary.models.WordData
import com.example.englishdictionary.retrofit.ApiHelperImpl
import com.example.englishdictionary.retrofit.RetrofitClient
import com.example.englishdictionary.ui.viewmodels.HomeViewModel
import com.example.englishdictionary.utils.SharedPref
import com.example.englishdictionary.utils.ViewModelFactory
import com.mindorks.example.coroutines.utils.Status
import kotlinx.coroutines.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"

/**
 * A simple [Fragment] subclass.
 * Use the [MeaningFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MeaningFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: WordData? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    private val TAG = "MeaningFragment"
    lateinit var binding: FragmentMeaningBinding
    lateinit var viewModel: HomeViewModel
    lateinit var wordDataList: ArrayList<WordData>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = FragmentMeaningBinding.inflate(inflater, container, false)
        wordDataList = ArrayList()
        SharedPref.getInstanceDis(requireContext())
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun loadUI() {
        val meaningRv = MeaningRv()
        meaningRv.setAdapter(emptyList())
        binding.meaningRv.adapter = meaningRv
        GlobalScope.launch(Dispatchers.Main) {
            val compare = compare()
            meaningRv.setAdapter(compare)
            meaningRv.notifyDataSetChanged()
        }
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

    private fun wordRequest(word: String) {
        viewModel.getWords(word).observe(viewLifecycleOwner, {
            when (it.status) {
                Status.SUCCESS -> {
                    binding.progress.visibility = View.GONE
                    wordDataList.addAll(it.data!!)
                    SharedPref.origin = wordDataList.last().origin
                    loadUI()
                }
                Status.ERROR -> {
                    Log.d(TAG, "loadDataError: ${it.message}")
                }
                Status.LOADING -> {

                }
            }
        })
    }

    suspend fun compare(): ArrayList<Definition> {
        var l = ArrayList<Definition>()
        withContext(Dispatchers.IO) {
            if (wordDataList.isNotEmpty()) {
                val meanings = wordDataList.last().meanings
                for (i in meanings!!) {
                    for (k in i.definitions!!) {
                        l.add(Definition(k.antonyms, k.definition, k.example, k.synonyms))
                    }
                }
            }
        }
        return l
    }

    override fun onStart() {
        super.onStart()
        setupViewModel()
        wordRequest(SharedPref.word!!)
    }
}