# Swift Words

Swift Words is an engaging and interactive word game app that challenges your vocabulary skills while keeping you entertained. Designed with a sleek UI using Jetpack Compose and robust backend technologies, the app delivers a seamless gaming experience for all users. The app is currently available on [Google Play Store](https://play.google.com/store/apps/details?id=com.yukuro.swiftwords).

---

## Architecture

Swift Words adheres to the MVVM (Model-View-ViewModel) architecture, ensuring separation of concerns and scalability. Key architectural components include:

- **ViewModel:** Manages UI-related data and business logic, keeping the UI codebase clean.
- **Room Database:** Provides local data storage for offline access to user preferences, game data, and other persistent data, ensuring a seamless experience even without network connectivity.
- **Coroutines:** Facilitates non-blocking asynchronous programming, ensuring smooth data operations and efficient use of resources.
- **Jetpack Navigation Compose:** Facilitates navigation within the app, enhancing user experience and flow.
- **WorkManager:** Schedules and manages background tasks such as sending daily notifications, even under constrained conditions like device restarts.
- **Custom Injections:** Ensures modularity and flexibility by providing dependency injection tailored to specific features.
- **Audio Manager:** Manages sound effects for a more immersive experience.

---

## Technology Stack

- **Frontend:** Kotlin, Jetpack Compose
- **Backend:** Room Database, ViewModel
- **Notifications:** WorkManager for scheduling notifications
- **Audio:** Managed via AudioManager for enhanced user experience

---

## Features

- **Dynamic Gameplay:** Offers exciting word challenges to test your vocabulary and speed.
- **User Preferences:** Customizable themes, sounds, and user settings stored locally for offline use.
- **Daily Notifications:** Keeps users engaged with streak reminders.
- **Interactive UI:** A sleek, responsive design powered by Jetpack Compose.
- **Seamless Audio Integration:** Sound effects.

