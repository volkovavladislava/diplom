package com.example.mydiplom.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.navigation.Navigation
import com.example.mydiplom.R
import com.example.mydiplom.data.Blog
import com.google.android.material.imageview.ShapeableImageView
import com.squareup.picasso.Picasso

class ListAdapterBlog(context: Context, dataArrayList: ArrayList<Blog?>?) :
    ArrayAdapter<Blog?>(context,
    R.layout.list_item_blog, dataArrayList!!){

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView
        val listData = getItem(position)

        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.list_item_blog, parent, false)
        }


        val containerBlog = view!!.findViewById<RelativeLayout>(R.id.containerBlog)
        val listImage= view!!.findViewById<ShapeableImageView>(R.id.listItemBlogImage)
        val listTitle = view!!.findViewById<TextView>(R.id.listItemBlogTitle)


        val imageUrl = "http://10.0.2.2:3000/files/" + listData!!.link

        Picasso.get()
            .load(imageUrl)
//            .placeholder(R.drawable.placeholder_image) // Опционально: изображение-заглушка, отображаемое во время загрузки
//            .error(R.drawable.error_image) // Опционально: изображение, отображаемое в случае ошибки загрузки
            .into(listImage)

        listTitle.text = listData!!.title
        containerBlog.setOnClickListener{
                view: View ->
            Navigation.findNavController(view).navigate(R.id.fragmentArticleOfBlog)
        }

        return view
    }
}