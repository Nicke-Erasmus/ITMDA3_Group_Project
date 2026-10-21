package com.example.itmda3_group_project.note_taker;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.itmda3_group_project.databinding.NotesFragmentBinding;
import com.example.itmda3_group_project.databinding.AddNoteBinding;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Note_Taker_Fragment extends Fragment {

    private NotesFragmentBinding binding;
    private NoteDao noteDao;
    private NoteAdapter adapter;
    private Uri selectedPdfUri = null;
    private AddNoteBinding currentDialogBinding = null;

    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    // PDF Picker
    private final ActivityResultLauncher<String[]> selectPdfLauncher =
            registerForActivityResult(new ActivityResultContracts.OpenDocument(),
                    uri -> {
                        if (uri != null) {
                            int takeFlags = Intent.FLAG_GRANT_READ_URI_PERMISSION;
                            requireContext().getContentResolver().takePersistableUriPermission(uri, takeFlags);
                            selectedPdfUri = uri;
                            if (currentDialogBinding != null) {
                                currentDialogBinding.textViewSelectedPdf.setText(
                                        uri.getLastPathSegment() != null ? uri.getLastPathSegment() : "PDF Selected"
                                );
                            }
                        }
                    });

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = NotesFragmentBinding.inflate(inflater, container, false);

        // Initialize DAO
        noteDao = AppDatabase.getInstance(requireContext()).noteDao();

        setupRecyclerView();
        setupListeners();
        loadNotes();

        return binding.getRoot();
    }

    private void setupRecyclerView() {
        adapter = new NoteAdapter(
                List.of(),
                this::openPdf,
                this::deleteNote,
                this::showEditNote
        );

        binding.recyclerViewNotes.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerViewNotes.setAdapter(adapter);
    }

    private void setupListeners() {
        binding.fabAddNote.setOnClickListener(v -> showAddNoteDialog());
    }

    private void loadNotes() {
        executor.execute(() -> {
            List<Note> notes = noteDao.getAllNotes();
            requireActivity().runOnUiThread(() -> {
                if (notes.isEmpty()) {
                    binding.layoutEmptyState.setVisibility(View.VISIBLE);
                    binding.recyclerViewNotes.setVisibility(View.GONE);
                } else {
                    binding.layoutEmptyState.setVisibility(View.GONE);
                    binding.recyclerViewNotes.setVisibility(View.VISIBLE);
                    adapter.updateNotes(notes);
                }
            });
        });
    }

    private void showAddNoteDialog() {
        selectedPdfUri = null;
        AddNoteBinding dialogBinding = AddNoteBinding.inflate(LayoutInflater.from(requireContext()));
        currentDialogBinding = dialogBinding;

        dialogBinding.buttonAttachPdf.setOnClickListener(v ->
                selectPdfLauncher.launch(new String[]{"application/pdf"})
        );

        new AlertDialog.Builder(requireContext())
                .setView(dialogBinding.getRoot())
                .setPositiveButton("Save", (dialog, which) -> {
                    String title = dialogBinding.editTextTitle.getText().toString().trim();
                    String content = dialogBinding.editTextContent.getText().toString().trim();

                    if (!title.isEmpty() || !content.isEmpty()) {
                        saveNote(title, content, selectedPdfUri != null ? selectedPdfUri.toString() : null);
                    } else {
                        Toast.makeText(requireContext(), "Cannot save an empty note", Toast.LENGTH_SHORT).show();
                    }
                    dialog.dismiss();
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                .setOnDismissListener(d -> currentDialogBinding = null)
                .show();
    }

    private void saveNote(String title, String content, String pdfUriString) {
        Note newNote = new Note(
                title.isEmpty() ? "Untitled Note" : title,
                content,
                pdfUriString,
                System.currentTimeMillis()
        );

        executor.execute(() -> {
            noteDao.insert(newNote);
            loadNotes();
        });
    }

    private void deleteNote(Note note) {
        executor.execute(() -> {
            noteDao.delete(note);
            loadNotes();
        });
    }

    private void openPdf(Uri uri) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(uri, "application/pdf");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(requireContext(), "No compatible PDF viewer app found.", Toast.LENGTH_SHORT).show();
        }
    }

    private void showEditNote(Note note) {
        selectedPdfUri = note.getPdfUri() != null ? Uri.parse(note.getPdfUri()) : null;
        AddNoteBinding dialogBinding = AddNoteBinding.inflate(LayoutInflater.from(requireContext()));
        currentDialogBinding = dialogBinding;

        // Pre-fill existing values
        dialogBinding.editTextTitle.setText(note.getTitle());
        dialogBinding.editTextContent.setText(note.getContent());
        if (selectedPdfUri != null) {
            dialogBinding.textViewSelectedPdf.setText(selectedPdfUri.getLastPathSegment());
        }

        dialogBinding.buttonAttachPdf.setOnClickListener(v ->
                selectPdfLauncher.launch(new String[]{"application/pdf"})
        );

        new AlertDialog.Builder(requireContext())
                .setView(dialogBinding.getRoot())
                .setPositiveButton("Update", (dialog, which) -> {
                    String newTitle = dialogBinding.editTextTitle.getText().toString().trim();
                    String newContent = dialogBinding.editTextContent.getText().toString().trim();

                    note.setTitle(newTitle.isEmpty() ? "Untitled Note" : newTitle);
                    note.setContent(newContent);
                    note.setPdfUri(selectedPdfUri != null ? selectedPdfUri.toString() : null);

                    executor.execute(() -> {
                        noteDao.update(note);
                        loadNotes(); // refresh list
                    });
                    dialog.dismiss();
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                .setOnDismissListener(d -> currentDialogBinding = null)
                .show();
    }
}
