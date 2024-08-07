package com.arekok.ink.apteka.medicineNew

import android.content.Context
import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.arekok.ink.apteka.R
import com.arekok.ink.apteka.databinding.MedicineItemBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

////////////////////////
class MedicineAdapter(
    var c: Context, var medicineList:ArrayList<MedicineData>, var nameUser: String
): RecyclerView.Adapter<MedicineAdapter.MedicineViewHolder>()
{
    inner class MedicineViewHolder(var v:MedicineItemBinding): RecyclerView.ViewHolder(v.root){}
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MedicineViewHolder {
        val inflter = LayoutInflater.from(parent.context)
        val v = DataBindingUtil.inflate<MedicineItemBinding>(
            inflter, R.layout.medicine_item,parent,
            false)
        return MedicineViewHolder(v)
    }

    private lateinit var database: DatabaseReference


    override fun onBindViewHolder(holder: MedicineViewHolder, position: Int) {

        val newList = medicineList[position]
        holder.v.isMedicine = medicineList[position]

        val nameM = newList.nameM
        val type = newList.type
        val price = newList.price
        val img  = newList.img
        val srok = newList.srok
        val structure = newList.structure
        val how  = newList.how

        database = FirebaseDatabase.getInstance().getReference("users")
        database.child(nameUser).get().addOnSuccessListener {
            val bs = it.child("favourite").child(nameM.toString()).value

            if ((bs == "0") || (bs == null) || (bs == "null")) {
                holder.v.Favourite.setImageResource(R.drawable.star_notselected)
            } else {
                holder.v.Favourite.setImageResource(R.drawable.star_selected)
            }
        }
        database.child(nameUser).get().addOnSuccessListener {
            val bs = it.child("basket").child(nameM.toString()).value

            if ((bs == "0") || (bs == null) || (bs == "null")) {
                holder.v.Basket.setImageResource(R.drawable.nobasket)
            } else {
                holder.v.Basket.setImageResource(R.drawable.onbasket)
            }
        }

        holder.v.root.setOnClickListener {
            val mIntent = Intent(c, MedicineInfo::class.java)
            mIntent.putExtra("img",img)
            mIntent.putExtra("nameM",nameM)
            mIntent.putExtra("srok",srok)
            mIntent.putExtra("how",how)
            mIntent.putExtra("structure",structure)
            c.startActivity(mIntent)
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

        holder.v.Favourite.setOnClickListener {
            database = FirebaseDatabase.getInstance().getReference("users")
            database.child(nameUser).get().addOnSuccessListener {
                val bs = it.child("favourite").child(nameM.toString()).value

                if ((bs == "0")||(bs == null)||(bs == "null")){
                    holder.v.Favourite.setImageResource(R.drawable.star_selected)
                    database.child(nameUser).child("favourite").child(nameM.toString()).setValue("1")
                } else{
                    holder.v.Favourite.setImageResource(R.drawable.star_notselected)
                    database.child(nameUser).child("favourite").child(nameM.toString()).setValue("0")
                }
            }
        }

    }
    override fun getItemCount(): Int {
        return  medicineList.size
    }
}