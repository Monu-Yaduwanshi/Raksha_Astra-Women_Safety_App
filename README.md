<h1 align="center">🚨 RAKSHA ASTRA – Women Safety Android App 🇮🇳</h1>

<p align="center">
  <img src="https://img.shields.io/badge/Platform-Android-green" />
  <img src="https://img.shields.io/badge/Language-Kotlin-blue" />
  <img src="https://img.shields.io/badge/UI-Jetpack%20Compose-orange" />
  <img src="https://img.shields.io/badge/Backend-Firebase-yellow" />
  <img src="https://img.shields.io/badge/Cloud-Cloudinary-purple" />
  <img src="https://img.shields.io/badge/Architecture-MVVM-red" />
</p>

<p align="center">
  <strong>Empowering women with instant safety, live tracking, and community support.</strong>
</p>

---

# 📸 App Screenshots  
*(Replace links with your real images)*  

<p align="center">
  <img src="YOUR_IMAGE_LINK_1" width="250" />
  <img src="YOUR_IMAGE_LINK_2" width="250" />
  <img src="YOUR_IMAGE_LINK_3" width="250" />
</p>

---

# 🌟 **About the Project**

**Raksha Astra** is a modern, powerful, and reliable Indian Women Safety Application designed to provide **instant help** during emergencies.  
It empowers women by connecting them with:

✔ Trusted contacts  
✔ Community responders  
✔ Nearby users  
✔ Authorities (future integration)

The app ensures **speed, accuracy, and reliability** using:

- Firebase  
- Cloudinary  
- Google Maps & GPS  
- Jetpack Compose  
- MVVM Architecture  

In a single tap, users can trigger a **complete emergency workflow** that shares details, sends alerts, uploads evidence, and starts real-time tracking.

---

# 🎯 **Problem Statement**

Women in India face safety challenges in:

- Public places  
- Travel routes  
- Late-night commutes  
- Crowded or unfamiliar areas  

Existing safety apps lack:

- Real-time live tracking  
- Instant SOS activation  
- Community alerts  
- Offline support  
- Cloud-based evidence upload  
- Reliable low-latency performance  

**Raksha Astra solves these problems** by offering a complete, integrated, fast, and secure safety platform.

---

# 💡 **Proposed Solution**

Raksha Astra provides:

### ⭐ Instant One-Tap SOS  
### ⭐ Live Location Tracking  
### ⭐ Red Zone Alerts  
### ⭐ Community Safety Network  
### ⭐ Safe Routes  
### ⭐ Offline Cache  
### ⭐ Evidence Upload (Photo/Video)  
### ⭐ Secure Authentication  

All powered by **Firebase + Cloudinary + Google APIs**.

---

# 🚨 **Core Features (Fully Explained)**

## 🔴 1. **One-Touch SOS Alerts**
When the user presses the SOS button:

- SMS instantly sent to trusted contacts  
- Push notifications sent through Firebase  
- Auto-call to emergency number (optional)  
- Live location starts updating  
- Community alert is triggered within selected radius  

Everything happens in **< 5 seconds**.

---

## 📍 2. **Live GPS Location Tracking**
- Firebase Realtime Database continuously updates location  
- Google Maps SDK displays user's live movement  
- Smart tracking reduces battery usage  
- Accurate lat-long every few seconds  

Useful for:

✔ Police  
✔ Family  
✔ Community responders  

---

## 🛣️ 3. **Safe Route Recommendation**
Using community reports, app shows:

- **Red Zones** = High-risk areas  
- **Yellow Zones** = Moderate-risk areas  
- **Green Zones** = Safe areas  

The app warns the user if she moves into a dangerous zone.

---

## 👥 4. **Community Alert System**
Nearby users are automatically notified when:

- A user triggers SOS  
- Evidence is uploaded  
- Location is updated in alert mode  

This creates a **crowd-powered safety network**.

---

