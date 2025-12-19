# Currency Alert App

Android application for monitoring CAD to KRW exchange rates with configurable alerts and historical data visualization.

## Features

- **Real-time Exchange Rates**: Live CAD to KRW conversion rates
- **Historical Charts**: View exchange rate trends with configurable time windows (1 day, 1 week, 1 month, 1 year, 5 years)
- **Background Monitoring**: Periodic rate checks via WorkManager
- **Push Notifications**: Alerts for rate changes during configured time windows
- **Configurable Settings**: Set notification time windows and preferences
- **Offline Support**: Local database caching with Room

## Tech Stack

### Architecture
- **MVVM Pattern**: ViewModel + StateFlow for reactive UI
- **Dependency Injection**: Hilt for clean architecture
- **Jetpack Compose**: Modern declarative UI

### Core Libraries
- **Kotlin Coroutines**: Asynchronous operations
- **Retrofit**: REST API integration
- **Room**: Local database persistence
- **WorkManager**: Background task scheduling
- **MPAndroidChart**: Historical data visualization
- **Material Design 3**: Modern UI components

### APIs
- **CurrencyAPI.net**: Real-time exchange rates (frequent updates)
- **FrankfurterAPI**: Historical exchange rate data

## Project Structure

```
app/src/main/java/com/student/currencyalert/
├── data/
│   ├── api/
│   │   ├── ExchangeRateService.kt      # CurrencyAPI.net integration
│   │   └── FrankfurterService.kt       # Historical data API
│   ├── database/
│   │   ├── CurrencyDatabase.kt         # Room database
│   │   ├── dao/
│   │   │   └── ExchangeRateDao.kt      # Database operations
│   │   └── entity/
│   │       └── ExchangeRateEntity.kt   # Rate data model
│   ├── model/
│   │   ├── ExchangeRateResponse.kt     # Current rate response
│   │   └── HistoricalRateResponse.kt   # Historical data response
│   └── repository/
│       └── ExchangeRateRepository.kt   # Data layer abstraction
├── di/
│   ├── NetworkModule.kt                # Retrofit/API DI
│   └── DatabaseModule.kt               # Room DI
├── ui/
│   ├── MainActivity.kt                 # Main screen with current rate
│   ├── MainViewModel.kt                # Main screen state management
│   ├── history/
│   │   ├── HistoryActivity.kt          # Historical chart screen
│   │   └── HistoryViewModel.kt         # Chart data management
│   └── settings/
│       ├── SettingsActivity.kt         # Settings screen
│       └── SettingsViewModel.kt        # Settings state management
├── utils/
│   ├── NotificationHelper.kt           # Push notification manager
│   └── PreferencesHelper.kt            # SharedPreferences wrapper
└── workers/
    └── RateFetchWorker.kt              # Background rate fetching
```

## Setup

### Prerequisites
- Android Studio Ladybug or later
- JDK 17
- Android SDK 34
- Minimum Android 8.0 (API 26)

### API Keys

1. **CurrencyAPI.net**
   - Sign up at https://currencyapi.net
   - Get your API key
   - Add to `local.properties`:
     ```
     API_KEY=your_currencyapi_key
     ```

2. **FrankfurterAPI**
   - No API key required (free public API)

### Build Configuration

