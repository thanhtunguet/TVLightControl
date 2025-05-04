package info.thanhtunguet.tvlightcontrol

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.speech.RecognizerIntent
import android.view.KeyEvent
import android.widget.TextView
import androidx.core.content.ContextCompat

class MainActivity : Activity() {

    private lateinit var statusText: TextView
    private var bedroomLightState = false
    private var bathroomLightState = false
    private val handler = Handler(Looper.getMainLooper())
    
    // Variables for tracking double press
    private var lastKeyCode = -1
    private var lastKeyTime = 0L
    private var keyPressCount = 0
    private val doublePressThreshold = 500L // Milliseconds to detect double press

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        statusText = findViewById(R.id.statusText)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        when (keyCode) {
            KeyEvent.KEYCODE_DPAD_LEFT, KeyEvent.KEYCODE_DPAD_RIGHT -> {
                val currentTime = System.currentTimeMillis()
                
                // Check if this is the same key as last press and within threshold
                if (keyCode == lastKeyCode && (currentTime - lastKeyTime) < doublePressThreshold) {
                    keyPressCount++
                } else {
                    keyPressCount = 1
                }
                
                lastKeyCode = keyCode
                lastKeyTime = currentTime
                
                return true
            }
            else -> return super.onKeyDown(keyCode, event)
        }
    }
    
    override fun onKeyUp(keyCode: Int, event: KeyEvent): Boolean {
        when (keyCode) {
            KeyEvent.KEYCODE_DPAD_LEFT -> {
                handler.postDelayed({
                    if (keyPressCount == 1) {
                        // Single press - Turn on bedroom light
                        bedroomLightState = true
                        toggleBedroomLight(true)
                    } else if (keyPressCount > 1) {
                        // Double press - Turn off bedroom light
                        bedroomLightState = false
                        toggleBedroomLight(false)
                    }
                    keyPressCount = 0
                }, doublePressThreshold)
                return true
            }
            KeyEvent.KEYCODE_DPAD_RIGHT -> {
                handler.postDelayed({
                    if (keyPressCount == 1) {
                        // Single press - Turn on bathroom light
                        bathroomLightState = true
                        toggleBathroomLight(true)
                    } else if (keyPressCount > 1) {
                        // Double press - Turn off bathroom light
                        bathroomLightState = false
                        toggleBathroomLight(false)
                    }
                    keyPressCount = 0
                }, doublePressThreshold)
                return true
            }
            else -> return super.onKeyUp(keyCode, event)
        }
    }

    private fun toggleBedroomLight(state: Boolean) {
        val command = if (state) "turn on bedroom light" else "turn off bedroom light"
        sendAssistantCommand(command)

        val statusMessage = if (state)
            getString(R.string.bedroom_light_on)
        else
            getString(R.string.bedroom_light_off)

        updateStatus(statusMessage)
    }

    private fun toggleBathroomLight(state: Boolean) {
        val command = if (state) "turn on bathroom light" else "turn off bathroom light"
        sendAssistantCommand(command)

        val statusMessage = if (state)
            getString(R.string.bathroom_light_on)
        else
            getString(R.string.bathroom_light_off)

        updateStatus(statusMessage)
    }

    private fun sendAssistantCommand(command: String) {
        // Primary approach: Use Assistant built-in on Android TV
        try {
            // Method 1: Using ACTION_VOICE_SEARCH
            val assistantIntent = Intent(Intent.ACTION_VOICE_COMMAND)
            assistantIntent.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            assistantIntent.putExtra(RecognizerIntent.EXTRA_PROMPT, command)
            assistantIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US")
            startActivity(assistantIntent)
            return
        } catch (e: ActivityNotFoundException) {
            // Fall back to other methods
        }

        try {
            // Method 2: Direct Google Assistant launch with query
            val assistantIntent = Intent(Intent.ACTION_VOICE_COMMAND)
            assistantIntent.putExtra(RecognizerIntent.EXTRA_PROMPT, command)
            startActivity(assistantIntent)
            return
        } catch (e: ActivityNotFoundException) {
            // Fall back to other methods
        }

        try {
            // Method 3: Launch Assistant with a specific query
            val assistantIntent = Intent("android.intent.action.ASSIST")
            assistantIntent.putExtra("query", command)
            startActivity(assistantIntent)
        } catch (e: Exception) {
            updateStatus("Could not access Assistant. Please try manually.")
        }
    }

    private fun updateStatus(message: String) {
        statusText.text = message

        // Reset status message after delay
        handler.postDelayed({
            statusText.text = getString(R.string.status)
        }, 3000)
    }
}
