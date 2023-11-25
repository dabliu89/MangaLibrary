package com.example.mangalibrary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mangalibrary.Mocks.UsuariosDAO;
import com.example.mangalibrary.Models.Noticia;
import com.squareup.picasso.Picasso;

public class VerNoticia extends AppCompatActivity {

    int resultadoSolicitacao;
    TextView tituloDaNoticia;
    ImageView imagemDaNoticia;
    TextView textoDaNoticia;
    Noticia noticia;
    int selectedAtual;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_noticia);
        this.resultadoSolicitacao = 00;
        Intent intent = getIntent();
        selectedAtual = intent.getIntExtra("position",-1);
        setNoticiaViews();
        this.noticia = (Noticia) intent.getSerializableExtra("noticia");
        setNoticia(this.noticia);
    }

    private void setNoticiaViews() {
        this.tituloDaNoticia = this.findViewById(R.id.tituloDaNoticiaAberta);
        this.textoDaNoticia = this.findViewById(R.id.textoDaNoticiaAberta);
        this.imagemDaNoticia = this.findViewById(R.id.imagemDaNoticiaAbertaView);
    }

    private void setNoticia(Noticia noticia) {
        tituloDaNoticia.setText(noticia.getTitulo());
        textoDaNoticia.setText(noticia.getTexto());
        Picasso.get().load(noticia.getImagem()).into(imagemDaNoticia);
    }

    public void editarNoticia (View view) {
        Intent intent = new Intent(this, EditarNoticia.class);
        intent.putExtra("tituloDaNoticia", this.tituloDaNoticia.getText());
        intent.putExtra("imagemDaNoticia", this.noticia.getImagem());
        intent.putExtra("textDaNoticia", this.textoDaNoticia.getText());
        startActivityForResult(intent,31);
    }

    public void fechaTelaVerNoticia (View view) {
        if (resultadoSolicitacao == 33) {
            Intent intent = new Intent();
            intent.putExtra("noticiaEditada",this.noticia);
            intent.putExtra("posicaoDaNoticia",this.selectedAtual);
            setResult(resultadoSolicitacao, intent);
            finish();
        }
        else {
            setResult(00);
            finish();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 31 && resultCode == 33) {
            String recebeNovoTituloDaNoticia = (String) data.getExtras().get("novoTituloDaNoticia");
            String recebeNovaImagemNoticia = (String) data.getExtras().get("novaImagemNoticia");
            String recebeNovoTextoNoticia = (String) data.getExtras().get("novoTextoNoticia");
            this.resultadoSolicitacao = 33;
            setEdicaoNoticiaAberta(recebeNovoTituloDaNoticia, recebeNovaImagemNoticia, recebeNovoTextoNoticia);
            Log.e("STATUS CHECK", "Notícia editada.");
        }
        else {
            Log.e("STATUS CHECK","Solicitação cancelada.");
        }
    }

    private void setEdicaoNoticiaAberta(String recebeNovoTituloDaNoticia, String recebeNovaImagemNoticia, String recebeNovoTextoNoticia) {
        this.noticia.setTitulo(recebeNovoTituloDaNoticia);
        this.noticia.setImagem(recebeNovaImagemNoticia);
        this.noticia.setTexto(recebeNovoTextoNoticia);
        setNoticia(noticia);
    }

    public void excluirNoticiaAberta (View view) {
        Intent intent = new Intent();
        intent.putExtra("posicaoDaNoticia",this.selectedAtual);
        setResult(34, intent);
        finish();
    }
}