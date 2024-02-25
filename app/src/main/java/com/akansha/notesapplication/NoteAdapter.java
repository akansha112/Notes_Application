package com.akansha.notesapplication;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {

    private List<NotesModel> notes;
    NotesDatabaseHelper db;

    public NoteAdapter(List<NotesModel> notes, Context context) {
        this.notes = notes;
        db = new NotesDatabaseHelper(context); // Initialize in the constructor
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_item, parent, false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        NotesModel note = notes.get(position);
        holder.titleTextView.setText(note.getTitle());
        holder.contentTextView.setText(note.getContent());
        holder.updateButton.setOnClickListener(v -> {
            // Get the note ID from the clicked item
            int noteId = notes.get(holder.getAdapterPosition()).getId();

            // Get the NavController
            NavController navController = Navigation.findNavController(holder.itemView);

            // Navigate to the UpdateFragment and pass the note ID as an argument
            Bundle bundle = new Bundle();
            bundle.putInt("note_id", noteId);
            navController.navigate(R.id.action_homeFragment_to_updateFragment, bundle);
        });

        holder.deleteButton.setOnClickListener(v -> {
            db.deleteNote(note.getId());
            refreshData(db.getAllNotes());
            Toast.makeText(holder.itemView.getContext(),"Note Deleted", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }
    public void refreshData(List<NotesModel> newNotes) {
        notes = newNotes;
        notifyDataSetChanged();
    }

    static class NoteViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        TextView contentTextView;
       ImageView updateButton;
       ImageView deleteButton;

        NoteViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            contentTextView = itemView.findViewById(R.id.contentTextView);
             updateButton=itemView.findViewById(R.id.updateButton);
             deleteButton=itemView.findViewById(R.id.deleteButton);
        }
    }
}
