package com.example.mangalibrary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mangalibrary.Models.Noticia;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class AdicionarNoticia extends AppCompatActivity {

    EditText editNoticiaTitulo, editNoticiaUrlImagem, editNoticiaTexto;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_noticia);

        db = FirebaseFirestore.getInstance();

        setViews();
    }

    private void setViews() {
        editNoticiaTitulo = (EditText) findViewById(R.id.editTituloNovaNoticia);
        editNoticiaUrlImagem = (EditText) findViewById(R.id.editImagemNovaNoticia);
        editNoticiaTexto = (EditText) findViewById(R.id.editTextoNovaNoticia);
    }

    public void adicionarNovaNoticia (View view) {

        Intent intent = new Intent();
        String noticiaTitulo = editNoticiaTitulo.getText().toString();
        String noticiaUrlImagem = editNoticiaUrlImagem.getText().toString();
        String noticiaTexto = editNoticiaTexto.getText().toString();
        if (noticiaTitulo.isEmpty() || noticiaUrlImagem.isEmpty() || noticiaTexto.isEmpty()) {
            Toast toast = Toast.makeText(this, "Ação requer o preenchimento de todos os campos.", Toast.LENGTH_LONG);
            toast.show();
            return;
        }
        Noticia novaNoticia = new Noticia(noticiaTitulo, noticiaUrlImagem, noticiaTexto);

        CollectionReference noticiasRef = db.collection("Noticias");

        noticiasRef.add(novaNoticia)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(this, "Notícia adicionada com sucesso!", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Erro ao adicionar a notícia: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });

        setResult(32, intent);
        finish();
    }

    public void fechaTelaDeNovaNoticia (View view) {
        setResult(00);
        finish();
    }
}