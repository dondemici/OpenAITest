# 🤖 Android OpenAI Prompt App

This Android app allows users to enter a text prompt and receive a response from OpenAI (ChatGPT / GPT-3.5-turbo). It demonstrates how to securely connect to the OpenAI API from an Android device.

---

## ✨ Features

- Text input for prompts
- Button to trigger API call
- Display of AI-generated responses
- Multi-line text input support
- API key kept safe using `local.properties`
- Compatible with OpenAI `gpt-3.5-turbo`

---

## 🛠️ Tech Stack

- Kotlin or Java (this version: Java)
- OkHttp for networking
- OpenAI API
- Android SDK 26+

---

## 🧰 Setup Instructions

### 1. 🔐 Add your API key to `local.properties`

Create or update the file in the **root project folder**:

```properties
openai.api.key=sk-your-real-api-key-here


2. ⚙️ Update build.gradle.kts (app-level)
Ensure the following lines exist inside the android {} block:

kotlin

buildFeatures {
    buildConfig = true
}

buildTypes {
    getByName("debug") {
        buildConfigField("String", "OPENAI_API_KEY", "\"${project.findProperty("openai.api.key") ?: ""}\"")
    }
    getByName("release") {
        buildConfigField("String", "OPENAI_API_KEY", "\"${project.findProperty("openai.api.key") ?: ""}\"")
        isMinifyEnabled = false
        proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
    }
}
Also add OkHttp to your dependencies:

kotlin

implementation("com.squareup.okhttp3:okhttp:4.10.0")
3. 📱 AndroidManifest.xml
Add Internet permission and register any new activities:

xml

<uses-permission android:name="android.permission.INTERNET"/>

<application
    ...>
    
    <activity android:name=".SecondActivity" />
    
    <activity android:name=".MainActivity"
              android:exported="true">
        <intent-filter>
            <action android:name="android.intent.action.MAIN" />
            <category android:name="android.intent.category.LAUNCHER" />
        </intent-filter>
    </activity>
</application>
4. 🧪 Example: Using the API key in Java
java

String apiKey = "Bearer " + BuildConfig.OPENAI_API_KEY;
Use this in your OkHttp request headers.

🧑‍💻 Optional Enhancements
Add copy-to-clipboard for response

Support speech-to-text

Add chat history using RecyclerView

Integrate error handling and loading indicators

✅ Git Tips
Make sure .gitignore includes:


lua
local.properties
To remove .env or .local.properties if accidentally committed:

bash
git rm --cached local.properties
git commit -m "Remove API key from repo"
