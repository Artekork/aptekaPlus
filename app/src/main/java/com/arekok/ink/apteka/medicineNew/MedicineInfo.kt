package com.arekok.ink.apteka.medicineNew

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.arekok.ink.apteka.R
import kotlinx.android.synthetic.main.activity_medicine_info.*
class MedicineInfo : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_medicine_info)

        val animalIntent = intent
        val medicineNameM = animalIntent.getStringExtra("nameM")
        val medicineImg = animalIntent.getStringExtra("img")
        val medicineHow = animalIntent.getStringExtra("how")
        val medicineStructure = animalIntent.getStringExtra("structure")
        val medicineSrok = animalIntent.getStringExtra("srok")
        medName.text = medicineNameM
        medHow.text = medicineHow
        medStructure.text = medicineStructure
        medSrok.text = medicineSrok
        medImg.loadImage(medicineImg, getProgessDrawable(this))
    }
    fun onClickBack(view: View){
        finish()
    }
}
























































