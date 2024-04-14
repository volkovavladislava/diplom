package com.example.mydiplom.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import androidx.navigation.Navigation
import com.example.mydiplom.data.MarksData
import com.example.mydiplom.R
import com.example.mydiplom.data.KindOfMark

class ListAdapterAddMarks(context: Context, dataArrayList: ArrayList<KindOfMark?>?):ArrayAdapter<KindOfMark?>(context,
    R.layout.list_item_marks, dataArrayList!!) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView
        val listData = getItem(position)

        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.list_item_marks, parent, false)
        }
        val listTitle = view!!.findViewById<Button>(R.id.listItemMarkButton)
        listTitle.text = listData!!.name

        listTitle.setOnClickListener{
            view: View->
            Navigation.findNavController(view).navigate(R.id.fragmentAddNewRecordMark)
        }

        return view
    }
}