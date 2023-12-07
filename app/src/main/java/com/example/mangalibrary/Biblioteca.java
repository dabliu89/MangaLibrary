package com.example.mangalibrary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mangalibrary.Mocks.MangasDAO;
import com.example.mangalibrary.Mocks.UsuariosDAO;
import com.example.mangalibrary.Models.Manga;
import com.example.mangalibrary.Models.Noticia;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class Biblioteca extends AppCompatActivity {

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    ArrayList<Manga> meusMangas;
    ListView listaDeMangas;
    ArrayAdapter adapter;
    EditText campoDeBusca;
    MangasDAO mangasDAO;
    int selected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_biblioteca);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        meusMangas = new ArrayList<>();
        mangasDAO = new MangasDAO();

        listaDeMangas = this.findViewById(R.id.listaMangas);

        this.selected = -1;

        campoDeBusca = (EditText) findViewById(R.id.campoDeBuscaBiblioteca);

        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, meusMangas);
        listaDeMangas.setAdapter(adapter);

        carregarMangasDoUsuario();

        campoDeBusca.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                filtrarMangas(editable.toString());
            }
        });

        listaDeMangas.setOnItemClickListener( new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3)
            {
                selected = position;
                Intent verManga = new Intent(Biblioteca.this, VerManga.class);
                verManga.putExtra("manga", meusMangas.get(selected));
                startActivityForResult(verManga,41);
            }
        } );
    }

    public void resetarMangas (View view) {
        carregarMangasDoUsuario();
    }

    protected void carregarMangasDoUsuario() {

        mangasDAO.carregarMangasDoUsuario(mAuth.getCurrentUser().getUid(), new MangasDAO.MangasLoadListener() {
            @Override
            public void onMangasLoaded(List<Manga> mangas) {
                meusMangas.clear();
                meusMangas.addAll(mangas);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onMangasLoadFailed() {
                Log.e("Biblioteca", "Falha ao carregar mangás do usuário");
                Toast.makeText(Biblioteca.this, "Falha ao carregar mangás do usuário", Toast.LENGTH_SHORT).show();
            }
        });
    }

    protected void filtrarMangas(String textoBusca) {
        List<Manga> mangasFiltrados = new ArrayList<>();

        for (Manga manga : meusMangas) {
            if (manga.getTitulo().toLowerCase().contains(textoBusca.toLowerCase())) {
                mangasFiltrados.add(manga);
            }
        }

        adapter.clear();
        adapter.addAll(mangasFiltrados);
        adapter.notifyDataSetChanged();
    }

    public void adicionarManga (View view) {
        carregarMangasDoUsuario();
        Intent intent = new Intent(this, AdicionarManga.class);
        startActivityForResult(intent,41);
    }

    public void fechaTelaMinhaBiblioteca (View view) {
        setResult(00);
        finish();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 41 && resultCode == 42) {
            carregarMangasDoUsuario();
        }
        else {
            Log.e("Result","Solicitação cancelada.");
        }
    }

}