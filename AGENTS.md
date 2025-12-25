# Repository Guidelines

## Project Structure & Module Organization
- Module: `app/` (Android application)
  - Source: `app/src/main/java/com/example/hellokebbi/`
    - Activities: `MainActivity`, `RealtimeActivity`, `FaceExpressionActivity`
    - Helpers: `RealtimeRobotHelper`, `FaceExpressionManager`
  - Resources: `app/src/main/res/` (layouts, values, mipmaps)
  - Tests: `app/src/test/` (unit), `app/src/androidTest/` (instrumented)
  - SDK AARs: `app/libs/` (Nuwa SDK)

## Build, Test, and Development Commands
- Build debug APK: `./gradlew :app:assembleDebug`
- Install on device: `adb install -r app/build/outputs/apk/debug/app-debug.apk` or `./deploy.sh`
- Run unit tests: `./gradlew :app:testDebugUnitTest`
- Run instrumented tests: `./gradlew :app:connectedDebugAndroidTest` (requires device/emulator)
- Lint: `./gradlew :app:lint` (Android Lint)
- Logs: `adb logcat | grep -i "HelloKebbi\|RealtimeRobotHelper"`

## Coding Style & Naming Conventions
- Language: Java (Android), 4-space indentation, no tabs.
- Packages: `com.example.hellokebbi`.
- Resources: layouts `activity_*.xml`, ids `lowerCamelCase`, strings in `res/values/strings.xml`.
- Avoid blocking UI: prefer `motionPlay(..., false)` and callbacks; never `Thread.sleep(...)` on main thread.
- Compatibility: avoid Java 9/11-only APIs on Android 9 (e.g., `String.strip`, `Map.of`).

## Testing Guidelines
- Frameworks: JUnit4 (`test/`), AndroidX Test + Espresso (`androidTest/`).
- Naming: test classes mirror target class (e.g., `RealtimeRobotHelperTest`).
- Run fast unit tests locally; reserve `connected*` tests for device integration.

## Commit & Pull Request Guidelines
- Commits: use concise, present-tense messages. Prefer conventional prefixes: `feat:`, `fix:`, `docs:`, `build:`, `refactor:` (seen in history).
- PRs must include:
  - Scope summary and motivation
  - Steps to test (commands, device/OS)
  - Screenshots/logs where relevant
  - Linked issues or task IDs

## Security & Configuration Tips
- OpenAI key: inject via Gradle `buildConfigField` (avoid committing secrets). Example in `app/build.gradle`:
  - `buildConfigField "String", "OPENAI_API_KEY", '"${OPENAI_API_KEY}"'` with env var export.
- Nuwa SDK: ensure AAR in `app/libs/` and dependency name matches file.
- Min/Target SDK: `minSdk 28`, `targetSdk 33`; test on Android 9 for UI responsiveness.


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
