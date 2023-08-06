package com.example.testaddproducttocart.Fargment


import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.testaddproducttocart.LoginActivity
import com.example.testaddproducttocart.R
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.ktx.Firebase
import de.hdodenhof.circleimageview.CircleImageView

class AdminAccountFragment : Fragment() {
    private lateinit var llLogout: LinearLayout
    private lateinit var llChangePassword: LinearLayout
    private lateinit var llInfoAccount: LinearLayout
    private lateinit var mview: View
    private lateinit var tvName: TextView
    private lateinit var tvEmail: TextView
    private lateinit var imgAvatar: CircleImageView
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var uri: Uri
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mview = inflater.inflate(R.layout.admin_account_fragment, container, false)
        initUi()
        activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                if (data != null) {
                    uri = data.data!!
                    imgAvatar.setImageURI(uri)
                }
            } else {
                Toast.makeText(activity, "No Image Selected", Toast.LENGTH_SHORT).show()
            }
        }
        showUserInformation()


        initListener()
        return mview
    }




    private fun initUi() {
        llLogout = mview.findViewById(R.id.ll_logout)
        llChangePassword = mview.findViewById((R.id.ll_change_password))
        llInfoAccount = mview.findViewById(R.id.ll_info_account)
        tvName = mview.findViewById(R.id.tv_name)
        tvEmail = mview.findViewById(R.id.tv_email)
        imgAvatar = mview.findViewById(R.id.img_avatar)
    }

    private fun showUserInformation() {
        val user = FirebaseAuth.getInstance().currentUser ?: return

        val name = user.displayName
        val email = user.email
        val photoUrl = user.photoUrl

        if (name == null) {
            tvName.visibility = View.GONE
        } else {
            tvName.visibility = View.VISIBLE
            tvName.text = name
        }


        tvEmail.text = email
        Glide.with(this).load(photoUrl).override(120, 120).error(R.drawable.ic_avatar_default)
            .into(imgAvatar)
    }

    private fun initListener() {
        llLogout.setOnClickListener {
            logout()
        }
        llChangePassword.setOnClickListener {
            changePassword()
        }
        llInfoAccount.setOnClickListener {
            infoAccount()
        }
//        imgAvatar.setOnClickListener{
//            val photoPicker=Intent(Intent.ACTION_PICK)
//            photoPicker.type = "image/*"
//            activityResultLauncher.launch(photoPicker)
//            val storageReference: StorageReference = FirebaseStorage.getInstance().reference.child("Data Images").child(uri.lastPathSegment!!)
//            val builder=AlertDialog.Builder(requireContext())
//            builder.setCancelable(false)
//            builder.setView(R.layout.progress_layout)
//            val  dialog=builder.create()
//            dialog.show()
//            storageReference.putFile(uri).addOnSuccessListener {
//                val uriTask=it.storage.downloadUrl
//                while (!uriTask.isComplete);
//                val urlImage = uriTask.result
//                val imageURL = urlImage.toString()
//                dialog.dismiss()
//            }.addOnFailureListener {
//                dialog.dismiss()
//            }
//
//        }

    }

    private fun infoAccount() {

        val builder = AlertDialog.Builder(requireContext())
        val dialogInfoAccount = layoutInflater.inflate(R.layout.dialog_info_account, null)
        val edtName: EditText = dialogInfoAccount.findViewById(R.id.edt_name)
        val edtEmail: EditText = dialogInfoAccount.findViewById(R.id.edt_email)
        val btnUpdateInfo: Button = dialogInfoAccount.findViewById(R.id.btn_update_info)
        val edtPassword: EditText = dialogInfoAccount.findViewById(R.id.edt_password)
        builder.setView(dialogInfoAccount)
        val dialog: AlertDialog = builder.create()

        //set user information
        val user = FirebaseAuth.getInstance().currentUser ?: return//?là user ==null

        edtName.setText(user.displayName)
        edtEmail.setText(user.email)

        btnUpdateInfo.setOnClickListener {
            val user = Firebase.auth.currentUser
            val profileUpdates = userProfileChangeRequest {
                val strNewName: String = edtName.text.toString().trim()
                displayName = strNewName


            }
            user!!.updateProfile(profileUpdates)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {

                        showUserInformation()
                    }

                }
            val strNewEmail: String = edtEmail.text.toString().trim()
            val password = edtPassword.text.toString().trim()
            if (TextUtils.isEmpty(password)) {
                Toast.makeText(context, "Nhập mật khẩu hiện tại", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
//            updateEmail(strNewEmail, password)
            val authCredential: AuthCredential =
                EmailAuthProvider.getCredential(user.email!!, password)
            user.reauthenticate(authCredential)
                .addOnSuccessListener {
                    user.updateEmail(strNewEmail)
                        .addOnSuccessListener {
                            //Toast.makeText(context, "Đã thay đổi mật khẩu", Toast.LENGTH_SHORT).show()
                            showUserInformation()
                            dialog.dismiss()
                            Toast.makeText(
                                context,
                                "Cập nhật thành công thông tin người dùng",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        .addOnFailureListener {
                            Toast.makeText(context, "" + it.message, Toast.LENGTH_SHORT).show()
                        }
                }
                .addOnFailureListener {
                    Toast.makeText(context, "" + it.message, Toast.LENGTH_SHORT).show()
                }


        }
        dialog.show()


    }



    private fun changePassword() {
        val builder = AlertDialog.Builder(requireContext())
        val dialogView = layoutInflater.inflate(R.layout.dialog_update_password, null)
        val edtCurrentPassword: EditText = dialogView.findViewById(R.id.edt_current_password)
        val edtChangePassword: EditText = dialogView.findViewById(R.id.edt_password_change)
        builder.setView(dialogView)
        val dialog: AlertDialog = builder.create()
        dialogView.findViewById<Button>(R.id.btnChange).setOnClickListener {
            val currentPassword = edtCurrentPassword.text.toString().trim()
            val newPassword = edtChangePassword.text.toString().trim()
            if (TextUtils.isEmpty(currentPassword)) {
                Toast.makeText(context, "Nhập mật khẩu hiện tại", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (newPassword.length < 6) {
                Toast.makeText(context, "Mật khẩu phải ít nhất 6 kí tự", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            updatePassword(currentPassword, newPassword)
            dialog.dismiss()

        }
        dialogView.findViewById<Button>(R.id.btnCancel).setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun updatePassword(currentPassword: String, newPassword: String) {
        val user = Firebase.auth.currentUser
        val authCredential: AuthCredential =
            EmailAuthProvider.getCredential(user?.email!!, currentPassword)
        user.reauthenticate(authCredential)
            .addOnSuccessListener {
                user.updatePassword(newPassword)
                    .addOnSuccessListener {
                        Toast.makeText(context, "Đã thay đổi mật khẩu", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener {
                        Toast.makeText(context, "" + it.message, Toast.LENGTH_SHORT).show()
                    }
            }
            .addOnFailureListener {
                Toast.makeText(context, "" + it.message, Toast.LENGTH_SHORT).show()
            }


    }


    private fun logout() {
        Firebase.auth.signOut()
        val intent = Intent(activity, LoginActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }
}