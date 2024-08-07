package com.arekok.ink.apteka.pharmacyNew

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.arekok.ink.apteka.R
import com.arekok.ink.apteka.databinding.PharmacyItemBinding
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

class PharmacyAdapter(
    var c: Context, var pharmacyList:ArrayList<PharmacyData>,
    var dataSender: DataUpdater
): RecyclerView.Adapter<PharmacyAdapter.PharmacyViewHolder>()
{
    inner class PharmacyViewHolder(var v: PharmacyItemBinding): RecyclerView.ViewHolder(v.root){}
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PharmacyViewHolder {
        val inflter = LayoutInflater.from(parent.context)
        val v = DataBindingUtil.inflate<PharmacyItemBinding>(
            inflter, R.layout.pharmacy_item,parent,
            false)
        return PharmacyViewHolder(v)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: PharmacyAdapter.PharmacyViewHolder, position: Int) {
        val newList = pharmacyList[position]
        holder.v.isPharmacy = pharmacyList[position]

        ///   Check time ///
        val end = newList.end
        val start = newList.start
        val currentDateTime = LocalDateTime.now()
        val data = currentDateTime.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))
        if (data < end.toString() && data > start.toString()){
            holder.v.imageStatus.setImageResource(R.drawable.open)
        } else {
            holder.v.imageStatus.setImageResource(R.drawable.close)
        }
        ////////////////////


        holder.v.root.setOnClickListener {
            val nameP = newList.nameP
            val work = newList.work
            val end = newList.end
            val start = newList.start
            val loc1 = newList.loc1
            val loc2 = newList.loc2
            val location = newList.location

            dataSender.addMarkFun(loc1!!,loc2!!, nameP!!, work!!)


//            val mIntent = Intent(c, MainActivity::class.java)
//            mIntent.putExtra("loc1",loc1)
//            mIntent.putExtra("loc2",loc2)
//            c.startActivity(mIntent)
        }

    }
    override fun getItemCount(): Int {
        return  pharmacyList.size
    }
}