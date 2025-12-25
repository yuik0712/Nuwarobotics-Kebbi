# `NuwaSDKExample`
The SDK Example to control robot product of nuwarobotics.

NuwaSDK and NuwaSDKExample allow use on every language\market.

# `Develop environment of sample code`
* Android Studio : android-studio-ide-201.6858069
* Android Minimum SDK : API 28 - Android 9.0(Pie)
* Language : Java
* Project setting : check Use legacy android.support library

# `Nuwa SDK`
Newest Nuwa SDK：2.1.0.08

# `Support Robot Product`
Robot Generation 1
* Kebbi(凱比) : Taiwan
* Danny(小丹) : China

Robot Generation 2
* Kebbi Air : Taiwan、China、Japan

# `TTS Capability`
The TTS language capability is difference between each market robot software.

Please reference following support list.
* Taiwan Market : Locale.CHINESE\Locale.ENGLISH
* Chinese Market : Locale.CHINESE\Locale.ENGLISH
* Japan Market : Locale.JAPANESE\Locale.CHINESE\Locale.ENGLISH
* Worldwide Market : Locale.ENGLISH

# `Nuwa Website`
* NuwaRobotics Website (https://www.nuwarobotics.com/)
* NuwaRobotics Developer Website (https://dss.nuwarobotics.com/)
* Nuwa SDK JavaDoc (https://developer-docs.nuwarobotics.com/sdk/javadoc/reference/packages.html)
* Nuwa Public Motion Preview (https://developer-docs.nuwarobotics.com/sdk/kebbi_motion_preview/showPic.html)

# `Start to Use`

* Please get NuwaSDK aar from [developer website](https://dss.nuwarobotics.com/) and modify app [build.gradle](https://github.com/nuwarobotics/NuwaSDKExample/blob/master/app/build.gradle)

```
dependencies {
    //NOTICE : Please declare filetree if you create your own Android Project
    implementation fileTree(include: ['*.jar'], dir: 'libs')
    //TODO : Please download newest NuwaSDK from Nuwa Developer Website https://dss.nuwarobotics.com/
    //Step 1 : Copy aar to project lib folder : NuwaSDKExample\app\libs
    //Step 2 : Replace below NuwaSDK file name
    implementation(name: 'NuwaSDK-2021-07-08_1058_2.1.0.08_e21fe7', ext: 'aar')
    //Please also include relative aar
    implementation "com.google.code.gson:gson:2.3.1"
}
repositories {
    flatDir {
        dirs 'libs'
    }
}
```

# `NuwaSDK example`
* Robot Motion Control
    - Query Motion List Example [Code link](https://github.com/nuwarobotics/NuwaSDKExample/blob/master/app/src/main/java/com/nuwarobotics/example/motion/demo/QueryMotionActivity.java)
    - Motion Play Example [Code link](https://github.com/nuwarobotics/NuwaSDKExample/blob/master/app/src/main/java/com/nuwarobotics/example/motion/demo/PlayMotionActivity.java)
    - Motion Play/Pause/Resume Control Example [Code link](https://github.com/nuwarobotics/NuwaSDKExample/blob/master/app/src/main/java/com/nuwarobotics/example/motion/demo/ControlMotionActivity.java)
    - Motion Play with window view control Example [Code link](https://github.com/nuwarobotics/NuwaSDKExample/blob/master/app/src/main/java/com/nuwarobotics/example/motion/demo/WindowControlWithMotionActivity.java)
* Robot Motor Control
    - Motor control Example [Code link](https://github.com/nuwarobotics/NuwaSDKExample/blob/master/app/src/main/java/com/nuwarobotics/example/motor/MotorControlActivity.java)
    - Movement control Example [Code link](https://github.com/nuwarobotics/NuwaSDKExample/blob/master/app/src/main/java/com/nuwarobotics/example/motor/MovementControlActivity.java)
* Other Sensor Example
    - LED Control Example [Code link](https://github.com/nuwarobotics/NuwaSDKExample/blob/master/app/src/main/java/com/nuwarobotics/example/led/LEDExampleActivity.java)
    - Sensor Detect Example [Code link](https://github.com/nuwarobotics/NuwaSDKExample/blob/master/app/src/main/java/com/nuwarobotics/example/sensor/SensorExampleActivity.java)
* ASR/TTS
    - Wakeup Example [Code link](https://github.com/nuwarobotics/NuwaSDKExample/blob/master/app/src/main/java/com/nuwarobotics/example/voice/WakeupActivity.java)
    - Local command Example [Code link](https://github.com/nuwarobotics/NuwaSDKExample/blob/master/app/src/main/java/com/nuwarobotics/example/voice/LocalcmdActivity.java)
    - Cloud ASR Example [Code link](https://github.com/nuwarobotics/NuwaSDKExample/blob/master/app/src/main/java/com/nuwarobotics/example/voice/CloudASRActivity.java)
    - Local command and Cloud ASR Example [Code link](https://github.com/nuwarobotics/NuwaSDKExample/blob/master/app/src/main/java/com/nuwarobotics/example/voice/LocalcmdAndCloudASRActivity.java)
    - TTS Example [Code link](https://github.com/nuwarobotics/NuwaSDKExample/blob/master/app/src/main/java/com/nuwarobotics/example/voice/TTSActivity.java)
* Face Control [Code link](https://github.com/nuwarobotics/NuwaSDKExample/blob/master/app/src/main/java/com/nuwarobotics/example/activity/FaceControlExampleActivity.java)
    - Face Show/Hide Example
    - Face touch event callback example
* System Control
    - disablePowerKey [Code link](https://github.com/nuwarobotics/NuwaSDKExample/blob/master/app/src/main/java/com/nuwarobotics/example/activity/DisablePowerkeyExampleActivity.java)
* Advanced
    - Motion with TTS Example [Code link](https://github.com/nuwarobotics/NuwaSDKExample/blob/master/app/src/main/java/com/nuwarobotics/example/motion/MotionTtsExampleActivity.java)
    - Launch Nuwa add family member [Code link](https://github.com/nuwarobotics/NuwaSDKExample/blob/master/app/src/main/java/com/nuwarobotics/example/activity/startNuwaFaceRecognitionActivity.java)
    - Launch Nuwa Video call, and call specific member [Code link](https://github.com/nuwarobotics/NuwaSDKExample/blob/master/app/src/main/java/com/nuwarobotics/example/activity/VideoCall.java)


# `Q & A`
Q : How to solve 「Unable to find method 'org.gradle.api.artifacts.result.ComponentSelectionReason.getDescription()Ljava/lang/String;'」problem ?

A : Please try to modify following configuration
* NuwaSDKExample/gradle/wrapper/gradle-wrapper.properties
    - distributionUrl=https\://services.gradle.org/distributions/gradle-6.8.3-bin.zip
* NuwaSDKExample/build.gradle
    - classpath 'com.android.tools.build:gradle:4.0.1'

# `NuwaSDK Implementation Patterns`

Based on the official NuwaSDKExample project, here are the key patterns for proper SDK implementation:

## Initialization Pattern

The proper way to initialize the Nuwa Robot SDK:

```java
public class MyActivity extends AppCompatActivity {
    private NuwaRobotAPI mRobotAPI;
    private IClientId mClientId;
    private boolean mSDKinit = false;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Step 1: Initial Nuwa API Object
        mClientId = new IClientId(this.getPackageName());
        mRobotAPI = new NuwaRobotAPI(this, mClientId);
        
        // Step 2: Register receive Robot Event
        mRobotAPI.registerRobotEventListener(robotEventListener);
    }
    
    RobotEventListener robotEventListener = new RobotEventListener() {
        @Override
        public void onWikiServiceStart() {
            // Nuwa Robot SDK is ready now
            Log.d(TAG, "onWikiServiceStart, robot ready to be control");
            mSDKinit = true;
            
            // Enable UI controls here
            runOnUiThread(() -> {
                // Enable buttons, etc.
            });
        }
        
        // Implement other required methods...
    };
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Release Nuwa Robot SDK resource
        mRobotAPI.release();
    }
}
```

## Network Permissions

Essential permissions needed in AndroidManifest.xml:

```xml
<!-- Network access for cloud features and WebSocket -->
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
<uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />

<!-- Audio recording for voice features -->
<uses-permission android:name="android.permission.RECORD_AUDIO" />
<uses-permission android:name="android.permission.MODIFY_AUDIO_SETTINGS" />

<!-- Storage access -->
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
```

## TTS (Text-to-Speech) Example

```java
// Start TTS
mRobotAPI.startTTS("Hello, I am Kebbi robot!");

// TTS with specific language
mRobotAPI.startTTS("こんにちは", Locale.JAPANESE.toString());
mRobotAPI.startTTS("Hello", Locale.ENGLISH.toString());
mRobotAPI.startTTS("你好", Locale.CHINESE.toString());

// Register voice event listener for TTS completion
mRobotAPI.registerVoiceEventListener(new VoiceEventListener() {
    @Override
    public void onTTSComplete(boolean isError) {
        Log.d(TAG, "TTS completed, success: " + !isError);
    }
    // Other methods...
});
```

## Motion Control Example

```java
// Play a motion
if (mRobotAPI != null && mRobotAPI.isKiWiServiceReady()) {
    mRobotAPI.motionPlay("666_LE_HAPPY", true);
}

// Stop motion
mRobotAPI.motionStop(true);

// Motion event callbacks
@Override
public void onStartOfMotionPlay(String motion) {
    Log.d(TAG, "Motion started: " + motion);
}

@Override
public void onCompleteOfMotionPlay(String motion) {
    Log.d(TAG, "Motion completed: " + motion);
}
```

## Voice Recognition (ASR) Example

```java
// Set language for recognition
mRobotAPI.setListenParameter(VoiceEventListener.ListenType.RECOGNIZE, "language", "en-US");

// Start listening without wakeup
mRobotAPI.startSpeech2Text(false);

// Stop listening
mRobotAPI.stopListen();
```

## LED Control Pattern

```java
// Disable system LED for app control
mRobotAPI.disableSystemLED();

// Enable system LED when done
mRobotAPI.enableSystemLED();
```

## Common Issues and Solutions

### WebSocket Connection Issues
1. **Stuck at "Connecting"**: 
   - Check API key validity
   - Verify network permissions
   - Add connection timeout
   - Implement retry logic

2. **Authentication Errors**:
   - Ensure API key is properly configured
   - Check for proper header formatting
   - Validate API key has required permissions

3. **Threading Issues**:
   - Use `runOnUiThread()` for UI updates
   - Run network operations on background threads
   - Handle WebSocket callbacks properly

### SDK Initialization Issues
1. **SDK not ready**: Always check `mSDKinit` or wait for `onWikiServiceStart()`
2. **Service crashes**: Implement `onWikiServiceCrash()` and recovery logic
3. **Permission issues**: Request permissions at runtime for Android 6.0+

## Best Practices

1. **Always wait for SDK initialization** before calling any robot APIs
2. **Release resources properly** in `onDestroy()`
3. **Handle service lifecycle** events (start, stop, crash, recovery)
4. **Use proper threading** for network and UI operations
5. **Validate robot readiness** with `isKiWiServiceReady()` before commands
6. **Log errors comprehensively** for debugging
