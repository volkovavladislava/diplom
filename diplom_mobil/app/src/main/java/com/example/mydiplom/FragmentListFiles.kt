package com.example.mydiplom

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mydiplom.databinding.FragmentListFilesBinding


class FragmentListFiles : Fragment() {

    private var binding: FragmentListFilesBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentListFilesBinding.inflate(inflater, container, false)
        return binding!!.root
    }


}