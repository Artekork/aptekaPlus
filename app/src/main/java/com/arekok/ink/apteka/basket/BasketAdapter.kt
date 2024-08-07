package com.arekok.ink.apteka.basket

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.arekok.ink.apteka.R
import com.arekok.ink.apteka.databinding.BasketItemBinding
import com.arekok.ink.apteka.drawerbar.MyProfile
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class BasketAdapter(
    var c: Context, var basketList:ArrayList<BasketData>, var nameUser: String
): RecyclerView.Adapter<BasketAdapter.BasketViewHolder>()
{
    inner class BasketViewHolder(var v: BasketItemBinding): RecyclerView.ViewHolder(v.root){}
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BasketViewHolder {
        val inflter = LayoutInflater.from(parent.context)
        val v = DataBindingUtil.inflate<BasketItemBinding>(
            inflter, R.layout.basket_item,parent,
            false)
        return BasketViewHolder(v)
    }

    private lateinit var database: DatabaseReference


    override fun onBindViewHolder(holder: BasketViewHolder, position: Int) {
        val newList = basketList[position]
        holder.v.isBasket = basketList[position]

        val nameM = newList.nameM
        val type = newList.type
        val price = newList.price


        database = FirebaseDatabase.getInstance().getReference("users")
        database.child(nameUser).get().addOnSuccessListener {
            val bs = it.child("basket").child(nameM.toString()).value

            if ((bs == "0") || (bs == null) || (bs == "null")) {
                holder.v.Basket.setImageResource(R.drawable.nobasket)
            } else {
                holder.v.Basket.setImageResource(R.drawable.onbasket)
            }
        }

        database = FirebaseDatabase.getInstance().getReference("users")
        holder.v.Basket.setOnClickListener {
            database = FirebaseDatabase.getInstance().getReference("users")
            database.child(nameUser).get().addOnSuccessListener {
                val bs = it.child("basket").child(nameM.toString()).value
                if ((bs == "0")||(bs == null)||(bs == "null")){
                    holder.v.Basket.setImageResource(R.drawable.onbasket)
                    database.child(nameUser).child("basket").child(nameM.toString()).setValue("1")
                } else{
                    holder.v.Basket.setImageResource(R.drawable.nobasket)
                    database.child(nameUser).child("basket").child(nameM.toString()).setValue("0")
                }
            }
        }
        var counter = 1
        holder.v.plus.setOnClickListener {
            database = FirebaseDatabase.getInstance().getReference("users")
            database.child(nameUser).get().addOnSuccessListener {
                counter+=1
                database.child(nameUser).child("basket").child(nameM.toString()).setValue(counter)
                holder.v.kolvo.text = counter.toString()
            }
        }
        holder.v.minus.setOnClickListener {
            database = FirebaseDatabase.getInstance().getReference("users")
            database.child(nameUser).get().addOnSuccessListener {
                if (counter != 0){
                    counter-=1
                    database.child(nameUser).child("basket").child(nameM.toString()).setValue(counter)
                    holder.v.kolvo.text = counter.toString()
                }
            }
        }

        database = FirebaseDatabase.getInstance().getReference("users")
        database.child(nameUser).get().addOnSuccessListener {

            database.child(nameUser).child("basket").child(nameM.toString()).setValue(counter)

        }


    }

    override fun getItemCount(): Int {
        return  basketList.size
    }
}