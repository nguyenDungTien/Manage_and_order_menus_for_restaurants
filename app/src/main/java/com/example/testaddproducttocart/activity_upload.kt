package com.example.testaddproducttocart

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import com.example.testaddproducttocart.Model.InputModel
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.text.DateFormat
import java.util.*

class activity_upload : AppCompatActivity() {
    private lateinit var spinner: Spinner
    private lateinit var uploadImg: ImageView
    private lateinit var saveButton: Button
    private lateinit var nameItem: EditText
    private lateinit var descItem: EditText
    private lateinit var priceItem: EditText
    private lateinit var promotionItem: EditText
    private lateinit var typeItem:String
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var uri: Uri
    private var valid: Boolean = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload)

        activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                if (data != null) {
                    uri = data.data!!
                    uploadImg.setImageURI(uri)
                }
            } else {
                Toast.makeText(this, "No Image Selected", Toast.LENGTH_SHORT).show()
            }
        }
        initView()
        setCustomSpinner()
        initListener()




    }

    private fun initView() {
        spinner = findViewById(R.id.spinner)
        saveButton=findViewById(R.id.btn_add_item)
        uploadImg=findViewById(R.id.uploadImage)
        nameItem=findViewById(R.id.edt_name_item)
        descItem=findViewById(R.id.edt_desc_item)
        priceItem=findViewById(R.id.edt_price_item)
        promotionItem=findViewById(R.id.edt_promotion_item)
    }

    private fun setCustomSpinner() {
        val items = resources.getStringArray(R.array.spinner_items)

        val adapter = ArrayAdapter(this, R.layout.custom_spinner_item, items)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedItem = items[position]
                // Thực hiện hành động với mục đã chọn
                typeItem=selectedItem
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Xử lý trường hợp không có mục nào được chọn
            }
        }
    }

    private fun initListener() {
        uploadImg.setOnClickListener {
            val photoPicker=Intent(Intent.ACTION_PICK)
            photoPicker.type = "image/*"
            activityResultLauncher.launch(photoPicker)

        }
        saveButton.setOnClickListener {
            checkField(nameItem)
            checkField(descItem)
            checkField(priceItem)
            checkField(promotionItem)
            if (valid) {
               saveData()
            }

        }
    }

    private fun saveData() {
        val storageReference: StorageReference = FirebaseStorage.getInstance().reference.child("Data Images").child(uri.lastPathSegment!!)
        val builder= AlertDialog.Builder(this)
        builder.setCancelable(false)
        builder.setView(R.layout.progress_layout)
        val  dialog=builder.create()
        dialog.show()
        storageReference.putFile(uri).addOnSuccessListener {
            val uriTask=it.storage.downloadUrl
            while (!uriTask.isComplete);
            val urlImage = uriTask.result
            val imageURL = urlImage.toString()
            uploadData(imageURL)
            dialog.dismiss()
        }.addOnFailureListener {
            dialog.dismiss()
        }
    }
    private fun uploadData(imageURL:String) {
        val nameItem:String=nameItem.text.toString()
        val typeItem:String=typeItem
        val descItem:String=descItem.text.toString()
        val priceItem:String=priceItem.text.toString()
        val promotionItem:String=promotionItem.text.toString()
        val dataInput= InputModel(nameItem,imageURL,typeItem,descItem,priceItem,promotionItem)
        val currentDate= DateFormat.getDateTimeInstance().format(Calendar.getInstance().time)
        FirebaseDatabase.getInstance("https://testaddproducttocart-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Data Items").child(currentDate).setValue(dataInput).addOnCompleteListener {
            if (it.isSuccessful){
                Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show()
                finish()
            }
        }.addOnFailureListener {
            Toast.makeText(this, it.message.toString(), Toast.LENGTH_SHORT).show()
        }
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
}