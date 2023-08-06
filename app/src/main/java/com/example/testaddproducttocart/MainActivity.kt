package com.example.testaddproducttocart

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.testaddproducttocart.Fargment.*
import com.example.testaddproducttocart.Listener.ICartLoadListener
import com.example.testaddproducttocart.Model.CartModel
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {
    private lateinit var badgeDrawable: BadgeDrawable
    private lateinit var bottomNavigationView: BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bottomNavigationView = findViewById(R.id.bottomNavigationView)







        replaceFragment(Home())
        bottomNavigationView.setOnItemSelectedListener  { menuItem ->
            when (menuItem.itemId) {
                R.id.home -> {
                    // Xử lý khi người dùng chọn mục Home
                    replaceFragment( Home())
                    true
                }
                R.id.cart -> {
                    // Xử lý khi người dùng chọn mục Search
                    replaceFragment( Cart())
                    true
                }
                R.id.feedback -> {
                    // Xử lý khi người dùng chọn mục Profile
                    replaceFragment( Feedback())
                    true
                }
                R.id.contact -> {
                    // Xử lý khi người dùng chọn mục Profile
                    replaceFragment( Contact())
                    true
                }
                R.id.profile -> {
                    // Xử lý khi người dùng chọn mục Profile
                    replaceFragment( Profile())
                    true
                }
                else -> false
            }
        }

    }
    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager: FragmentManager = supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout, fragment)
        fragmentTransaction.addToBackStack(null) // Optional: Add the fragment to the back stack to allow back navigation
        fragmentTransaction.commit()
    }

    fun updateBadge(count: Int) {
        badgeDrawable=bottomNavigationView.getOrCreateBadge(R.id.cart)
        badgeDrawable.isVisible = true
        badgeDrawable.isVisible = count > 0
        badgeDrawable.number = count
    }
    override fun onBackPressed() {
        val currentFragment = supportFragmentManager.findFragmentById(R.id.frameLayout)
        val homeFragment = Home()

        if (currentFragment != homeFragment) {
            // Nếu không phải, thực hiện điều hướng về Fragment Home bằng cách chọn tab Home trong BottomNavigationView
            bottomNavigationView.selectedItemId = R.id.home
        } else {
            // Nếu Fragment hiện tại là Fragment Home, thực hiện việc "Back" thông thường
            super.onBackPressed()
        }
    }


}