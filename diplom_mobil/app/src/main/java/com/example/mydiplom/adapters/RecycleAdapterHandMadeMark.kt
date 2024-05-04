package com.example.mydiplom.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.mydiplom.R
import com.example.mydiplom.data.KindOfMark
import com.example.mydiplom.viewmodel.SharedViewModel

class RecycleAdapterHandMadeMark (private val context: Context, private val dataList: ArrayList<KindOfMark>, private val viewModel: SharedViewModel) :
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
        var isFavorite = false
        holder.bth.setText(currentItem.name)

        if(currentItem.has_reference == 1){
            isFavorite = true
            holder.bthFavorite.foreground = ContextCompat.getDrawable(context, R.drawable.baseline_favorite_24_red)
        }

        holder.bthFavorite.setOnClickListener {
            if(isFavorite){
                holder.bthFavorite.foreground = ContextCompat.getDrawable(context, R.drawable.baseline_favorite_24_grey)
                viewModel.deleteFavorite(context, viewModel.userId, currentItem.id)
            }
            else{
                holder.bthFavorite.foreground = ContextCompat.getDrawable(context, R.drawable.baseline_favorite_24_red)
                viewModel.addToFavorite(context, viewModel.userId, currentItem.id)
            }
            isFavorite = !isFavorite
        }



        val bundle = bundleOf("title" to currentItem.name )

        holder.cardView.setOnClickListener{
                view: View->
            Navigation.findNavController(view).navigate(R.id.fragmentDetailedHandMadeMark, bundle)
            this.viewModel.handMadeMarkId.value = dataList[position].id
        }
    }


    class ViewHolderClass(itemView: View): RecyclerView.ViewHolder(itemView){
        val cardView: CardView = itemView.findViewById(R.id.recListItemHandMadeMarkContainer)
        val bth: TextView = itemView.findViewById(R.id.bthAddNewHandMadeMark)
        val bthFavorite: Button = itemView.findViewById(R.id.bthMatchFavoriteHandMadeMarks)
    }

}