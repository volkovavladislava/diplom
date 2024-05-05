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

class RecycleAdapterStatisticNum1 (private val dataList: ArrayList<Mark>,  private val viewModel: SharedViewModel) :
    RecyclerView.Adapter<RecycleAdapterStatisticNum1.ViewHolderClass>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderClass {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.recycle_list_item_statistic, parent, false)
        return RecycleAdapterStatisticNum1.ViewHolderClass(itemView)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: ViewHolderClass, position: Int) {
        val currentItem = dataList[position]
        holder.valueTextView.setText(currentItem.value_number.toString())
        holder.dateTextView.setText(formatDate(currentItem.date))
        holder.cardView.setOnClickListener{
                view: View->
            Navigation.findNavController(view).navigate(R.id.fragmentUpdateRecordMarkNum1)
            this.viewModel.updateNum1Id.value = dataList[position].id
            this.viewModel.updateNum1UserId.value = dataList[position].user_id
            this.viewModel.updateNum1KindOfMarkId.value = dataList[position].kind_of_mark_id
            this.viewModel.updateNum1Date.value = dataList[position].date
            this.viewModel.updateNum1ValueDouble.value = dataList[position].value_number


        }
    }


    class ViewHolderClass(itemView: View): RecyclerView.ViewHolder(itemView){
        val cardView: CardView = itemView.findViewById(R.id.recListItemStatisticContainer)
        val valueTextView: TextView = itemView.findViewById(R.id.recListItemStatisticName)
        val dateTextView: TextView = itemView.findViewById(R.id.recListItemStatisticDate)
    }


    fun formatDate(inputDate: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        val outputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())

        val date = inputFormat.parse(inputDate)
        return outputFormat.format(date)
    }


}