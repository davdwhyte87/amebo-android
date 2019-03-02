package com.example.amebo

import android.content.Intent
import android.os.Bundle
import android.provider.BaseColumns
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import com.example.amebo.Adapters.PostAdapter
import com.example.amebo.Contracts.UserContract
import com.example.amebo.Models.Post

import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    var token: String = ""
    var uid:String = ""
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onStart() {
        super.onStart()
        checkLogin()
//        displayPosts()
    }

    fun checkLogin() {

        val dbhelper= UserContract.UserEntry.DbHelper(this)
        val db=dbhelper.writableDatabase
        val selection="${BaseColumns._ID} =?"
        val selectionArgs= arrayOf(1)
        val projections= arrayOf(BaseColumns._ID,UserContract.UserEntry.COLUMN_NAME_UID,UserContract.UserEntry.COLUMN_NAME_NAME,UserContract.UserEntry.COLUMN_NAME_TOKEN)
        val cursor=db.query(UserContract.UserEntry.TABLE_NAME,projections,null,null,null,null,null)


        with(cursor) {
            while (moveToNext()) {
                uid = getString(getColumnIndex(UserContract.UserEntry.COLUMN_NAME_UID))
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


    fun displayPosts(){
        val post = Post()
        val posts = ArrayList<Post>()
        post.id = "82938939"
        post.name = "Manjara"
        posts.add(post)
        viewManager= LinearLayoutManager(this)
        viewAdapter=PostAdapter(posts,this)
        recyclerView=findViewById<RecyclerView>(R.id.main_recycler_view).apply {
            layoutManager=viewManager
            adapter=viewAdapter
        }
    }
}
