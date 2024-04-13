package com.example.mydiplom

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mydiplom.adapters.ListAdapterAddMarks
import com.example.mydiplom.adapters.ListAdapterBlog
import com.example.mydiplom.data.Blog
import com.example.mydiplom.data.KindOfMark
import com.example.mydiplom.databinding.FragmentBlogBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class FragmentBlog : Fragment() {


    private var binding: FragmentBlogBinding? = null

    private lateinit var listAdapterBlog: ListAdapterBlog
    private lateinit var listData: Blog
    var dataArrayList = ArrayList<Blog?>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = FragmentBlogBinding.inflate(inflater, container, false)


        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3000")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service: ApiController = retrofit.create(ApiController::class.java)
        val call: Call<List<Blog>> = service.getlistBlog()


        call.enqueue(object : Callback<List<Blog>> {
            override fun onResponse(call: Call<List<Blog>>, response: Response<List<Blog>>) {
                if (response.isSuccessful) {
                    Log.d("RetrofitClient","response " + response.body() )

                    var blogs = response.body() ?: emptyList()
                    dataArrayList.clear()
                    for(i in blogs.indices){
                        listData = Blog(blogs[i].id,blogs[i].title,blogs[i].description,blogs[i].link)
                        dataArrayList.add(listData)
                    }

                    val a = binding!!.listBlog
                    a.apply {
                        adapter =  ListAdapterBlog(this.context, dataArrayList)
                        isClickable = true}

                }
            }
            override fun onFailure(call: Call<List<Blog>>, t: Throwable) {
                Log.d("RetrofitClient","Receive kindOfMarks from server problem " + t)
            }
        })



        return binding!!.root
    }


}