package com.example.amebo

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle

class SigninActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)
        val intent= Intent(this,SignupActivity::class.java)
//        startActivity(intent)
    }
}
