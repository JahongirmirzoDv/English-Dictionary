package com.example.englishdictionary.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.viewpager2.widget.ViewPager2
import com.example.englishdictionary.MainActivity
import com.example.englishdictionary.adapters.SplashPagerAdapter
import com.example.englishdictionary.databinding.ActivitySplashBinding
import com.example.englishdictionary.utils.SharedPref
import com.example.englishdictionary.utils.Statusbar
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class Splash : AppCompatActivity() {
    lateinit var binding: ActivitySplashBinding
    lateinit var list: HashMap<String, String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        SharedPref.getInstanceDis(this)
        Statusbar.run(this)
        loadData()
        loadPager()

        if (SharedPref.user == "true") {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

    }

    private fun loadData() {
        list = HashMap()
        list["O'zingizga mos kursni tanlash"] =
            "Biz nafaqat yurtimizdagi balki butun dunyoda rivojlangan sohalar bo'yicha kurslarni taqdim etamiz va tanlash imkonini beramiz"
        list["O'quv jarayoni"] =
            "O'quv jarayoni video darslar amaliy mashqlar, mentor bilan ishlash imkoniyatihamda live meeting orqli olib boriladi."
        list["Real loyihalarni amalga oshirish"] =
            "Har bir kursdagi mavzulashtirilgan video darslarda real loyihalardan qismlar orqali tushuntirib beriladi."
    }

    private fun loadPager() {
        val splashPagerAdapter = SplashPagerAdapter(list)
        binding.viewpager2.adapter = splashPagerAdapter
        binding.wormDotsIndicator.setViewPager2(binding.viewpager2)
        binding.viewpager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if (position == 2) {
                    binding.skip.visibility = View.INVISIBLE
                    binding.next.visibility = View.VISIBLE
                } else {
                    binding.skip.visibility = View.VISIBLE
                    binding.next.visibility = View.INVISIBLE
                }
            }

        })
        binding.skip.setOnClickListener {
            binding.viewpager2.setCurrentItem(2, true)
        }
        binding.next.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            SharedPref.user = "true"
            finish()
        }
    }
}