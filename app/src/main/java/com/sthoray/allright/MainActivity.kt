package com.sthoray.allright

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        recyclerView_topLevel.setBackgroundColor(Color.LTGRAY)
        recyclerView_topLevel.layoutManager = LinearLayoutManager(this)
        recyclerView_topLevel.adapter = TopLevelAdapter()
    }
}
