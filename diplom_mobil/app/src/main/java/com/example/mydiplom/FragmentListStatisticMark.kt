package com.example.mydiplom

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mydiplom.databinding.FragmentListStatisticMarkBinding

class FragmentListStatisticMark : Fragment() {


    private var binding: FragmentListStatisticMarkBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentListStatisticMarkBinding.inflate(inflater, container, false)
        return binding!!.root
    }


}