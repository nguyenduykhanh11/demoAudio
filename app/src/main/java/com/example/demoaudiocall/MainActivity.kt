package com.example.demoaudiocall

import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
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
        mediaPlayer = MediaPlayer.create(this, R.raw.demo)

//        demoMedia()
        demoCall()

    }

    private fun demoMedia() {
        audioManager.mode = AudioManager.MODE_IN_COMMUNICATION
        audioManager.isSpeakerphoneOn = false

        binding.btbSwitchMedia.setOnClickListener {
            if (!speakerphone) {
                speakerphone = true
                binding.btbSwitchMedia.text = getString(R.string.speakerphone)
                audioManager.mode = AudioManager.MODE_NORMAL
                audioManager.isSpeakerphoneOn = true
            } else {
                speakerphone = false
                audioManager.mode = AudioManager.MODE_IN_COMMUNICATION
                audioManager.setStreamVolume(AudioManager.STREAM_SYSTEM, 5, 0)
                audioManager.isSpeakerphoneOn = false
                binding.btbSwitchMedia.text = getString(R.string.internalSpeaker)
            }
            if (mediaPlayer.isPlaying) {
                mediaPlayer.pause()
                mediaPlayer.start()
            }
        }

        binding.btnPlayMedia.setOnClickListener {
            if (!mediaPlayer.isPlaying) {
                mediaPlayer.start()
            }
        }
        binding.btnPauseMedia.setOnClickListener {
            mediaPlayer.pause()
        }
    }

    private fun demoCall() {
        audioManager.mode = AudioManager.MODE_IN_CALL
        audioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL,
            audioManager.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL),
            0)
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_VOICE_CALL)
        mediaPlayer.isLooping = true
        mediaPlayer.start()

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