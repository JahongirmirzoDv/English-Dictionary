package com.example.englishdictionary.ui

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.englishdictionary.R
import com.example.englishdictionary.adapters.ViewPagerAdapter
import com.example.englishdictionary.database.DatabaseBuilder
import com.example.englishdictionary.database.DatabaseHelperImpl
import com.example.englishdictionary.database.models.History
import com.example.englishdictionary.databinding.FragmentSearchBinding
import com.example.englishdictionary.retrofit.ApiHelperImpl
import com.example.englishdictionary.retrofit.RetrofitClient
import com.example.englishdictionary.ui.viewmodels.HomeViewModel
import com.example.englishdictionary.utils.InternetConnection
import com.example.englishdictionary.utils.SharedPref
import com.example.englishdictionary.utils.ViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    lateinit var binding: FragmentSearchBinding
    lateinit var viewModel: HomeViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        SharedPref.getInstanceDis(requireContext())
        setupViewModel()
        setViewPager()
        loadUI()
        workUI()
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

    private fun workUI() {
        binding.card1.setOnClickListener {
            copyText()
        }
        binding.card2.setOnClickListener {
            playSound(SharedPref.audio.toString())
        }
        binding.card3.setOnClickListener {
            var n: History? = null
            var m = false
            GlobalScope.launch(Dispatchers.Main) {
                viewModel.getLatest().observe(viewLifecycleOwner, {
                    for (i in it) {
                        if (i.word == binding.word.text.toString()) {
                            n = i
                            m = true
                        }
                    }
                })
                if (m) {
                    if (n?.saved == false) {
                        viewModel.updateHistroy(
                            History(
                                id = n!!.id,
                                word = n!!.word.toString(),
                                origin = n!!.origin,
                                saved = true,
                                audio = n!!.audio
                            )
                        )
                        binding.cardSave.setImageResource(R.drawable.benchmark1)
                    } else {
                        viewModel.updateHistroy(
                            History(
                                id = n!!.id,
                                word = n!!.word.toString(),
                                origin = n!!.origin,
                                saved = false,
                                audio = n!!.audio
                            )
                        )
                        binding.cardSave.setImageResource(R.drawable.benchmark2)
                    }
                }
            }
        }
        binding.card4.setOnClickListener {
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, "${binding.word.text}")
                type = "text/plain"
            }

            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)
        }
    }


    private fun playSound(uri: String) {
        if (InternetConnection(requireContext()).isNetworkConnected()) {
            if (uri != null) {
                val media = MediaPlayer().apply {
                    setAudioAttributes(
                        AudioAttributes.Builder()
                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                            .setUsage(AudioAttributes.USAGE_MEDIA)
                            .build()
                    )
                    setDataSource(requireContext(), "https:$uri".toUri())
                    prepare()
                    start()
                }
            } else Toast.makeText(
                requireContext(),
                "Internet bilan aloqani tekshiring",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun copyText() {
        val clipboard =
            activity?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        var clip = ClipData.newPlainText("word", binding.word.text.toString())
        clipboard.setPrimaryClip(clip)
        Toast.makeText(requireContext(), "Copy", Toast.LENGTH_SHORT).show()
    }

    private fun loadUI() {
        SharedPref.getInstanceDis(requireContext())
        binding.word.text = SharedPref.word
    }

    private fun setViewPager() {
        val viewPagerAdapter =
            ViewPagerAdapter(childFragmentManager, listOf("Meaning", "Origin"))
        binding.viewpager.adapter = viewPagerAdapter
        binding.tab.setupWithViewPager(binding.viewpager)

    }
}