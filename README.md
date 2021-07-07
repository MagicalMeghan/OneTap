## GIS Sample Repo
An example of using Google Identity Services (EAP M1).

### App Description
Tapping "Sign In" will try to retrieve stored password credentials or authorized Google Accounts
on user's device. Proceeding with Authorization will bring user to LoggedInUserActivity screen.

"Sign Up" button will request authentication using Google Account available on user's device.

When a user who didn't previously sign up tries to sign in app will display an error dialog suggesting
to Sign Up instead. Note: this can also happen if a user doesn't have any Google accounts on their device.

### Next steps
- Persist token in private storage until user signs out.
- Add tests.

### Screenshots
![main_screen](screenshots/screenshot_main.png)

User has an option to Sign In if they have existing account or sign up using Google Account.

![sign_up_dialog](screenshots/screenshot_sign_up.png)

When signing up user is presented with a consent dialog.

![missing_credentials](screenshots/screenshot_auth_error.png)

If there are no Google Accounts or user is not signed up this error dialog wll be displayed.

![sign_in_dialog](screenshots/screenshot_sign_in.png)

Previously signed up user can sign in with their Google Account.

![sign_in_in_progress](screenshots/screenshot_in_progress.png)

It's possible to go cancel sign in while user signing in.

![signed_in_user_screen](screenshots/screenshot_signed_in.png)

To finish session user must Sign Out.

### Questions and Considerations:
- What should be messaging around "User Cancel" and blocking app from accessing APIs
after 3 user cancellations?
- How to clear user "Signed Up" status? When clearing it from Google Accounts it still shows "Sign In again"

### Resources
[Google Sign-In sample](https://github.com/googlesamples/google-services/blob/master/android/signin/app/src/main/java/com/google/samples/quickstart/signin/SignInActivity.java)

[Credentials Quickstart](https://github.com/googlearchive/android-credentials/blob/master/credentials-quickstart/app/src/main/java/com/google/example/credentialsbasic/MainActivity.java)
