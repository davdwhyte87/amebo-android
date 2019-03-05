package com.example.amebo

import android.annotation.SuppressLint
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.app.Fragment
import android.content.ContentValues
import android.content.Intent
import android.util.Log
import android.view.MotionEvent
import android.view.animation.AlphaAnimation
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import org.json.JSONObject
import com.android.volley.toolbox.Volley
import com.example.amebo.Contracts.UserContract
import com.example.amebo.Models.AppData
import org.json.JSONArray
import org.json.JSONException

class SignupActivity : AppCompatActivity() {
    // global variables sortof
    lateinit var signupButton: Button
    lateinit var emailField: EditText
    lateinit var nameField: EditText
    lateinit var passField: EditText
    lateinit var name: String
    lateinit var email: String
    lateinit var pass: String
    lateinit var oldtext:String
    lateinit var userId: String
    lateinit var userToken: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

    }

    override fun onStart() {
        super.onStart()

        // clear fragments
        clearMem()

        // handle clicks
        handleClicks()

        // button click animation
        buttonClickEffect()
    }

    /**
     * This function handles clicks int his activity
     *
     */
    fun handleClicks() {
        signupButton= findViewById(R.id.btn_signup)

        signupButton.setOnClickListener {
            // carry out input data validation
            if (validate()) {
                // start a button load
                startLoading()

                // connect to the api
                sendSignupRequest()
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    fun buttonClickEffect() {
        val buttonAnimation: AlphaAnimation = AlphaAnimation(1F, 0.8F)
        signupButton.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    v.setBackgroundResource(R.drawable.ripple)
                    v.invalidate()
                }
                MotionEvent.ACTION_UP -> {
                    v.setBackgroundColor(resources.getColor(R.color.colorPrimary))
                    v.invalidate()
                }
            }
            false
        }
    }


    /**
     * This function validates input data sent to the api
     */
    fun validate(): Boolean {
        emailField = findViewById(R.id.f_email)
        nameField = findViewById(R.id.f_name)
        passField = findViewById(R.id.f_pass)
        name = nameField.text.toString()
        email = emailField.text.toString()
        pass = passField.text.toString()

        if(name.isEmpty()||email.isEmpty()||pass.isEmpty()){
            Toast.makeText(this,"Name, email and password are required!", Toast.LENGTH_SHORT).show()
            return false
        }
        if(email.length<8){
            Toast.makeText(this,"Email must not be less that 8 characters", Toast.LENGTH_SHORT).show()
            return false
        }
        if(pass.length<5){
            Toast.makeText(this,"Password must not be less that 5 characters", Toast.LENGTH_SHORT).show()
            return false
        }
        if(name.length<5){
            Toast.makeText(this,"Name must not be less that 5 characters", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }


    /**
     * This function clears the fragments data
     */
    fun clearMem() {
        val fragmengtManager = supportFragmentManager
        val fragmentTransaction = fragmengtManager.beginTransaction()
        val fragment = SplashOne()
        val fragment2 = SplashTwo()
        fragmentTransaction.remove(fragment)
        fragmentTransaction.remove((fragment2))
        fragmentTransaction.commit()
        fragmengtManager.popBackStack()
    }


    /**
     * This function sends user data to the api and handles request
     */
    fun sendSignupRequest(){
        val queue= Volley.newRequestQueue(this)
        var appData= AppData()
        val url = appData.baseApiUrl+"user/signup"

       // Formulate the request and handle the response.
        var req: JSONObject = JSONObject()
        req.put("name",name)
        req.put("email",email)
        req.put("password",pass)
        val jsonObjectRequest: JsonObjectRequest = object: JsonObjectRequest(
            Request.Method.POST, url, req,
            Response.Listener { response ->
                // stop loading button sims
                stopLoading()
                Log.d("RespiiDo",response.toString())
                if(response["status"]==201){
                    val responseObject: JSONArray =response.getJSONArray("data")
                    var count=responseObject.length()
                    var message = ""
                    while (count>0){
                        try {
                            val dataObject:JSONObject= responseObject.getJSONObject(count-1)
                            userId = dataObject.getString("id")
                            userToken = dataObject.getString("token")
                            message = dataObject.getString("message")
                            count--
                        } catch (e:JSONException) {
                            e.printStackTrace()
                        }
                    }

                    // save users data in the database
                    saveUserData()

                    // send user api message with toast
                    Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
                }
                else{

                    var msg:String = response["messag"].toString()
                    if(msg.isEmpty()){
                        msg="An error occured"
                    }
                    Toast.makeText(this,msg,Toast.LENGTH_SHORT).show()
                }
                Log.v("testserver",response["status"].toString())
            },
            Response.ErrorListener { error ->
                Log.v("testserver",error.toString())
                // stop loading
                stopLoading()
                Toast.makeText(this,"An error occurred M",Toast.LENGTH_SHORT).show()
            }
        ){
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                var params= HashMap<String,String>(super.getHeaders())
                params.put("Content-Type","application/json")
                //..add other headers
                return params
            }
        }

        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest)
    }


    /**
     * This function starts a button loading simulation
     */
    fun startLoading() {
        oldtext = signupButton.text.toString()
        signupButton.text = "Loading..."
    }

    /**
     * This function stops the loading simulation
     */
    fun stopLoading() {
        signupButton.text = oldtext
    }

    /**
     * This function saves a users data in database
     */
    fun saveUserData() {
        val dbhelper= UserContract.UserEntry.DbHelper(this)
        val db=dbhelper.writableDatabase
        val data= ContentValues().apply {
            put(UserContract.UserEntry.COLUMN_NAME_ID,userId)
            put(UserContract.UserEntry.COLUMN_NAME_TOKEN,userToken)
            put(UserContract.UserEntry.COLUMN_NAME_NAME,name)
        }
        db.insert(UserContract.UserEntry.TABLE_NAME,null,data)
    }

}
