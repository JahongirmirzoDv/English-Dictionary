package com.example.englishdictionary

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.PopupMenu
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.englishdictionary.database.DatabaseBuilder
import com.example.englishdictionary.database.DatabaseHelperImpl
import com.example.englishdictionary.databinding.ActivityMainBinding
import com.example.englishdictionary.retrofit.ApiHelperImpl
import com.example.englishdictionary.retrofit.RetrofitClient
import com.example.englishdictionary.ui.viewmodels.HomeViewModel
import com.example.englishdictionary.utils.ViewModelFactory
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        Statusbar.run(this)
        controlBottomNavigation()
        controlDrawer()

    }

    private fun controlDrawer() {
        val toolbar = binding.appBarMain.toolbar
        toolbar.setNavigationOnClickListener {
            binding.apply {
                drawerlayout.openDrawer(navView)
            }
        }
        binding.navView.setNavigationItemSelectedListener {
            if (it.title == "About") {
                MaterialAlertDialogBuilder(this)
                    .setTitle("Dictionary app")
                    .setMessage("This app created by Jahongirmirzo To'lqinov\n" +
                            "jahongirmirzotolqinov@gmail.com")
                    .setPositiveButton("Ok") { dialog, which ->
                        dialog.cancel()
                    }
                    .show()
            } else {
                val sendIntent: Intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, "Hi my friend")
                    type = "text/plain"
                }

                val shareIntent = Intent.createChooser(sendIntent, null)
                startActivity(shareIntent)
            }
            true
        }
    }

    private fun controlBottomNavigation() {
        binding.appBarMain.include2.bottomBar.setOnItemSelectedListener {
            when (it) {
                1 -> {
                    val findNavController =
                        findNavController(R.id.nav_host_fragment_content_main)
                    findNavController.popBackStack()
                    findNavController.navigate(R.id.searchFragment)

                }
                2 -> {
                    val findNavController =
                        findNavController(R.id.nav_host_fragment_content_main)
                    findNavController.popBackStack()
                    findNavController.navigate(R.id.savedFragment)

                }
                3 -> {
                    val findNavController =
                        findNavController(R.id.nav_host_fragment_content_main)
                    findNavController.popBackStack()
                    findNavController.navigate(R.id.historyFragment)

                }
                else -> {
                    val findNavController =
                        findNavController(R.id.nav_host_fragment_content_main)
                    findNavController.popBackStack()
                    findNavController.navigate(R.id.homeFragment)
                }
            }
        }
    }


    override fun onBackPressed() {
        if (binding.drawerlayout.isDrawerOpen(binding.navView)) {
            binding.drawerlayout.closeDrawers()
        } else {
            super.onBackPressed()
        }
    }
}
