package com.example.mydiplom

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.example.mydiplom.adapters.ListAdapterAddMarks
import com.example.mydiplom.adapters.ListAdapterBlog
import com.example.mydiplom.data.Blog
import com.example.mydiplom.data.User
import com.example.mydiplom.databinding.FragmentArticleOfBlogBinding
import com.example.mydiplom.databinding.FragmentListAddMarksBinding
import com.example.mydiplom.viewmodel.SharedViewModel
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class FragmentArticleOfBlog : Fragment() {

    private var binding: FragmentArticleOfBlogBinding? = null

    private val viewModel: SharedViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentArticleOfBlogBinding.inflate(inflater, container, false)

        var blogId = viewModel.articleId.value ?: 1

        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3000")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service: ApiController = retrofit.create(ApiController::class.java)
        val call: Call<Blog> = service.getBlog(blogId)

        call.enqueue(object : Callback<Blog> {
            override fun onResponse(call: Call<Blog>, response: Response<Blog>) {
                if (response.isSuccessful) {
                    var blogData = response.body()
                    Log.d("RetrofitClient"," blogData" + blogData)
                    blogData?.let {
                        val imageUrl = "http://10.0.2.2:3000/files/" + it.link
                        Picasso.get()
                            .load(imageUrl)
                            .into(binding!!.blogImage)

                        binding!!.blogTitle.setText(it.title.toString())
                        binding!!.blogDescription.setText(it.description.toString())

                    }
                }
                else{}
            }
            override fun onFailure(call: Call<Blog>, t: Throwable) {
                Log.d("RetrofitClient","Receive user from server problem " + t)
            }
        })

        return binding!!.root
}


}