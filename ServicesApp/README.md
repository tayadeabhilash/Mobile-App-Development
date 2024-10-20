# PDF Download Activity

This Android app allows users to input up to five PDF file URLs and download them in the background using Android Services. The app features a simple user interface and displays download progress through system notifications.

## Overview

The **PDF Download Activity** app provides a user-friendly interface for downloading PDFs from provided URLs. Users can specify up to five PDF file locations, and the app handles the download process in the background, displaying notifications for progress and completion.

## Features

- **User Input**: Simple form where users can input up to 5 PDF URLs.
- **Background Downloads**: Uses Android Services to download PDFs in the background.
- **Notifications**: Shows download progress and completion using Android's notification system.
- **Modern UI**: Beautiful and intuitive design with clear instructions and well-placed input fields.
- **Android 10+ Support**: Uses `MediaStore` for file storage and respects Android 13+ permissions for notifications.

## Requirements

- Android Studio with Gradle support
- Android 10 (API level 29) or above
- Android device or emulator
- Internet permission in your Android project (`INTERNET`)