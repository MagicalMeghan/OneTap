## OneTap Kotlin Sample
An example of using OneTap in Kotlin.

### API Key
Before starting with the app, get an API Key here: 
1. Navigate here https://console.developers.google.com/
2. Under the OAuth consent screen tab, specify if your app is Internal or External
3. Fill out your app information in the OAuth tab
4. Under the Credentials tab, create an Android client ID, you will need the apps package name and the SHA-1 signature. This is how you get the SHA https://stackoverflow.com/questions/27609442/how-to-get-the-sha-1-fingerprint-certificate-in-android-studio-for-debug-mode
5. In the Android Studio project, navigate to strings.xml and replace the gcp_client string with your client id
6. Run the app and tap on the sign up button.

NOTE: Make sure you are signed into an email address on your emulator or device that you are testing on.

### App Description
Tapping "Sign In" will try to retrieve stored password credentials or authorized Google Accounts
on user's device. Proceeding with Authorization will bring user to LoggedInUserActivity screen.

"Sign Up" button will request authentication using Google Account available on user's device.

When a user who didn't previously sign up tries to sign in app will display an error dialog suggesting
to Sign Up instead. Note: this can also happen if a user doesn't have any Google accounts on their device.
