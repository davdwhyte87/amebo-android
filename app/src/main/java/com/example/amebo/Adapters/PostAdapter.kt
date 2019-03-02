package com.example.amebo.Adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.example.amebo.Models.Post
import kotlinx.android.synthetic.main.post_item.view.*
import com.example.amebo.R

class PostAdapter(val items:ArrayList<Post>, val context: Context): RecyclerView.Adapter<PostAdapter.ViewHolder>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val view= LayoutInflater.from(p0.context).inflate(R.layout.post_item,p0,false) as LinearLayout
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.view.post_name.text=items[position].name
    }

    class ViewHolder(val view: View): RecyclerView.ViewHolder(view){}
}