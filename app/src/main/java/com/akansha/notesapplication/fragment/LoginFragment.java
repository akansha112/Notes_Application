package com.akansha.notesapplication.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
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
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SignInButton googleSignInButton = view.findViewById(R.id.sign_in_button);
        googleSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInWithGoogle();
            }
        });

        // Check if the user is already logged in
        sharedPreferences = requireActivity().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        if (isLoggedIn()) {
            // If logged in, navigate to the home screen or the desired destination
            Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_homeFragment);
        }

        // Configure Google Sign-In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
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

            // Save login state in shared preferences
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(KEY_EMAIL, account.getEmail());
            editor.apply();
            getParentFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

            // Navigate to the home screen or the desired destination
            Navigation.findNavController(requireView()).navigate(R.id.action_loginFragment_to_homeFragment);

        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            Toast.makeText(requireContext(), "Google Sign-In failed", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isLoggedIn() {
        // Check if there is a saved email in shared preferences
        return sharedPreferences.contains(KEY_EMAIL);
    }
}
