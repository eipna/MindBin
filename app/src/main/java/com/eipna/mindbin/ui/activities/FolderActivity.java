package com.eipna.mindbin.ui.activities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;

import com.eipna.mindbin.R;
import com.eipna.mindbin.databinding.ActivityFolderBinding;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class FolderActivity extends BaseActivity {

    private ActivityFolderBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityFolderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        binding.fab.setOnClickListener(v -> showCreateFolderDialog());
    }

    private void showCreateFolderDialog() {
        View createFolderDialogView = LayoutInflater.from(this).inflate(R.layout.dialog_create_folder, null, false);

        TextInputLayout nameLayout = createFolderDialogView.findViewById(R.id.field_folder_name_layout);
        TextInputLayout descriptionLayout = createFolderDialogView.findViewById(R.id.field_folder_description_layout);

        TextInputEditText nameInput = createFolderDialogView.findViewById(R.id.field_folder_name_input);
        TextInputEditText descriptionInput = createFolderDialogView.findViewById(R.id.field_folder_description_input);

        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this)
                .setTitle(R.string.dialog_folder_create_title)
                .setView(createFolderDialogView)
                .setIcon(R.drawable.ic_add_folder)
                .setNegativeButton(R.string.dialog_button_close, null)
                .setPositiveButton(R.string.dialog_button_create, null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}