package com.arekok.ink.apteka.drawerbar

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.arekok.ink.apteka.databinding.ActivityGameBinding


class Game : AppCompatActivity() {
    var counter = 90
    var strenght = 1
    var strenghtDio = 0
    var strenghtDiavolo = 0
    var valueStrenghtDio: Int = 100
    var valueStrenghtDiavolo: Int = 1000

    lateinit var binding3: ActivityGameBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding3 = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding3.root)
        binding3.Counter.text = counter.toString()
        binding3.str.text = "Сила нажатия - " + strenght.toString()
        binding3.imageView2.setOnClickListener {
            counter = counter + strenght
            binding3.Counter.text = counter.toString()
        }
        binding3.conoDioDa.setOnClickListener {
            if (counter > valueStrenghtDio) {
                strenght++
                strenghtDio++
                binding3.str.text = "Сила нажатия - " + strenght.toString()
                counter -= valueStrenghtDio
                binding3.Counter.text = counter.toString()
                valueStrenghtDio = (valueStrenghtDio * 1.2).toInt()
                binding3.dio.text = "Помощь Дио(+1) - " + valueStrenghtDio.toString()
            }
        }
        binding3.conoDiavoloDa.setOnClickListener {
                if (counter > valueStrenghtDiavolo) {
                    strenght+=10
                    strenghtDiavolo++
                    binding3.str.text = "Сила нажатия - " + strenght.toString()
                    counter -= valueStrenghtDiavolo
                    binding3.Counter.text = counter.toString()
                    valueStrenghtDiavolo = (valueStrenghtDiavolo * 1.2).toInt()
                    binding3.diavolo.text = "Помощь Диаволо(+10) - " + valueStrenghtDiavolo.toString()
                }
        }
    }
}
