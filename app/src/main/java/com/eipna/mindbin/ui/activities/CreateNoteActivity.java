package com.eipna.mindbin.ui.activities;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.eipna.mindbin.R;
import com.eipna.mindbin.databinding.ActivityCreateNoteBinding;
import com.google.android.material.shape.MaterialShapeDrawable;

import java.util.Objects;

public class CreateNoteActivity extends AppCompatActivity {

    private ActivityCreateNoteBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityCreateNoteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Drawable drawable = MaterialShapeDrawable.createWithElevationOverlay(this);
        binding.appBar.setStatusBarForeground(drawable);

        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.create_note, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) finish();
        if (item.getItemId() == R.id.share) showShareIntent();
        return true;
    }

    private void showShareIntent() {
        String noteContent = Objects.requireNonNull(binding.contentInput.getText()).toString();
        if (noteContent.isEmpty()) {
            Toast.makeText(this, "Nothing to share here", Toast.LENGTH_SHORT).show();
        } else {
           Intent sendIntent = new Intent();
           sendIntent.setAction(Intent.ACTION_SEND);
           sendIntent.putExtra(Intent.EXTRA_TEXT, noteContent);
           sendIntent.setType("text/plain");

           Intent shareIntent = Intent.createChooser(sendIntent, null);
           startActivity(shareIntent);
        }
    }
}