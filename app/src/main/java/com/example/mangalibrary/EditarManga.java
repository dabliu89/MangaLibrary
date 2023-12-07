package com.example.mangalibrary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mangalibrary.Models.Manga;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class EditarManga extends AppCompatActivity {

    private FirebaseFirestore db;
    Manga mangaEmEdicao;
    TextView tituloDoMangaEmEdicao;
    TextView imagemDoMangaEmEdicao;
    TextView totalDePaginasDoMangaEmEdicao;
    TextView editoraDoMangaEmEdicao;
    TextView dataPublicacaoDoMangaEmEdicao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_manga);
        db = FirebaseFirestore.getInstance();
        Intent intent = getIntent();
        this.mangaEmEdicao = (Manga) intent.getSerializableExtra("manga");
        setMangaViews();
        setMangaEmEdicao(this.mangaEmEdicao);
    }

    private void setMangaViews() {
        this.tituloDoMangaEmEdicao = (TextView) findViewById(R.id.editTituloMangaEmEdicao);
        this.imagemDoMangaEmEdicao = (TextView) findViewById(R.id.editImagemMangaEmEdicao);
        this.totalDePaginasDoMangaEmEdicao = (TextView) findViewById(R.id.editPaginasMangaEmEdicao);
        this.editoraDoMangaEmEdicao = (TextView) findViewById(R.id.editEditoraMangaEmEdicao);
        this.dataPublicacaoDoMangaEmEdicao = (TextView) findViewById(R.id.editDataDaPublicacaoMangaEmEdicao);
    }

    private void setMangaEmEdicao(Manga mangaEmEdicao) {
        this.tituloDoMangaEmEdicao.setText(mangaEmEdicao.getTitulo());
        this.imagemDoMangaEmEdicao.setText(mangaEmEdicao.getCapa());
        this.totalDePaginasDoMangaEmEdicao.setText(mangaEmEdicao.getPaginas());
        this.editoraDoMangaEmEdicao.setText(mangaEmEdicao.getEditora());
        this.dataPublicacaoDoMangaEmEdicao.setText(mangaEmEdicao.getDataPublicacao());
    }

    public void processarEdicaoDoManga (View view) {
        String novoTitulo = this.tituloDoMangaEmEdicao.getText().toString().trim();
        String novaCapa = this.imagemDoMangaEmEdicao.getText().toString().trim();
        String novoTotalDePaginas = this.totalDePaginasDoMangaEmEdicao.getText().toString().trim();
        String novaEditora = this.editoraDoMangaEmEdicao.getText().toString().trim();
        String novaDataDePublicacao = this.dataPublicacaoDoMangaEmEdicao.getText().toString().trim();
        if (novoTitulo.isEmpty() || novaCapa.isEmpty() || novoTotalDePaginas.isEmpty() || novaEditora.isEmpty() || novaDataDePublicacao.isEmpty()) {
            Toast toast = Toast.makeText(this, "A ação requer o preenchimento de todos os campos.", Toast.LENGTH_LONG);
            toast.show();
            return;
        }

        Map<String, Object> manga = new HashMap<>();
        manga.put("titulo", novoTitulo);
        manga.put("paginas", novoTotalDePaginas);
        manga.put("editora", novaEditora);
        manga.put("dataPublicacao", novaDataDePublicacao);
        manga.put("capa", novaCapa);

        db.collection("Mangas").document(this.mangaEmEdicao.getIsbn())
                .set(manga)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(EditarManga.this, "Mangá editado com sucesso.", Toast.LENGTH_SHORT).show();
                        setResult(42);
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(EditarManga.this, "Houve um problema ao editar o mangá.", Toast.LENGTH_SHORT).show();
                    }
                });

    }
}