**gradle.properties:**
```properties
android.useAndroidX=true
android.enableJetifier=true
org.gradle.jvmargs=-Xmx2048m -XX:MaxMetaspaceSize=512m --add-opens=jdk.compiler/com.sun.tools.javac.main=ALL-UNNAMED --add-opens=jdk.compiler/com.sun.tools.javac.code=ALL-UNNAMED --add-opens=jdk.compiler/com.sun.tools.javac.comp=ALL-UNNAMED --add-opens=jdk.compiler/com.sun.tools.javac.file=ALL-UNNAMED --add-opens=jdk.compiler/com.sun.tools.javac.jvm=ALL-UNNAMED --add-opens=jdk.compiler/com.sun.tools.javac.parser=ALL-UNNAMED --add-opens=jdk.compiler/com.sun.tools.javac.processing=ALL-UNNAMED --add-opens=jdk.compiler/com.sun.tools.javac.tree=ALL-UNNAMED --add-opens=jdk.compiler/com.sun.tools.javac.util=ALL-UNNAMED
kotlin.daemon.jvmargs=-Xmx2048m --add-opens=jdk.compiler/com.sun.tools.javac.main=ALL-UNNAMED --add-opens=jdk.compiler/com.sun.tools.javac.code=ALL-UNNAMED --add-opens=jdk.compiler/com.sun.tools.javac.comp=ALL-UNNAMED --add-opens=jdk.compiler/com.sun.tools.javac.file=ALL-UNNAMED --add-opens=jdk.compiler/com.sun.tools.javac.jvm=ALL-UNNAMED --add-opens=jdk.compiler/com.sun.tools.javac.parser=ALL-UNNAMED --add-opens=jdk.compiler/com.sun.tools.javac.processing=ALL-UNNAMED --add-opens=jdk.compiler/com.sun.tools.javac.tree=ALL-UNNAMED --add-opens=jdk.compiler/com.sun.tools.javac.util=ALL-UNNAMED
```

### Installation

1. Clone the repository
2. Add API key to `local.properties`
3. Open project in Android Studio
4. Sync Gradle
5. Run on emulator or device (API 26+)

## Usage

### Main Screen
- Displays current CAD to KRW exchange rate
- Pull to refresh for latest rate
- Settings icon (top-right) for notification configuration
- Chart icon (top-right) for historical data

### Settings Screen
- Configure notification start/end times
- Enable/disable notifications
- Save preferences

### History Screen
- View historical exchange rate trends
- Select time window: 1 Day, 1 Week, 1 Month, 1 Year, 5 Years
- Interactive chart with zoom and pan

### Background Worker
- Fetches rates every 15 minutes
- Only sends notifications during configured time window
- Requires network connectivity

## Key Implementation Details

### API Integration
- **Current Rates**: CurrencyAPI.net with query parameter authentication
- **Historical Data**: FrankfurterAPI with date range queries
- Error handling with Result type for safe error propagation

### Database
- Room database for offline caching
- Stores exchange rate history with timestamps
- Flow-based reactive queries

### Background Processing
- WorkManager for periodic rate fetching
- Hilt-integrated workers
- Network constraint enforcement

### Notifications
- Notification channels for Android 8.0+
- Configurable time windows via SharedPreferences
- Rate change alerts

## Dependencies

```kotlin
// Core
androidx.core:core-ktx:1.12.0
androidx.lifecycle:lifecycle-runtime-ktx:2.7.0

// Compose
androidx.compose:compose-bom:2023.10.01
androidx.compose.material3:material3
androidx.activity:activity-compose:1.8.2

// Hilt
com.google.dagger:hilt-android:2.48
androidx.hilt:hilt-work:1.1.0
androidx.hilt:hilt-navigation-compose:1.1.0

// Retrofit
com.squareup.retrofit2:retrofit:2.9.0
com.squareup.retrofit2:converter-gson:2.9.0

// Room
androidx.room:room-runtime:2.6.1
androidx.room:room-ktx:2.6.1

// WorkManager
androidx.work:work-runtime-ktx:2.9.0

// Chart
com.github.PhilJay:MPAndroidChart:v3.1.0

// SwipeRefresh
com.google.accompanist:accompanist-swiperefresh:0.32.0
```

## Build Requirements

- **Gradle**: 8.5
- **Android Gradle Plugin**: 8.2.0
- **Kotlin**: 1.9.20
- **Compose Compiler**: 1.5.5
- **compileSdk**: 34
- **targetSdk**: 34
- **minSdk**: 26

## Known Issues

- JDK 21 requires additional JVM args for kapt compatibility
- First launch may show empty chart until background worker runs
- Notification permissions must be granted on Android 13+

## Future Enhancements

- Customizable alert thresholds
- Multiple currency pair support
- Export historical data
- Widget support
- Dark mode theme

## License

This project is for educational purposes.

## Author

Built with Android best practices and modern architecture patterns.
