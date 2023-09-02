![Imgur](https://i.imgur.com/TlgJHka.png?1)

Source code for a simplified version of my Android app "It's On".

# Contents
This app showcases the implementation of custom quick settings tiles, in this case, to keep the screen on for longer than the system settings.

Due to API restrictions, it's minimum OS version requirement is 6.0 (Marshmallow).

This is done by automatically changing this system setting when the tile is activated. To achieve this, it's necessary to have a special permission that can only be changed via the app settings. A guided flow to this is also implemented in the app so the users understand and can enable this permission easily.

This project also features examples of unit testing in apps. To show how to test isolated functions that do not require the Android framework, there is a test case of a helper function using JUnit. To test the app's functionalities and structure, there are instrumented tests using UI Automator, Espresso and Runner. They cover checking the app's state by the visible views (e.g. show a warning message if the permission is not granted or a normal message if it's granted) and checking if system settings have been set successfully.

The source code is in written and documented in English and the app is translated in both English and Brazilian Portuguese.

The original app "It's On" (or "Ligadão") is available in the Play Store [here](https://play.google.com/store/apps/details?id=br.not.sitedoicaro.itson).

# Usage
Clone the repository and build using Android Studio after making sure you have all the dependencies installed in your Android SDK.

# Disclaimer
This repository is not meant to represent the original app's source code and serves only for the purpose of providing sample code for those curious in understanding how the fun and interesting features of the app were implemented. Feel free to use it as means of studying or if you need guidance in how to implement certain functionalities.
