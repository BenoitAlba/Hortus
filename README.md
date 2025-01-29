![badge-android](http://img.shields.io/badge/platform-android-6EDB8D.svg?style=flat)
![badge-ios](http://img.shields.io/badge/platform-ios-CDCDCD.svg?style=flat)

<h1 align="center">
    <img height="200" src="https://github.com/BenoitAlba/Hortus/blob/main/composeApp/src/commonMain/composeResources/drawable/hortus3-removebg.png"/>
    <br>
    Hortus - Experimental application developed with KMP/CMP to explore its capabilities.
</h1>

Hortus is a smart gardening app that uses AI to help you identify and care for your plants.
Simply enter a plant's name or description, and the AI will generate a detailed profile.
Additionally, by providing your location,
Hortus can inform you about local weather conditions and alert you if your plants are at risk from cold temperatures.

<span style="text-decoration: underline;">Coming soon:</span>. customize your plants in detail and receive notifications to ensure their health, even in cold weather

## Libraries used
- **_Koin:_**  A dependency injection library used for managing dependencies and providing them to your application classes.
- **_Kotlin Serialization with kotlinx.json:_** Enables serialization and deserialization of data objects to and from JSON format, useful for network communication.
- **_Ktor:_** used for HTTP requests
- **_Room:_** Provides an abstraction layer for interacting with SQLite databases on Android.
- **_Voyager:_** A navigation library for Jetpack Compose, offering a declarative way to manage navigation within your app.
- **_Peekaboo:_** Kotlin Multiplatform library for Compose Multiplatform, designed for seamless integration of an image picker feature in iOS and Android applications.
- **_Coil:_** Provides an efficient way to load and display images in Compose applications.
- **_Compass:_** Compass is a Kotlin Multiplatform library location toolkit. It provides a set of tools for working with location data, including geocoding, reverse geocoding, and more.
- **_Compose Shimmer:_** Library for creating shimmer effects (placeholder animations) in Compose UIs.
- **_Compottie:_** Compose Multiplatform Adobe After Effects Bodymovin (Lottie) animations rendering engine.
- **_Multiplatform Settings:_**: Used to persist key-value data.
- **_firebase.vertexai_** The Vertex AI Gemini API gives access to the latest generative AI models from Google: the Gemini models

## File hierarchy
* `/composeApp` is for code that will be shared across your Compose Multiplatform applications.
  It contains several subfolders:
  - `commonMain` is for code that’s common for all targets.
  - Other folders are for Kotlin code that will be compiled for only the platform indicated in the folder name.
    For example, if you want to use Apple’s CoreCrypto for the iOS part of your Kotlin app,
    `iosMain` would be the right folder for such calls.

* `/iosApp` contains iOS applications. Even if you’re sharing your UI with Compose Multiplatform,
  you need this entry point for your iOS app. This is also where you should add SwiftUI code for your project.

## Screenshots

![Home screen](https://imgur.com/a/uPzWXqU)
![Add a plant](https://imgur.com/a/3H4QJiw)
![Search a plant](https://imgur.com/a/QMrBvib)
![Add a plant - chose exposure](https://imgur.com/a/F9RAI59)
![details screen](https://imgur.com/a/vUDwaud)
![details screen 2](https://imgur.com/a/fP3d2u3)
