package com.arekok.ink.apteka

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.arekok.ink.apteka.databinding.ActivityOrderBinding
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main.*

class Order : AppCompatActivity() {
    private lateinit var database: DatabaseReference

    lateinit var binding: ActivityOrderBinding
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val userName = intent.getStringExtra("user_login").toString()
        binding.data.visibility= View.GONE
        database = FirebaseDatabase.getInstance().getReference("users")
        database.child(userName).get().addOnSuccessListener {

            val name = it.child("name").value
            val surname = it.child("surname").value
            val phone = it.child("phone").value
            val location = it.child("location").value
            val mail = it.child("mail").value

            if ((name != null)||(surname != null)||(phone != null)||(location != null)||(mail != null)){
                binding.data.visibility= View.VISIBLE
            }

            if (name != null) {
                binding.name11.text = name.toString()
            } else {
                binding.name1.visibility = View.GONE
                binding.name11.visibility = View.GONE
            }
            if (surname != null) {
                binding.surname11.text = surname.toString()
            }else {
                binding.surname1.visibility = View.GONE
                binding.surname11.visibility = View.GONE
            }
            if (phone != null) {
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

                binding.numb11.text = lastPhone.toString()
            }else {
                binding.numb1.visibility = View.GONE
                binding.numb11.visibility = View.GONE
            }
            if (location != null) {
                if (location.toString().length > 26){
                    var newLoc = location.toString().substring(0,24)+"..."
                    binding.loc11.text = newLoc
                }
                else binding.loc11.text = location.toString()
            }else {
                binding.loc1.visibility = View.GONE
                binding.loc11.visibility = View.GONE
            }
            if (mail != null) {
                if (mail.toString().length > 26){
                    var newMail = mail.toString().substring(0,24)+"..."
                    binding.loc11.text = newMail
                }
                else binding.mail11.text = mail.toString()
            }else {
                binding.mail1.visibility = View.GONE
                binding.mail11.visibility = View.GONE
            }

            binding.accept.setOnClickListener {
                if ((name != null)&&(location != null)&&(mail != null)){
                    /////////////////////////////////////////////////////////////////////////////////////////
                    Toast.makeText(this, "Заказ успешно одобрен!", Toast.LENGTH_SHORT).show()

                    val perenos = Intent(this, MainActivity::class.java)
                    perenos.putExtra("user_login", userName)
                    startActivity(perenos)
                    finish()
                } else{
                    binding.errorTxt.visibility = View.VISIBLE
                }
            }

        }
        //for prices

        var priceList = mutableListOf<String>()

        database = FirebaseDatabase.getInstance().getReference("medicine")
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    for (basketSnapshot in snapshot.children){
                        var price = basketSnapshot.child("price").value.toString()

                        priceList.add(price)
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@Order,
                    error.message, Toast.LENGTH_SHORT).show()
            }
        })

        // sending

        var kolvoList = mutableListOf<Int>()

        database = FirebaseDatabase.getInstance().getReference("users")
        database.child(userName).child("basket")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    var ii = 0
                    if (snapshot.exists()){

                        for (basketSnapshot in snapshot.children){
                            var kolvo = basketSnapshot.value.toString().toInt()
                            kolvoList.add(kolvo)
                            ii++
                        }
                    }

                    var totalPrice = 0.0

                    for (i in 0..ii-1) {
                        var ans = priceList[i].filter(Char::isDigit)
                        val done = ans.addCharAtIndex('.', ans.length - 2).toDouble()
                        totalPrice = (totalPrice + (kolvoList[i] * done))
                    }
                    binding.allPrice.text = "%.2f".format(totalPrice).toString()+ "р."
                }
                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@Order, error.message, Toast.LENGTH_SHORT).show()
                }
            })

        /////////////////////////////////////////////////////////////////////
    }

    fun String.addCharAtIndex(char: Char, index: Int) =
        StringBuilder(this).apply { insert(index, char) }.toString()

    fun onClickBack(view: View){
        finish()
    }
}