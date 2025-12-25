#!/bin/bash

# HelloKebbi Deployment Script

# Colors for output
GREEN='\033[0;32m'
RED='\033[0;31m'
NC='\033[0m' # No Color

# ADB path
ADB_PATH="$HOME/Library/Android/sdk/platform-tools/adb"

# APK path
APK_PATH="app/build/outputs/apk/debug/app-debug.apk"

echo "HelloKebbi Deployment Script"
echo "============================"

# Check if ADB exists
if [ ! -f "$ADB_PATH" ]; then
    echo -e "${RED}Error: ADB not found at $ADB_PATH${NC}"
    echo "Please install Android SDK or update the ADB_PATH in this script"
    exit 1
fi

# Check if APK exists
if [ ! -f "$APK_PATH" ]; then
    echo -e "${RED}Error: APK not found at $APK_PATH${NC}"
    echo "Please build the project first with: ./gradlew assembleDebug"
    exit 1
fi

echo -e "${GREEN}Found APK:${NC} $APK_PATH"

# Check connected devices
echo -e "\n${GREEN}Checking connected devices...${NC}"
$ADB_PATH devices

# Ask for confirmation
echo -e "\n${GREEN}Ready to install HelloKebbi on the connected device${NC}"
read -p "Continue? (y/n) " -n 1 -r
echo

if [[ $REPLY =~ ^[Yy]$ ]]; then
    echo -e "\n${GREEN}Installing APK...${NC}"
    $ADB_PATH install -r "$APK_PATH"
    
    if [ $? -eq 0 ]; then
        echo -e "\n${GREEN}Installation successful!${NC}"
        echo "You can now launch 'HelloKebbi' on your robot"
        
        # Option to view logs
        echo -e "\n${GREEN}View logs?${NC} (y/n)"
        read -p "" -n 1 -r
        echo
        
        if [[ $REPLY =~ ^[Yy]$ ]]; then
            echo -e "\n${GREEN}Showing logs (Ctrl+C to stop)...${NC}"
            $ADB_PATH logcat | grep -E "(HelloKebbi|com.example.hellokebbi)"
        fi
    else
        echo -e "\n${RED}Installation failed!${NC}"
        echo "Please check the error message above"
    fi
else
    echo "Deployment cancelled"
fi