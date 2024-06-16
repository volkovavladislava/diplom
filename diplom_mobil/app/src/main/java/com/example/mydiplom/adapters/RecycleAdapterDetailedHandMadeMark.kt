package com.example.mydiplom.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.mydiplom.R
import com.example.mydiplom.data.Mark
import com.example.mydiplom.viewmodel.SharedViewModel
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class RecycleAdapterDetailedHandMadeMark (private val dataList: ArrayList<Mark>, private val viewModel: SharedViewModel) :
    RecyclerView.Adapter<RecycleAdapterDetailedHandMadeMark.ViewHolderClass>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecycleAdapterDetailedHandMadeMark.ViewHolderClass {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.recycle_list_item_detailed_handmade_mark, parent, false)
        return RecycleAdapterDetailedHandMadeMark.ViewHolderClass(itemView)
    }


    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: RecycleAdapterDetailedHandMadeMark.ViewHolderClass, position: Int) {
        val currentItem = dataList[position]
        holder.date.setText(formatDate(currentItem.date))
        holder.value.setText(currentItem.value_string)
        holder.cardView.setOnClickListener{
                view: View->
            Navigation.findNavController(view).navigate(R.id.fragmentUpdateRecordHandMadeMark)
            this.viewModel.handMadeMarkRecordId.value = dataList[position].id


        }
    }



    class ViewHolderClass(itemView: View): RecyclerView.ViewHolder(itemView){
        val cardView: CardView = itemView.findViewById(R.id.recListItemDetailedHandMadeMarkContainer)
        val date: TextView = itemView.findViewById(R.id.recListItemDetailedHandMadeMarkDate)
        val value: TextView = itemView.findViewById(R.id.recListItemDetailedHandMadeMarkValue)
    }





    fun formatDate(inputDate: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        val outputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        inputFormat.timeZone = TimeZone.getTimeZone("UTC")
        outputFormat.timeZone = TimeZone.getTimeZone("Asia/Singapore")

        val date = inputFormat.parse(inputDate)
        return outputFormat.format(date)
    }


}