package com.eipna.mindbin.ui.activities;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.eipna.mindbin.R;
import com.eipna.mindbin.data.MindBinDatabase;
import com.eipna.mindbin.data.note.Note;
import com.eipna.mindbin.data.note.NoteListener;
import com.eipna.mindbin.data.note.NoteRepository;
import com.eipna.mindbin.databinding.ActivityMainBinding;
import com.eipna.mindbin.ui.adapters.NoteAdapter;
import com.eipna.mindbin.ui.adapters.NoteItemDecoration;
import com.eipna.mindbin.util.SharedPreferenceUtil;
import com.google.android.material.color.DynamicColors;
import com.google.android.material.shape.MaterialShapeDrawable;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NoteListener {

    private ActivityMainBinding binding;
    private NoteRepository noteRepository;
    private NoteAdapter noteAdapter;
    private ArrayList<Note> noteList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferenceUtil sharedPreferenceUtil = new SharedPreferenceUtil(this);
        boolean isDynamicColorsAvailable = sharedPreferenceUtil.getBoolean("dynamic_colors", false);
        if (isDynamicColorsAvailable) DynamicColors.applyToActivityIfAvailable(this);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());

        Drawable drawable = MaterialShapeDrawable.createWithElevationOverlay(this);
        binding.appBar.setStatusBarForeground(drawable);
        setSupportActionBar(binding.toolbar);
        noteRepository = new NoteRepository(this);

        noteList = new ArrayList<>(noteRepository.getNotes());
        noteAdapter = new NoteAdapter(this, this, noteList);

        binding.emptyIndicator.setVisibility(noteList.isEmpty() ? View.VISIBLE : View.GONE);
        binding.noteList.setLayoutManager(new LinearLayoutManager(this));
        binding.noteList.addItemDecoration(new NoteItemDecoration(16));
        binding.noteList.setAdapter(noteAdapter);

        binding.newNote.setOnClickListener(view -> {
            Intent createNoteIntent = new Intent(getApplicationContext(), CreateNoteActivity.class);
            createNoteLauncher.launch(createNoteIntent);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.settings) startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
        if (item.getItemId() == R.id.about) startActivity(new Intent(getApplicationContext(), AboutActivity.class));
        if (item.getItemId() == R.id.folder) startActivity(new Intent(getApplicationContext(), FolderActivity.class));
        if (item.getItemId() == R.id.archive) startActivity(new Intent(getApplicationContext(), ArchiveActivity.class));
        if (item.getItemId() == R.id.trash) startActivity(new Intent(getApplicationContext(), TrashActivity.class));
        return true;
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
                createdNote.setTitle(resultIntent.getStringExtra(MindBinDatabase.COLUMN_NOTE_TITLE));
                createdNote.setContent(result.getData().getStringExtra(MindBinDatabase.COLUMN_NOTE_CONTENT));
                noteRepository.create(createdNote);
                noteList = new ArrayList<>(noteRepository.getNotes());
                noteAdapter.update(noteList);
            }
        }
    });

    private final ActivityResultLauncher<Intent> updateNoteLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == RESULT_OK) {
            Intent resultIntent = result.getData();
            if (resultIntent != null) {
                Note updatedNote = new Note();
                updatedNote.setID(resultIntent.getIntExtra(MindBinDatabase.COLUMN_NOTE_ID, -1));
                updatedNote.setTitle(resultIntent.getStringExtra(MindBinDatabase.COLUMN_NOTE_TITLE));
                updatedNote.setContent(resultIntent.getStringExtra(MindBinDatabase.COLUMN_NOTE_CONTENT));
                noteRepository.update(updatedNote);
                noteList = new ArrayList<>(noteRepository.getNotes());
                noteAdapter.update(noteList);
            }
        }
    });

    @Override
    public void OnNoteClick(int position) {
        Note selectedNote = noteList.get(position);
        Intent updateNoteIntent = new Intent(getApplicationContext(), UpdateNoteActivity.class);
        updateNoteIntent.putExtra(MindBinDatabase.COLUMN_NOTE_ID, selectedNote.getID());
        updateNoteIntent.putExtra(MindBinDatabase.COLUMN_NOTE_TITLE, selectedNote.getTitle());
        updateNoteIntent.putExtra(MindBinDatabase.COLUMN_NOTE_CONTENT, selectedNote.getContent());
        updateNoteLauncher.launch(updateNoteIntent);
    }

    @Override
    public void OnNoteLongClick(int position) {
        // Do action mode operations
    }
}