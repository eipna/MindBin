package com.eipna.mindbin.ui.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.eipna.mindbin.R;
import com.eipna.mindbin.data.note.Note;
import com.eipna.mindbin.util.DateUtil;
import com.eipna.mindbin.util.PreferenceUtil;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textview.MaterialTextView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder>{

    private final Context context;
    private final Listener listener;
    private ArrayList<Note> list;

    public interface Listener {
        void onClick(int position);
    }

    public NoteAdapter(@NotNull Context context, Listener listener, ArrayList<Note> list) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void search(ArrayList<Note> queriedList) {
        list = queriedList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NoteAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_note, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteAdapter.ViewHolder holder, int position) {
        Note currentNote = list.get(position);
        holder.bind(currentNote);

        holder.itemView.setOnClickListener(view -> listener.onClick(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void update(ArrayList<Note> updatedList) {
        list.clear();
        list.addAll(updatedList);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        PreferenceUtil preferences;

        MaterialCardView parent;
        MaterialTextView title;
        MaterialTextView content;
        MaterialTextView dateCreated;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            preferences = new PreferenceUtil(itemView.getContext());
            parent = itemView.findViewById(R.id.recyclerNoteParent);
            title = itemView.findViewById(R.id.recyclerNoteTitle);
            content = itemView.findViewById(R.id.recyclerNoteContent);
            dateCreated = itemView.findViewById(R.id.recyclerNoteDateCreated);
        }

        public void bind(Note note) {
            parent.setRadius(preferences.isRoundedNotes() ? 32.0f : 0.0f);
            dateCreated.setVisibility(preferences.isNoteDateCreatedEnabled() ? View.VISIBLE : View.GONE);

            title.setText(note.getTitle().isEmpty() ? "Empty note" : note.getTitle());
            content.setText(note.getContent().isEmpty() ? "Empty note" : note.getContent());
            dateCreated.setText(DateUtil.getString(preferences.getNoteDateCreatedFormat(), note.getDateCreated()));
        }
    }
}