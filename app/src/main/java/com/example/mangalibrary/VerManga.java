package com.example.mangalibrary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mangalibrary.Mocks.MangasDAO;
import com.example.mangalibrary.Models.Manga;
import com.example.mangalibrary.Models.Noticia;
import com.squareup.picasso.Picasso;

public class VerManga extends AppCompatActivity {

    int resultadoSolicitacao;
    MangasDAO mangas;
    TextView tituloDoManga;
    ImageView imagemDoManga;
    TextView totalDePaginasDoManga;
    TextView editoraDoManga;
    TextView dataPublicacaoDoManga;
    TextView isbnDoManga;
    Manga manga;
    int selectedAtual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_manga);
        this.resultadoSolicitacao = 00;
        Intent intent = getIntent();
        setMangaViews();
        this.mangas = (MangasDAO) intent.getSerializableExtra("mangas");
        this.manga = (Manga) intent.getSerializableExtra("manga");
        for (Manga m : mangas.getMangasCadastrados()) {
            if (m.getIsbn().equals(this.manga.getIsbn())) {
                selectedAtual = mangas.getMangasCadastrados().indexOf(m);
            }
        }
        Log.e("CHECK MANGA", this.manga.getTitulo());
        setManga(this.manga);
    }

    private void setManga(Manga manga) {
        tituloDoManga.setText(manga.getTitulo());
        totalDePaginasDoManga.append(manga.getPaginas());
        editoraDoManga.append(manga.getEditora());
        dataPublicacaoDoManga.append(manga.getDataPublicacao());
        isbnDoManga.append(manga.getIsbn());
        Picasso.get().load(manga.getCapa()).into(imagemDoManga);
    }

    private void setMangaViews() {
        this.tituloDoManga = (TextView) findViewById(R.id.tituloMangaVerManga);
        this.imagemDoManga = (ImageView) findViewById(R.id.imageViewMangaVerManga);
        this.totalDePaginasDoManga = (TextView) findViewById(R.id.paginasMangaVerManga);
        this.editoraDoManga = (TextView) findViewById(R.id.editoraMangaVerManga);
        this.dataPublicacaoDoManga = (TextView) findViewById(R.id.dataDaPublicacaoMangaVerManga);
        this.isbnDoManga = (TextView) findViewById(R.id.isbnMangaVerManga);
        Log.e("CHECK SET", "Setou as views");
    }

    public void editarMangaAberto (View view) {
        Intent intent = new Intent(this, EditarManga.class);
        intent.putExtra("manga",this.manga);
        startActivityForResult(intent,41);
    }

    public void excluirMangaAberto (View view) {
        Intent intent = new Intent();
        intent.putExtra("indice",this.selectedAtual);
        setResult(45,intent);
        finish();
    }

    public void fechaTelaVerManga (View view) {
        if (this.resultadoSolicitacao == 44) {
            Intent intent = new Intent();
            intent.putExtra("manga",this.manga);
            intent.putExtra("indice",this.selectedAtual);
            setResult(resultadoSolicitacao,intent);
            finish();
        }
        setResult(00);
        finish();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 41 && resultCode == 44) {
            Manga m = (Manga) data.getExtras().get("editManga");
            this.manga = m;
            setManga(this.manga);
            this.resultadoSolicitacao = 44;
            return;
        }
        else {
            Log.e("Result","Solicitação cancelada.");
        }
    }
}