#!/bin/bash

# Set environment variables
export ANDROID_HOME=/usr/local/android-sdk
export PATH=$PATH:$ANDROID_HOME/tools:$ANDROID_HOME/platform-tools

# Download and unzip Android SDK
wget https://dl.google.com/android/repository/sdk-tools-linux-${ANDROID_SDK_VERSION}.zip
unzip sdk-tools-linux-${ANDROID_SDK_VERSION}.zip -d $ANDROID_HOME
rm sdk-tools-linux-${ANDROID_SDK_VERSION}.zip

# Install Android Build Tools
echo y | android update sdk --no-ui --all --filter build-tools-${ANDROID_BUILD_TOOLS_VERSION}

# Accept licenses
yes | $ANDROID_HOME/tools/bin/sdkmanager --licenses
