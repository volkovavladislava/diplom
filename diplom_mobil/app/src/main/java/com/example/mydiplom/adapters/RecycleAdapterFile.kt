package com.example.mydiplom.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.mydiplom.R
import com.example.mydiplom.data.File
import com.example.mydiplom.viewmodel.SharedViewModel
import java.text.SimpleDateFormat
import java.util.Locale

class RecycleAdapterFile (private val dataList: ArrayList<File>,  private val viewModel: SharedViewModel) :
    RecyclerView.Adapter<RecycleAdapterFile.ViewHolderClass>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderClass {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.recycle_list_item_files, parent, false)
        return RecycleAdapterFile.ViewHolderClass(itemView)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: ViewHolderClass, position: Int) {
        val currentItem = dataList[position]
        holder.nameTextView.setText(currentItem.name)
        holder.dateTextView.setText(formatDate(currentItem.date))
        holder.cardView.setOnClickListener{
            view: View->
            Navigation.findNavController(view).navigate(R.id.fragmentDetailedFile)
            this.viewModel.fileId.value = dataList[position].id

        }
    }

    class ViewHolderClass(itemView: View): RecyclerView.ViewHolder(itemView){
        val cardView: CardView = itemView.findViewById(R.id.recListItemFileContainer)
        val nameTextView: TextView = itemView.findViewById(R.id.recListItemFileName)
        val dateTextView: TextView = itemView.findViewById(R.id.recListItemFileDate)
    }

    fun formatDate(inputDate: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        val outputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())

        val date = inputFormat.parse(inputDate)
        return outputFormat.format(date)
    }

}

