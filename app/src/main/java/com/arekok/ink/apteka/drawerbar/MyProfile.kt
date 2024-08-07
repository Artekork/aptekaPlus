package com.arekok.ink.apteka.drawerbar

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.arekok.ink.apteka.Registr
import com.arekok.ink.apteka.databinding.ActivityMyProfileBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import android.R
import android.R.attr

import com.arekok.ink.apteka.MainActivity

import android.R.attr.data
import android.annotation.SuppressLint
import android.net.Uri
import android.opengl.Visibility
import android.widget.Toast
import com.arekok.ink.apteka.Autorization
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.database.DatabaseError

import androidx.annotation.NonNull
import com.google.android.gms.auth.api.signin.internal.Storage

import com.google.firebase.database.DataSnapshot

import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlin.system.exitProcess


class MyProfile : AppCompatActivity() {
    private lateinit var database: DatabaseReference
    private lateinit var dataStorage: StorageReference
    val db = Firebase.firestore

    lateinit var binding3: ActivityMyProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        val userName = intent.getStringExtra("user_login").toString()
        super.onCreate(savedInstanceState)
        binding3= ActivityMyProfileBinding.inflate(layoutInflater)
        setContentView(binding3.root)

        val databaseUsers = FirebaseDatabase.getInstance().getReference("users")
        databaseUsers.child(userName).get().addOnSuccessListener {
            val photoUrl1:String = it.child("photourl").value.toString()
            if (photoUrl1 != "" && photoUrl1 != "null") {
                Picasso.get().load(photoUrl1).into(binding3.imageView4)
            }
        }

        readData(userName)


