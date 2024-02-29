package com.akansha.notesapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navController = Navigation.findNavController(this, R.id.fragmentContainerView);
    }


    @Override
    public void onBackPressed() {
        // If there's anything on the back stack, pop it
        if (!navController.popBackStack()) {
            finish();
            // If the back stack is empty, finish the activity
            super.onBackPressed();
        }
    }

}
