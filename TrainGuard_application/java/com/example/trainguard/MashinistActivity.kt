package com.example.trainguard

import                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                             android.os.Bundle
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.trainguard.databinding.MashinistActivityBinding
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MashinistActivity : AppCompatActivity() {
    lateinit var mashinistBinding: MashinistActivityBinding
    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mashinistBinding = MashinistActivityBinding.inflate(layoutInflater)
        setContentView(mashinistBinding.root)

        mashinistBinding.personButton.setOnClickListener {
            sendFunction()
        }
    }

    fun sendFunction() {
        mashinistBinding.personButton.visibility = View.INVISIBLE
        Client.addTrain(mashinistBinding.editTextText.text.toString())
        GlobalScope.launch {
            Looper.prepare()
            while (true) {
                Client.updateTrainInfo()
                delay(1000)
            }
        }
    }
}