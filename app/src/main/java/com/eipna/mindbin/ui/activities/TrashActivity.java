package com.eipna.mindbin.ui.activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.eipna.mindbin.R;
import com.eipna.mindbin.data.ViewMode;
import com.eipna.mindbin.data.note.Note;
import com.eipna.mindbin.data.note.NoteListener;
import com.eipna.mindbin.data.note.NoteRepository;
import com.eipna.mindbin.data.note.NoteState;
import com.eipna.mindbin.databinding.ActivityTrashBinding;
import com.eipna.mindbin.ui.adapters.NoteAdapter;
import com.eipna.mindbin.ui.adapters.NoteItemDecoration;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.shape.MaterialShapeDrawable;

import java.util.ArrayList;

public class TrashActivity extends BaseActivity implements NoteListener {

    private ActivityTrashBinding binding;
    private NoteRepository noteRepository;
    private ArrayList<Note> noteList;
    private NoteAdapter noteAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityTrashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Drawable drawable = MaterialShapeDrawable.createWithElevationOverlay(this);
        binding.appBar.setStatusBarForeground(drawable);

        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        noteRepository = new NoteRepository(this);
        noteList = new ArrayList<>(noteRepository.getByState(NoteState.TRASH));
        noteAdapter = new NoteAdapter(this, this, noteList);
        binding.emptyIndicator.setVisibility(noteList.isEmpty() ? View.VISIBLE : View.GONE);

        String viewMode = preferences.getViewMode();
        if (viewMode.equals(ViewMode.LIST.value)) {
            binding.noteList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        } else if (viewMode.equals(ViewMode.TILES.value)) {
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
        inflater.inflate(R.menu.menu_trash, menu);
        menu.findItem(R.id.clear).setVisible(!noteList.isEmpty());
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.clear).setVisible(!noteList.isEmpty());
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.clear) showClearDialog();
        if (item.getItemId() == android.R.id.home) return super.onOptionsItemSelected(item);
        return true;
    }

    private void showClearDialog() {
        @SuppressLint("UseCompatLoadingForDrawables")
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this)
                .setTitle(R.string.dialog_note_clear_all_title)
                .setMessage(R.string.dialog_note_clear_all_message)
                .setIcon(R.drawable.ic_warning_filled)
                .setNegativeButton(R.string.dialog_button_close, null)
                .setPositiveButton(R.string.dialog_button_clear, (dialogInterface, i) -> {
                    noteRepository.clearByState(NoteState.TRASH);
                    noteList = new ArrayList<>(noteRepository.getByState(NoteState.TRASH));
                    noteAdapter.update(noteList);
                    binding.emptyIndicator.setVisibility(noteList.isEmpty() ? View.VISIBLE : View.GONE);
                    invalidateOptionsMenu();
                });

        Dialog clearDialog = builder.create();
        clearDialog.show();
    }

    private final ActivityResultLauncher<Intent> updateNoteLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == RESULT_OK) {
            Intent resultIntent = result.getData();
            if (resultIntent != null) {
                Note updatedNote = resultIntent.getParcelableExtra("updated_note");
                if (updatedNote != null) {
                    noteRepository.update(updatedNote);
                    noteList = new ArrayList<>(noteRepository.getByState(NoteState.TRASH));
                    noteAdapter.update(noteList);

                    binding.emptyIndicator.setVisibility(noteList.isEmpty() ? View.VISIBLE : View.GONE);
                    invalidateOptionsMenu();
                }
            }
        }

        if (result.getResultCode() == RESULT_DELETE) {
            Intent deleteIntent = result.getData();
            if (deleteIntent != null) {
                Note deletedNote = deleteIntent.getParcelableExtra("deleted_note");
                if (deletedNote != null) {
                    noteRepository.delete(deletedNote);
                    noteList = new ArrayList<>(noteRepository.getByState(NoteState.TRASH));
                    noteAdapter.update(noteList);

                    binding.emptyIndicator.setVisibility(noteList.isEmpty() ? View.VISIBLE : View.GONE);
                    invalidateOptionsMenu();
                }
            }
        }

        if (result.getResultCode() == RESULT_UPDATE_STATE) {
            Intent resultIntent = result.getData();
            if (resultIntent != null) {
                Note updatedNote = resultIntent.getParcelableExtra("updated_note");
                if (updatedNote != null) {
                    noteRepository.updateState(updatedNote.getID(), updatedNote.getState());
                    noteList = new ArrayList<>(noteRepository.getByState(NoteState.TRASH));
                    noteAdapter.update(noteList);

                    binding.emptyIndicator.setVisibility(noteList.isEmpty() ? View.VISIBLE : View.GONE);
                    invalidateOptionsMenu();
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