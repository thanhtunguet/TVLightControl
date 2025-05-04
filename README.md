# TV Light Control

A simple Android TV application that lets you control smart lights using D-pad buttons on your remote.

## Features

- Controls smart lights through Google Assistant voice commands
- Automatically starts on device boot
- Simple and intuitive interface with visible status updates
- Optimized for Android TV with D-pad navigation

## How to Use

### Basic Controls

- **Left D-pad**
  - **Single press**: Turn ON bedroom light
  - **Double press**: Turn OFF bedroom light

- **Right D-pad**
  - **Single press**: Turn ON bathroom light
  - **Double press**: Turn OFF bathroom light

### Requirements

- Android TV device
- Google Assistant enabled on your device
- Smart lights configured with Google Assistant
- Smart lights named "bedroom light" and "bathroom light" in your Google Home

## Installation

1. Enable Developer options on your Android TV
2. Enable USB debugging
3. Install the APK via ADB: `adb install TVLightControl.apk`
4. Launch the app from your Apps list

## Technical Details

The app uses several approaches to communicate with Google Assistant:
- Primary: Uses `ACTION_VOICE_COMMAND` intent to trigger voice commands
- Fallback: Uses direct Assistant queries if the primary method fails

The app requires the following permissions:
- `INTERNET`: For connectivity
- `ASSIST`: To communicate with Google Assistant
- `RECEIVE_BOOT_COMPLETED`: To auto-start on device boot

## Development

Built with standard Android development tools:
- Kotlin
- Android SDK for TV
- Leanback libraries

## Troubleshooting

- If commands don't work, ensure Google Assistant is properly set up on your device
- Make sure your smart lights are properly named and configured in Google Home
- Check that the appropriate voice commands work directly with Google Assistant

## License

This project is open-source, feel free to modify and distribute as needed.
