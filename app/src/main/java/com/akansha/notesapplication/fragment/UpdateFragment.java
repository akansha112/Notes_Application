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

public class UpdateFragment extends Fragment {

    private NotesDatabaseHelper db;
    private int noteId = -1;

    public UpdateFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_update, container, false);

        db = new NotesDatabaseHelper(requireContext());

        Bundle bundle = getArguments();
        if (bundle != null) {
            noteId = bundle.getInt("note_id", -1);
        }

        if (noteId == -1) {
            requireActivity().finish();
            return view;
        }

        NotesModel note = db.getNoteById(noteId);

        EditText updateTitleEditText = view.findViewById(R.id.updateTitleEditText);
        EditText updateContentEditText = view.findViewById(R.id.updateContentEditText);
        ImageView updateSaveButton = view.findViewById(R.id.updateSaveButton);

        updateTitleEditText.setText(note.getTitle());
        updateContentEditText.setText(note.getContent());

        updateSaveButton.setOnClickListener(v -> {
            String newTitle = updateTitleEditText.getText().toString();
            String newContent = updateContentEditText.getText().toString();

            NotesModel updatedNote = new NotesModel(noteId, newTitle, newContent);
            db.updateNote(updatedNote);

            // Use NavController to navigate back to the home fragment
            NavController navController = Navigation.findNavController(requireActivity(), R.id.fragmentContainerView);
            navController.navigate(R.id.action_updateFragment_to_homeFragment);
            Toast.makeText(requireContext(), "Changes Saved", Toast.LENGTH_SHORT).show();
        });

        return view;
    }
}
