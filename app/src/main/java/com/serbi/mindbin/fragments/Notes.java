package com.serbi.mindbin.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.serbi.mindbin.R;
import com.serbi.mindbin.activities.CreateNote;

public class Notes extends Fragment {

    private View view;
    private FloatingActionButton btn_add_note;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_notes, container, false);

        btn_add_note = view.findViewById(R.id.btn_add_note);
        btn_add_note.setOnClickListener(v -> startActivity(new Intent(getContext(), CreateNote.class)));
        return view;
    }
}