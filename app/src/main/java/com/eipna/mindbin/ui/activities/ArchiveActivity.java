package com.eipna.mindbin.ui.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.eipna.mindbin.data.ViewMode;
import com.eipna.mindbin.data.note.Note;
import com.eipna.mindbin.data.note.NoteRepository;
import com.eipna.mindbin.data.note.NoteState;
import com.eipna.mindbin.databinding.ActivityArchiveBinding;
import com.eipna.mindbin.ui.adapters.NoteAdapter;
import com.eipna.mindbin.ui.adapters.NoteItemDecoration;

import java.util.ArrayList;

public class ArchiveActivity extends BaseActivity implements NoteAdapter.Listener {

    private ActivityArchiveBinding binding;
    private NoteRepository noteRepository;
    private ArrayList<Note> noteList;
    private NoteAdapter noteAdapter;

    private final ActivityResultLauncher<Intent> editNoteLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK) {
                    refreshList();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityArchiveBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        noteRepository = new NoteRepository(this);
        noteList = new ArrayList<>(noteRepository.getByState(NoteState.ARCHIVE));
        noteAdapter = new NoteAdapter(this, this, noteList);
        binding.emptyIndicator.setVisibility(noteList.isEmpty() ? View.VISIBLE : View.GONE);

        String viewMode = preferences.getViewMode();
        if (viewMode.equals(ViewMode.LIST.value)) {
            binding.noteList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        } else if (viewMode.equals(ViewMode.TILES.value)) {
            binding.noteList.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        }

        binding.noteList.addItemDecoration(new NoteItemDecoration(32));
        binding.noteList.setAdapter(noteAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    @SuppressLint("NotifyDataSetChanged")
    private void refreshList() {
        noteList.clear();
        noteList.addAll(noteRepository.getByState(NoteState.ARCHIVE));
        binding.emptyIndicator.setVisibility(noteList.isEmpty() ? View.VISIBLE : View.GONE);
        noteAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(int position) {
        Note selectedNote = noteList.get(position);
        Intent editNoteIntent = new Intent(getApplicationContext(), EditNoteActivity.class);
        editNoteIntent.putExtra("selected_note", selectedNote);
        editNoteLauncher.launch(editNoteIntent);
    }
}