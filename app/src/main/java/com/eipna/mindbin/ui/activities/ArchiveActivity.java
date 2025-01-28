package com.eipna.mindbin.ui.activities;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.eipna.mindbin.R;
import com.eipna.mindbin.data.ViewMode;
import com.eipna.mindbin.data.note.Note;
import com.eipna.mindbin.data.note.NoteListener;
import com.eipna.mindbin.data.note.NoteRepository;
import com.eipna.mindbin.data.note.NoteSort;
import com.eipna.mindbin.data.note.NoteState;
import com.eipna.mindbin.databinding.ActivityArchiveBinding;
import com.eipna.mindbin.ui.adapters.NoteAdapter;
import com.eipna.mindbin.ui.adapters.NoteItemDecoration;
import com.google.android.material.shape.MaterialShapeDrawable;

import java.util.ArrayList;
import java.util.Objects;

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

        NoteSort selectedSort = NoteSort.getSort(sharedPreferenceUtil.getString("sort_notes", NoteSort.LAST_UPDATED_LATEST.NAME));
        noteRepository = new NoteRepository(this);
        noteList = new ArrayList<>(noteRepository.getByState(NoteState.ARCHIVE));
        noteList.sort(Objects.requireNonNull(selectedSort).ORDER);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_archive, menu);
        return true;
    }

    private final ActivityResultLauncher<Intent> updateNoteLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == RESULT_OK) {
            Intent resultIntent = result.getData();
            if (resultIntent != null) {
                Note updatedNote = resultIntent.getParcelableExtra("updated_note");
                if (updatedNote != null) {
                    NoteSort selectedSort = NoteSort.getSort(sharedPreferenceUtil.getString("sort_notes", NoteSort.LAST_UPDATED_LATEST.NAME));
                    noteRepository.update(updatedNote);
                    noteList = new ArrayList<>(noteRepository.getByState(NoteState.ARCHIVE));
                    noteList.sort(Objects.requireNonNull(selectedSort).ORDER);
                    binding.emptyIndicator.setVisibility(noteList.isEmpty() ? View.VISIBLE : View.GONE);
                    noteAdapter.update(noteList);
                }
            }
        }

        if (result.getResultCode() == RESULT_UPDATE_STATE) {
            Intent resultIntent = result.getData();
            if (resultIntent != null) {
                Note updatedNote = resultIntent.getParcelableExtra("updated_note");
                if (updatedNote != null) {
                    noteRepository.updateState(updatedNote.getID(), updatedNote.getState());
                    noteList = new ArrayList<>(noteRepository.getByState(NoteState.ARCHIVE));
                    binding.emptyIndicator.setVisibility(noteList.isEmpty() ? View.VISIBLE : View.GONE);
                    noteAdapter.update(noteList);
                }
            }
        }
    });

    @Override
    public void OnNoteClick(int position) {
        Note selectedNote = noteList.get(position);
        Intent updateNoteIntent = new Intent(getApplicationContext(), UpdateNoteActivity.class);
        updateNoteIntent.putExtra("selected_note", selectedNote);
        updateNoteLauncher.launch(updateNoteIntent);
    }

    @Override
    public void OnNoteLongClick(int position) {

    }
}