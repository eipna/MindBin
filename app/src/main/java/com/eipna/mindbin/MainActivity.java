package com.eipna.mindbin;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.eipna.mindbin.databinding.ActivityMainBinding;
import com.google.android.material.shape.MaterialShapeDrawable;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());

        Drawable drawable = MaterialShapeDrawable.createWithElevationOverlay(this);
        binding.appBar.setStatusBarForeground(drawable);
        
        setSupportActionBar(binding.toolbar);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}