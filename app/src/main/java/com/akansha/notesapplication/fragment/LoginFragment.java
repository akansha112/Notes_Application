package com.akansha.notesapplication.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.akansha.notesapplication.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

public class LoginFragment extends Fragment {

    private static final String PREF_NAME = "user_prefs";
    private static final String KEY_EMAIL = "email";
    private static final int RC_SIGN_IN = 9001;

    private SharedPreferences sharedPreferences;
    private GoogleSignInClient googleSignInClient;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("LoginFragment", "onCreateView");
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d("LoginFragment", "onViewCreated");

        SignInButton googleSignInButton = view.findViewById(R.id.sign_in_button);
        googleSignInButton.setOnClickListener(v -> {
            if (!isLoggedIn()) {
                // User is not logged in, initiate Google Sign-In
                signInWithGoogle();
            }
            // else: User is already logged in, no need to sign in again
        });

        // Check if the user is already logged in
        sharedPreferences = requireActivity().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        if (isLoggedIn()) {
            // If logged in, navigate to the home screen or the desired destination
            navigateToHomeFragment();
        }

        // Configure Google Sign-In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.web_client_id))
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso);
    }

    private void signInWithGoogle() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            Log.d("Navigation", "Handling sign-in result. Navigating to HomeFragment.");

            // Save login state in shared preferences
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(KEY_EMAIL, account.getEmail());
            editor.apply();

            // Navigate to the home screen
            navigateToHomeFragment();



        } catch (ApiException e) {
            Log.e("GoogleSignIn", "Google Sign-In failed: " + e.getStatusCode(), e);
        }
    }

    private void navigateToHomeFragment() {
        NavController navController = Navigation.findNavController(requireView());


        // Navigate to the home screen or the desired destination
        navController.navigate(R.id.action_loginFragment_to_homeFragment);

    }
    private boolean isLoggedIn() {
        // Check if there is a saved email in shared preferences
        String savedEmail = sharedPreferences.getString(KEY_EMAIL, "");
        return !savedEmail.isEmpty();
    }
}