        binding3.save.setOnClickListener{
            changeData(userName)
        }
        var i = 0
        binding3.deleteUser.setOnClickListener{

            if (i==1) onClickDeleteUser(userName)
            else Toast.makeText(this, "Вы уверены?", Toast.LENGTH_SHORT).show()
            i++
        }
        binding3.cardView2.setOnClickListener{
            onClickChangePhoto()
        }
        binding3.changePhotoProf.setOnClickListener{
            onClickChangePhoto()
        }
    }

    private fun readData(userName: String) {
        database = FirebaseDatabase.getInstance().getReference("users")
        database.child(userName).get().addOnSuccessListener {

            val name = it.child("name").value
            val surname = it.child("surname").value
            val login = it.child("login").value
            val location = it.child("location").value
            val pass = it.child("pass").value
            val mail = it.child("mail").value
            val phone = it.child("phone").value
            binding3.loginProfText.text = login.toString()
            if (name != null){
                binding3.nameInp.hint= name.toString()
            }else{
                binding3.nameInp.hint= ""
            }
            if (surname != null){
                binding3.surNameInp.hint= surname.toString()
            }else{
                binding3.surNameInp.hint= ""
            }
            if (location != null){
                binding3.locationInp.hint=location.toString()
            }else{
                binding3.locationInp.hint= ""
            }
            if (pass != null){
                binding3.passInp.hint=pass.toString()
            }else{
                binding3.passInp.hint= ""
            }
            if (mail != null){
                binding3.mailInp.hint=mail.toString()
            }else{
                binding3.mailInp.hint= ""
            }
            if (phone != null){

                val phone2 = phone.toString().addCharAtIndex('+', 0)
                val phone3 = phone2.toString().addCharAtIndex('3', 1)
                val phone4 = phone3.toString().addCharAtIndex('7', 2)
                val phone5 = phone4.toString().addCharAtIndex('5', 3)
                val phone6 = phone5.toString().addCharAtIndex(' ', 4)
                val phone7 = phone6.toString().addCharAtIndex('(', 5)
                val phone8 = phone7.toString().addCharAtIndex(')', 8)
                val phone9 = phone8.toString().addCharAtIndex(' ', 9)
                val phone10 = phone9.toString().addCharAtIndex(' ', 13)
                val lastPhone = phone10.toString().addCharAtIndex(' ', 16)
                binding3.phoneInp.hint=lastPhone.toString()
            }else{
                binding3.phoneInp.hint= ""
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun changeData(userName: String){
        database = FirebaseDatabase.getInstance().getReference("users")
        database.child(userName).get().addOnSuccessListener {
            if (binding3.nameInp.text.toString() != ""){
                val newName = binding3.nameInp.text.toString()
                database.child(userName).child("name").setValue(newName)
                database.child(userName).get().addOnSuccessListener {
                    val name = it.child("name").value
                    binding3.nameInp.hint= name.toString()
                }
                binding3.nameInp.text = null
            }
            if (binding3.surNameInp.text.toString() != ""){
                val newSurName = binding3.surNameInp.text.toString()
                database.child(userName).child("surname").setValue(newSurName)
                database.child(userName).get().addOnSuccessListener {
                    val surname = it.child("surname").value
                    binding3.surNameInp.hint= surname.toString()
                }
                binding3.surNameInp.text = null
            }
            if (binding3.locationInp.text.toString() != ""){
                val newLocation = binding3.locationInp.text.toString()
                database.child(userName).child("location").setValue(newLocation)
                database.child(userName).get().addOnSuccessListener {

                    val location = it.child("location").value
                    binding3.locationInp.hint= location.toString()
                }
                binding3.locationInp.text = null
            }
            if (binding3.passInp.text.toString() != ""){
                if (" " in binding3.passInp.text.toString()) {
                    binding3.errorProf.visibility = View.VISIBLE
                    binding3.errorProf.text = "Пароль не должен содержать пробелов"
                } else {
                    if (binding3.passInp.text.toString() != "") {
                        val newPass = binding3.passInp.text.toString()
                        database.child(userName).child("pass").setValue(newPass)
                        database.child(userName).get().addOnSuccessListener {
                            val pass = it.child("pass").value
                            binding3.passInp.hint = pass.toString()
                            binding3.errorProf.text = ""
                            binding3.errorProf.visibility = View.GONE
                        }
                        binding3.passInp.text = null
                    }
                }
            }

            if (binding3.mailInp.text.toString() != ""){
                if (" " in binding3.mailInp.text.toString()) {
                    binding3.errorProf.visibility = View.VISIBLE
                    binding3.errorProf.text = "Почта не должна содержать пробелов"
                } else {
                    if ("\u0040" in binding3.mailInp.text.toString()) {
                        val newMail = binding3.mailInp.text.toString()
                        database.child(userName).child("mail").setValue(newMail)
                        database.child(userName).get().addOnSuccessListener {

                            val mail = it.child("mail").value
                            binding3.mailInp.hint = mail.toString()
                            binding3.errorProf.text = ""
                            binding3.errorProf.visibility = View.GONE
                        }
                        binding3.mailInp.text = null
                    } else{
                        binding3.errorProf.visibility = View.VISIBLE
                        binding3.errorProf.text = "Почта должна содержать символ \u0040"
                    }
                }
            }

            if (binding3.phoneInp.text.toString() != ""){
                if (binding3.phoneInp.text.toString().length == 9) {
                    val newPhone = binding3.phoneInp.text.toString()
                    database.child(userName).child("phone").setValue(newPhone)
                    database.child(userName).get().addOnSuccessListener {

                        val phone = it.child("phone").value

                        val phone2 = phone.toString().addCharAtIndex('+', 0)
                        val phone3 = phone2.toString().addCharAtIndex('3', 1)
                        val phone4 = phone3.toString().addCharAtIndex('7', 2)
                        val phone5 = phone4.toString().addCharAtIndex('5', 3)
                        val phone6 = phone5.toString().addCharAtIndex(' ', 4)
                        val phone7 = phone6.toString().addCharAtIndex('(', 5)
                        val phone8 = phone7.toString().addCharAtIndex(')', 8)
                        val phone9 = phone8.toString().addCharAtIndex(' ', 9)
                        val phone10 = phone9.toString().addCharAtIndex(' ', 13)
                        val lastPhone = phone10.toString().addCharAtIndex(' ', 16)

                        binding3.phoneInp.hint = lastPhone.toString()
                        binding3.errorProf.text = ""
                        binding3.errorProf.visibility = View.GONE

                    }
                    binding3.phoneInp.text = null
                } else {
                    binding3.errorProf.visibility = View.VISIBLE
                    binding3.errorProf.text = "Только последние 9 цифр!"
                }
            }

        }
    }

    fun String.addCharAtIndex(char: Char, index: Int) =
        StringBuilder(this).apply { insert(index, char) }.toString()
    fun onClickBack(view: View){
        val perenosToMain = Intent(this, MainActivity::class.java)
        perenosToMain.putExtra("user_login", binding3.loginProfText.text)
        startActivity(perenosToMain)
        finish()
    }
    fun onClickDeleteUser(userName: String){

        val reference = FirebaseDatabase.getInstance().getReference("users")
        reference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                reference.child(userName).ref.removeValue()
            }
            override fun onCancelled(databaseError: DatabaseError) {}
        })
        Toast.makeText(this, "Аккаунт успешно удалён", Toast.LENGTH_SHORT).show()
        val perenosToStart = Intent(this, Autorization::class.java)
        startActivity(perenosToStart)
        finish()
    }
    companion object{
        val IMAGE_REQUEST_CODE = 100
    }
    fun onClickChangePhoto(){
        CropImage.activity()
            .setAspectRatio(1,1)
            .setRequestedSize(600,600)
            .setCropShape(CropImageView.CropShape.OVAL)
            .start(this)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK && data != null){
            val uri = CropImage.getActivityResult(data).uri
            dataStorage = FirebaseStorage.getInstance().reference
            var userName1 = intent.getStringExtra("user_login").toString()
            val path = dataStorage.child("usersImage").child(userName1)
            path.putFile(uri).addOnCompleteListener{ task1 ->
                if (task1.isSuccessful){
                    path.downloadUrl.addOnCompleteListener{ task2 ->
                        if (task2.isSuccessful){
                            val photoUrl: String = task2.result.toString()
                            database.child(userName1).child("photourl").setValue(photoUrl).addOnCompleteListener{
                                if (it.isSuccessful){
                                    Picasso.get()
                                        .load(photoUrl)
                                        .into(binding3.imageView4)
                                    Toast.makeText(this, "Успешно", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    }
                }
            }
        }
    }

}