package com.example.englishdictionary.ui

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.os.Bundle
import android.speech.RecognizerIntent
import android.util.Log
import android.view.*
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.englishdictionary.adapters.WordsRvAdapter
import com.example.englishdictionary.database.DatabaseBuilder
import com.example.englishdictionary.database.DatabaseHelperImpl
import com.example.englishdictionary.database.models.History
import com.example.englishdictionary.databinding.FragmentHomeBinding
import com.example.englishdictionary.models.WordData
import com.example.englishdictionary.retrofit.ApiHelperImpl
import com.example.englishdictionary.retrofit.RetrofitClient
import com.example.englishdictionary.ui.viewmodels.HomeViewModel
import com.example.englishdictionary.utils.SharedPref
import com.example.englishdictionary.utils.ViewModelFactory
import com.mindorks.example.coroutines.utils.Status
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import android.media.AudioManager

import android.media.MediaPlayer
import android.text.method.HideReturnsTransformationMethod
import androidx.core.net.toUri
import androidx.navigation.fragment.findNavController
import com.example.englishdictionary.R
import com.example.englishdictionary.utils.InternetConnection


@DelicateCoroutinesApi
class HomeFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    lateinit var binding: FragmentHomeBinding
    lateinit var viewModel: HomeViewModel
    private val TAG = "HomeFragment"
    lateinit var suggestion: ArrayList<WordData>
    lateinit var adapter: ArrayAdapter<String>
    lateinit var wordsList: HashMap<String, String>
    lateinit var wordDataList: ArrayList<WordData>
    lateinit var history_List: ArrayList<History>
    lateinit var saved_List: ArrayList<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        suggestion = ArrayList()
        wordDataList = ArrayList()
        history_List = ArrayList()
        saved_List = ArrayList()
        wordsList = HashMap()
        SharedPref.getInstanceDis(requireContext())
        setupViewModel()
        if (SharedPref.word != null) {
            binding.word.text = SharedPref.word
        } else {
            binding.word.text = "Book"
        }
        suggestion()
        workUI()
        loadHistory()
        loadSaved()

        return binding.root
    }

    private fun playSound(uri: String) {
        if (InternetConnection(requireContext()).isNetworkConnected()) {
            if (uri != null) {
                val media = MediaPlayer.create(requireContext(), "https://$uri".toUri())
                media.start()
            }
        } else Toast.makeText(
            requireContext(),
            "Internet bilan aloqani tekshiring",
            Toast.LENGTH_SHORT
        ).show()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun loadSaved() {
        var n = ArrayList<History>()
        val wordsRvAdapter = WordsRvAdapter()
        wordsRvAdapter.setAdapter(emptyList(), 1)
        binding.savedRv.adapter = wordsRvAdapter
        viewModel.getSaved().observe(viewLifecycleOwner, {
            n.clear()
            for (i in it) {
                if (i.saved == true) {
                    n.add(i)
                    saved_List.add(i.word.toString())
                }
            }
            wordsRvAdapter.setAdapter(n, 1)
            wordsRvAdapter.notifyDataSetChanged()
            wordsRvAdapter.onpress = object : WordsRvAdapter.onPress {
                override fun onclick(history: History) {
                    GlobalScope.launch(Dispatchers.Main) {
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
                            binding.cardSave.setImageResource(R.drawable.benchmark1)
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
                            binding.cardSave.setImageResource(R.drawable.benchmark2)
                        }
                    }
                }
            }
        })
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun loadHistory() {
        val wordsRvAdapter = WordsRvAdapter()
        wordsRvAdapter.setAdapter(emptyList(), 1)
        binding.historyRv.adapter = wordsRvAdapter
        viewModel.getLatest().observe(viewLifecycleOwner, {
            if (it.isNotEmpty()) {
                history_List.addAll(it)
                wordsRvAdapter.setAdapter(it.reversed(), 1)
                wordsRvAdapter.notifyDataSetChanged()
                wordsRvAdapter.onpress = object : WordsRvAdapter.onPress {
                    override fun onclick(history: History) {
                        GlobalScope.launch(Dispatchers.Main) {
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
                                binding.cardSave.setImageResource(R.drawable.benchmark1)
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
                                binding.cardSave.setImageResource(R.drawable.benchmark2)
                            }
                        }
                    }
                }
            }
        })
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun workUI() {
        setDate()
        binding.copy.setOnClickListener {
            copyText()
        }
        binding.microphone.setOnClickListener {
            speechToText()
        }
        binding.card2.setOnClickListener {
            var uri = ""
            viewModel.getLatest().observe(viewLifecycleOwner, {
                if (it.isEmpty()) {
                    uri = "//ssl.gstatic.com/dictionary/static/sounds/20200429/book--_gb_1.mp3"
                } else {
                    for (i in it) {
                        if (SharedPref.word == i.word || binding.word.text.toString() == i.word) {
                            if (i.audio != null) {
                                uri = i.audio.toString()
                                break
                            }
                        }
                    }
                }
            })
            if (uri != "") {
                playSound(uri)
            }
        }
        binding.card3.setOnClickListener {
            for (i in history_List) {
                if (i.word == binding.word.text.toString()) {
                    if (i.saved == false) {
                        viewModel.updateHistroy(
                            History(
                                id = i.id,
                                word = i.word.toString(),
                                origin = i.origin,
                                saved = true,
                                audio = i.audio
                            )
                        )
                        binding.cardSave.setImageResource(R.drawable.benchmark1)
                    } else {
                        viewModel.updateHistroy(
                            History(
                                id = i.id,
                                word = i.word.toString(),
                                origin = i.origin,
                                saved = false,
                                audio = i.audio
                            )
                        )
                        binding.cardSave.setImageResource(R.drawable.benchmark2)
                    }
                }
            }
        }
        binding.card4.setOnClickListener {
            if (binding.search.text.toString().isNotEmpty()) {
                val sendIntent: Intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, "${binding.search.text}")
                    type = "text/plain"
                }

                val shareIntent = Intent.createChooser(sendIntent, null)
                startActivity(shareIntent)
            }
        }
    }

    private fun speechToText() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )
        intent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE,
            Locale.getDefault()
        )
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak to text")

        try {
            startActivityForResult(intent, 1)
        } catch (e: Exception) {
            Toast
                .makeText(
                    requireContext(), " " + e.message,
                    Toast.LENGTH_SHORT
                )
                .show()
        }
    }

    private fun copyText() {
        val clipboard =
            activity?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        var clip = ClipData.newPlainText("word", binding.word.text.toString())
        clipboard.setPrimaryClip(clip)
        Toast.makeText(requireContext(), "Copy", Toast.LENGTH_SHORT).show()
    }

    private fun setDate() {
        val c = Calendar.getInstance().time
        val df = SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault())
        val formattedDate: String = df.format(c)
        binding.date.text = formattedDate
    }

    @DelicateCoroutinesApi
    private fun suggestion() {
        binding.search.doOnTextChanged { text, start, before, count ->
            wordRequest(text.toString())
            binding.word.text = text.toString()
            for (i in saved_List) {
                if (i == text.toString()) {
                    binding.cardSave.setImageResource(R.drawable.benchmark1)
                } else binding.cardSave.setImageResource(R.drawable.benchmark2)
            }
            if (text.toString() == "") {
                binding.word.text = SharedPref.word
            }
        }
        binding.search.setOnItemClickListener { parent, view, position, id ->
            var text = (view as TextView).text.toString()
            binding.word.text = text
            GlobalScope.launch {
                viewModel.findHistory(text)
                viewModel.insertHistory(
                    History(
                        word = text,
                        origin = suggestion.last().phonetics?.last()?.text,
                        saved = false,
                        audio = suggestion.last().phonetics?.last()?.audio
                    )
                )
            }
            val imm =
                activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
            binding.search.dismissDropDown()
            SharedPref.word = text
        }
        binding.search.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT) {
                wordRequest(v.text.toString())
                GlobalScope.launch {
                    viewModel.findHistory(v.text.toString())
                    viewModel.insertHistory(
                        History(
                            word = v.text.toString(),
                            origin = if (suggestion.size != 0) suggestion.last().phonetics?.last()?.text else binding.search.text.toString(),
                            saved = false,
                            audio = if (suggestion.size != 0) suggestion.last().phonetics?.last()?.audio else null
                        )
                    )
                }
            }
            val imm =
                activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view?.windowToken, 0)
            binding.search.dismissDropDown()
            SharedPref.word = v.text.toString()
            true
        }
    }

    private fun wordRequest(word: String) {
        viewModel.getWords(word).observe(viewLifecycleOwner, {
            when (it.status) {
                Status.SUCCESS -> {
                    wordDataList.clear()
                    suggestion.clear()
                    adapter =
                        ArrayAdapter(
                            requireContext(),
                            android.R.layout.simple_list_item_1,
                            wordsList.keys.toList().reversed()
                        )
                    binding.search.threshold = 1
                    binding.search.setAdapter(adapter)
                    for (i in it.data!!) {
                        suggestion.add(i)
                        wordsList[i.word.toString()] = i.word.toString()
                        adapter.notifyDataSetChanged()
                    }
                    wordDataList.addAll(it.data)
                }
                Status.ERROR -> {
                    Log.d(TAG, "loadDataError: ${it.message}")
                }
                Status.LOADING -> {

                }
            }
        })
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

    override fun onActivityResult(
        requestCode: Int, resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1) {
            if (resultCode == RESULT_OK && data != null) {
                val result = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS
                )
                binding.search.setText(
                    Objects.requireNonNull(result)!![0]
                )
            }
        }
    }

    override fun onPause() {
        super.onPause()
        SharedPref.getInstanceDis(requireContext())
        if (binding.search.text.toString().isNotEmpty()) {
            SharedPref.word = binding.search.text.toString()
        } else {
            SharedPref.word = binding.word.text.toString()
        }
        viewModel.getLatest().observe(viewLifecycleOwner, {
            if (it.isEmpty()) {
                SharedPref.audio =
                    "//ssl.gstatic.com/dictionary/static/sounds/20200429/book--_gb_1.mp3"
            } else {
                for (i in it) {
                    if (binding.search.text.toString() == i.word || binding.word.toString() == i.word) {
                        if (i.audio != null) {
                            SharedPref.audio = i.audio.toString()
                            break
                        }
                    }
                }
            }
        })
    }
}