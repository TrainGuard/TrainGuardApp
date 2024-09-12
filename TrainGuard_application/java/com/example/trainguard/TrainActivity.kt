package com.example.trainguard

import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.trainguard.databinding.TrainActivityBinding
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class TrainActivity : AppCompatActivity() {
    lateinit var trainBinding: TrainActivityBinding
    private var mMediaPlayer: MediaPlayer? = null

    fun showTextRange() {
        var strtext = "Расстояние до ближайшего поезда: ${Client.getRangeFromServer()}"

        runOnUiThread { trainBinding.textView2.setText(strtext) }
    }

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        trainBinding = TrainActivityBinding.inflate(layoutInflater)
        setContentView(trainBinding.root)

        val audioManager = applicationContext.getSystemService(AUDIO_SERVICE) as AudioManager

        mMediaPlayer = MediaPlayer().apply {
            setDataSource("https://track.pinkamuz.pro/download/d33534b7b0b0b43037348d3731353332b63436360000/448f52fc2a407f16c67892d99657e62c/%D0%9F%D0%BE%D0%B5%D0%B7%D0%B4%20%D0%BF%D1%80%D0%B8%D0%B1%D0%BB%D0%B8%D0%B6%D0%B0%D0%B5%D1%82%D1%81%D1%8F%20%D0%B8%D0%B7%D0%B4%D0%B0%D0%BB%D0%B5%D0%BA%D0%B0%20%D0%B8%20%D1%81%D0%B8%D0%B3%D0%BD%D0%B0%D0%BB%D0%B8%D1%82%20-%20%D0%9F%D0%BE%D0%B5%D0%B7%D0%B4%20%D0%BF%D1%80%D0%B8%D0%B1%D0%BB%D0%B8%D0%B6%D0%B0%D0%B5%D1%82%D1%81%D1%8F%20%D0%B8%D0%B7%D0%B4%D0%B0%D0%BB%D0%B5%D0%BA%D0%B0%20%D0%B8%20%D1%81%D0%B8%D0%B3%D0%BD%D0%B0%D0%BB%D0%B8%D1%82.mp3")
            setAudioAttributes(
                AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build()
            )
        }

        mMediaPlayer!!.prepare()

        GlobalScope.launch {
            Looper.prepare()
            while (true) {
                showTextRange()
                if (Client.getRangeFromServer() <= 10000) {
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 100, AudioManager.FLAG_SHOW_UI)
                    for (i in 1..3) {
                        mMediaPlayer!!.start()
                        delay(8_000)
                    }
                }
                delay(3000)
            }
        }
    }
}
