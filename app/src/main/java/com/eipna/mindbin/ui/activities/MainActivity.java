package com.eipna.mindbin.ui.activities;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.eipna.mindbin.R;
import com.eipna.mindbin.data.ViewMode;
import com.eipna.mindbin.data.note.Note;
import com.eipna.mindbin.data.note.NoteListener;
import com.eipna.mindbin.data.note.NoteRepository;
import com.eipna.mindbin.data.note.NoteSort;
import com.eipna.mindbin.data.note.NoteState;
import com.eipna.mindbin.databinding.ActivityMainBinding;
import com.eipna.mindbin.ui.adapters.NoteAdapter;
import com.eipna.mindbin.ui.adapters.NoteItemDecoration;
import com.google.android.material.shape.MaterialShapeDrawable;

import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends BaseActivity implements NoteListener {

    private ActivityMainBinding binding;
    private NoteRepository noteRepository;
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
        noteRepository = new NoteRepository(this);

        NoteSort selectedSort = NoteSort.getSort(sharedPreferenceUtil.getString("sort_notes_normal", NoteSort.LAST_UPDATED_LATEST.NAME));
        noteList = new ArrayList<>(noteRepository.getByState(NoteState.NORMAL));
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

        binding.newNote.setOnClickListener(view -> {
            Intent createNoteIntent = new Intent(getApplicationContext(), CreateNoteActivity.class);
            createNoteLauncher.launch(createNoteIntent);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        MenuItem searchItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        assert searchView != null;
        searchView.setQueryHint(getString(R.string.menu_search_note));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                searchNote(query);
                return true;
            }
        });

        String selectedSort = sharedPreferenceUtil.getString("sort_notes_normal", NoteSort.LAST_UPDATED_LATEST.NAME);
        if (selectedSort.equals(NoteSort.TITLE_ASCENDING.NAME)) {
            menu.findItem(R.id.sort_title_asc).setChecked(true);
        } else if (selectedSort.equals(NoteSort.TITLE_DESCENDING.NAME)) {
            menu.findItem(R.id.sort_title_desc).setChecked(true);
        } else if (selectedSort.equals(NoteSort.DATE_CREATED_LATEST.NAME)) {
            menu.findItem(R.id.sort_created_latest).setChecked(true);
        } else if (selectedSort.equals(NoteSort.DATE_CREATED_OLDEST.NAME)) {
            menu.findItem(R.id.sort_created_oldest).setChecked(true);
        } else if (selectedSort.equals(NoteSort.LAST_UPDATED_LATEST.NAME)) {
            menu.findItem(R.id.sort_updated_latest).setChecked(true);
        } else if (selectedSort.equals(NoteSort.LAST_UPDATED_OLDEST.NAME)) {
            menu.findItem(R.id.sort_updated_oldest).setChecked(true);
        }
        return true;
    }

    private void searchNote(String searchQuery) {
        ArrayList<Note> queriedNotes = new ArrayList<>();
        for (Note note : noteList) {
            if (note.getTitle().toLowerCase().contains(searchQuery.toLowerCase())) {
                queriedNotes.add(note);
            }
        }
        noteAdapter.search(queriedNotes);
        binding.emptyIndicator.setVisibility(queriedNotes.isEmpty() ? View.VISIBLE : View.GONE);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.settings) startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
        if (item.getItemId() == R.id.folder) startActivity(new Intent(getApplicationContext(), FolderActivity.class));
        if (item.getItemId() == R.id.archive) startActivity(new Intent(getApplicationContext(), ArchiveActivity.class));
        if (item.getItemId() == R.id.trash) startActivity(new Intent(getApplicationContext(), TrashActivity.class));

        if (item.getItemId() == R.id.sort_title_asc) {
            item.setChecked(true);
            sortNotes(NoteSort.TITLE_ASCENDING);
        }

        if (item.getItemId() == R.id.sort_title_desc) {
            item.setChecked(true);
            sortNotes(NoteSort.TITLE_DESCENDING);
        }

        if (item.getItemId() == R.id.sort_created_latest) {
            item.setChecked(true);
            sortNotes(NoteSort.DATE_CREATED_LATEST);
        }

        if (item.getItemId() == R.id.sort_created_oldest) {
            item.setChecked(true);
            sortNotes(NoteSort.DATE_CREATED_OLDEST);
        }

        if (item.getItemId() == R.id.sort_updated_latest) {
            item.setChecked(true);
            sortNotes(NoteSort.LAST_UPDATED_LATEST);
        }

        if (item.getItemId() == R.id.sort_updated_oldest) {
            item.setChecked(true);
            sortNotes(NoteSort.LAST_UPDATED_OLDEST);
        }
        return true;
    }

    private void sortNotes(NoteSort sort) {
        if (noteList.isEmpty()) {
            Toast.makeText(this, getString(R.string.toast_decline_sort), Toast.LENGTH_SHORT).show();
        } else {
            ArrayList<Note> sortedList = new ArrayList<>(noteList);
            sortedList.sort(sort.ORDER);
            noteAdapter.update(sortedList);
            sharedPreferenceUtil.setString("sort_notes_normal", sort.NAME);
        }
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
                Note createdNote = resultIntent.getParcelableExtra("created_note");
                if (createdNote != null) {
                    noteRepository.create(createdNote);
                    NoteSort selectedSort = NoteSort.getSort(sharedPreferenceUtil.getString("sort_notes_normal", NoteSort.LAST_UPDATED_LATEST.NAME));
                    noteList = new ArrayList<>(noteRepository.getByState(NoteState.NORMAL));
                    noteList.sort(Objects.requireNonNull(selectedSort).ORDER);
                    binding.emptyIndicator.setVisibility(noteList.isEmpty() ? View.VISIBLE : View.GONE);
                    noteAdapter.update(noteList);
                }
            }
        }
    });

    private final ActivityResultLauncher<Intent> updateNoteLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == RESULT_OK) {
            Intent resultIntent = result.getData();
            if (resultIntent != null) {
                Note updatedNote = resultIntent.getParcelableExtra("updated_note");
                if (updatedNote != null) {
                    noteRepository.update(updatedNote);
                    NoteSort selectedSort = NoteSort.getSort(sharedPreferenceUtil.getString("sort_notes_normal", NoteSort.LAST_UPDATED_LATEST.NAME));
                    noteList = new ArrayList<>(noteRepository.getByState(NoteState.NORMAL));
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
                    NoteSort selectedSort = NoteSort.getSort(sharedPreferenceUtil.getString("sort_notes_normal", NoteSort.LAST_UPDATED_LATEST.NAME));
                    noteList = new ArrayList<>(noteRepository.getByState(NoteState.NORMAL));
                    noteList.sort(Objects.requireNonNull(selectedSort).ORDER);
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
        // Do action mode operations
    }
}