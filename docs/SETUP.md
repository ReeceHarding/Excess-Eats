# Excess Eats Setup Guide

## Prerequisites

- Android Studio Arctic Fox or later
- JDK 11 or later
- Firebase account
- Google Maps API key

## Development Environment Setup

1. **Android Studio**
   - Download and install Android Studio
   - Install required SDK platforms (minimum API 21)
   - Install Google Play Services via SDK Manager

2. **Firebase Setup**
   ```bash
   # Install Firebase CLI
   npm install -g firebase-tools

   # Login to Firebase
   firebase login

   # Initialize Firebase in project
   firebase init
   ```

3. **Environment Variables**
   Create a `local.properties` file:
   ```properties
   sdk.dir=/path/to/android/sdk
   MAPS_API_KEY=your_google_maps_api_key
   ```

4. **Dependencies**
   All required dependencies are in the app's build.gradle file.

## Building the Project

1. Sync project with Gradle files
2. Build project: `./gradlew build`
3. Run tests: `./gradlew test`

## Common Issues

1. **Build Errors**
   - Clean and rebuild project
   - Invalidate caches and restart

2. **Firebase Connection**
   - Verify google-services.json
   - Check Firebase console settings

3. **Maps Not Loading**
   - Verify API key in local.properties
   - Enable Maps SDK in Google Cloud Console
