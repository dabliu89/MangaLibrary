package com.example.mangalibrary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.mangalibrary.Mocks.MangasDAO;
import com.example.mangalibrary.Models.Manga;
import com.example.mangalibrary.Models.Usuario;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class VerAmigo extends AppCompatActivity {

    MangasDAO mangas;
    Usuario amigoEmVisualizacao;
    ImageView fotoAmigo;
    TextView nomeAmigo;
    TextView emailAmigo;
    ListView colecaoAmigo;
    ArrayList<Manga> todosMangasAmigoEmVisualizacao;
    ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_amigo);
        Intent intent = getIntent();
        amigoEmVisualizacao = (Usuario) intent.getSerializableExtra("amigo");
        mangas = (MangasDAO) intent.getSerializableExtra("mangas");
        todosMangasAmigoEmVisualizacao = new ArrayList<Manga>();
        setAmigoViews();
        setAmigo(amigoEmVisualizacao);
    }

    private void setAmigo(Usuario amigoEmVisualizacao) {
        nomeAmigo.setText(amigoEmVisualizacao.getNome());
        emailAmigo.setText(amigoEmVisualizacao.getEmail());
        Picasso.get().load(amigoEmVisualizacao.getFoto()).into(fotoAmigo);
        for (Manga manga : mangas.getMangasCadastrados()) {
            for (String string : amigoEmVisualizacao.getMangas()) {
                if (manga.getIsbn().contains(string)) {
                    todosMangasAmigoEmVisualizacao.add(manga);
                }
            }
        }
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, todosMangasAmigoEmVisualizacao);
        colecaoAmigo.setAdapter(adapter);
    }

    private void setAmigoViews() {
        fotoAmigo = (ImageView) findViewById(R.id.imagemPerfilVerAmigo);
        nomeAmigo = (TextView) findViewById(R.id.nomeAmigoVerAmigo);
        emailAmigo = (TextView) findViewById(R.id.emailAmigoVerAmigo);
        colecaoAmigo = (ListView) findViewById(R.id.listViewMangasVerAmigo);
    }

    public void desfazerAmizade (View view) {
        Intent intent = new Intent();
        intent.putExtra("amigoParaExclusão", amigoEmVisualizacao.getEmail());
        setResult(53,intent);
        finish();
    }

    public void fechaTelaVerAmigo (View view) {
        setResult(00);
        finish();
    }
}