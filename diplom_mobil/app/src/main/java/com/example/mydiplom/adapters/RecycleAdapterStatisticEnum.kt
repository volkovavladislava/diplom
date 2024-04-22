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

class RecycleAdapterStatisticEnum (private val dataList: ArrayList<Mark>, private val viewModel: SharedViewModel) :
    RecyclerView.Adapter<RecycleAdapterStatisticEnum.ViewHolderClass>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecycleAdapterStatisticEnum.ViewHolderClass {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.recycle_list_item_statistic_enum, parent, false)
        return RecycleAdapterStatisticEnum.ViewHolderClass(itemView)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: RecycleAdapterStatisticEnum.ViewHolderClass, position: Int) {
        val currentItem = dataList[position]
        //поменять на нужное value
        holder.valueTextView.setText(currentItem.value_number1.toString())
        holder.dateTextView.setText(formatDate(currentItem.date))
        holder.cardView.setOnClickListener{
                view: View->
            Navigation.findNavController(view).navigate(R.id.fragmentDetailedFile)
            this.viewModel.markId.value = dataList[position].id

        }
    }


    class ViewHolderClass(itemView: View): RecyclerView.ViewHolder(itemView){
        val cardView: CardView = itemView.findViewById(R.id.recListItemStatisticEnumContainer)
        val valueTextView: TextView = itemView.findViewById(R.id.recListItemStatisticEnumName)
        val dateTextView: TextView = itemView.findViewById(R.id.recListItemStatisticEnumDate)
    }


    fun formatDate(inputDate: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        val outputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())

        val date = inputFormat.parse(inputDate)
        return outputFormat.format(date)
    }
}