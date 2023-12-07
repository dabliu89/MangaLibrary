package com.example.mangalibrary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mangalibrary.Models.Manga;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

public class VerManga extends AppCompatActivity {

    TextView tituloDoManga;
    ImageView imagemDoManga;
    TextView totalDePaginasDoManga;
    TextView editoraDoManga;
    TextView dataPublicacaoDoManga;
    TextView isbnDoManga;
    Manga manga;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_manga);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        Intent intent = getIntent();
        this.manga = (Manga) intent.getSerializableExtra("manga");
        setMangaViews();
        setManga(this.manga);
    }

    private void setMangaViews() {
        this.tituloDoManga = (TextView) findViewById(R.id.tituloMangaVerManga);
        this.imagemDoManga = (ImageView) findViewById(R.id.imageViewMangaVerManga);
        this.totalDePaginasDoManga = (TextView) findViewById(R.id.paginasMangaVerManga);
        this.editoraDoManga = (TextView) findViewById(R.id.editoraMangaVerManga);
        this.dataPublicacaoDoManga = (TextView) findViewById(R.id.dataDaPublicacaoMangaVerManga);
        this.isbnDoManga = (TextView) findViewById(R.id.isbnMangaVerManga);
    }

    private void setManga(Manga manga) {
        tituloDoManga.setText(manga.getTitulo());
        totalDePaginasDoManga.append(manga.getPaginas());
        editoraDoManga.append(manga.getEditora());
        dataPublicacaoDoManga.append(manga.getDataPublicacao());
        isbnDoManga.append(manga.getIsbn());
        Picasso.get().load(manga.getCapa()).into(imagemDoManga);
    }

    public void editarMangaAberto (View view) {
        Intent intent = new Intent(this, EditarManga.class);
        intent.putExtra("manga",this.manga);
        startActivityForResult(intent,41);
    }

    public void excluirMangaAberto (View view) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirmar exclusão");
        builder.setMessage("Tem certeza de que deseja excluir este mangá?");

        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                DocumentReference userDocRef = db.collection("Usuarios").document(mAuth.getCurrentUser().getUid());

                userDocRef.update("mangas", FieldValue.arrayRemove(manga.getIsbn())).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.e("CHECK", "Mangá removido do usuário");
                        Toast.makeText(VerManga.this, "Mangá excluída com sucesso!", Toast.LENGTH_SHORT).show();
                        setResult(42);
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(VerManga.this, "Erro ao excluir Mangá: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("CHECK", "Erro ao excluir Mangá", e);
                    }
                });
            }
        });

        builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

    }

    public void fechaTelaVerManga (View view) {
        setResult(00);
        finish();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 41 && resultCode == 42) {
            setResult(42);
            finish();
        }
        else {
            Log.e("CHECK","Solicitação cancelada.");
        }
    }
}