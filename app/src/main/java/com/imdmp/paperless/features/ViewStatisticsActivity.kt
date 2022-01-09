package com.imdmp.paperless.features

import android.app.Activity
import android.os.Bundle
import com.imdmp.paperless.databinding.ActivityViewStatisticsBinding

class ViewStatisticsActivity : Activity() {

    lateinit var binding: ActivityViewStatisticsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewStatisticsBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}