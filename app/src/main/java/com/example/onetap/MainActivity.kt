package com.example.onetap

import android.content.Intent
import android.content.IntentSender.SendIntentException
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.BeginSignInRequest.GoogleIdTokenRequestOptions
import com.google.android.gms.auth.api.identity.BeginSignInRequest.PasswordRequestOptions
import com.google.android.gms.auth.api.identity.BeginSignInResult
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.Snackbar

private val TAG = MainActivity::class.java.simpleName
private const val REQ_ONE_TAP = 123
class MainActivity : AppCompatActivity() {
    private lateinit var oneTapClient: SignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<View>(R.id.signInButton).setOnClickListener { signIn() }
        findViewById<View>(R.id.signUpButton).setOnClickListener { signUp() }
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQ_ONE_TAP -> {
                try {
                    val credential = oneTapClient.getSignInCredentialFromIntent(data)
                    val idToken = credential.googleIdToken
                    val username = credential.id
                    val password = credential.password
                    when {
                        idToken != null -> {
                            // Got an ID token from Google. Use it to authenticate
                            // with your backend.
                            Log.d(TAG, "Got ID token.")
                        }
                        password != null -> {
                            // Got a saved username and password. Use them to authenticate
                            // with your backend.
                            Log.d(TAG, "Got password.")
                        }
                        else -> {
                            // Shouldn't happen.
                            Log.d(TAG, "No ID token or password!")
                        }
                    }
                } catch (e: ApiException) {
                    // ...
                }
            }
        }
    }

    private fun signIn() {
        startAuthentication(false)
    }

    private fun signUp() {
        startAuthentication(true)
    }

    private fun startAuthentication(isSignUp: Boolean) {
        Identity.getSignInClient(this)
            .beginSignIn(buildAuthenticationRequest(isSignUp))
            .addOnSuccessListener { beginSignInResult ->
                Log.e("", "Success")
                launchSignInFlow(beginSignInResult)
            }
            .addOnFailureListener { e -> handleAuthenticationException(e) }
    }

    private fun launchSignInFlow(beginSignInResult: BeginSignInResult) {
        try {
            // Show consent dialog or account selection dialog if user has > 1 credential
            startIntentSenderForResult(
                beginSignInResult.pendingIntent.intentSender,
                REQ_ONE_TAP,  /* fillInIntent= */
                null,  /* flagsMask= */
                0,  /* flagsValue= */
                0,  /* extraFlags= */
                0,  /* options= */
                null
            )
        } catch (e: SendIntentException) {
            Log.d(TAG, "Launching the PendingIntent failed because: \n", e)
            showMessage("Launching the PendingIntent failed", null)
        }
    }

    private fun handleAuthenticationException(exception: Exception) {
        // TODO: We get error (CANCELED) when there are no authorized account's found,
        //  is there a better way to communicate it through the CommonStatusCodes?
        if (exception.message == "16: Cannot find a matching credential.") {
            AlertDialog.Builder(this)
                .setTitle(R.string.auth_error_title)
                .setMessage(R.string.auth_error_message)
                .setNeutralButton(android.R.string.ok
                ) { dialog, _ -> dialog.dismiss() }
                .show()
        } else {
            showMessage("Sign-in failed with error code: ", exception)
        }
    }


    private fun buildAuthenticationRequest(isSignUp: Boolean): BeginSignInRequest {
        // In order to support credentials saved with Smart Lock for Passwords
        val passwordRequestOptions =
            PasswordRequestOptions.builder()
                .setSupported(!isSignUp)
                .build()
        val googleIdTokenRequestOptions =
            GoogleIdTokenRequestOptions.builder()
                .setServerClientId(getString(R.string.gcp_client))
                .setFilterByAuthorizedAccounts(!isSignUp)
                .setNonce("nonce")
                .setSupported(true)
                .build()
        return BeginSignInRequest.builder()
            .setPasswordRequestOptions(passwordRequestOptions)
            .setGoogleIdTokenRequestOptions(googleIdTokenRequestOptions)
            .build()
    }

    private fun showMessage(message: String, exception: Exception?) {
        val parent = findViewById<View>(R.id.container)
        Snackbar.make(parent, message, Snackbar.LENGTH_SHORT).show()
        if (exception == null) {
            Snackbar.make(parent, message, Snackbar.LENGTH_SHORT).show()
            Log.i(TAG, message)
        } else {
            Snackbar.make(parent, message + exception.message, Snackbar.LENGTH_SHORT).show()
            Log.e(TAG, message, exception)
        }
    }
}
