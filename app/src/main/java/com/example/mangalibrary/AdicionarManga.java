package com.example.mangalibrary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mangalibrary.Mocks.MangasDAO;
import com.example.mangalibrary.Models.Manga;
import com.example.mangalibrary.Models.Noticia;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdicionarManga extends AppCompatActivity {

    MangasDAO mangas;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    ArrayList<Manga> meusMangas;
    TextView isbnDoManga;
    TextView tituloDoManga;
    TextView urlDaCapaDoManga;
    TextView totalPaginasDoManga;
    TextView editoraDoManga;
    TextView dataDePublicacaoDoManga;
    String isbn, titulo, capa, paginas, editora, dataPublicacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        mangas = new MangasDAO();
        meusMangas = new ArrayList<Manga>();
        setContentView(R.layout.activity_adicionar_manga);
        setAdicionarMangaViews();
        carregarMangasDoUsuario();
    }

    private void setAdicionarMangaViews() {
        this.isbnDoManga = (TextView) findViewById(R.id.editIsbnNovoManga);
        this.tituloDoManga = (TextView) findViewById(R.id.editTituloNovoManga);
        this.urlDaCapaDoManga = (TextView) findViewById(R.id.editImagemNovoManga);
        this.totalPaginasDoManga = (TextView) findViewById(R.id.editPaginasNovoManga);
        this.editoraDoManga = (TextView) findViewById(R.id.editEditoraNovoManga);
        this.dataDePublicacaoDoManga = (TextView) findViewById(R.id.editDataDaPublicacaoNovoManga);
    }

    protected void carregarMangasDoUsuario() {
        mangas.carregarMangasDoUsuario(mAuth.getCurrentUser().getUid(), new MangasDAO.MangasLoadListener() {
            @Override
            public void onMangasLoaded(List<Manga> mangas) {
                    meusMangas.clear();
                    meusMangas.addAll(mangas);
            }
            @Override
            public void onMangasLoadFailed() {
                Log.e("Biblioteca", "Falha ao carregar mangás do usuário");
                Toast.makeText(AdicionarManga.this, "Falha ao carregar mangás do usuário", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean mangaEstaNaLista(String mangaId) {
        for (Manga manga : meusMangas) {
            if (manga.getIsbn().equals(mangaId)) {
                return true;
            }
        }
        return false;
    }

    public void buscarMangaPorIsbn (View view) {
        String isbnBusca = this.isbnDoManga.getText().toString().trim();

        if (isbnBusca.isEmpty()) {
            Toast.makeText(AdicionarManga.this, "Preencha o ISBN para continuar.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (mangaEstaNaLista(isbnBusca) == true) {
            Toast.makeText(AdicionarManga.this, "O usuário já possui o mangá com o ISBN informado.", Toast.LENGTH_SHORT).show();
            return;
        }

        mangas.buscarMangaPorId(isbnBusca, new MangasDAO.MangaLoadListener() {
            @Override
            public void onMangaLoaded(Manga manga) {
                tituloDoManga.setText(manga.getTitulo());
                urlDaCapaDoManga.setText(manga.getCapa());
                totalPaginasDoManga.setText(manga.getPaginas());
                editoraDoManga.setText(manga.getEditora());
                dataDePublicacaoDoManga.setText(manga.getDataPublicacao());
                Toast.makeText(AdicionarManga.this, "Informações carregadas com sucesso!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onMangaLoadFailed() {
                Toast.makeText(AdicionarManga.this, "Mangá não encontrado. Informe os campos.", Toast.LENGTH_SHORT).show();
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
        });
    }

    public void adicionarNovoManga (View view) {
        this.isbn = isbnDoManga.getText().toString().trim();
        this.titulo = tituloDoManga.getText().toString();
        this.capa = urlDaCapaDoManga.getText().toString();
        this.paginas = totalPaginasDoManga.getText().toString();
        this.editora = editoraDoManga.getText().toString();
        this.dataPublicacao = dataDePublicacaoDoManga.getText().toString();
        Log.e("isbn",isbn);
        Log.e("titulo",titulo);
        Log.e("paginas",paginas);
        Log.e("editora",editora);
        Log.e("dataPublicacao",dataPublicacao);
        Log.e("capa",capa);
        if (isbn.isEmpty() || titulo.isEmpty() || capa.isEmpty() || paginas.isEmpty() || editora.isEmpty() || dataPublicacao.isEmpty()) {
            Toast toast = Toast.makeText(this, "A ação requer o preenchimento de todos os campos.", Toast.LENGTH_LONG);
            toast.show();
            return;
        }
        if (mangaEstaNaLista(isbn) == true) {
            Toast.makeText(AdicionarManga.this, "O usuário já possui o mangá com o ISBN informado.", Toast.LENGTH_SHORT).show();
            return;
        }

        mangas.verificarExistenciaManga(isbn, new MangasDAO.DocumentExistenceListener() {
            @Override
            public void onDocumentExists(boolean exists) {
                adicionarMangaAoUsuario(mAuth.getCurrentUser().getUid(), isbn);
                setResult(42);
                finish();
            }

            @Override
            public void onCheckFailed() {
                Log.e("CHECK","Mangá não existe na base de dados.");
                Map<String, Object> manga = new HashMap<>();
                manga.put("titulo", titulo);
                manga.put("paginas", paginas);
                manga.put("editora", editora);
                manga.put("dataPublicacao", dataPublicacao);
                manga.put("capa", capa);

                db.collection("Mangas").document(isbn)
                        .set(manga)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(AdicionarManga.this, "Mangá adicionado com sucesso.", Toast.LENGTH_SHORT).show();
                                setResult(42);
                                finish();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(AdicionarManga.this, "Houve um problema ao adicionar o mangá.", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }

    private void adicionarMangaAoUsuario(String idDoUsuario, String idDoManga) {
        DocumentReference usuarioRef = FirebaseFirestore.getInstance().collection("Usuarios").document(idDoUsuario);

        usuarioRef.update("mangas", FieldValue.arrayUnion(idDoManga.trim()))
                .addOnSuccessListener(aVoid -> {
                    Log.d("Firestore", "ID do manga adicionado ao usuário com sucesso");
                })
                .addOnFailureListener(e -> {
                    Log.e("Firestore", "Erro ao adicionar ID do manga ao usuário", e);
                });
    }


    public void fechaTelaDeNovoManga (View view) {
        setResult(00);
        finish();
    }
}