package com.example.mangalibrary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.mangalibrary.Mocks.UsuariosDAO;
import com.example.mangalibrary.Models.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AdicionarAmigo extends AppCompatActivity {

    List<Usuario> resultadosFiltrados;
    ArrayList<Usuario> resultadosDaBuscaPorNovoAmigo;
    ListView listaDeNovosAmigos;
    ArrayAdapter adapter;
    EditText campoDeBuscaPorNovoAmigo;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    String userId;
    int selected = -1;
    Button btn;
    boolean filtro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_amigo);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        userId = mAuth.getCurrentUser().getUid();
        resultadosDaBuscaPorNovoAmigo = new ArrayList<>();
        resultadosFiltrados = new ArrayList<>();
        btn = findViewById(R.id.button6);
        filtro = false;

        this.selected = -1;

        listaDeNovosAmigos = findViewById(R.id.listaDeNovosAmigos);
        campoDeBuscaPorNovoAmigo = findViewById(R.id.campoDeBuscaNovoAmigo);
        setListaNovoAmigoInicial();
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, resultadosDaBuscaPorNovoAmigo);
        listaDeNovosAmigos.setAdapter(adapter);
        listaDeNovosAmigos.setOnItemClickListener( new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3)
            {
                selected = position;
            }
        } );
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adicionarAmigoSelecionado();
            }
        });
    }

    private void setListaNovoAmigoInicial() {
        db.collection("Usuarios")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<Usuario> usuarios = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Usuario usuario = document.toObject(Usuario.class);
                            usuario.setId(document.getId());
                            usuarios.add(usuario);
                        }
                        resultadosDaBuscaPorNovoAmigo = (ArrayList<Usuario>) usuarios;

                        adapter = new ArrayAdapter<>(AdicionarAmigo.this, android.R.layout.simple_list_item_1, resultadosDaBuscaPorNovoAmigo);
                        listaDeNovosAmigos.setAdapter(adapter);
                    } else {
                        Log.e("CHECK", "Falha ao obter os usuários");
                    }
                });
    }


    public void buscarNovoAmigo(View view) {
        String termoDeBusca = campoDeBuscaPorNovoAmigo.getText().toString().trim();
        if (termoDeBusca.isEmpty()) {
            Toast.makeText(this, "Insira um termo de busca.", Toast.LENGTH_LONG).show();
            return;
        }

        this.resultadosFiltrados.clear();

        for (Usuario usuario : resultadosDaBuscaPorNovoAmigo) {
            if (usuario.getNome().toLowerCase().contains(termoDeBusca.toLowerCase())) {
                resultadosFiltrados.add(usuario);
            }
        }

        if (resultadosFiltrados.isEmpty()) {
            Toast.makeText(AdicionarAmigo.this, "Nenhum usuário encontrado.", Toast.LENGTH_LONG).show();
        }

        adapter = new ArrayAdapter<>(AdicionarAmigo.this, android.R.layout.simple_list_item_1, resultadosFiltrados);
        listaDeNovosAmigos.setAdapter(adapter);
        filtro = true;
    }


    public void resetarBuscaAmigos (View view) {
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, resultadosDaBuscaPorNovoAmigo);
        listaDeNovosAmigos.setAdapter(adapter);
        filtro = false;
    }

    public void adicionarAmigoSelecionado () {
        if (selected == -1) {
            Toast.makeText(this, "Nenhum amigo foi selecionado.", Toast.LENGTH_LONG).show();
            return;
        }

        Usuario amigoSelecionado;

        if (filtro) {
            amigoSelecionado = resultadosFiltrados.get(selected);
        }
        else {
            amigoSelecionado = resultadosDaBuscaPorNovoAmigo.get(selected);
        }

        if (amigoSelecionado.getId() == null) {
            Log.e("CHECK","ID ESTÁ NULO");
            return;
        }

        if (amigoSelecionado.getId().equals(userId)) {
            Toast.makeText(this, "Você não pode se adicionar como amigo.", Toast.LENGTH_SHORT).show();
            return;
        }

        DocumentReference currentUserRef = db.collection("Usuarios").document(userId);

        currentUserRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                List<String> amigos = (List<String>) documentSnapshot.get("amigos");
                if (amigos != null && amigos.contains(amigoSelecionado.getId())) {
                    Toast.makeText(this, "Este usuário já está na sua lista de amigos.", Toast.LENGTH_SHORT).show();
                } else {
                    adicionarAmigo(amigoSelecionado.getId());
                    setResult(52);
                    finish();
                }
            } else {
                Log.d("TAG", "O documento não existe");
            }
        }).addOnFailureListener(e -> {
            Log.d("TAG", "Erro ao obter documento", e);
        });
    }


    private void adicionarAmigo(String amigoId) {
        DocumentReference currentUserRef = db.collection("Usuarios").document(userId);

        currentUserRef.update("amigos", FieldValue.arrayUnion(amigoId))
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(AdicionarAmigo.this, "Amigo adicionado com sucesso.", Toast.LENGTH_SHORT).show();

                    DocumentReference amigoRef = db.collection("Usuarios").document(amigoId);
                    amigoRef.update("amigos", FieldValue.arrayUnion(userId))
                            .addOnSuccessListener(aVoid1 -> {
                                Log.e("CHECK","Sucesso ao adicionar.");
                            })
                            .addOnFailureListener(e -> {
                                Log.e("CHECK","Falha ao adicionar.");
                            });

                })
                .addOnFailureListener(e -> {
                    Toast.makeText(AdicionarAmigo.this, "Erro ao adicionar amigo.", Toast.LENGTH_SHORT).show();
                });
    }


    public void fechaTelaNovoAmigo (View view) {
        setResult(00);
        finish();
    }

}