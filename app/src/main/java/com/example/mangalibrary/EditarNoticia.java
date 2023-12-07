package com.example.mangalibrary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mangalibrary.Mocks.NoticiasDAO;
import com.example.mangalibrary.Models.Noticia;

public class EditarNoticia extends AppCompatActivity {

    TextView tituloDaNoticia, imagemDaNoticia, textoDaNoticia;
    Noticia noticia;
    NoticiasDAO noticiasDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_noticia);

        noticiasDAO = new NoticiasDAO();
        setEdicaoNoticiaViews();

        Intent intent = getIntent();
        noticia = (Noticia) intent.getSerializableExtra("noticia");
        String titulo = noticia.getTitulo();
        String imagem = noticia.getImagem();
        String texto = noticia.getTexto();
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
            Toast toast = Toast.makeText(this, "Ação requer o preenchimento de todos os campos.", Toast.LENGTH_LONG);
            toast.show();
            return;
        }
        noticiasDAO.atualizarNoticiaNoFirestore(this.noticia.getIdNoticia(),novoTituloNoticia,novaImagemNoticia,novoTextoNoticia);
        Toast toast = Toast.makeText(this, "Notícia editada.", Toast.LENGTH_LONG);
        toast.show();
        setResult(33);
        finish();
    }

    public void fechaTelaDeEdicaoDeNoticia (View view) {
        setResult(00);
        finish();
    }

}