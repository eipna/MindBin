package com.eipna.mindbin.ui.activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.eipna.mindbin.R;
import com.eipna.mindbin.data.Database;
import com.eipna.mindbin.data.ViewMode;
import com.eipna.mindbin.data.folder.Folder;
import com.eipna.mindbin.data.folder.FolderRepository;
import com.eipna.mindbin.data.note.Note;
import com.eipna.mindbin.databinding.ActivityFolderNotesBinding;
import com.eipna.mindbin.ui.adapters.NoteAdapter;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Objects;

public class FolderNotesActivity extends BaseActivity implements NoteAdapter.Listener {

    private ActivityFolderNotesBinding binding;
    private FolderRepository folderRepository;
    private ArrayList<Folder> folders;
    private ArrayList<Note> notes;
    private NoteAdapter noteAdapter;

    private String UUIDExtra;
    private String nameExtra;
    private String descriptionExtra;
    private int isPinnedExtra;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFolderNotesBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());

        UUIDExtra = getIntent().getStringExtra(Database.COLUMN_FOLDER_ID);
        nameExtra = getIntent().getStringExtra(Database.COLUMN_FOLDER_NAME);
        descriptionExtra = getIntent().getStringExtra(Database.COLUMN_FOLDER_DESCRIPTION);
        isPinnedExtra = getIntent().getIntExtra(Database.COLUMN_FOLDER_PINNED, Folder.NOT_PINNED);

        folderRepository = new FolderRepository(this);
        folders = new ArrayList<>(folderRepository.get());

        notes = new ArrayList<>(folderRepository.getNotes(UUIDExtra));
        noteAdapter = new NoteAdapter(this, this, notes);

        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        String viewMode = preferences.getViewMode();
        if (viewMode.equals(ViewMode.LIST.value)) {
            binding.recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        } else if (viewMode.equals(ViewMode.TILES.value)) {
            binding.recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        }

        binding.emptyIndicator.setVisibility(notes.isEmpty() ? View.VISIBLE : View.GONE);
        binding.recyclerView.setAdapter(noteAdapter);

        binding.toolbar.setTitle(nameExtra);
        binding.toolbar.setSubtitle(String.format("%s notes", notes.size()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_folder, menu);
        if (isPinnedExtra == Folder.IS_PINNED) {
            menu.findItem(R.id.pin).setIcon(R.drawable.ic_filled_pin);
            menu.findItem(R.id.pin).setTitle(getString(R.string.menu_unpin));
        } else {
            menu.findItem(R.id.pin).setIcon(R.drawable.ic_outlined_pin);
            menu.findItem(R.id.pin).setTitle(getString(R.string.menu_pin));
        }
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (isPinnedExtra == Folder.IS_PINNED) {
            menu.findItem(R.id.pin).setIcon(R.drawable.ic_filled_pin);
            menu.findItem(R.id.pin).setTitle(getString(R.string.menu_unpin));
        } else {
            menu.findItem(R.id.pin).setIcon(R.drawable.ic_outlined_pin);
            menu.findItem(R.id.pin).setTitle(getString(R.string.menu_pin));
        }
        return true;
    }

    private void showEditFolderDialog() {
        View createFolderDialogView = LayoutInflater.from(this).inflate(R.layout.dialog_folder, null, false);

        TextInputLayout nameLayout = createFolderDialogView.findViewById(R.id.field_folder_name_layout);

        TextInputEditText nameInput = createFolderDialogView.findViewById(R.id.field_folder_name_input);
        TextInputEditText descriptionInput = createFolderDialogView.findViewById(R.id.field_folder_description_input);

        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this)
                .setTitle(R.string.dialog_folder_edit_title)
                .setView(createFolderDialogView)
                .setIcon(R.drawable.ic_folder_edit)
                .setNegativeButton(R.string.dialog_button_close, null)
                .setPositiveButton(R.string.dialog_button_edit, null);

        AlertDialog dialog = builder.create();
        dialog.setOnShowListener(dialogInterface -> {
            nameInput.setText(nameExtra);
            descriptionInput.setText(descriptionExtra);

            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
                String name = Objects.requireNonNull(nameInput.getText()).toString();
                String description = Objects.requireNonNull(descriptionInput.getText()).toString();

                if (name.isEmpty()) {
                    nameLayout.setError(getString(R.string.field_error_required));
                    return;
                }

                if (nameExtra.equals(name) && descriptionExtra.equals(description)) {
                    dialog.dismiss();
                    return;
                }

                if (nameExtra.equals(name)) {
                    Folder editedFolder = new Folder();
                    editedFolder.setUUID(UUIDExtra);
                    editedFolder.setDescription(description);

                    descriptionExtra = description;
                    folderRepository.editDescription(editedFolder);
                    dialog.dismiss();
                    return;
                }

                Folder editedFolder = new Folder();
                editedFolder.setUUID(UUIDExtra);
                editedFolder.setName(name);
                editedFolder.setDescription(description);

                if (folderAlreadyExist(name)) {
                    nameInput.setText("");
                    nameLayout.setError(getString(R.string.field_error_folder_exists));
                } else {
                    binding.toolbar.setTitle(name);
                    nameExtra = name;
                    descriptionExtra = description;
                    folderRepository.edit(editedFolder);
                    dialog.dismiss();
                }
            });
        });
        dialog.show();
    }

    private boolean folderAlreadyExist(String name) {
        for (Folder folder : folders) {
            if (folder.getName().trim().equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) return super.onOptionsItemSelected(item);
        if (item.getItemId() == R.id.edit) showEditFolderDialog();
        if (item.getItemId() == R.id.delete) showDeleteFolderDialog();
        if (item.getItemId() == R.id.pin) togglePin();
        return true;
    }

    private void showDeleteFolderDialog() {
        @SuppressLint("UseCompatLoadingForDrawables")
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this)
                .setTitle(R.string.dialog_folder_delete_title)
                .setMessage(R.string.dialog_folder_delete_message)
                .setIcon(R.drawable.ic_warning_filled)
                .setNegativeButton(R.string.dialog_button_close, null)
                .setPositiveButton(R.string.dialog_button_delete, (dialogInterface, i) -> {
                    folderRepository.delete(UUIDExtra);
                    setResult(RESULT_OK);
                    finish();
                });

        Dialog clearDialog = builder.create();
        clearDialog.show();
    }

    private void togglePin() {
        isPinnedExtra = (isPinnedExtra == Folder.IS_PINNED) ? Folder.NOT_PINNED : Folder.IS_PINNED;
        folderRepository.togglePin(UUIDExtra, isPinnedExtra);
        invalidateOptionsMenu();
    }

    @Override
    public void onClick(int position) {

    }
}