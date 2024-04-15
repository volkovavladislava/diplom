package com.example.mydiplom.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import com.example.mydiplom.R
import com.example.mydiplom.data.KindOfMark
import com.example.mydiplom.viewmodel.SharedViewModel

class ListAdapterStatistic (context: Context, dataArrayList: ArrayList<KindOfMark?>?, private val viewModel: SharedViewModel):
    ArrayAdapter<KindOfMark?>(context, R.layout.list_item_statisticmark, dataArrayList!!){

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView
        val listData = getItem(position)

        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.list_item_statisticmark, parent, false)
        }
        val listTitle = view!!.findViewById<Button>(R.id.listItemStatisticMarkButton)
        listTitle.text = listData!!.name

        listTitle.setOnClickListener{
            viewModel.kindOfMarkIdStatistic.value = listData.id
//            if (listData!!.enum_kind_of_mark_id == 1){
//                Navigation.findNavController(view).navigate(R.id.fragmentAddNewRecordMark1number)
//            }
//            if (listData!!.enum_kind_of_mark_id == 4){
//                Navigation.findNavController(view).navigate(R.id.fragmentAddNewRecordMarkEnum)
//            }
//            if (listData!!.enum_kind_of_mark_id == 5){
//                Navigation.findNavController(view).navigate(R.id.fragmentAddNewRecordMark2number)
//            }

        }

        return view
    }
}