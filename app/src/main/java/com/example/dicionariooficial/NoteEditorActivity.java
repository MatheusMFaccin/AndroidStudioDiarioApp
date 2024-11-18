package com.example.dicionariooficial;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class NoteEditorActivity extends AppCompatActivity {
    private EditText editTextTitle, editTextContent;
    private DatabaseHelper dbHelper;
    private long noteId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_editor);

        dbHelper = new DatabaseHelper(this);
        editTextTitle = findViewById(R.id.editTextTitle);
        editTextContent = findViewById(R.id.editTextContent);
        Button buttonSave = findViewById(R.id.buttonSave);

        // Verificar se estamos editando uma nota existente
        Intent intent = getIntent();
        if (intent.hasExtra("note_id")) {
            noteId = intent.getLongExtra("note_id", -1);
            editTextTitle.setText(intent.getStringExtra("note_title"));
            editTextContent.setText(intent.getStringExtra("note_content"));
        }

        buttonSave.setOnClickListener(v -> saveNote());
    }

    private void saveNote() {
        String title = editTextTitle.getText().toString().trim();
        String content = editTextContent.getText().toString().trim();

        if (title.isEmpty()) {
            editTextTitle.setError("Digite um título");
            return;
        }

        Note note = new Note();
        note.setTitle(title);
        note.setContent(content);

        if (noteId != -1) {
            // Atualizar nota existente
            note.setId(noteId);
            dbHelper.updateNote(note);
        } else {
            // Criar nova nota
            dbHelper.insertNote(note);
        }

        finish();
    }
}