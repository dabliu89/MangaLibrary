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
import android.widget.Toast;

import com.example.mangalibrary.Mocks.MangasDAO;
import com.example.mangalibrary.Mocks.UsuariosDAO;
import com.example.mangalibrary.Models.Manga;
import com.example.mangalibrary.Models.Noticia;
import com.example.mangalibrary.Models.Usuario;

import java.util.ArrayList;
import java.util.Iterator;

public class Amigos extends AppCompatActivity {

    UsuariosDAO usuarios;
    MangasDAO mangas;
    String usuarioAtivo;
    ArrayList<String> todosOsAmigos;
    ArrayList<Usuario> resultadosDaBuscaPorAmigos;
    ListView listaDeAmigos;
    ArrayAdapter adapter;
    EditText campoDeBuscaPorAmigos;
    int selected;
    int resultadoMudanca;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amigos);
        this.selected = -1;
        Intent intent = getIntent();
        this.usuarios = (UsuariosDAO) intent.getSerializableExtra("usuarios");
        this.mangas = (MangasDAO) intent.getSerializableExtra("mangas");
        this.usuarioAtivo = (String) intent.getStringExtra("usuarioAtivo");
        Log.e("CHECK DATA",this.usuarioAtivo);
        campoDeBuscaPorAmigos = (EditText) findViewById(R.id.campoDeBuscaAmigos);
        setListaAmigosInicial();
        listaDeAmigos.setOnItemClickListener( new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3)
            {
                selected = position;
                Intent verAmigo = new Intent(Amigos.this, VerAmigo.class);
                verAmigo.putExtra("amigo", resultadosDaBuscaPorAmigos.get(selected));
                verAmigo.putExtra("mangas", mangas);
                startActivityForResult(verAmigo,51);
            }
        } );
    }

    private void setListaAmigosInicial() {
        this.todosOsAmigos = new ArrayList<String>();
        this.todosOsAmigos.addAll(this.usuarios.buscarUser(usuarioAtivo).getAmigos());
        resultadosDaBuscaPorAmigos = new ArrayList<Usuario>();
        for (String str : todosOsAmigos) {
            for (Usuario user : this.usuarios.usuariosAtivos) {
                if (str.equals(user.email) && str.equals(this.usuarioAtivo) != true) {
                    resultadosDaBuscaPorAmigos.add(user);
                }
            }
        }
        listaDeAmigos = this.findViewById(R.id.listaDeAmigos);
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, resultadosDaBuscaPorAmigos);
        listaDeAmigos.setAdapter(adapter);
    }

    public void buscarAmigos (View view) {
        if (campoDeBuscaPorAmigos.getText().toString().isEmpty()) {
            Log.e("CHECK","Termo de busca não encontrado.");
            Toast toast = Toast.makeText(this, "Insira um termo de busca.", Toast.LENGTH_LONG);
            toast.show();
            return;
        }
        String termoDeBusca = campoDeBuscaPorAmigos.getText().toString();
        resultadosDaBuscaPorAmigos.clear();
        for (String str : todosOsAmigos) {
            for (Usuario user : this.usuarios.usuariosAtivos) {
                if (user.getEmail().equals(str) && user.getNome().contains(termoDeBusca)) {
                    resultadosDaBuscaPorAmigos.add(user);
                }
            }
        }
        this.adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, resultadosDaBuscaPorAmigos);
        listaDeAmigos.setAdapter(adapter);
    }

    public void resetarBuscaAmigos (View view) {
        resultadosDaBuscaPorAmigos.clear();
        for (String str : todosOsAmigos) {
            for (Usuario user : this.usuarios.usuariosAtivos) {
                if (str.equals(user.email) && str.equals(usuarioAtivo) != true) {
                    resultadosDaBuscaPorAmigos.add(user);
                }
            }
        }
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, resultadosDaBuscaPorAmigos);
        listaDeAmigos.setAdapter(adapter);
    }

    public void adicionarAmigo (View view) {
        Intent addAmigo = new Intent(this, AdicionarAmigo.class);
        addAmigo.putExtra("usuarios", this.usuarios);
        addAmigo.putExtra("todosOsAmigos",this.todosOsAmigos);
        addAmigo.putExtra("usuarioAtivo", this.usuarioAtivo);
        startActivityForResult(addAmigo,51);
    }

    public void fechaTelaMeusAmigos (View view) {
        if (resultadoMudanca == 52) {
            Intent intent = new Intent();
            intent.putExtra("usuarios",usuarios);
            setResult(resultadoMudanca,intent);
            finish();
        }
        setResult(00);
        finish();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 51 && resultCode == 52) {
            Usuario recebeNovoAmigo = (Usuario) data.getExtras().get("novoAmigo");
            usuarios.buscarUser(usuarioAtivo).getAmigos().add(recebeNovoAmigo.getEmail());
            usuarios.buscarUser(recebeNovoAmigo.getEmail()).getAmigos().add(usuarioAtivo);
            todosOsAmigos.clear();
            Log.e("ARRAY SIZE", ""+todosOsAmigos.size());
            todosOsAmigos.addAll(usuarios.buscarUser(usuarioAtivo).getAmigos());
            Log.e("ARRAY SIZE", ""+todosOsAmigos.size());
            resultadosDaBuscaPorAmigos.clear();
            for (String str : todosOsAmigos) {
                for (Usuario user : this.usuarios.usuariosAtivos) {
                    if (str.equals(user.email) && str.equals(usuarioAtivo) != true) {
                        resultadosDaBuscaPorAmigos.add(user);
                    }
                }
            }
            adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, resultadosDaBuscaPorAmigos);
            listaDeAmigos.setAdapter(adapter);
            Toast toast = Toast.makeText(this, "Amigo adicionado com sucesso.", Toast.LENGTH_LONG);
            toast.show();
            resultadoMudanca = 52;
        }
        if (requestCode == 51 && resultCode == 53) {
            String amigoParaExlusao = (String) data.getExtras().get("amigoParaExclusão");
            for (Usuario user : usuarios.usuariosAtivos) {
                if (user.getEmail().equals(usuarioAtivo)) {
                    Iterator<String> iterator = user.getAmigos().iterator();
                    while (iterator.hasNext()) {
                        if (iterator.next().contains(amigoParaExlusao)) {
                            iterator.remove();
                            break;
                        }
                    }
                }
            }
            for (Usuario user : usuarios.usuariosAtivos) {
                if (user.getEmail().equals(amigoParaExlusao)) {
                    Iterator<String> iterator = user.getAmigos().iterator();
                    while (iterator.hasNext()) {
                        if (iterator.next().contains(usuarioAtivo)) {
                            iterator.remove();
                            break;
                        }
                    }
                }
            }
            todosOsAmigos.clear();
            Log.e("ARRAY SIZE", ""+todosOsAmigos.size());
            todosOsAmigos.addAll(usuarios.buscarUser(usuarioAtivo).getAmigos());
            Log.e("ARRAY SIZE", ""+todosOsAmigos.size());
            resultadosDaBuscaPorAmigos.clear();
            for (String str : todosOsAmigos) {
                for (Usuario user : this.usuarios.usuariosAtivos) {
                    if (str.equals(user.email) && str.equals(usuarioAtivo) != true) {
                        resultadosDaBuscaPorAmigos.add(user);
                    }
                }
            }
            adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, resultadosDaBuscaPorAmigos);
            listaDeAmigos.setAdapter(adapter);
            resultadoMudanca = 52;
            Toast toast = Toast.makeText(this, "Amizade desfeita com sucesso.", Toast.LENGTH_LONG);
            toast.show();
        }
        else {
            Log.e("Result","Solicitação cancelada.");
        }
    }
}