## ⚡ 5. **Offline Mode (Very Important Feature)**
Using **Room / DataStore**, the app saves:

- Trusted contacts  
- Last safe routes  
- Last location  
- User profile  

Even if internet is OFF:

✔ SOS still works  
✔ Contacts still available  
✔ Data syncs automatically when network returns  

---

## ☁️ 6. **Secure Media Upload – Cloudinary**
- If user captures a photo/video during SOS  
- It is uploaded to Cloudinary in encrypted form  
- A link is shared with trusted contacts  

This helps as **evidence** during investigation.

---

## 🔐 7. **Firebase Authentication**
- Login with Email or Phone OTP  
- Encrypted local storage for security  
- Auto-login enabled  
- Cloud profile backup  

---

# 🏗 **System Architecture – MVVM Pattern**

┌────────────────┐ ┌─────────────────┐ ┌──────────────────────┐
│ VIEW │◀────▶│ VIEWMODEL │◀────▶│ REPOSITORY │
└────────────────┘ └─────────────────┘ └──────────────────────┘
│ Remote + Local Sources
▼
Firebase • Cloudinary • RoomDB


---

# 🔧 **Technology Stack**

## **Frontend**
- Kotlin  
- Jetpack Compose (Material 3)  
- StateFlow / LiveData  
- Navigation Compose  

## **Backend**
- Firebase Authentication  
- Firebase Realtime Database  
- Firebase Cloud Messaging  
- Firebase Cloud Functions  

## **Storage**
- Cloudinary (Media uploads)  
- Room Database (Offline cache)  
- DataStore Preferences  

## **Location & Maps**
- Google Maps SDK  
- Fused Location Provider API  

## **Background Services**
- WorkManager (for location updates)  

---

# 🧩 **Software Modules**

## **1. Authentication Module**
- Firebase Login (Email / OTP)  
- Profile creation  
- Emergency contacts manager  

## **2. SOS Alert Module**
- One-tap emergency  
- SMS alert  
- FCM push  
- Auto-call feature  

## **3. Location Tracking Module**
- Live GPS stream  
- Map display  
- Firebase sync  

## **4. Community Module**
- Nearby users alert  
- Red Zone mapping  
- Evidence sharing  

## **5. Offline Module**
- Cached last route  
- Cached contacts  
- Offline data sync  

## **6. Admin/Police Integration (Future Ready)**
- Dashboard for alert monitoring  
- API integration  

---

# 🧪 **Testing & Quality Assurance**

### ✔ Network failure scenarios  
### ✔ Poor GPS signal testing  
### ✔ Offline-first reliability  
### ✔ Low battery mode checks  
### ✔ UI performance & responsiveness  
### ✔ Data encryption & security audit  

---

# 🚀 **Expected Outcome**

| Feature | Benefit |
|--------|---------|
| Instant SOS | Faster emergency response |
| Live Tracking | Family can monitor safely |
| Red Zones | Avoid unsafe areas |
| Community Support | Local help within minutes |
| Offline Mode | Reliable even in remote areas |
| Evidence Upload | Strong proof for legal cases |

---

# 👨‍💻 **Team Responsibilities**

| Role | Work |
|------|------|
| Team Lead | Architecture, DI, Navigation |
| Frontend Dev | Compose UI Screens |
| Backend Dev | Firebase/Cloud Integration |
| QA & Docs | Testing + Documentation |

---

# 🛠 **How to Install & Run**

```bash
1. Clone the repository  
2. Open in Android Studio  
3. Connect Firebase project  
4. Add google-services.json  
5. Add Cloudinary credentials  
6. Build & Run the project  

RakshaAstra/
│── app/
│   ├── data/
│   │   ├── repository/
│   │   ├── local/ (Room)
│   │   ├── remote/ (Firebase)
│   ├── ui/
│   │   ├── screens/
│   │   ├── components/
│   │   └── theme/
│   ├── utils/
│   └── viewmodel/
│
└── README.md
