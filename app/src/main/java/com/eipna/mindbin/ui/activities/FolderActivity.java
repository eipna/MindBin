package com.eipna.mindbin.ui.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.eipna.mindbin.R;
import com.eipna.mindbin.data.Database;
import com.eipna.mindbin.data.folder.Folder;
import com.eipna.mindbin.data.folder.FolderRepository;
import com.eipna.mindbin.databinding.ActivityFolderBinding;
import com.eipna.mindbin.ui.adapters.FolderAdapter;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

public class FolderActivity extends BaseActivity implements FolderAdapter.Listener {

    private ActivityFolderBinding binding;
    private ArrayList<Folder> folders;
    private FolderRepository folderRepository;
    private FolderAdapter folderAdapter;

    private final ActivityResultLauncher<Intent> editFolderLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK) {
                    refreshList();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityFolderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        folderRepository = new FolderRepository(this);
        folders = new ArrayList<>(folderRepository.get());
        folderAdapter = new FolderAdapter(this, this, folders);

        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        binding.emptyIndicator.setVisibility(folders.isEmpty() ? View.VISIBLE : View.GONE);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(folderAdapter);

        binding.fab.setOnClickListener(v -> showCreateFolderDialog());
    }

    private void showCreateFolderDialog() {
        View createFolderDialogView = LayoutInflater.from(this).inflate(R.layout.dialog_create_folder, null, false);

        TextInputLayout nameLayout = createFolderDialogView.findViewById(R.id.field_folder_name_layout);

        TextInputEditText nameInput = createFolderDialogView.findViewById(R.id.field_folder_name_input);
        TextInputEditText descriptionInput = createFolderDialogView.findViewById(R.id.field_folder_description_input);

        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this)
                .setTitle(R.string.dialog_folder_create_title)
                .setView(createFolderDialogView)
                .setIcon(R.drawable.ic_add_folder)
                .setNegativeButton(R.string.dialog_button_close, null)
                .setPositiveButton(R.string.dialog_button_create, null);

        AlertDialog dialog = builder.create();
        dialog.setOnShowListener(dialogInterface -> dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            String name = Objects.requireNonNull(nameInput.getText()).toString();
            String description = Objects.requireNonNull(descriptionInput.getText()).toString();

            if (name.isEmpty()) {
                nameLayout.setError(getString(R.string.field_error_required));
                return;
            }

            Folder createdFolder = new Folder();
            createdFolder.setUUID(UUID.randomUUID().toString());
            createdFolder.setName(name);
            createdFolder.setDescription(description);
            createdFolder.setIsPinned(Folder.NOT_PINNED);

            if (folderRepository.create(createdFolder)) {
                refreshList();
                dialog.dismiss();
            } else {
                nameInput.setText("");
                nameLayout.setError(getString(R.string.field_error_folder_exists));
            }
        }));
        dialog.show();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void refreshList() {
        folders.clear();
        folders.addAll(folderRepository.get());
        binding.emptyIndicator.setVisibility(folders.isEmpty() ? View.VISIBLE : View.GONE);
        folderAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    @Override
    public void onClick(int position) {
        Folder selectedFolder = folders.get(position);
        Intent folderIntent = new Intent(FolderActivity.this, NotesActivity.class);
        folderIntent.putExtra(Database.COLUMN_FOLDER_ID, selectedFolder.getUUID());
        folderIntent.putExtra(Database.COLUMN_FOLDER_NAME, selectedFolder.getName());
        folderIntent.putExtra(Database.COLUMN_FOLDER_DESCRIPTION, selectedFolder.getDescription());
        folderIntent.putExtra(Database.COLUMN_FOLDER_PINNED, selectedFolder.getIsPinned());
        editFolderLauncher.launch(folderIntent);
    }
}