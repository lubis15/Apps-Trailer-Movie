package com.lazday.appmovie.fragment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.tabs.TabLayoutMediator
import com.lazday.appmovie.R
import com.lazday.appmovie.adapter.TabAdapter
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)


        val tabAdapter = TabAdapter(supportFragmentManager, lifecycle)
        view_pager.adapter = tabAdapter

        val tabTitles = arrayOf("Popular", "Now Playing")
        TabLayoutMediator(tab_layout, view_pager) { tab, position ->
            tab.text = tabTitles[position]

        //biar dia muncul
        }.attach()
    }
}