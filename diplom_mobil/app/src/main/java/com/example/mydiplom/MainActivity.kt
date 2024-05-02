package com.example.mydiplom

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.mydiplom.data.User
import com.example.mydiplom.databinding.MainActivityBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MainActivity : AppCompatActivity() {

    private lateinit var binding: MainActivityBinding
    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3000")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service: ApiController = retrofit.create(ApiController::class.java)


        val toolbar = binding.toolbar;
        setSupportActionBar(toolbar);

        drawerLayout = binding.drawerLayout

        val navController = this.findNavController(R.id.appNavHostFragment)
        NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout)
        NavigationUI.setupWithNavController(binding.navView, navController)
        val navigationView = binding.navView
        val headerView = navigationView.getHeaderView(0)


        val call: Call<User> = service.getUser(1)
        call.enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {
                    var userData = response.body()
                    userData?.let {
                        val textView = headerView.findViewById<TextView>(R.id.textViewMenuLabelProfile)
                        textView.text = userData.name
                    }
                }
                else{}
            }
            override fun onFailure(call: Call<User>, t: Throwable) {
                Log.d("RetrofitClient","Receive user from server problem " + t)
            }
        })


        headerView.setOnClickListener {
                view: View ->
            navController.navigate(R.id.fragmentProfile)
            drawerLayout.closeDrawer(GravityCompat.START)

        }

    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.appNavHostFragment)
        return NavigationUI.navigateUp(navController, drawerLayout)
    }
}

