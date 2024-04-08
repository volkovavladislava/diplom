package com.example.mydiplom

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import androidx.navigation.Navigation

class ListAdapterAddMarks(context: Context, dataArrayList: ArrayList<MarksData?>?):ArrayAdapter<MarksData?>(context, R.layout.list_item_marks, dataArrayList!!) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView
        val listData = getItem(position)

        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.list_item_marks, parent, false)
        }
        val listTitle = view!!.findViewById<Button>(R.id.listItemMarkButton)
        listTitle.text = listData!!.title

        listTitle.setOnClickListener{
            view: View->
            Navigation.findNavController(view).navigate(R.id.fragmentAddNewRecordMark)
        }

        return view
    }
}