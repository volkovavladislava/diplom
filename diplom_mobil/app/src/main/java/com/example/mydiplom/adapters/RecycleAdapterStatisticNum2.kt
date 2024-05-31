package com.example.mydiplom.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.mydiplom.R
import com.example.mydiplom.data.MarkDavlenie
import com.example.mydiplom.viewmodel.SharedViewModel
import java.text.SimpleDateFormat
import java.util.Locale

class RecycleAdapterStatisticNum2 (private val dataList: ArrayList<MarkDavlenie>, private val viewModel: SharedViewModel) :
    RecyclerView.Adapter<RecycleAdapterStatisticNum2.ViewHolderClass>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecycleAdapterStatisticNum2.ViewHolderClass {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.recycle_list_item_statistic_num2, parent, false)
        return RecycleAdapterStatisticNum2.ViewHolderClass(itemView)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: RecycleAdapterStatisticNum2.ViewHolderClass, position: Int) {
        val currentItem = dataList[position]
        holder.value1TextView.setText(currentItem.value_number1.toString())
        holder.value2TextView.setText(currentItem.value_number2.toString())
        holder.dateTextView.setText(formatDate(currentItem.date))
        holder.cardView.setOnClickListener{
                view: View->
            //ПОМЕНЯТЬ UPDATE
            Navigation.findNavController(view).navigate(R.id.fragmentUpdateRecordMarkNum2)
            this.viewModel.updateNum2Id1.value = dataList[position].id1
            this.viewModel.updateNum2Id2.value = dataList[position].id2
            this.viewModel.updateNum2UserId.value = dataList[position].user_id
            this.viewModel.updateNum2KindOfMarkId.value = dataList[position].kind_of_mark_id
            this.viewModel.updateNum2Date.value = dataList[position].date
            this.viewModel.updateNum2Situation.value = dataList[position].situation
            this.viewModel.updateNum2Value1Double.value = dataList[position].value_number1
            this.viewModel.updateNum2Value2Double.value = dataList[position].value_number2


        }
    }


    class ViewHolderClass(itemView: View): RecyclerView.ViewHolder(itemView){
        val cardView: CardView = itemView.findViewById(R.id.recListItemStatisticNum2Container)
        val value1TextView: TextView = itemView.findViewById(R.id.recListItemStatisticNum2Value1)
        val value2TextView: TextView = itemView.findViewById(R.id.recListItemStatisticNum2Value2)
        val dateTextView: TextView = itemView.findViewById(R.id.recListItemStatisticNum2Date)
    }


    fun formatDate(inputDate: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        val outputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())

        val date = inputFormat.parse(inputDate)
        return outputFormat.format(date)
    }
}