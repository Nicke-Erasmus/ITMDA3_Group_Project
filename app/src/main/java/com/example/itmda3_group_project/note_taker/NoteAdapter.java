package com.example.itmda3_group_project.note_taker;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.itmda3_group_project.databinding.ItemNoteBinding;

import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {

    private List<Note> notes;
    private final OnPdfClickListener onPdfClick;
    private final OnDeleteClickListener onDeleteClick;
    private final OnNoteClickListener onNoteClick;

    // Define functional interfaces for callbacks
    public interface OnPdfClickListener {
        void onPdfClick(Uri uri);
    }

    public interface OnDeleteClickListener {
        void onDeleteClick(Note note);
    }

    public interface OnNoteClickListener {
        void onNoteClick(Note note);
    }

    public NoteAdapter(List<Note> notes,
                       OnPdfClickListener onPdfClick,
                       OnDeleteClickListener onDeleteClick,
                       OnNoteClickListener onNoteClick) {
        this.notes = notes;
        this.onPdfClick = onPdfClick;
        this.onDeleteClick = onDeleteClick;
        this.onNoteClick = onNoteClick;
    }

    public static class NoteViewHolder extends RecyclerView.ViewHolder {
        ItemNoteBinding binding;

        public NoteViewHolder(ItemNoteBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemNoteBinding binding = ItemNoteBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new NoteViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        Note note = notes.get(position);

        holder.binding.textViewTitle.setText(note.getTitle());
        holder.binding.textViewContent.setText(note.getContent());

        if (!isNullOrEmpty(note.getPdfUri())) {
            holder.binding.layoutPdfBadge.setVisibility(View.VISIBLE);
            Uri uri = Uri.parse(note.getPdfUri());
            holder.binding.textViewPdfName.setText(
                    uri.getLastPathSegment() != null ? uri.getLastPathSegment() : "Attached PDF"
            );
            holder.binding.layoutPdfBadge.setOnClickListener(v -> onPdfClick.onPdfClick(uri));
        } else {
            holder.binding.layoutPdfBadge.setVisibility(View.GONE);
        }

        holder.binding.buttonDelete.setOnClickListener(v -> onDeleteClick.onDeleteClick(note));
        holder.itemView.setOnClickListener(v -> onNoteClick.onNoteClick(note));holder.itemView.setOnClickListener(v -> onNoteClick.onNoteClick(note));
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public void updateNotes(List<Note> newNotes) {
        this.notes = newNotes;
        notifyDataSetChanged();
    }

    private boolean isNullOrEmpty(String str) {
        return str == null || str.isEmpty();
    }
}

