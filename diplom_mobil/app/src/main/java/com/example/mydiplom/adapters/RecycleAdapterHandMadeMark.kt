package com.example.mydiplom.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.mydiplom.R
import com.example.mydiplom.data.KindOfMark

import com.example.mydiplom.viewmodel.SharedViewModel

class RecycleAdapterHandMadeMark (private val dataList: ArrayList<KindOfMark>, private val viewModel: SharedViewModel) :
    RecyclerView.Adapter<RecycleAdapterHandMadeMark.ViewHolderClass>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecycleAdapterHandMadeMark.ViewHolderClass {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.recycle_list_item_handmade_marks, parent, false)
        return RecycleAdapterHandMadeMark.ViewHolderClass(itemView)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }


    override fun onBindViewHolder(holder: RecycleAdapterHandMadeMark.ViewHolderClass, position: Int) {
        val currentItem = dataList[position]
        Log.d("RetrofitClient","currentItem " + currentItem.name)
        holder.bth.setText(currentItem.name)

        holder.cardView.setOnClickListener{
                view: View->
            Navigation.findNavController(view).navigate(R.id.fragmentDetailedHandMadeMark)
            this.viewModel.handMadeMarkId.value = dataList[position].id


        }
    }


    class ViewHolderClass(itemView: View): RecyclerView.ViewHolder(itemView){
        val cardView: CardView = itemView.findViewById(R.id.recListItemHandMadeMarkContainer)
        val bth: TextView = itemView.findViewById(R.id.bthAddNewHandMadeMark)
    }

}