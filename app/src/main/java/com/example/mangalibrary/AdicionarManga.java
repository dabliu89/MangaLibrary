package com.example.mangalibrary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mangalibrary.Mocks.MangasDAO;
import com.example.mangalibrary.Models.Manga;

import java.util.ArrayList;

public class AdicionarManga extends AppCompatActivity {

    MangasDAO mangas;
    ArrayList<String> usuarioMangas;
    Manga manga;
    TextView isbnDoManga;
    TextView tituloDoManga;
    TextView urlDaCapaDoManga;
    TextView totalPaginasDoManga;
    TextView editoraDoManga;
    TextView dataDePublicacaoDoManga;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_manga);
        Intent intent = getIntent();
        this.mangas = (MangasDAO) intent.getSerializableExtra("mangas");
        this.usuarioMangas = (ArrayList<String>) intent.getSerializableExtra("usuarioMangas");
        setAdicionarMangaViews();
    }

    private void setAdicionarMangaViews() {
        this.isbnDoManga = (TextView) findViewById(R.id.editIsbnNovoManga);
        this.tituloDoManga = (TextView) findViewById(R.id.editTituloNovoManga);
        this.urlDaCapaDoManga = (TextView) findViewById(R.id.editImagemNovoManga);
        this.totalPaginasDoManga = (TextView) findViewById(R.id.editPaginasNovoManga);
        this.editoraDoManga = (TextView) findViewById(R.id.editEditoraNovoManga);
        this.dataDePublicacaoDoManga = (TextView) findViewById(R.id.editDataDaPublicacaoNovoManga);
    }

    public void buscarMangaPorIsbn (View view) {
        String isbnBusca = this.isbnDoManga.getText().toString();
        for (String str : this.usuarioMangas) {
            if (str.equals(isbnBusca)) {
                Toast toast = Toast.makeText(this, "O mangá já está cadastrado na sua biblioteca.", Toast.LENGTH_LONG);
                toast.show();
                return;
            }
        }
        for (Manga manga : this.mangas.mangasCadastrados) {
            if (manga.getIsbn().equals(isbnBusca)) {
                tituloDoManga.setText(manga.getTitulo());
                urlDaCapaDoManga.setText(manga.getCapa());
                totalPaginasDoManga.setText(manga.getPaginas());
                editoraDoManga.setText(manga.getEditora());
                dataDePublicacaoDoManga.setText(manga.getDataPublicacao());
                return;
            }
        }
        isbnDoManga.setEnabled(true);
        tituloDoManga.setEnabled(true);
        urlDaCapaDoManga.setEnabled(true);
        totalPaginasDoManga.setEnabled(true);
        editoraDoManga.setEnabled(true);
        dataDePublicacaoDoManga.setEnabled(true);
        tituloDoManga.setText("");
        urlDaCapaDoManga.setText("");
        totalPaginasDoManga.setText("");
        editoraDoManga.setText("");
        dataDePublicacaoDoManga.setText("");
    }

    public void adicionarNovoManga (View view) {
        String isbn = isbnDoManga.getText().toString();
        String titulo = isbnDoManga.getText().toString();
        String capa = isbnDoManga.getText().toString();
        String paginas = isbnDoManga.getText().toString();
        String editora = isbnDoManga.getText().toString();
        String dataPublicacao = isbnDoManga.getText().toString();
        if (isbn.isEmpty() || titulo.isEmpty() || capa.isEmpty() || paginas.isEmpty() || editora.isEmpty() || dataPublicacao.isEmpty()) {
            Toast toast = Toast.makeText(this, "A ação requer o preenchimento de todos os campos.", Toast.LENGTH_LONG);
            toast.show();
            return;
        }
        for (Manga m : this.mangas.mangasCadastrados) {
            if (m.getIsbn().equals(isbn)) {
                Intent intent = new Intent();
                intent.putExtra("mangaIsbn",m.getIsbn());
                setResult(42,intent);
                finish();
            }
        }
        this.manga = new Manga(isbn,titulo,paginas,editora,dataPublicacao,capa);
        Intent intent = new Intent();
        intent.putExtra("manga",manga);
        setResult(43, intent);
        finish();
    }

    public void fechaTelaDeNovoManga (View view) {
        setResult(00);
        finish();
    }
}