package com.example.testaddproducttocart

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import com.example.testaddproducttocart.Fargment.AdminAccountFragment
import com.example.testaddproducttocart.Fargment.AdminFeedbackFragment
import com.example.testaddproducttocart.Fargment.AdminHomeFragment
import com.example.testaddproducttocart.Fargment.AdminOrderFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class AdminActivity : AppCompatActivity() {
    private lateinit var mBottomNavigationView: BottomNavigationView
    private lateinit var mViewPager: FrameLayout
    private lateinit var btnAddFood:FloatingActionButton
    private lateinit var adminFeedbackFragment:AdminFeedbackFragment
    private lateinit var adminHomeFragment: AdminHomeFragment
    private lateinit var adminAccountFragment: AdminAccountFragment
    private lateinit var adminOrderFragment: AdminOrderFragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)
        unitUi()
        supportFragmentManager.beginTransaction().replace(R.id.view_pager,adminHomeFragment).commit()
        unitListener()
    }




    private fun unitUi() {
        mBottomNavigationView = findViewById(R.id.bottom_nav)
        mViewPager=findViewById(R.id.view_pager)
        btnAddFood=findViewById(R.id.btn_add_food)
        adminFeedbackFragment= AdminFeedbackFragment()
        adminAccountFragment= AdminAccountFragment()
        adminOrderFragment= AdminOrderFragment()
        adminHomeFragment= AdminHomeFragment()
    }

    private fun unitListener() {
        mBottomNavigationView.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.action_home -> {
                    supportFragmentManager.beginTransaction().replace(R.id.view_pager,adminHomeFragment).commit()
                    true
                }
                R.id.action_feedback -> {
                    supportFragmentManager.beginTransaction().replace(R.id.view_pager,adminFeedbackFragment).commit()
                    true
                }
                R.id.action_order -> {
                    supportFragmentManager.beginTransaction().replace(R.id.view_pager,adminOrderFragment).commit()
                    true
                }
                R.id.action_account -> {
                    supportFragmentManager.beginTransaction().replace(R.id.view_pager,adminAccountFragment).commit()
                    true
                }

                else -> false
            }
        }
        btnAddFood.setOnClickListener {
            startActivity(Intent(this,activity_upload::class.java))
        }

    }




}


