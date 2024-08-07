package com.arekok.ink.apteka

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.arekok.ink.apteka.databinding.ActivityRegistrBinding
import com.google.firebase.database.*

class Registr : AppCompatActivity() {
    private lateinit var database: DatabaseReference
    lateinit var binding2: ActivityRegistrBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding2= ActivityRegistrBinding.inflate(layoutInflater)
        setContentView(binding2.root)
        binding2.create.setOnClickListener{
            var mail = binding2.mailReg.text.toString()
            var pass = binding2.passReg.text.toString()
            var pass2 = binding2.passSecond.text.toString()

            if (mail != ""){
                if (" " in mail){
                    binding2.errorReg.text = "Логин не должен содержать пробел!"
                } else {
                    database = FirebaseDatabase.getInstance().getReference("users")
                    val database = FirebaseDatabase.getInstance().getReference("users")
                    database.addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            if (dataSnapshot.hasChild(mail)) {
                                binding2.errorReg.text = "Такой тип уже есть"
                            } else {
                                if (pass != "" && pass2 != "") {
                                    if (" " in pass){
                                        binding2.errorReg.text = "Пароль не должен содержать пробел!"
                                    } else {
                                        if (pass == pass2) {
                                            binding2.errorReg.visibility = View.INVISIBLE

                                            success2(mail, pass)
                                            mail = ""
                                            pass = ""

                                        } else {
                                            binding2.errorReg.text = "Пароли не совпадают"
                                        }
                                    }
                                } else {
                                    binding2.errorReg.text = "Введите пароль"
                                }

                            }
                        }
                        override fun onCancelled(error: DatabaseError) {
                        }
                    })
                }
            } else{
                binding2.errorReg.text = "Введите логин"
            }
        }
    }

    private fun success2(ml: String, ps: String) {
        database = FirebaseDatabase.getInstance().getReference("users")
        val setLogin = database.child(ml).child("login")
        setLogin.setValue(ml)
        val setPass = database.child(ml).child("pass")
        setPass.setValue(ps)

        val perenosToMain = Intent(this, MainActivity::class.java)
        perenosToMain.putExtra("user_login", binding2.mailReg.text.toString())
        startActivity(perenosToMain)
        finish()
    }

    fun onClickGoAutor(view: View){
        val perenos2 = Intent(this, Autorization::class.java)
        startActivity(perenos2)
        finish()
    }
}