package com.example.mydiplom.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import com.example.mydiplom.R
import com.example.mydiplom.data.KindOfMark
import com.example.mydiplom.viewmodel.SharedViewModel

class ListAdapterAddMarks(context: Context, dataArrayList: ArrayList<KindOfMark?>?, private val viewModel: SharedViewModel):ArrayAdapter<KindOfMark?>(context,
    R.layout.list_item_addmarks, dataArrayList!!) {



    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView
        val listData = getItem(position)

        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.list_item_addmarks, parent, false)
        }
        val listTitle = view!!.findViewById<Button>(R.id.listItemMarkButton)
        listTitle.text = listData!!.name


        val bthFavorite = view!!.findViewById<Button>(R.id.bthMatchFavoriteAddMarks)

        var isFavorite = false

        if(listData!!.has_reference == 1){
            Log.d("RetrofitClient","has_reference " + listData)
            isFavorite = true
            bthFavorite.foreground = ContextCompat.getDrawable(context, R.drawable.baseline_favorite_24_red)
        }

        bthFavorite.setOnClickListener {
            if(isFavorite){
                bthFavorite.foreground = ContextCompat.getDrawable(context, R.drawable.baseline_favorite_24_grey)
                viewModel.deleteFavorite(context, viewModel.userId, listData!!.id)
            }
            else{
                bthFavorite.foreground = ContextCompat.getDrawable(context, R.drawable.baseline_favorite_24_red)
                viewModel.addToFavorite(context, viewModel.userId, listData!!.id)
            }
            isFavorite = !isFavorite
        }

        listTitle.setOnClickListener{
            viewModel.kindOfMarkIdAddMark.value = listData.id
            viewModel.kindOfMarkNameAddMark.value = listData.name

            val bundle = bundleOf("title" to listData!!.name )

            if (listData!!.enum_kind_of_mark_id == 1){
                Navigation.findNavController(view).navigate(R.id.fragmentAddNewRecordMark1number, bundle)
            }
            if (listData!!.enum_kind_of_mark_id == 4){
                Navigation.findNavController(view).navigate(R.id.fragmentAddNewRecordMarkEnum, bundle)
            }
            if (listData!!.enum_kind_of_mark_id == 5){
                Navigation.findNavController(view).navigate(R.id.fragmentAddNewRecordMark2number, bundle)
            }

        }

        return view
    }
}