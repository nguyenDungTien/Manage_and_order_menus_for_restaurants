package com.example.testaddproducttocart

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContentProviderCompat.requireContext
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {
    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var btnLogin: Button
    private lateinit var btnSignUp: LinearLayout
    private lateinit var auth: FirebaseAuth
    private lateinit var fStore: FirebaseFirestore
    private lateinit var forgotPassword:LinearLayout
    private var valid: Boolean = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        initUi()


        initListener()
    }

    private fun initUi() {
        email = findViewById(R.id.edt_email)
        password = findViewById(R.id.edt_password)
        btnLogin = findViewById(R.id.btn_login)
        forgotPassword = findViewById(R.id.layout_forgot_password)
        btnSignUp = findViewById(R.id.layout_sign_up)
        auth = FirebaseAuth.getInstance()
        fStore = FirebaseFirestore.getInstance()
    }

    private fun checkField(textField: EditText): Boolean {
        if (textField.text.toString().isEmpty()) {
            textField.error = "Error"
            valid = false
        } else {
            valid = true
        }

        return valid
    }

    private fun initListener() {
        btnSignUp.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }
        btnLogin.setOnClickListener {
            onClickSignIn()
        }
        forgotPassword.setOnClickListener{
            onClickForgotPassword()
        }

    }



    private fun onClickSignIn() {
        checkField(email)
        checkField(password)
        if (valid) {
            auth.signInWithEmailAndPassword(email.text.toString(), password.text.toString())
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        task.result.user?.let { checkUserAccessLevel(it.uid) }

                    } else {
                        // Sign-in fails
                        Toast.makeText(
                            this,
                            "Authentication failed.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
    }

    private fun checkUserAccessLevel(uid: String) {
        val df: DocumentReference = fStore.collection("Users").document(uid)

        df.get().addOnSuccessListener { documentSnapshot ->
            if (documentSnapshot.getString("isAdmin") != null) {
                startActivity(Intent(applicationContext, AdminActivity::class.java))
                finish()
            }
            if (documentSnapshot.getString("isUser") != null) {
                startActivity(Intent(applicationContext, MainActivity::class.java))
                finish()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            val userId = currentUser.uid
            val usersCollection = FirebaseFirestore.getInstance().collection("Users")
            val userDocument = usersCollection.document(userId)

            userDocument.get().addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.getString("isAdmin") != null) {
                    // User is an admin
                    startActivity(Intent(applicationContext, AdminActivity::class.java))
                    finish()
                } else if (documentSnapshot.getString("isUser") != null) {
                    // User is a regular user
                    startActivity(Intent(applicationContext, MainActivity::class.java))
                    finish()
                } else {
                    // User document does not have admin or user access level
                    handleInvalidAccessLevel()
                }
            }.addOnFailureListener {
                // Failed to retrieve user document
                handleInvalidAccessLevel()
            }
        }
    }

    private fun handleInvalidAccessLevel() {
        FirebaseAuth.getInstance().signOut()
        startActivity(Intent(applicationContext, LoginActivity::class.java))
        finish()
    }
    private fun onClickForgotPassword() {
        val builder=AlertDialog.Builder(this@LoginActivity)
        val dialogView=layoutInflater.inflate(R.layout.dialog_forgot,null)
        val editText=dialogView.findViewById<EditText>(R.id.emailBox)
        builder.setView(dialogView)
        val dialog:AlertDialog=builder.create()
        dialogView.findViewById<Button>(R.id.btnReset).setOnClickListener {
            val userEmail=editText.text.toString().trim()
            if (TextUtils.isEmpty(userEmail)&&!Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()){
                Toast.makeText(this@LoginActivity, "Enter your registered email id", Toast.LENGTH_SHORT).show()
            }
            Firebase.auth.sendPasswordResetEmail(userEmail)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Check your email", Toast.LENGTH_SHORT).show()
                        dialog.dismiss()
                    } else {
                        Toast.makeText(this, "Unable to send, failed", Toast.LENGTH_SHORT).show()
                    }
                }
        }
        dialogView.findViewById<Button>(R.id.btnCancel).setOnClickListener{
            dialog.dismiss()
        }

        dialog.show()

    }
    override fun onBackPressed() {
        // Show an alert dialog to confirm if the user wants to exit
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle("Exit App")
        alertDialogBuilder.setMessage("Are you sure you want to exit the app?")
        alertDialogBuilder.setPositiveButton("Yes") { _, _ ->
            // If the user confirms, finish the activity to exit the app
            finish()
        }
        alertDialogBuilder.setNegativeButton("No") { dialog, _ ->
            // If the user cancels, simply dismiss the dialog
            dialog.dismiss()
        }

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }
}







