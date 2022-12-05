package com.example.networkingpr

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.ToggleButton
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.NightMode
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object global {

    var darktheme = true
    var renderimages = true

}


class MainActivity : AppCompatActivity() {
    lateinit var adapter: NewsAdapter
    private var articles = mutableListOf<Article>()

    lateinit var drawerLayout: DrawerLayout
    lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    private lateinit var navView: NavigationView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        drawerLayout = findViewById(R.id.drawerLayout)
        actionBarDrawerToggle =
            ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close)


        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        navView = findViewById(R.id.navView)


        navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.darkthemetoggle -> {
                    global.darktheme = findViewById<ToggleButton>(R.id.darkthemetoggle).isActivated

                    if (!global.darktheme) {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    } else {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    }
                }
                R.id.renderimagetoggle -> {
                    global.renderimages =
                        findViewById<ToggleButton>(R.id.renderimagetoggle).isActivated
                    Log.e("#toggleGS12@@", global.renderimages.toString())
                }

                R.id.nav_account -> {
                    Log.e("#toggleGS12@@", "my acc pressed")
                }


            }
            true
        }



        adapter = NewsAdapter(this@MainActivity, articles)
        val rv = findViewById<RecyclerView>(R.id.newsList)
        rv.adapter = adapter
        rv.layoutManager = LinearLayoutManager(this@MainActivity)
        getNews()


    }

    private fun getNews() {
        val news: Call<News> = NewsService.newsInstance.getHeadlines("tech")

        news.enqueue(object : Callback<News> {
            override fun onResponse(call: Call<News>, response: Response<News>) {
                val news = response.body()
                if (news != null) {
                    Log.d("GS#123789@", news.toString())
                    articles.addAll(news.articles)
                    adapter.notifyDataSetChanged()

                }
            }

            override fun onFailure(call: Call<News>, t: Throwable) {
                Log.d("GS#123789@", "Can't fetch news", t)
            }
        })


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true
        }

        return super.onOptionsItemSelected(item)
    }

}