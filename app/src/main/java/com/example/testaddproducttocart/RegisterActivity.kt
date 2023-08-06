package com.example.testaddproducttocart

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.collections.HashMap

class RegisterActivity : AppCompatActivity() {
    private lateinit var name: EditText
    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var phone: EditText
    private lateinit var registerBtn: Button
    private lateinit var goToLogin: Button
    private lateinit var auth: FirebaseAuth
    private lateinit var fStore: FirebaseFirestore
    var valid: Boolean = true
    private lateinit var chkadmin: CheckBox
    private lateinit var chkuser: CheckBox
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        initUi()

        //hàm bắt sự kiện
        initListener()
    }


    private fun initUi() {
        name = findViewById(R.id.edt_name)
        email = findViewById(R.id.edt_email)
        password = findViewById(R.id.edt_password)
        phone = findViewById(R.id.edt_phone)
        registerBtn = findViewById(R.id.btn_register)
        auth = FirebaseAuth.getInstance()
        fStore = FirebaseFirestore.getInstance()
        chkadmin = findViewById(R.id.chk_admin)
        chkuser = findViewById(R.id.chk_user)
        goToLogin = findViewById(R.id.gotoLogin)
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
        registerBtn.setOnClickListener {
            onClickSignIn()
        }
        chkadmin.setOnCheckedChangeListener { _, isChecked ->
            // Handle the checked state change here
            if (isChecked) {
                chkuser.isChecked = false
            }
        }
        chkuser.setOnCheckedChangeListener { _, isChecked ->
            // Handle the checked state change here
            if (isChecked) {
                chkadmin.isChecked = false
            }
        }
        goToLogin.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finishAffinity()
        }
    }

    private fun onClickSignIn() {
        checkField(name)
        checkField(email)
        checkField(password)
        checkField(phone)
        if (!(chkadmin.isChecked || chkuser.isChecked)) {
            Toast.makeText(this, "Select the account type", Toast.LENGTH_LONG).show()
        }
        if (valid) {
            auth.createUserWithEmailAndPassword(email.text.toString(), password.text.toString())
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val user: FirebaseUser = auth.currentUser!!
                        val df: DocumentReference = fStore.collection("Users").document(user.uid)
                        val userInfo: MutableMap<String, Any> = HashMap()
                        userInfo["Name"] = name.text.toString()
                        userInfo["Email"] = email.text.toString()
                        userInfo["Phone"] = phone.text.toString()
                        userInfo["imageURL"] = "null"
                        if (chkadmin.isChecked) {
                            userInfo["isAdmin"] = "1"
                            startActivity(Intent(applicationContext, AdminActivity::class.java))
                            finish()
                        }
                        if (chkuser.isChecked) {
                            userInfo["isUser"] = "1"
                            startActivity(Intent(applicationContext, MainActivity::class.java))
                            finish()
                        }

                        df.set(userInfo)
                        Toast.makeText(
                            this,
                            "Authentication success.",
                            Toast.LENGTH_SHORT,
                        ).show()


                    } else {
                        // If sign in fails, display a message to the user.

                        Toast.makeText(
                            this,
                            "Authentication failed.",
                            Toast.LENGTH_SHORT,
                        ).show()

                    }
                }
        }

    }

}