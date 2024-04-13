package com.example.mydiplom

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mydiplom.adapters.ListAdapterAddMarks
import com.example.mydiplom.adapters.ListAdapterBlog
import com.example.mydiplom.data.Blog
import com.example.mydiplom.databinding.FragmentArticleOfBlogBinding
import com.example.mydiplom.databinding.FragmentListAddMarksBinding


class FragmentArticleOfBlog : Fragment() {

    private var binding: FragmentArticleOfBlogBinding? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentArticleOfBlogBinding.inflate(inflater, container, false)





        return binding!!.root
//        return inflater.inflate(R.layout.fragment_article_of_blog, container, false)
    }


}