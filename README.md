# 📱 Seamless Document Sharing App  

An Android application built with **Kotlin** and **Jetpack Compose**, enabling users to securely share, store, and manage documents in real-time. Powered by **Firebase** (Auth, Firestore, Storage, Analytics) and the **Gemini API** for enhanced AI-driven features.  

---

## ✨ Features  
- 🔑 **User Authentication** (Firebase Auth)  
- 📂 **Secure Document Uploads** (Firebase Storage)  
- 📊 **Real-time Database Sync** (Cloud Firestore)  
- 📈 **Analytics Tracking** (Firebase Analytics)  
- 🤖 **AI-powered Enhancements** via **Gemini API**  
- 📱 **Modern UI** with Jetpack Compose  
- ☁️ **Seamless cloud-based sharing**  

---

## 🛠️ Tech Stack  
- **Language:** Kotlin  
- **UI:** Jetpack Compose  
- **Backend Services:** Firebase (Auth, Firestore, Storage, Analytics)  
- **AI Integration:** Gemini API  
- **Architecture:** MVVM + Coroutines + Flow  
- **Dependency Injection:** Hilt / Koin (whichever you used)  

---

## 🚀 Getting Started  

### Prerequisites  
- Android Studio (Latest Stable Version)  
- Firebase Project (with Auth, Firestore, Storage, Analytics enabled)  
- Gemini API Key  

### Setup Instructions  
1. **Clone the repository**  
   ```bash
   git clone https://github.com/sage/sage.git
   cd sage
   ```

2. **Add Firebase Configuration**  
   - Download your `google-services.json` from Firebase Console  
   - Place it inside:  
     ```
     app/src/main/
     ```

3. **Configure Gemini API**  
   - Add your Gemini API Key in `local.properties`  
     ```
     GEMINI_API_KEY=your_api_key_here
     ```

4. **Build & Run**  
   - Open the project in Android Studio  
   - Sync Gradle  
   - Run on emulator or physical device  

---

## 📷 Screenshots  

| Home Screen | Document Upload | Profile | 
|-------------|-----------------|--------------|
| ![Home](https://github.com/user-attachments/assets/3adbd85b-d617-4e82-ac2e-c596de88f104) | ![Upload]([screenshots/upload.png](https://github.com/user-attachments/assets/83d5a75f-c80a-4e47-a90f-9842443809c1)) | ![Share]([screenshots/share.png](https://github.com/user-attachments/assets/050bde91-934c-4979-a9fe-34a58a982d10)) |

<!--
| Login | Profile | Settings | AI Features |
|-------|---------|----------|--------------|
| ![Login](screenshots/login.png) | ![Profile](screenshots/profile.png) | ![Settings](screenshots/settings.png) | ![AI](screenshots/ai.png) |
-->
---

## 📂 Project Structure  
```
app/
 ├── data/          # Repositories, Firestore access, storage handlers
 ├── di/            # Dependency Injection setup
 ├── ui/            # Jetpack Compose UI screens
 ├── presentation/     # ViewModels (MVVM)
 ├── utils/         # Helper functions, constants
 └── MainActivity.kt
```

---

## 🤝 Contributing  
Contributions are welcome!  
- Fork the repo  
- Create a feature branch (`git checkout -b feature/your-feature`)  
- Commit changes (`git commit -m "Add new feature"`) 

- Push (`git push origin feature/your-feature`)  
- Create a Pull Request  

---

## 📜 License  
[MIT License](LICENSE)  


