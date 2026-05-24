package com.telo.app.notes;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import com.telo.app.R;

public class SecureNoteActivity extends AppCompatActivity {

    private SecureNoteViewModel viewModel;
    private EditText            etTitle;
    private EditText            etContent;
    private boolean             isEditMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secure_note);

        viewModel = new ViewModelProvider(this)
            .get(SecureNoteViewModel.class);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        etTitle   = findViewById(R.id.et_note_title);
        etContent = findViewById(R.id.et_note_content);

        // Load existing note
        String noteId = getIntent().getStringExtra("note_id");
        if (noteId != null) {
            isEditMode = true;
            // Load note from repository
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_note, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_save) {
            saveNote();
            return true;
        } else if (id == R.id.action_delete && isEditMode) {
            showDeleteConfirm();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveNote() {
        String title   = etTitle.getText().toString().trim();
        String content = etContent.getText().toString().trim();

        if (title.isEmpty()) {
            etTitle.setError("Title required");
            return;
        }

        viewModel.saveNote(title, content);
        finish();
    }

    private void showDeleteConfirm() {
        new android.app.AlertDialog.Builder(this)
            .setTitle("Delete note?")
            .setMessage("This cannot be undone")
            .setPositiveButton("Delete", (d, w) -> {
                viewModel.deleteNote(
                    viewModel.getCurrentNote()
                );
                finish();
            })
            .setNegativeButton("Cancel", null)
            .show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}