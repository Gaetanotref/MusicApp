package com.apptest.musicapp

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import com.apptest.musicapp.databinding.ActivityMainBinding
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var finalTime: Double = 00.00
    private var startTime: Double = 00.00
    private var oneTimeOnly = 0
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var handler: Handler
    private var forwardTime = 10000
    private var backwardTime = 10000


    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        handler = Looper.myLooper()?.let { Handler(it) }!!

        //Media Player
        mediaPlayer = MediaPlayer.create(
            this,
            R.raw.astronaut
        )
        binding.seekBar.isClickable = false

        binding.playBtn.setOnClickListener {
            mediaPlayer.start()
            finalTime = mediaPlayer.duration.toDouble()
            startTime = mediaPlayer.currentPosition.toDouble()

            if (oneTimeOnly == 0) {
                binding.seekBar.max = finalTime.toInt()
                oneTimeOnly = 1
            }

            binding.timeLeftText.text = startTime.toString()
            binding.seekBar.setProgress(startTime.toInt())

            handler.postDelayed(UpdateSongTime, 100)

        }

        //Settings the music title
        binding.songTitle.text = resources.getResourceEntryName(R.raw.astronaut)

        //Stop Button
        binding.pauseBtn.setOnClickListener {
            mediaPlayer.pause()
        }

        //Forward Button
        binding.forwardBtn.setOnClickListener {
            val temp = startTime
            if ((temp + forwardTime) <= finalTime) {
                startTime += forwardTime
                mediaPlayer.seekTo(startTime.toInt())
            } else {
                Toast.makeText(this, "Can't jump forward", Toast.LENGTH_SHORT).show()
            }
        }

        binding.backBtn.setOnClickListener {
            var temp = startTime
            if ((temp - backwardTime) > 0) {
                startTime = -backwardTime.toDouble()
                mediaPlayer.seekTo(startTime.toInt())
            } else {
                Toast.makeText(this, "Can't Jump backwards", Toast.LENGTH_SHORT).show()
            }
        }

    }

    //Creating the Runnable
    val UpdateSongTime: Runnable = object : Runnable {
        override fun run() {
            startTime = mediaPlayer.currentPosition.toDouble()
            binding.timeLeftText.text = buildString {
                append("")
                append(
                    String.format(
                        "%d min , %d sec",
                        TimeUnit.MILLISECONDS.toMinutes(startTime.toLong()),
                        TimeUnit.MILLISECONDS.toSeconds(
                            startTime.toLong()
                                    - TimeUnit.MINUTES.toSeconds(
                                TimeUnit.MILLISECONDS.toMinutes(startTime.toLong())
                            )
                        )
                    )
                )
            }
            binding.seekBar.progress = startTime.toInt()
            handler.postDelayed(this, 100)
        }

    }

}