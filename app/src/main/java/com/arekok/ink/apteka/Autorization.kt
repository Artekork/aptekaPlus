package com.arekok.ink.apteka

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.arekok.ink.apteka.databinding.ActivityAutorizationBinding
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DatabaseReference

class Autorization : AppCompatActivity() {
    private lateinit var database: DatabaseReference

    lateinit var binding1: ActivityAutorizationBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding1=ActivityAutorizationBinding.inflate(layoutInflater)
        setContentView(binding1.root)


        binding1.autorization.setOnClickListener{
            database = FirebaseDatabase.getInstance().getReference("users")
            var mail:String = binding1.mailAutor.text.toString()
            var pass:String = binding1.passAutor.text.toString()
            val database = FirebaseDatabase.getInstance().getReference("users").child(mail)
            database.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (mail != "" && pass != ""){
                        val passClient = dataSnapshot.child("pass").value.toString()
                        if(pass == passClient){
                            Success()
                            mail = ""
                            pass = ""
                        } else {
                            binding1.errorAutor.text = "Неправильный логин или пароль"
                        }
                    } else {
                        binding1.errorAutor.text = "Введите данные"
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                }
            })
        }
    }
    fun onClickGoReg(view: View){
        val perenos2 = Intent(this, Registr::class.java)
        startActivity(perenos2)
        finish()
    }

    private fun Success(){
        val perenosToMain = Intent(this, MainActivity::class.java)
        perenosToMain.putExtra("user_login", binding1.mailAutor.text.toString())
        startActivity(perenosToMain)
        finish()
    }
}