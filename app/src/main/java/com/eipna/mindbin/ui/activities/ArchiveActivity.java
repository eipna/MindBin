package com.eipna.mindbin.ui.activities;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.eipna.mindbin.data.MindBinDatabase;
import com.eipna.mindbin.data.ViewMode;
import com.eipna.mindbin.data.note.Note;
import com.eipna.mindbin.data.note.NoteListener;
import com.eipna.mindbin.data.note.NoteRepository;
import com.eipna.mindbin.data.note.NoteState;
import com.eipna.mindbin.databinding.ActivityArchiveBinding;
import com.eipna.mindbin.ui.adapters.NoteAdapter;
import com.eipna.mindbin.ui.adapters.NoteItemDecoration;
import com.google.android.material.shape.MaterialShapeDrawable;

import java.util.ArrayList;

public class ArchiveActivity extends BaseActivity implements NoteListener {

    private ActivityArchiveBinding binding;
    private NoteRepository noteRepository;
    private ArrayList<Note> noteList;
    private NoteAdapter noteAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityArchiveBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Drawable drawable = MaterialShapeDrawable.createWithElevationOverlay(this);
        binding.appBar.setStatusBarForeground(drawable);

        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        noteRepository = new NoteRepository(this);
        noteList = new ArrayList<>(noteRepository.getByState(NoteState.ARCHIVE));
        noteAdapter = new NoteAdapter(this, this, noteList);
        binding.emptyIndicator.setVisibility(noteList.isEmpty() ? View.VISIBLE : View.GONE);

        String selectedViewMode = sharedPreferenceUtil.getString("view_mode", ViewMode.LIST.value);
        if (selectedViewMode.equals(ViewMode.LIST.value)) {
            binding.noteList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        } else if (selectedViewMode.equals(ViewMode.TILES.value)) {
            binding.noteList.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        }

        binding.noteList.addItemDecoration(new NoteItemDecoration(16));
        binding.noteList.setAdapter(noteAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    private final ActivityResultLauncher<Intent> updateNoteLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == RESULT_OK) {
            Intent resultIntent = result.getData();
            if (resultIntent != null) {
                Note updatedNote = new Note();
                updatedNote.setID(resultIntent.getIntExtra(MindBinDatabase.COLUMN_NOTE_ID, -1));
                updatedNote.setTitle(resultIntent.getStringExtra(MindBinDatabase.COLUMN_NOTE_TITLE));
                updatedNote.setContent(resultIntent.getStringExtra(MindBinDatabase.COLUMN_NOTE_CONTENT));
                updatedNote.setLastUpdated(resultIntent.getLongExtra(MindBinDatabase.COLUMN_NOTE_LAST_UPDATED, -1));
                updatedNote.setState(resultIntent.getIntExtra(MindBinDatabase.COLUMN_NOTE_STATE, -2));
                noteRepository.update(updatedNote);
                noteList = new ArrayList<>(noteRepository.getByState(NoteState.ARCHIVE));
                binding.emptyIndicator.setVisibility(noteList.isEmpty() ? View.VISIBLE : View.GONE);
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
        updateNoteIntent.putExtra(MindBinDatabase.COLUMN_NOTE_DATE_CREATED, selectedNote.getDateCreated());
        updateNoteIntent.putExtra(MindBinDatabase.COLUMN_NOTE_LAST_UPDATED, selectedNote.getLastUpdated());
        updateNoteIntent.putExtra(MindBinDatabase.COLUMN_NOTE_STATE, selectedNote.getState());
        updateNoteLauncher.launch(updateNoteIntent);
    }

    @Override
    public void OnNoteLongClick(int position) {

    }
}