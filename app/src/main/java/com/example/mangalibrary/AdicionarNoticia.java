package com.example.mangalibrary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class AdicionarNoticia extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_noticia);
    }

    public void adicionarNovaNoticia (View view) {

        Intent intent = new Intent();
        EditText editNoticiaTitulo, editNoticiaUrlImagem, editNoticiaTexto;
        editNoticiaTitulo = (EditText) findViewById(R.id.editTituloNovaNoticia);
        editNoticiaUrlImagem = (EditText) findViewById(R.id.editImagemNovaNoticia);
        editNoticiaTexto = (EditText) findViewById(R.id.editTextoNovaNoticia);
        String noticiaTitulo = editNoticiaTitulo.getText().toString();
        String noticiaUrlImagem = editNoticiaUrlImagem.getText().toString();
        String noticiaText = editNoticiaTexto.getText().toString();
        if (noticiaTitulo.isEmpty() || noticiaUrlImagem.isEmpty() || noticiaText.isEmpty()) {
            Log.e("CHECK","Não satisfez os parâmetros vazios.");
            Toast toast = Toast.makeText(this, "Ação requer o preenchimento de todos os campos.", Toast.LENGTH_LONG);
            toast.show();
            return;
        }
        intent.putExtra("tituloDaNovaNoticia",noticiaTitulo);
        intent.putExtra("imagemDaNovaNoticia",noticiaUrlImagem);
        intent.putExtra("textoDaNovaNoticia",noticiaText);
        setResult(32, intent);
        finish();
    }

    public void fechaTelaDeNovaNoticia (View view) {
        setResult(00);
        finish();
    }
}