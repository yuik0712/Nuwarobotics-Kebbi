# HelloKebbi Deployment Instructions

## APK Location
The debug APK has been successfully built and is located at:
```
app/build/outputs/apk/debug/app-debug.apk
```

Size: 9.2MB

## ADB Setup

### Add ADB to PATH (macOS)
Add this line to your ~/.zshrc or ~/.bash_profile:
```bash
export PATH="$PATH:$HOME/Library/Android/sdk/platform-tools"
```

Then reload your shell configuration:
```bash
source ~/.zshrc  # or source ~/.bash_profile
```

### Verify ADB Installation
```bash
adb version
```

## Deployment Steps

### 1. Connect Kebbi Robot
- Connect the Kebbi robot to your computer via USB cable
- The robot should be powered on

### 2. Check Device Connection
```bash
adb devices
```
You should see your Kebbi robot listed.

### 3. Install the APK
```bash
# Using full path to ADB (if not in PATH)
~/Library/Android/sdk/platform-tools/adb install app/build/outputs/apk/debug/app-debug.apk

# Or if ADB is in PATH
adb install app/build/outputs/apk/debug/app-debug.apk
```

Note: You may need to enter the ADB password when prompted (as mentioned in TODO.md).

### 4. Launch the Application
The app should appear in the robot's application list as "HelloKebbi".

### 5. Monitor Logs (Optional)
To see debug output and SDK initialization status:
```bash
adb logcat | grep "HelloKebbi"
```

## Troubleshooting

### Device Not Found
- Ensure USB debugging is enabled on the robot
- Try different USB cables or ports
- Check if drivers are properly installed

### Installation Failed
- Uninstall previous versions: `adb uninstall com.example.hellokebbi`
- Check available storage on the robot
- Ensure the robot's Android version is compatible (minSdk 28)

### SDK Initialization Issues
- The app will show a Toast message indicating if SDK initialization succeeded or failed
- Check logs for detailed error messages
- Ensure the robot has the required Nuwa services installed

## Testing the SDK

Once installed, the app will:
1. Initialize the Nuwa SDK on startup
2. Show a toast message indicating success or failure
3. Manage LED states during app lifecycle (disable on start, enable on pause)

For full SDK functionality testing, you'll need to implement additional features as described in the Nuwa SDK documentation.