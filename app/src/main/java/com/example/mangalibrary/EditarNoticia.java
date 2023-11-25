package com.example.mangalibrary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class EditarNoticia extends AppCompatActivity {

    TextView tituloDaNoticia;
    TextView imagemDaNoticia;
    TextView textoDaNoticia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_noticia);
        setEdicaoNoticiaViews();
        Intent intent = getIntent();

        String titulo = intent.getStringExtra("tituloDaNoticia");
        String imagem = intent.getStringExtra("imagemDaNoticia");
        String texto = intent.getStringExtra("textDaNoticia");
        setEdicaoNoticiaViewsContent(titulo, imagem, texto);
    }

    private void setEdicaoNoticiaViews() {
        tituloDaNoticia = (EditText) findViewById(R.id.editTituloNoticiaEmEdicao);
        imagemDaNoticia = (EditText) findViewById(R.id.editImagemNoticiaEmEdicao);
        textoDaNoticia = (EditText) findViewById(R.id.editTextoDaNoticiaEmEdicao);
    }

    private void setEdicaoNoticiaViewsContent (String titulo, String imagem, String texto) {
        tituloDaNoticia.setText(titulo);
        imagemDaNoticia.setText(imagem);
        textoDaNoticia.setText(texto);
    }

    public void processarEdicao (View view) {
        String novoTituloNoticia = tituloDaNoticia.getText().toString();
        String novaImagemNoticia = imagemDaNoticia.getText().toString();
        String novoTextoNoticia = textoDaNoticia.getText().toString();
        if (novoTituloNoticia.isEmpty() || novaImagemNoticia.isEmpty() || novoTextoNoticia.isEmpty()) {
            Log.e("CHECK","Há campos náo preenchidos.");
            Toast toast = Toast.makeText(this, "Ação requer o preenchimento de todos os campos.", Toast.LENGTH_LONG);
            toast.show();
            return;
        }
        Intent intent = new Intent();
        intent.putExtra("novoTituloDaNoticia",novoTituloNoticia);
        intent.putExtra("novaImagemNoticia",novaImagemNoticia);
        intent.putExtra("novoTextoNoticia",novoTextoNoticia);
        setResult(33, intent);
        finish();
    }

    public void fechaTelaDeEdicaoDeNoticia (View view) {
        setResult(00);
        finish();
    }

}