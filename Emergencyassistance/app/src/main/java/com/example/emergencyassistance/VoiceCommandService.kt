package com.example.emergencyassistance

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.os.PowerManager
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class VoiceCommandService : Service() {

    private var speechRecognizer: SpeechRecognizer? = null
    private lateinit var recognizerIntent: Intent
    private val sosHelper by lazy { SOSHelper(applicationContext) }
    private var wakeLock: PowerManager.WakeLock? = null
    private lateinit var emergencyContactDao: EmergencyContactDao

    companion object {
        private const val CHANNEL_ID = "VoiceCommandChannel"
        private const val NOTIFICATION_ID = 1
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()

        val notification = buildNotification()
        startForeground(NOTIFICATION_ID, notification)

        val powerManager = getSystemService(Context.POWER_SERVICE) as PowerManager
        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "VoiceCommandService:WakeLock")
        wakeLock?.acquire()

        emergencyContactDao = AppDatabase.getDatabase(applicationContext).emergencyContactDao()
        setupSpeechRecognizer()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startListening()
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        stopListening()
        wakeLock?.release()
    }

    private fun setupSpeechRecognizer() {
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this)
        speechRecognizer?.setRecognitionListener(object : RecognitionListener {
            override fun onReadyForSpeech(params: Bundle?) {}
            override fun onBeginningOfSpeech() {}
            override fun onRmsChanged(rmsdB: Float) {}
            override fun onBufferReceived(buffer: ByteArray?) {}
            override fun onEndOfSpeech() {}
            override fun onError(error: Int) {
                Log.e("VoiceCommandService", "Speech recognition error: $error")
                restartListening()
            }
            override fun onResults(results: Bundle?) {
                val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                if (matches != null) {
                    val command = matches.firstOrNull()?.toLowerCase()
                    if (command?.contains("sos") == true || command?.contains("help") == true || command?.contains("emergency") == true) {
                        triggerSOS()
                    } else {
                        Toast.makeText(applicationContext, "Command not recognized", Toast.LENGTH_SHORT).show()
                    }
                }
                restartListening()
            }
            override fun onPartialResults(partialResults: Bundle?) {}
            override fun onEvent(eventType: Int, params: Bundle?) {}
        })

        recognizerIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, this@VoiceCommandService.packageName)
            putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1)
        }
    }

    private fun startListening() {
        speechRecognizer?.startListening(recognizerIntent)
    }

    private fun stopListening() {
        speechRecognizer?.destroy()
        speechRecognizer = null
    }

    private fun restartListening() {
        stopListening()
        setupSpeechRecognizer()
        startListening()
    }

    private fun triggerSOS() {
        CoroutineScope(Dispatchers.IO).launch {
            val contacts = emergencyContactDao.getAllContacts()
            contacts.forEach { contact ->
                launch(Dispatchers.IO){ //send each message in background.
                    sosHelper.triggerSOS(contact.phoneNumber)
                }
            }
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Voice Command Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun buildNotification(): Notification {
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE)

        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Voice Command Service")
            .setContentText("Listening for SOS commands...")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentIntent(pendingIntent)

        return builder.build()
    }
}