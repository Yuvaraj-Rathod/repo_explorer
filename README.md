# Repo Explorer - Android Developer Evaluation 📱

## Overview

Repo Explorer is an Android application built for evaluating technical skills in Android development.
It uses the [GitHub Repo API](https://api.github.com/users/{username}/repos) to fetch repositories, and for fetching repositories based on key words it Uses [GitHub Query API](https://api.github.com/search/repositories),
provides offline caching, implements Google Sign-In, and integrates Firebase Cloud Messaging (FCM) for push notifications.

## Features

- Fetches GitHub repositories of a user.
- Offline caching using Room Database.
- Search functionality to filter repositories.
- Google Sign-In authentication.
- Firebase Cloud Messaging (FCM) for notifications.

---

## **1️⃣ Explain the functionality**
Repo Explorer allows users to:
1. **Fetch GitHub Repositories**: Uses the GitHub API to retrieve public repositories of a user.
2. **Offline Caching**: Stores repositories locally using Room Database.
3. **Search Repositories**: Implements a search bar to filter repositories.
4. **Google Sign-In**: Users can sign in using their Google account.
5. **Firebase Cloud Messaging (FCM)**: Sends push notifications to users.

---

## **2️⃣ What Components are used in the project?**
- **Jetpack Compose**: For UI design.
- **Retrofit + OkHttp**: For network calls to the GitHub API.
- **Room Database**: For local caching.
- **Firebase Authentication**: For Google Sign-In.
- **Firebase Cloud Messaging (FCM)**: For push notifications.
- **Hilt (DI)**: For dependency injection.

---

## **3️⃣ What architecture did you use and why do you think it’s best suitable?**
The project follows the **MVVM (Model-View-ViewModel)** architecture:

✅ **Separation of Concerns**: Business logic is in `ViewModel`, UI in `Composable`, and data handling in `Repository`.  
✅ **Easier Testing**: ViewModels and Repositories can be unit-tested independently.  
✅ **Scalability**: New features can be added without breaking existing functionality.  
✅ **LiveData & StateFlow**: Ensures UI is reactive and updates efficiently.

---

## **4️⃣ How do you ensure that code is testable?**
To ensure testability, we followed these practices:

- **Dependency Injection (Hilt)**: Allows injecting fake dependencies for testing.
- **Repository Pattern**: Makes data management modular and easy to test.
- **ViewModels with StateFlow**: Enables observing UI state changes in tests.
- **Unit Testing for API & Database**: Can Use JUnit and MockK for testing API calls and database queries.

---

## 🚀 Automating Deployment

### ✅ Is there any way to automate the deployment?
Yes! We can automate the deployment using **GitLab CI/CD**.

### **🔹 Automated Deployment Approach**
We use **GitLab CI/CD** to automatically:
- Build the APK on every push.
- Upload the APK to Firebase App Distribution or Google Drive.
- Notify the team via Slack or Email when the build is ready.


### 📂 Drive Links:

- [📥 Download APK](https://drive.google.com/file/d/1yS8HFnJ4_M8HhQLKWzauf1r87LgPnWED/view?usp=drive_link)
- [▶️ Watch Video](https://drive.google.com/file/d/1yThGvYzMYJ1x_iw2PbqCmD4bCV-8Xmvt/view?usp=sharing)
