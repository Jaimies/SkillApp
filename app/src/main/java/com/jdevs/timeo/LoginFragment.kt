package com.jdevs.timeo

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInStatusCodes.SIGN_IN_CANCELLED
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.GoogleAuthProvider
import com.jdevs.timeo.databinding.FragmentLoginBinding
import com.jdevs.timeo.util.RC_SIGN_IN
import com.jdevs.timeo.util.TAG
import com.jdevs.timeo.util.hideKeyboard
import com.jdevs.timeo.util.isValidEmail
import com.jdevs.timeo.viewmodels.LoginViewModel

class LoginFragment : Fragment(),
    OnCompleteListener<AuthResult>,
    LoginViewModel.Navigator {

    private val auth by lazy { FirebaseAuth.getInstance() }

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(LoginViewModel::class.java)
    }

    private val googleSignInClient by lazy {
        val googleSignInOptions = GoogleSignInOptions.Builder()
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        GoogleSignIn.getClient(context!!, googleSignInOptions)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentLoginBinding.inflate(inflater, container, false)

        binding.also {

            it.viewmodel = viewModel.also { viewModel ->
                viewModel.navigator = this@LoginFragment
            }

            it.lifecycleOwner = this@LoginFragment
        }

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.navigator = null
    }

    override fun hideKeyboard() {
        hideKeyboard(activity)
    }

    override fun signIn(email: String, password: String) {
        when {
            email.isEmpty() -> viewModel.setEmailError("Email must not be empty")
            !email.isValidEmail() -> viewModel.setEmailError("Email is invalid")
            password.isEmpty() -> viewModel.setPasswordError("Password must not be empty")

            else -> {
                if (auth.currentUser?.isAnonymous == true) {
                    auth.currentUser?.delete()?.addOnFailureListener { exception ->
                        Log.w(TAG, "Failed to delete user data", exception)
                    }
                }

                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this)

                hideKeyboard()
                viewModel.showLoader()
            }
        }
    }

    override fun signInWithGoogle() {
        hideKeyboard()
        viewModel.showLoader()

        googleSignInClient.signOut()
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)

        return
    }

    override fun onComplete(task: Task<AuthResult>) {

        if (task.isSuccessful) {
            findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
            return
        }

        viewModel.hideLoader()

        when (task.exception) {
            is FirebaseAuthInvalidCredentialsException -> {
                viewModel.setPasswordError("Username and password do not match")
            }

            is FirebaseAuthInvalidUserException -> {
                viewModel.setEmailError("User with that email does not exist")
            }

            is FirebaseNetworkException -> {
                Snackbar.make(view!!, "Please check your Internet connection", Snackbar.LENGTH_LONG)
                    .show()
            }

            else -> {
                Log.w(TAG, "Failed to sign in", task.exception)
                Snackbar.make(view!!, "Failed to sign in", Snackbar.LENGTH_LONG).show()
            }
        }
    }

    override fun navigateToSignup() {
        findNavController().navigate(R.id.action_loginFragment_to_signupFragment)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        viewModel.hideLoader()

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)

            try {
                val account = task.getResult(ApiException::class.java) ?: return
                firebaseLinkGoogleAccount(account)
            } catch (e: ApiException) {
                if (e.statusCode == SIGN_IN_CANCELLED) {
                    Log.i(TAG, "Sign in was cancelled by user")
                    return
                }

                Log.w(TAG, "Google sign in failed", e)
                Snackbar.make(view!!, "Failed to sign in with Google", Snackbar.LENGTH_LONG).show()
            }
        }
    }

    private fun firebaseLinkGoogleAccount(account: GoogleSignInAccount) {

        val credential = GoogleAuthProvider.getCredential(account.idToken, null)

        hideKeyboard()
        viewModel.showLoader()

        val user = auth.currentUser

        if (user != null && user.isAnonymous) {

            user.linkWithCredential(credential)
                .addOnCompleteListener {
                    viewModel.hideLoader()
                }
                .addOnSuccessListener {
                    findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
                }
                .addOnFailureListener { exception ->

                    if (exception is FirebaseAuthUserCollisionException) {
                        firebaseAuthWithGoogle(credential)
                        return@addOnFailureListener
                    }

                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", exception)
                    Snackbar.make(view!!, "Authentication Failed", Snackbar.LENGTH_SHORT).show()
                }
        } else {
            firebaseAuthWithGoogle(credential)
        }
    }

    private fun firebaseAuthWithGoogle(credential: AuthCredential) {

        viewModel.showLoader()
        hideKeyboard()

        if (auth.currentUser?.isAnonymous == true) {

            auth.currentUser?.delete()?.addOnFailureListener { exception ->
                Log.w(TAG, "Failed to delete anonymous user", exception)
            }
        }

        auth.signInWithCredential(credential)
            .addOnCompleteListener {

                viewModel.hideLoader()
            }
            .addOnSuccessListener {

                findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
            }
            .addOnFailureListener { exception ->

                Log.w(TAG, "signInWithCredential:failure", exception)
                Snackbar.make(view!!, "Authentication Failed", Snackbar.LENGTH_SHORT).show()
            }
    }
}
