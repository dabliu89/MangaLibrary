package com.example.mangalibrary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mangalibrary.Mocks.MangasDAO;
import com.example.mangalibrary.Models.Manga;
import com.example.mangalibrary.Models.Usuario;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class VerAmigo extends AppCompatActivity {

    MangasDAO mangas;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    Usuario amigoEmVisualizacao;
    ImageView fotoAmigo;
    TextView nomeAmigo;
    TextView emailAmigo;
    ListView colecaoAmigo;
    ArrayList<Manga> todosMangasAmigoEmVisualizacao;
    ArrayAdapter adapter;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_amigo);
        db = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        mangas = new MangasDAO();
        Intent intent = getIntent();
        amigoEmVisualizacao = (Usuario) intent.getSerializableExtra("amigo");
        todosMangasAmigoEmVisualizacao = new ArrayList<Manga>();
        setAmigoViews();
        setAmigo(amigoEmVisualizacao);
        setMangas();
    }

    private void setAmigoViews() {
        fotoAmigo = (ImageView) findViewById(R.id.imagemPerfilVerAmigo);
        nomeAmigo = (TextView) findViewById(R.id.nomeAmigoVerAmigo);
        emailAmigo = (TextView) findViewById(R.id.emailAmigoVerAmigo);
        colecaoAmigo = (ListView) findViewById(R.id.listViewMangasVerAmigo);
    }

    private void setAmigo(Usuario amigoEmVisualizacao) {
        nomeAmigo.setText(amigoEmVisualizacao.getNome());
        emailAmigo.setText(amigoEmVisualizacao.getEmail());
        StorageReference profileRef = storageReference.child("users/"+amigoEmVisualizacao.getId()+"/profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(fotoAmigo);
            }
        });
    }

    protected void setMangas () {
        mangas.carregarMangasDoUsuario(amigoEmVisualizacao.getId(), new MangasDAO.MangasLoadListener() {
            @Override
            public void onMangasLoaded(List<Manga> mangas) {
                todosMangasAmigoEmVisualizacao.addAll(mangas);
                adapter = new ArrayAdapter(VerAmigo.this, android.R.layout.simple_list_item_1, todosMangasAmigoEmVisualizacao);
                colecaoAmigo.setAdapter(adapter);
            }
            @Override
            public void onMangasLoadFailed() {
                Log.e("CHECK","Houve um problema ao tentar recuperar os mangás do amigo.");
            }
        });
    }

    public void desfazerAmizade(View view) {
        String amigoId = amigoEmVisualizacao.getId();

        DocumentReference currentUserRef = db.collection("Usuarios").document(mAuth.getCurrentUser().getUid());

        DocumentReference amigoRef = db.collection("Usuarios").document(amigoId);

        currentUserRef.update("amigos", FieldValue.arrayRemove(amigoId))
                .addOnSuccessListener(aVoid -> {
                    Log.d("CHECK", "Amigo removido da lista de amigos do usuário atual");

                    amigoRef.update("amigos", FieldValue.arrayRemove(mAuth.getCurrentUser().getUid()))
                            .addOnSuccessListener(aVoid1 -> {
                                Log.d("CHECK", "Usuário atual removido da lista de amigos do amigo");
                                Toast toast = Toast.makeText(this, "Amigo removido com sucesso.", Toast.LENGTH_SHORT);
                                toast.show();
                                setResult(52);
                                finish();
                            })
                            .addOnFailureListener(e -> {
                                Log.e("CHECK", "Erro ao remover usuário atual da lista de amigos do amigo", e);
                                Toast toast = Toast.makeText(this, "Falha ao remover o amigo.", Toast.LENGTH_SHORT);
                                toast.show();
                            });
                })
                .addOnFailureListener(e -> {
                    Log.e("CHECK", "Erro ao remover amigo da lista de amigos do usuário atual", e);
                });
    }

    public void fechaTelaVerAmigo (View view) {
        setResult(00);
        finish();
    }
}