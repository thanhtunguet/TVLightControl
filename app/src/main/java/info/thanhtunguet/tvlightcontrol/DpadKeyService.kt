package info.thanhtunguet.tvlightcontrol

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.os.Handler
import android.os.Looper
import android.view.KeyEvent
import android.view.accessibility.AccessibilityEvent
import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo

class DpadKeyService : AccessibilityService() {
    private val handler = Handler(Looper.getMainLooper())
    private var bedroomLightState = false
    private var bathroomLightState = false

    override fun onServiceConnected() {
        super.onServiceConnected()
        val info = AccessibilityServiceInfo()
        info.eventTypes =
            AccessibilityEvent.TYPE_VIEW_FOCUSED or AccessibilityEvent.TYPE_VIEW_CLICKED
        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC
        info.flags = AccessibilityServiceInfo.FLAG_REQUEST_FILTER_KEY_EVENTS
        serviceInfo = info
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        // No-op: This service only handles key events
    }

    override fun onKeyEvent(event: KeyEvent): Boolean {
        if (event.repeatCount >= 1 && event.action == KeyEvent.ACTION_DOWN) {
            when (event.keyCode) {
                KeyEvent.KEYCODE_DPAD_LEFT -> {
                    // Long press: Turn ON bedroom light
                    if (!bedroomLightState) {
                        bedroomLightState = true
                        toggleBedroomLight(true)
                    }
                    return true
                }

                KeyEvent.KEYCODE_DPAD_RIGHT -> {
                    // Long press: Turn OFF bedroom light
                    if (bedroomLightState) {
                        bedroomLightState = false
                        toggleBedroomLight(false)
                    }
                    return true
                }

                KeyEvent.KEYCODE_DPAD_UP -> {
                    // Long press: Turn ON bathroom light
                    if (!bathroomLightState) {
                        bathroomLightState = true
                        toggleBathroomLight(true)
                    }
                    return true
                }

                KeyEvent.KEYCODE_DPAD_DOWN -> {
                    // Long press: Turn OFF bathroom light
                    if (bathroomLightState) {
                        bathroomLightState = false
                        toggleBathroomLight(false)
                    }
                    return true
                }
            }
        }
        return false
    }

    override fun onInterrupt() {}

    private fun toggleBedroomLight(state: Boolean) {
        val command = if (state) "turn on bedroom light" else "turn off bedroom light"
        sendAssistantCommand(command)
    }

    private fun toggleBathroomLight(state: Boolean) {
        val command = if (state) "turn on bathroom light" else "turn off bathroom light"
        sendAssistantCommand(command)
    }

    private fun sendAssistantCommand(command: String) {
        // You may want to use a broadcast or other IPC to communicate with MainActivity, or duplicate the logic here
        // For now, this is a placeholder for the actual implementation
    }
}
