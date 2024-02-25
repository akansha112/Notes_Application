package com.akansha.notesapplication.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.akansha.notesapplication.NoteAdapter;
import com.akansha.notesapplication.NotesDatabaseHelper;
import com.akansha.notesapplication.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class HomeFragment extends Fragment {

    private NoteAdapter notesAdapter;
    private NotesDatabaseHelper db;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        FloatingActionButton fabAddNote = view.findViewById(R.id.fabAddNote);

        fabAddNote.setOnClickListener(v -> {
            // Navigate from HomeFragment to AddFragment
            navigateToAddFragment();
        });

        // Database initialization
        db = new NotesDatabaseHelper(requireContext());

        // NotesAdapter initialization
        notesAdapter = new NoteAdapter(db.getAllNotes(), requireContext());

        // RecyclerView setup
        RecyclerView notesRecyclerView = view.findViewById(R.id.recyclerViewNotes);
        notesRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        notesRecyclerView.setAdapter(notesAdapter);

        return view;
    }

    private void navigateToAddFragment() {
        NavController navController = Navigation.findNavController(requireActivity(), R.id.fragmentContainerView);
        navController.navigate(R.id.action_homeFragment_to_addFragment);
    }

    @Override
    public void onResume() {
        super.onResume();
        // Refresh data in the adapter when the fragment resumes
        notesAdapter.refreshData(db.getAllNotes());
    }
}
