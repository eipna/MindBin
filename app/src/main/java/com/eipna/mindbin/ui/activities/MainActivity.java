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
import com.eipna.mindbin.data.note.NoteRepository;
import com.eipna.mindbin.databinding.ActivityMainBinding;
import com.eipna.mindbin.ui.adapters.NoteAdapter;
import com.google.android.material.shape.MaterialShapeDrawable;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private NoteRepository noteRepository;
    private NoteAdapter noteAdapter;
    private ArrayList<Note> noteList;

    public MainActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());

        Drawable drawable = MaterialShapeDrawable.createWithElevationOverlay(this);
        binding.appBar.setStatusBarForeground(drawable);
        
        setSupportActionBar(binding.toolbar);

        noteRepository = new NoteRepository(this);
        noteList = new ArrayList<>();
        noteList.addAll(noteRepository.getNotes());
        noteAdapter = new NoteAdapter(this, noteList);

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
                Note note = new Note();
                note.setTitle(resultIntent.getStringExtra(Database.COLUMN_NOTE_TITLE));
                note.setContent(result.getData().getStringExtra(Database.COLUMN_NOTE_CONTENT));
                noteRepository.create(note);

                noteList.clear();
                noteList.addAll(noteRepository.getNotes());
                noteAdapter.update(noteList);
            }
        }
    });
}