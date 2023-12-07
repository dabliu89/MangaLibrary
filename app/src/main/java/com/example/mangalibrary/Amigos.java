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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Amigos extends AppCompatActivity {

    ArrayList<Usuario> resultadosDaBuscaPorAmigos;
    ListView listaDeAmigos;
    ArrayAdapter adapter;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    String userId;
    EditText campoDeBuscaPorAmigos;
    int selected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amigos);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        listaDeAmigos = findViewById(R.id.listaDeAmigos);
        resultadosDaBuscaPorAmigos = new ArrayList<Usuario>();
        this.selected = -1;
        userId = mAuth.getCurrentUser().getUid();
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
                startActivityForResult(verAmigo,51);
            }
        } );
    }

    private void setListaAmigosInicial() {
        resultadosDaBuscaPorAmigos.clear();
        DocumentReference currentUserRef = db.collection("Usuarios").document(userId);

        currentUserRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    List<String> amigos = (List<String>) document.get("amigos");
                    if (amigos != null && !amigos.isEmpty()) {
                        for (String amigoId : amigos) {
                            DocumentReference amigoRef = db.collection("Usuarios").document(amigoId);
                            amigoRef.get().addOnCompleteListener(amigoTask -> {
                                if (amigoTask.isSuccessful()) {
                                    DocumentSnapshot amigoDocument = amigoTask.getResult();
                                    if (amigoDocument.exists()) {
                                        Usuario amigo = amigoDocument.toObject(Usuario.class);
                                        amigo.setId(amigoDocument.getId());
                                        resultadosDaBuscaPorAmigos.add(amigo);
                                        adapter.notifyDataSetChanged();
                                    }
                                }
                            });
                        }
                    } else {
                        Toast.makeText(Amigos.this, "O usuário logado não possui amigos.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.e("CHECK", "O documento do usuário logado não existe");
                }
            } else {
                Log.e("CHECK", "Falha ao obter o documento do usuário logado");
            }
        });
         adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, resultadosDaBuscaPorAmigos);
         listaDeAmigos.setAdapter(adapter);
    }


    public void buscarAmigos(View view) {
        if (campoDeBuscaPorAmigos.getText().toString().isEmpty()) {
            Toast toast = Toast.makeText(this, "Insira um termo de busca.", Toast.LENGTH_LONG);
            toast.show();
            return;
        }

        String termoDeBusca = campoDeBuscaPorAmigos.getText().toString().toLowerCase();

        ArrayList<Usuario> amigosEncontrados = new ArrayList<>();

        for (Usuario amigo : resultadosDaBuscaPorAmigos) {
            String nomeAmigo = amigo.getNome().toLowerCase();

            if (nomeAmigo.contains(termoDeBusca)) {
                amigosEncontrados.add(amigo);
            }
        }

        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, amigosEncontrados);
        listaDeAmigos.setAdapter(adapter);
    }


    public void resetarBuscaAmigos (View view) {
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, resultadosDaBuscaPorAmigos);
        listaDeAmigos.setAdapter(adapter);
    }

    public void adicionarAmigo (View view) {
        Intent intent = new Intent(this, AdicionarAmigo.class);
        startActivityForResult(intent,51);
    }

    public void fechaTelaMeusAmigos (View view) {
        setResult(00);
        finish();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 51 && resultCode == 52) {
            setListaAmigosInicial();
        }
        else {
            Log.e("Result","Solicitação cancelada.");
        }
    }

}