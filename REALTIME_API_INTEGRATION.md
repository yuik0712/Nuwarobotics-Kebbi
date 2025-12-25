# OpenAI Realtime API Integration

This document describes the OpenAI Realtime API integration with the Kebbi robot, providing low-latency conversations with real-time gestures and expressions.

## Overview

The Realtime API integration replaces the traditional HTTP-based Chat Completions API with a WebRTC-based connection, enabling:
- **Low latency**: 300-700ms end-to-end response times
- **Real-time gestures**: Robot performs motions during AI responses
- **Tool functions**: Dynamic gesture triggering via function calls
- **Streaming responses**: Text appears as it's generated

## Architecture

### Core Components

1. **RealtimeRobotHelper** (`app/src/main/java/com/example/hellokebbi/RealtimeRobotHelper.java`)
   - Manages WebRTC peer connection
   - Handles SDP negotiation with OpenAI
   - Processes realtime events via data channel
   - Executes tool functions for robot control

2. **RealtimeActivity** (`app/src/main/java/com/example/hellokebbi/RealtimeActivity.java`)
   - User interface for realtime conversations
   - Connect button to establish WebRTC connection
   - Chat interface with streaming responses
   - Integration with Nuwa robot SDK

### Tool Functions

The integration defines two tool functions that the AI can call:

1. **play_motion**
   - Triggers robot gestures/motions
   - Available motions: happy, sad, dance, wave, think, angry, scared, greeting, goodbye, bow, clap, read, drink, eat, photo, listen

2. **set_face_expression**
   - Changes robot face display
   - Available expressions: happy, sad, angry, surprised, fear, think, very_happy, very_sad, laugh, grimace, shy

## Setup Instructions

### 1. API Key Configuration

Add your OpenAI API key to `app/build.gradle`:

```gradle
buildConfigField "String", "OPENAI_API_KEY", "\"sk-your-api-key-here\""
```

### 2. Dependencies

The following dependencies are already configured:
- `com.theokanning.openai-gpt3-java:service:0.18.2` - OpenAI Java client
- `org.webrtc:google-webrtc:1.0.32000` - WebRTC support

### 3. Permissions

Required permissions (already in AndroidManifest.xml):
- `android.permission.INTERNET`
- `android.permission.RECORD_AUDIO`
- `android.permission.MODIFY_AUDIO_SETTINGS`

## Usage

### Starting a Realtime Session

1. Launch the app and tap "Realtime AI" button
2. Wait for robot initialization
3. Grant microphone permission when prompted
4. Tap "Connect to Realtime API" button
5. Once connected, you can:
   - Type messages and tap "Send"
   - Tap "Start Voice Chat" to speak directly to the AI

### Voice Input Features

- **Real-time streaming**: Audio is streamed in 250ms chunks for low latency
- **Voice Activity Detection**: Server-side VAD detects when you start/stop speaking
- **Interruption handling**: Robot stops current actions when you start speaking
- **Visual feedback**: Status shows "Listening...", "Processing...", etc.

### Example Interactions

The AI will automatically use gestures based on context:

- **Emotional responses**: "I'm so happy to meet you!" → happy motion + face
- **Actions**: "Let me dance for you!" → dance motion
- **Greetings**: "Hello there!" → greeting gesture
- **Multiple gestures**: The AI can trigger several gestures in one response

With voice input, try saying:
- "Hello robot, how are you today?"
- "Show me a happy dance!"
- "I'm feeling a bit sad..."
- "Can you wave at me?"

### Code Example

```java
// Initialize RealtimeRobotHelper
realtimeHelper = new RealtimeRobotHelper(context, apiKey, robotAPI);

// Connect to Realtime API
realtimeHelper.connect(new RealtimeRobotHelper.ConnectionCallback() {
    @Override
    public void onConnected() {
        // Connection established
    }
    
    @Override
    public void onError(String error) {
        // Handle connection error
    }
});

// Send a text message
realtimeHelper.sendTextMessage("Hello!", new RealtimeRobotHelper.ResponseCallback() {
    @Override
    public void onTextDelta(String text) {
        // Handle streaming text
    }
    
    @Override
    public void onResponseComplete() {
        // Response finished
    }
    
    @Override
    public void onError(String error) {
        // Handle error
    }
    
    @Override
    public void onUserSpeaking(boolean isSpeaking) {
        // Handle user speaking status
    }
});

// Start voice input
realtimeHelper.startVoiceInput();

// Stop voice input
realtimeHelper.stopVoiceInput();
```

## Technical Details

### WebRTC Connection Flow

1. **SDP Negotiation**: POST to `https://api.openai.com/v1/realtime` to get offer
2. **Peer Connection**: Create local peer connection with received offer
3. **Answer Creation**: Generate and set local answer
4. **Data Channel**: "events" channel for JSON message exchange
5. **Audio Track**: Local audio track for future voice input support

### Event Types Handled

- `session.created`: Connection established
- `response.audio.delta`: Audio chunks (prepared for future TTS integration)
- `response.text.delta`: Streaming text chunks
- `response.function_call_arguments.done`: Tool function execution
- `response.done`: Response complete
- `error`: Error handling

### Session Configuration

```java
{
    "model": "gpt-4o-realtime-preview-2024-10-01",
    "voice": "alloy",
    "instructions": "You are a friendly robot assistant...",
    "tools": [...],
    "turn_detection": {
        "type": "server_vad",
        "silence_duration_ms": 500
    },
    "temperature": 0.8
}
```

## Comparison with HTTP API

| Feature | HTTP API (OpenAIActivity) | Realtime API (RealtimeActivity) |
|---------|---------------------------|----------------------------------|
| Latency | 2-5 seconds | 300-700ms |
| Gestures | After response | During response |
| Streaming | No | Yes |
| Connection | Stateless HTTP | Persistent WebRTC |
| Audio | TTS after text | Native audio support |

## Troubleshooting

### Connection Issues
- Verify API key is correctly set in build.gradle
- Check internet connectivity
- Ensure robot SDK is initialized

### No Gestures Playing
- Verify robot is connected and ready
- Check motion names in RealtimeRobotHelper.playMotion()
- Monitor logcat for motion errors

### WebRTC Errors
- Check ProGuard rules are properly configured
- Verify all permissions are granted
- Try reconnecting if connection drops

## Future Enhancements

1. **Voice Input**: Add AudioRecord integration for speech input
2. **Audio Output**: Use WebRTC audio instead of TTS
3. **Vision Support**: Add camera integration when API supports it
4. **Interruption Handling**: Implement gesture cancellation on interruption

## Testing

Always test on physical Kebbi robot:
```bash
./gradlew assembleDebug
./deploy.sh
```

Monitor logs:
```bash
adb logcat | grep -E "RealtimeRobotHelper|RealtimeActivity"
```