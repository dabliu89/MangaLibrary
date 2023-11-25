package com.example.mangalibrary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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

import java.util.ArrayList;

public class Biblioteca extends AppCompatActivity {

    MangasDAO mangas;
    UsuariosDAO usuarios;
    String usuarioAtivo;
    ArrayList<String> todosOsMangas;
    ArrayList<Manga> resultadosDaBusca;
    ListView listaDeMangas;
    ArrayAdapter adapter;
    EditText campoDeBusca;
    int selected;
    int resultadoMudanca;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_biblioteca);
        this.selected = -1;
        Intent intent = getIntent();
        this.mangas = (MangasDAO) intent.getSerializableExtra("mangas");
        this.usuarios = (UsuariosDAO) intent.getSerializableExtra("usuarios");
        this.usuarioAtivo = (String) intent.getStringExtra("usuarioAtivo");
        campoDeBusca = (EditText) findViewById(R.id.campoDeBuscaBiblioteca);
        setListaMangasInicial();
        listaDeMangas.setOnItemClickListener( new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3)
            {
                selected = position;
                Intent verManga = new Intent(Biblioteca.this, VerManga.class);
                verManga.putExtra("manga", resultadosDaBusca.get(selected));
                verManga.putExtra("mangas", mangas);
                startActivityForResult(verManga,41);
            }
        } );
    }

    protected void setListaMangasInicial () {
        this.todosOsMangas = this.usuarios.buscarUser(usuarioAtivo).getMangas();
        resultadosDaBusca = new ArrayList<Manga>();
        for (String str : todosOsMangas) {
            for (Manga manga : this.mangas.mangasCadastrados) {
                if (str.equals(manga.isbn)) {
                    resultadosDaBusca.add(manga);
                }
            }
        }
        listaDeMangas = this.findViewById(R.id.listaMangas);
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, resultadosDaBusca);
        listaDeMangas.setAdapter(adapter);
    }

    public void buscarMangas (View view) {
        if (campoDeBusca.getText().toString().isEmpty()) {
            Log.e("CHECK","Termo de busca não encontrado.");
            Toast toast = Toast.makeText(this, "Insira um termo de busca.", Toast.LENGTH_LONG);
            toast.show();
            return;
        }
        String termoDeBusca = campoDeBusca.getText().toString();
        resultadosDaBusca.clear();
        for (String str : todosOsMangas) {
            for (Manga manga : this.mangas.mangasCadastrados) {
                if (manga.getIsbn().equals(str) && manga.getTitulo().contains(termoDeBusca)) {
                    resultadosDaBusca.add(manga);
                }
            }
        }
        this.adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, resultadosDaBusca);
        listaDeMangas.setAdapter(adapter);
    }

    public void resetarBusca (View view) {
        resultadosDaBusca.clear();
        for (String str : todosOsMangas) {
            for (Manga manga : this.mangas.mangasCadastrados) {
                if (str.equals(manga.isbn)) {
                    resultadosDaBusca.add(manga);
                }
            }
        }
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, resultadosDaBusca);
        listaDeMangas.setAdapter(adapter);
    }

    public void adicionarManga (View view) {
        Intent addManga = new Intent(this, AdicionarManga.class);
        addManga.putExtra("mangas", this.mangas);
        addManga.putExtra("usuarioMangas",this.todosOsMangas);
        startActivityForResult(addManga,41);
    }

    public void fechaTelaMinhaBiblioteca (View view) {
        if (this.resultadoMudanca == 42) {
            Intent intent = new Intent();
            intent.putExtra("mangas",this.mangas);
            intent.putExtra("usuarios",this.usuarios);
            setResult(resultadoMudanca,intent);
            finish();
        }
        setResult(00);
        finish();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 41 && resultCode == 42) {
            String recebeIsbnNovoManga = (String) data.getExtras().get("mangaIsbn");
            this.todosOsMangas.add(recebeIsbnNovoManga);
            this.usuarios.buscarUser(usuarioAtivo).setMangas(this.todosOsMangas);
            this.usuarios.buscarUser(usuarioAtivo).setUltimoCadastrado(recebeIsbnNovoManga);
            Log.e("CHECK DATA", "Mangás do usuário: " + this.todosOsMangas.size());
            resultadosDaBusca.clear();
            for (String str : this.todosOsMangas) {
                for (Manga manga : this.mangas.mangasCadastrados) {
                    if (str.equals(manga.isbn)) {
                        resultadosDaBusca.add(manga);
                    }
                }
            }
            adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, resultadosDaBusca);
            listaDeMangas.setAdapter(adapter);
            this.resultadoMudanca = 42;
            return;
        }
        if (requestCode == 41 && resultCode == 43) {
            Manga novoManga = (Manga) data.getExtras().get("manga");
            this.mangas.mangasCadastrados.add(novoManga);
            this.todosOsMangas.add(novoManga.getIsbn());
            this.usuarios.buscarUser(usuarioAtivo).setMangas(this.todosOsMangas);
            this.usuarios.buscarUser(usuarioAtivo).setUltimoCadastrado(novoManga.getIsbn());
            Log.e("CHECK DATA", "Mangás do usuário: " + this.todosOsMangas.size());
            resultadosDaBusca.clear();
            for (String str : todosOsMangas) {
                for (Manga manga : this.mangas.mangasCadastrados) {
                    if (str.equals(manga.isbn)) {
                        resultadosDaBusca.add(manga);
                    }
                }
            }
            adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, resultadosDaBusca);
            listaDeMangas.setAdapter(adapter);
            this.resultadoMudanca = 42;
            return;
        }
        if (requestCode == 41 && resultCode == 44) {
            Manga mangaEditado = (Manga) data.getExtras().get("manga");
            int indiceMangaEditado = (int) data.getExtras().get("indice");
            this.mangas.getMangasCadastrados().set(indiceMangaEditado,mangaEditado);
            resultadosDaBusca.clear();
            for (String str : todosOsMangas) {
                for (Manga manga : this.mangas.mangasCadastrados) {
                    if (str.equals(manga.isbn)) {
                        resultadosDaBusca.add(manga);
                    }
                }
            }
            adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, resultadosDaBusca);
            listaDeMangas.setAdapter(adapter);
            this.resultadoMudanca = 42;
            return;
        }
        if (requestCode == 41 && resultCode == 45) {
            int indiceMangaExcluido = (int) data.getExtras().get("indice");
            this.mangas.mangasCadastrados.remove(indiceMangaExcluido);
            resultadosDaBusca.clear();
            for (String str : todosOsMangas) {
                for (Manga manga : this.mangas.mangasCadastrados) {
                    if (str.equals(manga.isbn)) {
                        resultadosDaBusca.add(manga);
                    }
                }
            }
            adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, resultadosDaBusca);
            listaDeMangas.setAdapter(adapter);
            this.resultadoMudanca = 42;
            return;
        }
        else {
            Log.e("Result","Solicitação cancelada.");
        }
    }

}