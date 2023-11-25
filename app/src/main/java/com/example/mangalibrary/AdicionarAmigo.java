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

import com.example.mangalibrary.Mocks.UsuariosDAO;
import com.example.mangalibrary.Models.Usuario;

import java.util.ArrayList;
import java.util.Iterator;

public class AdicionarAmigo extends AppCompatActivity {

    UsuariosDAO usuarios;
    String usuarioAtivo;
    ArrayList<String> todosOsAmigos;
    ArrayList<Usuario> resultadosDaBuscaPorNovoAmigo;
    ArrayList<Usuario> resultadosDosNaoAmigos;
    ListView listaDeNovosAmigos;
    ArrayAdapter adapter;
    EditText campoDeBuscaPorNovoAmigo;
    int selected = -1;
    Usuario amigoParaAdicionar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_amigo);
        this.selected = -1;
        Intent intent = getIntent();
        this.usuarios = (UsuariosDAO) intent.getSerializableExtra("usuarios");
        this.todosOsAmigos = (ArrayList<String>) intent.getSerializableExtra("todosOsAmigos");
        this.usuarioAtivo = (String) intent.getStringExtra("usuarioAtivo");
        Log.e("CHECK DATA ADD",this.usuarioAtivo);
        campoDeBuscaPorNovoAmigo = (EditText) findViewById(R.id.campoDeBuscaNovoAmigo);
        setListaNovoAmigoInicial();
        listaDeNovosAmigos.setOnItemClickListener( new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3)
            {
                selected = position;
                amigoParaAdicionar = resultadosDaBuscaPorNovoAmigo.get(selected);
            }
        } );
    }

    private void setListaNovoAmigoInicial() {
        resultadosDaBuscaPorNovoAmigo = new ArrayList<Usuario>();
        resultadosDosNaoAmigos = new ArrayList<Usuario>();
        resultadosDosNaoAmigos.addAll(usuarios.usuariosAtivos);
        for (String str : todosOsAmigos) {
            Iterator<Usuario> iterator = resultadosDosNaoAmigos.iterator();
            while (iterator.hasNext()) {
                if (iterator.next().getEmail().equals(str)) {
                    iterator.remove();
                }
            }
        }
        resultadosDaBuscaPorNovoAmigo.addAll(resultadosDosNaoAmigos);
        listaDeNovosAmigos = this.findViewById(R.id.listaDeNovosAmigos);
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, resultadosDaBuscaPorNovoAmigo);
        listaDeNovosAmigos.setAdapter(adapter);
    }

    public void buscarNovoAmigo (View view) {
        if (campoDeBuscaPorNovoAmigo.getText().toString().isEmpty()) {
            Log.e("CHECK","Termo de busca não encontrado.");
            Toast toast = Toast.makeText(this, "Insira um termo de busca.", Toast.LENGTH_LONG);
            toast.show();
            return;
        }
        Log.e("ARRAY SIZE", ""+resultadosDaBuscaPorNovoAmigo.size());
        resultadosDaBuscaPorNovoAmigo.clear();
        Log.e("ARRAY SIZE 2", ""+resultadosDaBuscaPorNovoAmigo.size());

        resultadosDaBuscaPorNovoAmigo.addAll(resultadosDosNaoAmigos);
        Log.e("ARRAY SIZE 3", ""+resultadosDaBuscaPorNovoAmigo.size());
        Log.e("ARRAY SIZE NA", ""+resultadosDosNaoAmigos.size());

        String termoDeBusca = campoDeBuscaPorNovoAmigo.getText().toString();
        Log.e("ACHOU",termoDeBusca);
        Iterator<Usuario> iterator = resultadosDaBuscaPorNovoAmigo.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().getNome().contains(termoDeBusca) != true) {
                iterator.remove();
            }
        }
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, resultadosDaBuscaPorNovoAmigo);
        listaDeNovosAmigos.setAdapter(adapter);
    }

    public void resetarBuscaAmigos (View view) {
        resultadosDaBuscaPorNovoAmigo.clear();
        resultadosDaBuscaPorNovoAmigo.addAll(resultadosDosNaoAmigos);
        listaDeNovosAmigos = this.findViewById(R.id.listaDeNovosAmigos);
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, resultadosDaBuscaPorNovoAmigo);
        listaDeNovosAmigos.setAdapter(adapter);
    }

    public void adicionarAmigoSelecionado (View view) {
        if (selected == -1) {
            Toast toast = Toast.makeText(this, "Nenhum amigo foi selecionado.", Toast.LENGTH_LONG);
            toast.show();
            return;
        }
        Intent intent = new Intent();
        intent.putExtra("novoAmigo", this.amigoParaAdicionar);
        setResult(52,intent);
        finish();
    }

    public void fechaTelaNovoAmigo (View view) {
        setResult(00);
        finish();
    }

}