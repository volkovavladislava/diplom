package com.example.mydiplom

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.mydiplom.databinding.FragmentDetailedPromptBinding
import com.example.mydiplom.databinding.FragmentListRemindersBinding
import com.example.mydiplom.viewmodel.SharedViewModel

class FragmentDetailedPrompt : Fragment() {

    private var binding: FragmentDetailedPromptBinding? = null
    private val viewModel: SharedViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentDetailedPromptBinding.inflate(inflater, container, false)

        var promptId = viewModel.promptId.value ?: 1
        Log.d("RetrofitClient","promptId " + promptId)

        return binding!!.root

    }


}