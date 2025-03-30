# Currency Converter App

This app was created to showcase the skills of the developer and is for demonstration only.
A currency converter app built in Kotlin that follows Clean Architecture and the MVVM pattern. The app allows users to convert between currencies with commission fee calculations, and maintains a wallet with multiple currency balances.

## Features

- **Currency Conversion:**
    - Converts amounts from one currency to another.
    - Calculates and applies commission fees
    - Provides a preview of the conversion result, including the net amount and commission fee.

- **Wallet Management:**
    - Displays the user's wallet with balances for different currencies.
    - Updates the wallet after each successful conversion.

## Architecture

The project is build on MVVM with Clean Architecture:

- **Domain Layer:**  
  Contains business logic

- **Data Layer:**  
  Implements repository interfaces, handles API calls using Retrofit with Moshi, and manages the account's data.

- **Presentation Layer:**  
  Uses the MVVM pattern with ViewModels and Fragments to manage the UI. The UI layer observes LiveData from the ViewModel and updates accordingly.

- **Dependency Injection:**  
  Dagger Hilt is used to inject dependencies across the app, making it easier to test and maintain.

## Technologies Used

- Kotlin
- Android Jetpack (ViewModel, LiveData, Coroutines)
- Dagger Hilt
- Retrofit with Moshi
- Material Components
- JUnit for testing

## Setup Instructions

1. Clone the Repository and stay on main
2. Open the Project in Android Studio and wait for the Gradle to finish syncing
3. Build and Run the app
