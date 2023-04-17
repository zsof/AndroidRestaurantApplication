# AndroidRestaurantApplication
### About

This project is being prepared as thesis work of the BME master's degree in Computer Science.
The application consists of an Android project and a Spring Boot backend written in Kotlin. The Android part is completed with XML description language and in Jetpack Compose. 

This code is written in XML and here is the Jetpack Compose codebase: https://github.com/zsof/RestaurantAppJetpackNew/tree/migration

The Spring Boot backend can be found here: https://github.com/zsof/restaurantApp/tree/develop

### The Application

The App is a Restaurant/Bakery/Patessiere... registration app, where users can filter for the right conditions, such as whether lactose-free food is prepared, the place is family-friendly, dogs can be brought in, or whether there is free parking. 
They can also see the places on the map, which one is closest to them, or they can also save the given place in a list of favorites.

Other information about the places is also available in the detailed view of the place, such as opening hours, website/phone number, additional photos or reviews.

### Techstack

- The app is based on the MVVM architecture
- It communicates with the backend via a Rest api, using Retrofit for this
- Part of the data will be saved in a local database, using Room libary
- The app displays the images using Glide
- Dependency Injection - Dagger Hilt
- Coroutines
- Firebase
- Google Map Api
- The app will be published on Google Play

### Screenshots about the app
