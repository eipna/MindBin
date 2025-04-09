package com.eipna.mindbin.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.eipna.mindbin.R;
import com.eipna.mindbin.data.folder.Folder;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;

public class FolderAdapter extends RecyclerView.Adapter<FolderAdapter.ViewHolder> {

    private final Context context;
    private final Listener listener;
    private final ArrayList<Folder> folders;

    public FolderAdapter(Context context, Listener listener, ArrayList<Folder> folders) {
        this.context = context;
        this.listener = listener;
        this.folders = folders;
    }

    public interface Listener {
        void onClick(int position);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_folder, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Folder folder = folders.get(position);
        holder.bind(folder);

        holder.itemView.setOnClickListener(v -> listener.onClick(position));
    }

    @Override
    public int getItemCount() {
        return folders.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        MaterialTextView name, description;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.folder_name);
            description = itemView.findViewById(R.id.folder_description);
        }

        public void bind(Folder folder) {
            name.setText(folder.getName());
            description.setText(folder.getDescription());
        }
    }
}