package com.example.amebo

import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle

class SplashScreens : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screens)
        supportActionBar!!.hide()
        val fragmengtManager = supportFragmentManager
        val fragmentTransaction = fragmengtManager.beginTransaction()
        val fragment = SplashOne()
        fragmentTransaction.add(R.id.splash_screens, fragment)
        fragmentTransaction.commit()

    }
}
