# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

HelloKebbi is an Android application for controlling Kebbi robots using the Nuwa SDK. It integrates OpenAI's API to create an AI-powered robot assistant that responds with appropriate movements and expressions.

## Build Commands

```bash
# Build debug APK
./gradlew assembleDebug

# Clean build
./gradlew clean

# Deploy to robot (requires USB connection)
./deploy.sh
# Or manually:
adb install app/build/outputs/apk/debug/app-debug.apk

# Run unit tests (limited coverage)
./gradlew test

# Run instrumented tests
./gradlew connectedAndroidTest
```

## Project Architecture

### Core Components

1. **Robot Control Layer**
   - `NuwaRobotAPI` - Main SDK interface
   - `RobotEventListener` - Handles robot events
   - Motion control, TTS, LED, and sensor management

2. **UI Activities**
   - `MainActivity` - Motion grid selector with search
   - `OpenAIActivity` - AI chat interface
   - `FaceExpressionActivity` - Face expression testing

3. **Integration Layer**
   - `OpenAIRobotHelper` - Maps AI responses to robot actions
   - `FaceExpressionManager` - Coordinates face animations and motions
   - Emotion detection and automatic gesture mapping

### Key Technical Constraints

- **Java 11** target (configured via toolchain)
- **Min SDK 28**, Target SDK 33
- **Physical device required** - No emulator support due to hardware dependencies
- **Nuwa SDK** loaded as local AAR file
- **OpenAI integration** uses direct HTTP calls (OkHttp)

### Motion and Expression System

- **20+ predefined motions**: happy, dance, wave, clap, etc.
- **15+ face expressions**: happy, sad, surprised, thinking, etc.
- **Emotion mapping**: Automatic detection from AI responses
- **Expression queuing**: Sequential animation management

### Development Workflow

1. **Before modifying robot control**:
   - Check if robot is connected via ADB
   - Verify Nuwa services are running
   - Test on physical device only

2. **Adding new features**:
   - Follow existing Activity patterns
   - Use ExecutorService for network calls
   - Register robot listeners properly
   - Release resources in onDestroy()

3. **OpenAI integration**:
   - API key in `OpenAIActivity.OPENAI_API_KEY`
   - Emotion detection in `detectEmotion()`
   - Action mapping in `mapEmotionToRobotAction()`

### Common Patterns

```java
// SDK initialization pattern
IClientId id = new IClientId(getPackageName());
mRobot = new NuwaRobotAPI(this, id);
mRobot.registerRobotEventListener(listener);

// Async robot control
if (mRobot.isKiWiServiceReady()) {
    mRobot.motionPlay(motionName, true);
}

// Resource cleanup
@Override
protected void onDestroy() {
    super.onDestroy();
    if (mRobot != null) {
        mRobot.release();
    }
}
```

### Important Files

- `app/build.gradle` - Dependencies and SDK configuration
- `app/libs/NuwaSDK-*.aar` - Robot SDK (do not modify)
- `TODO.md` - Development log and task tracking
- `deploy.sh` - Deployment script

### Known Issues

1. **ConnectionManager$ReceiveDataCallback** missing from SDK
2. **LED color control** referenced but not fully exposed in SDK
3. **Voice recognition** mentioned but not implemented
4. **API key security** - Currently hardcoded
5. **Acoustic echo (feedback)**: The device’s microphone may pick up audio from its own speakers, causing unintended sound feedback.

## Development Standards

### Code Organization
- Activities handle UI and lifecycle
- Helper classes manage complex integrations
- Data classes for motion/expression metadata
- Callbacks for all async operations

### Error Handling
- Check SDK readiness before API calls
- Handle network failures gracefully
- Log errors but remove debug logs for production
- Validate robot connection state

### Testing Approach
- Manual testing on physical robot required
- Motion preview grid for quick testing
- Face expression test UI included
- Limited automated test coverage

### Git Exclusions
- `TODO.md` - Development log (private)
- `CLAUDE.md` - This file (private)
- `.idea/` - IDE configuration
- API keys and credentials

🚨 **MANDATORY AI CODING ASSISTANT RULES - NO EXCEPTIONS** 🚨

⚠️ **CRITICAL**: These rules are FREQUENTLY IGNORED - PAY ATTENTION! ⚠️

- **🔧 TOOLS - STRICT REQUIREMENTS**

  - 🛑 **MANDATORY**: Use JDK 11 for Java development (NOT JDK 8, NOT JDK 17)
  - 🛑 **MANDATORY**: Fix errors after ALL changes

- **📝 CODE CHANGES - ZERO TOLERANCE POLICY**

  - ✅ **ONLY modify relevant code parts** - Do NOT touch unrelated code
  - ✅ **PRESERVE ALL**: formatting, names, and documentation unless EXPLICITLY requested
  - ✅ **FOLLOW EXISTING PATTERNS**: Refer to existing similar code structure when generating new code

- **📋 PROJECT MANAGEMENT - ABSOLUTELY REQUIRED**

  - 🔴 **MANDATORY**: Use TODO.md for tasks, progress, and issues. Update regularly - NO EXCEPTIONS
  - 🔴 **SESSION START CHECKLIST**: review TODO.md, run `git status`, check recent commits - DO NOT SKIP

- **⚡ DEVELOPMENT PROCESS - ENFORCE STRICTLY**

  - 🛑 **REQUIRED**: Plan and discuss approaches before coding - NO RUSHING
  - 🛑 **REQUIRED**: Make small, testable changes - NO BIG CHANGES
  - 🛑 **REQUIRED**: Eliminate duplicates proactively
  - 🛑 **REQUIRED**: Log recurring issues in TODO.md - ALWAYS DOCUMENT

- **🔒 CODE QUALITY - NON-NEGOTIABLE STANDARDS**

  - ✅ **MANDATORY**: Handle errors and validate inputs - NO EXCEPTIONS
  - ✅ **MANDATORY**: Follow conventions and secure secrets - NEVER EXPOSE SECRETS
  - ✅ **MANDATORY**: Write clear, type-safe code - NO SHORTCUTS
  - ✅ **PRODUCTION RULE**: Remove ALL debug logs before production - CLEAN CODE ONLY

- **📐 DEVELOPMENT STANDARDS - ABSOLUTE REQUIREMENTS**
  - 🎯 **PRIORITY #1**: Simplicity and readability over clever solutions
  - 🎯 **APPROACH**: Start with minimal working functionality - BUILD INCREMENTALLY
  - 🎯 **CONSISTENCY**: Maintain consistent style throughout - NO STYLE MIXING

🔥 **FINAL WARNING**: If you violate these rules, you are COMPLETELY IGNORING the project standards!
