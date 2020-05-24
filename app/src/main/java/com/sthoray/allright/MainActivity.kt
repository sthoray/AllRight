package com.sthoray.allright

import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.*
import java.io.IOException

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Toast.makeText(this, "Welcome", Toast.LENGTH_SHORT).show()

//        recyclerView_topLevel.setBackgroundColor(Color.LTGRAY)
        recyclerView_topLevel.layoutManager = GridLayoutManager(this, 3)
//        recyclerView_topLevel.adapter = TopLevelAdapter()

        fetchJson()
    }

    fun fetchJson() {
        println("Attempting to Fetch JSON")

        val baseUrl = "https://allgoods.co.nz/api/"
        val url = baseUrl + "category/topLevel"

        val request = Request.Builder()
            .url(url)
            .build()

        val client = OkHttpClient()
        // execute the request on a background thread
        client.newCall(request).enqueue(object: Callback {
            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()
                println(body)

                val gson = GsonBuilder().create()

                // category/topLevel returns an array of objects
                val topLevel: Array<TopLevelCategory> =
                    gson.fromJson(body, Array<TopLevelCategory>::class.java)

                // we must update the UI from from the main thread
                runOnUiThread {
                    recyclerView_topLevel.adapter = TopLevelAdapter(topLevel)
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                println("Failed to execute request")
            }
        })
    }
}

class TopLevelCategory(val id: Int, val name: String, val listing_count: Int, val icon: String)