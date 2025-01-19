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
import com.eipna.mindbin.data.note.NoteListener;
import com.eipna.mindbin.util.SharedPreferenceUtil;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textview.MaterialTextView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder>{

    private final Context context;
    private final ArrayList<Note> list;
    private final NoteListener noteListener;

    public NoteAdapter(@NotNull Context context, NoteListener noteListener, ArrayList<Note> list) {
        this.context = context;
        this.list = list;
        this.noteListener = noteListener;
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

        holder.itemView.setOnClickListener(view -> noteListener.OnNoteClick(position));
        holder.itemView.setOnLongClickListener(view -> {
            noteListener.OnNoteLongClick(position);
            return true;
        });
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

        SharedPreferenceUtil sharedPreferenceUtil;

        MaterialCardView parent;
        MaterialTextView title;
        MaterialTextView content;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            sharedPreferenceUtil = new SharedPreferenceUtil(itemView.getContext());
            parent = itemView.findViewById(R.id.recyclerNoteParent);
            title = itemView.findViewById(R.id.recyclerNoteTitle);
            content = itemView.findViewById(R.id.recyclerNoteContent);
        }

        public void bind(Note note) {
            boolean isRoundedCorners = sharedPreferenceUtil.getBoolean("rounded_corners", true);
            int maxNoteTitleLines = sharedPreferenceUtil.getInt("max_note_title", 1);
            int maxNoteContentLines = sharedPreferenceUtil.getInt("max_note_content", 1);

            parent.setRadius(isRoundedCorners ? 12.0f : 0.0f);
            title.setMaxLines(maxNoteTitleLines);
            content.setMaxLines(maxNoteContentLines);

            title.setText(note.getTitle());
            content.setText(note.getContent());
        }
    }
}