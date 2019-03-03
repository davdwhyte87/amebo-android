package com.example.amebo

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.MotionEvent.*
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.widget.Button


class SplashOne : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_splash_one, container, false)
    }

    override fun onStart() {
        super.onStart()
        // handles click effects
        buttonClickEffect()
        //handle click events
        handleClicks()
    }

    @SuppressLint("ClickableViewAccessibility")
    fun buttonClickEffect() {
        val buttonAnimation:AlphaAnimation = AlphaAnimation(1F, 0.8F)
        val nextButton = view!!.findViewById<Button>(R.id.splash_one_next_btn)
        nextButton.setOnTouchListener { v, event ->
            when (event.action) {
                ACTION_DOWN -> {
                    v.setBackgroundResource(R.drawable.ripple)
                    v.invalidate()
                }
                ACTION_UP -> {
                    v.setBackgroundColor(resources.getColor(R.color.colorWhite))
                    v.invalidate()
                }
            }
            false
        }
    }

    /**
     *This function handles all the click events in this fragment
     */
    fun handleClicks() {
        val nextButton = view!!.findViewById<Button>(R.id.splash_one_next_btn)
        nextButton.setOnClickListener {
            openSplashTwo()
        }
    }


    /**
     *This function opens a new fragment in the parent activity
     */
    fun openSplashTwo() {
        val fragmentTransaction = fragmentManager!!.beginTransaction()
        val fragment = SplashTwo()
        fragmentTransaction.replace(R.id.splash_screens, fragment)
        fragmentTransaction.commit()
    }
}
