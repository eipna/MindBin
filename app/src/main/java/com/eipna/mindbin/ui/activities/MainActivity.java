package com.eipna.mindbin.ui.activities;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.eipna.mindbin.data.Database;
import com.eipna.mindbin.data.note.Note;
import com.eipna.mindbin.data.note.NoteListener;
import com.eipna.mindbin.databinding.ActivityMainBinding;
import com.eipna.mindbin.ui.adapters.NoteAdapter;
import com.google.android.material.shape.MaterialShapeDrawable;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NoteListener {

    private ActivityMainBinding binding;
    private Database database;
    private NoteAdapter noteAdapter;
    private ArrayList<Note> noteList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());

        Drawable drawable = MaterialShapeDrawable.createWithElevationOverlay(this);
        binding.appBar.setStatusBarForeground(drawable);
        setSupportActionBar(binding.toolbar);
        database = new Database(this);

        noteList = new ArrayList<>(database.getNotes());
        noteAdapter = new NoteAdapter(this, this, noteList);

        binding.noteList.setLayoutManager(new LinearLayoutManager(this));
        binding.noteList.setAdapter(noteAdapter);

        binding.newNote.setOnClickListener(view -> {
            Intent createNoteIntent = new Intent(getApplicationContext(), CreateNoteActivity.class);
            createNoteLauncher.launch(createNoteIntent);
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    private final ActivityResultLauncher<Intent> createNoteLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == RESULT_OK) {
            Intent resultIntent = result.getData();
            if (resultIntent != null) {
                Note createdNote = new Note();
                createdNote.setTitle(resultIntent.getStringExtra(Database.COLUMN_NOTE_TITLE));
                createdNote.setContent(result.getData().getStringExtra(Database.COLUMN_NOTE_CONTENT));
                database.createNote(createdNote);
                noteList = new ArrayList<>(database.getNotes());
                noteAdapter.update(noteList);
            }
        }
    });

    private final ActivityResultLauncher<Intent> updateNoteLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == RESULT_OK) {
            Intent resultIntent = result.getData();
            if (resultIntent != null) {
                Note updatedNote = new Note();
                updatedNote.setID(resultIntent.getIntExtra(Database.COLUMN_NOTE_ID, -1));
                updatedNote.setTitle(resultIntent.getStringExtra(Database.COLUMN_NOTE_TITLE));
                updatedNote.setContent(resultIntent.getStringExtra(Database.COLUMN_NOTE_CONTENT));
                database.updateNote(updatedNote);
                noteList = new ArrayList<>(database.getNotes());
                noteAdapter.update(noteList);
            }
        }
    });

    @Override
    public void OnNoteClick(int position) {
        Note selectedNote = noteList.get(position);
        Intent updateNoteIntent = new Intent(getApplicationContext(), UpdateNoteActivity.class);
        updateNoteIntent.putExtra(Database.COLUMN_NOTE_ID, selectedNote.getID());
        updateNoteIntent.putExtra(Database.COLUMN_NOTE_TITLE, selectedNote.getTitle());
        updateNoteIntent.putExtra(Database.COLUMN_NOTE_CONTENT, selectedNote.getContent());
        updateNoteLauncher.launch(updateNoteIntent);
    }

    @Override
    public void OnNoteLongClick(int position) {
        // Do action mode operations
    }
}