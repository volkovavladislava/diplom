package com.example.mydiplom

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.Navigation

import com.example.mydiplom.databinding.FragmentListRemindersBinding


class FragmentListReminders : Fragment() {

    private var binding: FragmentListRemindersBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentListRemindersBinding.inflate(inflater, container, false)

        binding!!.bthAddPrompt.setOnClickListener {
                view: View->
            Navigation.findNavController(view).navigate(R.id.fragmentAddPrompt)
        }



        return binding!!.root
    }


}