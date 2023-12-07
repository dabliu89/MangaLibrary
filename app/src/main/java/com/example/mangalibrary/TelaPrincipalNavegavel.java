package com.example.mangalibrary;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mangalibrary.Mocks.NoticiasDAO;
import com.example.mangalibrary.Models.Noticia;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class TelaPrincipalNavegavel extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    StorageReference storageReference;

    ListenerRegistration registration;

    String userId;

    ImageView homeProfilePic;
    TextView avisoVerificarEmail;
    ListView listaNoticiasView;
    ArrayList<Noticia> listaNoticias;
    ArrayAdapter adapter;
    int selected;
    FirebaseUser fuser;
    NoticiasDAO noticiasDAO;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_principal_navegavel);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        userId = mAuth.getCurrentUser().getUid();

        listaNoticiasView = findViewById(R.id.listaNoticias);
        listaNoticias = new ArrayList<>();
        noticiasDAO = new NoticiasDAO();

        progressBar = findViewById(R.id.progressBarCarregamento);

        new LoadDataAsync().execute();

        fuser = mAuth.getCurrentUser();

        avisoVerificarEmail = findViewById(R.id.avisoVerificarEmail);

        if (fuser != null && fuser.isEmailVerified()) {
            avisoVerificarEmail.setVisibility(View.GONE);
        }

        selected = -1;

        listaNoticiasView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                selected = position;
                Intent verNoticia = new Intent(TelaPrincipalNavegavel.this, VerNoticia.class);
                verNoticia.putExtra("noticia", listaNoticias.get(selected));
                startActivityForResult(verNoticia, 31);
            }
        });
    }

    private class LoadDataAsync extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            buscarNoticias();
            setInformacaoUsuario();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressBar.setVisibility(View.GONE);
        }
    }

    private void buscarNoticias() {
        listaNoticias.clear();

        noticiasDAO.carregarNoticias(new NoticiasDAO.NoticiasLoadListener() {
            @Override
            public void onNoticiasLoaded(List<Noticia> noticias) {
                listaNoticias.addAll(noticias);
                adapter = new ArrayAdapter<>(TelaPrincipalNavegavel.this, android.R.layout.simple_list_item_1, listaNoticias);
                listaNoticiasView.setAdapter(adapter);
            }

            @Override
            public void onNoticiasLoadFailed() {
                Toast.makeText(TelaPrincipalNavegavel.this, "Não foi possível recuperar as notícias.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setInformacaoUsuario() {

        homeProfilePic = findViewById(R.id.homeProfileWelcome);
        StorageReference profileRef = storageReference.child("users/" + mAuth.getCurrentUser().getUid() + "/profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(uri -> Picasso.get().load(uri).into(homeProfilePic));

        TextView usuarioWelcome = findViewById(R.id.homeNomeWelcome);
        DocumentReference documentReference = db.collection("Usuarios").document(userId);
        registration = documentReference.addSnapshotListener(this, (value, error) -> {
            if (value != null) {
                usuarioWelcome.setText(value.getString("nome"));
            } else {
                Log.e("Error", "Snapshot null: " + error);
            }
        });
    }

    public void validarEmail() {
        if (fuser != null) {
            fuser.sendEmailVerification().addOnSuccessListener(unused -> {
                Toast.makeText(TelaPrincipalNavegavel.this, "Um e-mail de verificação foi enviado.", Toast.LENGTH_SHORT).show();
            }).addOnFailureListener(e -> {
                Log.d("CHECK", "E-mail não enviado: " + e.getMessage());
            });
        }
    }

    public void novaNoticia (View view) {
        Intent intent = new Intent(this, AdicionarNoticia.class);
        startActivityForResult(intent, 31);
    }

    public void verBiblioteca (View view) {
        Intent intent = new Intent(this, Biblioteca.class);
        startActivity(intent);
    }

    public void verAmigos (View view) {
        Intent intent = new Intent(this, Amigos.class);
        startActivity(intent);
    }

    public void verLojas (View view) {
        Intent verLojas = new Intent(this, Lojas.class);
        startActivity(verLojas);
    }

    public void verPerfil (View view) {
        Intent verPerfil = new Intent(this, Perfil.class);
        startActivityForResult(verPerfil,61);
    }

    public void logout (View view) {
        setResult(02);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (registration != null) {
            registration.remove();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 61 && resultCode == 99) {
            setResult(99);
            finish();
        }
        else if (requestCode == 61 && resultCode == 62) {
            setInformacaoUsuario();
        }
        else if (requestCode == 31 && resultCode == 32) {
            buscarNoticias();
        }
        else {
            Log.e("Result","Solicitação cancelada.");
        }
    }

}