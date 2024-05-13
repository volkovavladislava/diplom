package com.example.mydiplom

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.mydiplom.databinding.MainActivityBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        setSupportActionBar(findViewById(R.id.toolbarLogin))
        drawerLayout = findViewById(R.id.drawerLayoutLogin)

        val navController = findNavController(R.id.appNavHostFragmentLogin)
        NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout)


//        if (savedInstanceState == null) {
//            supportFragmentManager.beginTransaction()
//                .add(R.id.fragment_container, FragmentLogin()) // Добавляем фрагмент
//                .commit()
//        }

    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.appNavHostFragmentLogin)
        return NavigationUI.navigateUp(navController, drawerLayout)
    }




}