package com.example.demoaudiocall

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.demoaudiocall.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var audioManager: AudioManager
    private lateinit var binding: ActivityMainBinding
    private var speakerphone = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        initMediaPlay()
    }

    private fun initMediaPlay() {
        audioManager.isSpeakerphoneOn = false
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val audioAttributes = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_VOICE_COMMUNICATION)
                .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                .build()
            mediaPlayer = MediaPlayer()
            mediaPlayer.setDataSource(applicationContext, Uri.parse("android.resource://" + packageName + "/" + R.raw.demo))
            mediaPlayer.setAudioAttributes(audioAttributes)
            mediaPlayer.prepare()

            audioManager.mode = AudioManager.MODE_IN_COMMUNICATION
            audioManager.setStreamVolume(
                AudioManager.STREAM_VOICE_CALL,
                audioManager.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL),
                0
            )
        } else {
            val mediaPlayer = MediaPlayer.create(this, R.raw.demo)
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_VOICE_CALL)

            audioManager.mode = AudioManager.MODE_IN_CALL
            audioManager.setStreamVolume(
                AudioManager.STREAM_VOICE_CALL,
                audioManager.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL),
                0
            )
        }

        binding.btbSwitchCall.setOnClickListener {
            if (!speakerphone) {
                speakerphone = true
                binding.btbSwitchCall.text = getString(R.string.speakerphone)
                audioManager.isSpeakerphoneOn = true
            } else {
                speakerphone = false
                audioManager.isSpeakerphoneOn = false
                binding.btbSwitchCall.text = getString(R.string.internalSpeaker)
            }
            if (mediaPlayer.isPlaying) {
                mediaPlayer.pause()
                mediaPlayer.start()
            }
        }

        binding.btnPlayCall.setOnClickListener {
            if (!mediaPlayer.isPlaying) {
                mediaPlayer.start()
            }
        }
        binding.btnPauseCall.setOnClickListener {
            mediaPlayer.pause()
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }

}