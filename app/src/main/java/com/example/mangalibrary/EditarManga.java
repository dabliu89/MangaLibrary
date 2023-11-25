package com.example.mangalibrary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mangalibrary.Models.Manga;

public class EditarManga extends AppCompatActivity {

    Manga mangaEmEdicao;
    TextView tituloDoMangaEmEdicao;
    TextView imagemDoMangaEmEdicao;
    TextView totalDePaginasDoMangaEmEdicao;
    TextView editoraDoMangaEmEdicao;
    TextView dataPublicacaoDoMangaEmEdicao;
    TextView isbnDoMangaEmEdicao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_manga);
        Intent intent = getIntent();
        this.mangaEmEdicao = (Manga) intent.getSerializableExtra("manga");
        setMangaViews();
        setMangaEmEdicao(this.mangaEmEdicao);
    }

    private void setMangaEmEdicao(Manga mangaEmEdicao) {
        this.tituloDoMangaEmEdicao.setText(mangaEmEdicao.getTitulo());
        this.imagemDoMangaEmEdicao.setText(mangaEmEdicao.getCapa());
        this.totalDePaginasDoMangaEmEdicao.setText(mangaEmEdicao.getPaginas());
        this.editoraDoMangaEmEdicao.setText(mangaEmEdicao.getEditora());
        this.dataPublicacaoDoMangaEmEdicao.setText(mangaEmEdicao.getDataPublicacao());
        this.isbnDoMangaEmEdicao.setText(mangaEmEdicao.getIsbn());
    }

    private void setMangaViews() {
        this.tituloDoMangaEmEdicao = (TextView) findViewById(R.id.editTituloMangaEmEdicao);
        this.imagemDoMangaEmEdicao = (TextView) findViewById(R.id.editImagemMangaEmEdicao);
        this.totalDePaginasDoMangaEmEdicao = (TextView) findViewById(R.id.editPaginasMangaEmEdicao);
        this.editoraDoMangaEmEdicao = (TextView) findViewById(R.id.editEditoraMangaEmEdicao);
        this.dataPublicacaoDoMangaEmEdicao = (TextView) findViewById(R.id.editDataDaPublicacaoMangaEmEdicao);
        this.isbnDoMangaEmEdicao = (TextView) findViewById(R.id.editIsbnMangaEmEdicao);
        Log.e("CHECK SET", "Setou as views");
    }

    public void processarEdicaoDoManga (View view) {
        String novoTitulo = this.tituloDoMangaEmEdicao.getText().toString();
        String novaCapa = this.imagemDoMangaEmEdicao.getText().toString();
        String novoTotalDePaginas = this.totalDePaginasDoMangaEmEdicao.getText().toString();
        String novaEditora = this.editoraDoMangaEmEdicao.getText().toString();
        String novaDataDePublicacao = this.dataPublicacaoDoMangaEmEdicao.getText().toString();
        String novoIsbn = this.isbnDoMangaEmEdicao.getText().toString();
        if (novoTitulo.isEmpty() || novaCapa.isEmpty() || novoTotalDePaginas.isEmpty() || novaEditora.isEmpty() || novaDataDePublicacao.isEmpty() || novoIsbn.isEmpty()) {
            Toast toast = Toast.makeText(this, "A ação requer o preenchimento de todos os campos.", Toast.LENGTH_LONG);
            toast.show();
            return;
        }
        Manga editManga = new Manga(novoIsbn,novoTitulo,novoTotalDePaginas,novaEditora,novaDataDePublicacao,novaCapa);
        Intent intent = new Intent();
        intent.putExtra("editManga", editManga);
        setResult(44,intent);
        finish();
    }
}