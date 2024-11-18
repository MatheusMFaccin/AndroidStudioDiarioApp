package com.example.dicionariooficial;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity implements NoteAdapter.OnNoteClickListener {
    private RecyclerView recyclerView;
    private NoteAdapter adapter;
    private DatabaseHelper dbHelper;
    private List<Note> notes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DatabaseHelper(this);
        notes = dbHelper.getAllNotes();

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new NoteAdapter(this, notes, this);
        recyclerView.setAdapter(adapter);

        FloatingActionButton fabAddNote = findViewById(R.id.fabAddNote);
        fabAddNote.setOnClickListener(v -> startNoteEditorActivity(null));
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateNotesList();
    }

    private void updateNotesList() {
        notes = dbHelper.getAllNotes();
        adapter.updateNotes(notes);
    }

    @Override
    public void onNoteClick(Note note) {
        startNoteEditorActivity(note);
    }

    @Override
    public void onNoteLongClick(Note note) {
        new AlertDialog.Builder(this)
                .setTitle("Excluir nota")
                .setMessage("Deseja excluir esta nota?")
                .setPositiveButton("Sim", (dialog, which) -> {
                    dbHelper.deleteNote(note.getId());
                    updateNotesList();
                })
                .setNegativeButton("Não", null)
                .show();
    }

    private void startNoteEditorActivity(Note note) {
        Intent intent = new Intent(this, NoteEditorActivity.class);
        if (note != null) {
            intent.putExtra("note_id", note.getId());
            intent.putExtra("note_title", note.getTitle());
            intent.putExtra("note_content", note.getContent());
        }
        startActivity(intent);
    }
}
