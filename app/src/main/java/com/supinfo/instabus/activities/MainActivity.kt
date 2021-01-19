package com.supinfo.instabus.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.supinfo.instabus.R

class MainActivity : AppCompatActivity() {
    /**
    Main activity of the application, you can find a container to display different fragments
    and a bottom action bar.
     **/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Adding a bottom action bar
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        val navController = findNavController(R.id.mainFragment)
        bottomNavigationView.setupWithNavController(navController)
    }

}