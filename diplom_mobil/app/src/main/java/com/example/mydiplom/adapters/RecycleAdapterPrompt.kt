package com.example.mydiplom.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.mydiplom.R
import com.example.mydiplom.data.Prompt
import com.example.mydiplom.viewmodel.SharedViewModel
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class RecycleAdapterPrompt(private val dataList: ArrayList<Prompt>, val listener: Listener, private val viewModel: SharedViewModel) :
    RecyclerView.Adapter<RecycleAdapterPrompt.ViewHolderClass>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderClass {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.recycle_list_item_reminders, parent, false)
        return ViewHolderClass(itemView)
    }

    override fun getItemCount(): Int {
       return dataList.size
    }

    override fun onBindViewHolder(holder: ViewHolderClass, position: Int) {
        val currentItem = dataList[position]
        holder.nameTextView.setText(currentItem.name)
        holder.dateTextView.setText(formatDate(currentItem.date))
        holder.cardView.setOnClickListener{
//            fun onClick(prompt: Prompt){

                view: View->
                Navigation.findNavController(view).navigate(R.id.fragmentDetailedPrompt)
                this.viewModel.promptId.value = dataList[position].id


//                listener.onItemClick(dataList[position])
//            }
        }

    }

    class ViewHolderClass(itemView: View): RecyclerView.ViewHolder(itemView){
        val cardView: CardView = itemView.findViewById(R.id.recListItemReminderContainer)
        val nameTextView: TextView = itemView.findViewById(R.id.recListItemReminderName)
        val dateTextView: TextView = itemView.findViewById(R.id.recListItemReminderDate)
    }


    interface Listener{
        fun onItemClick(prompt: Prompt)
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

