package com.akansha.notesapplication.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.akansha.notesapplication.NotesDatabaseHelper;
import com.akansha.notesapplication.NotesModel;
import com.akansha.notesapplication.R;

public class AddFragment extends Fragment {

    private NotesDatabaseHelper db;
    private EditText titleEditText;
    private EditText contentEditText;
    private ImageView saveButton;

    public AddFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add, container, false);

        db = new NotesDatabaseHelper(requireContext());

        titleEditText = view.findViewById(R.id.titleEditText);
        contentEditText = view.findViewById(R.id.ContentEditText);
        saveButton = view.findViewById(R.id.saveButton);

        saveButton.setOnClickListener(v -> {
            String title = titleEditText.getText().toString();
            String content = contentEditText.getText().toString();
            NotesModel note = new NotesModel(0, title, content);
            db.insertNote(note);

            // Navigate to home fragment
            navigateToHomeFragment();

            Toast.makeText(requireContext(), "Note Saved", Toast.LENGTH_SHORT).show();
        });

        return view;
    }

    private void navigateToHomeFragment() {
        NavController navController = Navigation.findNavController(requireActivity(), R.id.fragmentContainerView);
        navController.navigate(R.id.action_addFragment_to_homeFragment);
    }
}
