package com.example.testaddproducttocart

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // Đặt thời gian delay trước khi chuyển sang Activity tiếp theo (ví dụ MainActivity)
        val splashTimeOut: Long = 1000 // 1 giây

        // Sử dụng Handler để chuyển sang Activity tiếp theo sau khi đợi trong một khoảng thời gian
        Handler(Looper.getMainLooper()).postDelayed({
            nextActivity()
        }, splashTimeOut)
    }

    private fun nextActivity() {
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            val userId = currentUser.uid
            val usersCollection = FirebaseFirestore.getInstance().collection("Users")
            val userDocument = usersCollection.document(userId)

            userDocument.get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val documentSnapshot = task.result
                    if (documentSnapshot != null && documentSnapshot.exists()) {
                        if (documentSnapshot.getString("isAdmin") != null) {
                            // User is an admin
                            startActivity(Intent(this, AdminActivity::class.java))
                        } else if (documentSnapshot.getString("isUser") != null) {
                            // User is a regular user
                            startActivity(Intent(this, MainActivity::class.java))
                        } else {
                            // User document does not have admin or user access level
                            handleInvalidAccessLevel()
                        }
                    } else {
                        // Document does not exist or is null
                        handleInvalidAccessLevel()
                    }
                } else {
                    // Failed to retrieve user document
                    handleInvalidAccessLevel()
                }
                finish()
            }
        } else {
            // Current user is null (not logged in)
            handleInvalidAccessLevel()
        }
    }

    private fun handleInvalidAccessLevel() {
        FirebaseAuth.getInstance().signOut()
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}
