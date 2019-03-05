package com.example.amebo

import android.content.Intent
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.BaseColumns
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.example.amebo.Adapters.PostAdapter
import com.example.amebo.Contracts.UserContract
import com.example.amebo.Models.Post

class PostActivity : AppCompatActivity() {

    var token: String = ""
    var uid:String = ""
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager
    final val SHARED_PREF_NAME = "MyPrefFile"



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)
    }



    override fun onStart() {
        super.onStart()
        var settings: SharedPreferences = this.getSharedPreferences(SHARED_PREF_NAME, 0)
        if (settings.getBoolean("MyFirstTime", true)) {
            openSplash1()
            Log.d("isFirstTime", "Yes")
        }
//        checkLogin()
//        displayPosts()
    }

    //This function helps open a splash screen if its the users first time of using the app
    fun openSplash1() {
        val intent: Intent = Intent(this, SplashScreens::class.java)
        startActivity(intent)
    }

    //This function displays the posts with a recycler view
    fun displayPosts(){
        val post = Post()
        val posts = ArrayList<Post>()
        post.id = "82938939"
        post.name = "Manjara"
        val post2 = Post()
        post2.id = "2989394"
        post2.name = "Lome Green"
        posts.add(post)
        posts.add(post2)
        viewManager= LinearLayoutManager(this)
        viewAdapter= PostAdapter(posts,this)
        recyclerView=findViewById<RecyclerView>(R.id.main_recycler_view).apply {
            layoutManager=viewManager
            adapter=viewAdapter
        }
    }

    fun checkLogin() {

        val dbhelper= UserContract.UserEntry.DbHelper(this)
        val db=dbhelper.writableDatabase
        val selection="${BaseColumns._ID} =?"
        val selectionArgs= arrayOf(1)
        val projections= arrayOf(
            BaseColumns._ID,
            UserContract.UserEntry.COLUMN_NAME_ID,
            UserContract.UserEntry.COLUMN_NAME_NAME,
            UserContract.UserEntry.COLUMN_NAME_TOKEN)
        val cursor=db.query(UserContract.UserEntry.TABLE_NAME,projections,null,null,null,null,null)


        with(cursor) {
            while (moveToNext()) {
                uid = getString(getColumnIndex(UserContract.UserEntry.COLUMN_NAME_ID))
                token=getString(getColumnIndex(UserContract.UserEntry.COLUMN_NAME_TOKEN))
            }
        }
        if(uid.isEmpty()){
            val intent= Intent(this,SigninActivity::class.java)
            startActivity(intent)
        }
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
