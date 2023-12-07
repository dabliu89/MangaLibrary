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

import com.example.mangalibrary.Mocks.NoticiasDAO;
import com.example.mangalibrary.Models.Noticia;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

public class VerNoticia extends AppCompatActivity {

    TextView tituloDaNoticia;
    ImageView imagemDaNoticia;
    TextView textoDaNoticia;
    Noticia noticia;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_noticia);

        db = FirebaseFirestore.getInstance();

        Intent intent = getIntent();
        setNoticiaViews();
        this.noticia = (Noticia) intent.getSerializableExtra("noticia");
        setNoticia(this.noticia);
    }

    private void setNoticiaViews() {
        this.tituloDaNoticia = this.findViewById(R.id.tituloDaNoticiaAberta);
        this.textoDaNoticia = this.findViewById(R.id.textoDaNoticiaAberta);
        this.imagemDaNoticia = this.findViewById(R.id.imagemDaNoticiaAbertaView);
    }

    private void setNoticia(Noticia noticia) {
        tituloDaNoticia.setText(noticia.getTitulo());
        textoDaNoticia.setText(noticia.getTexto());
        Picasso.get().load(noticia.getImagem()).into(imagemDaNoticia);
    }

    public void editarNoticia (View view) {
        Intent intent = new Intent(this, EditarNoticia.class);
        intent.putExtra("noticia", this.noticia);
        startActivityForResult(intent,31);
    }

    public void excluirNoticiaAberta (View view) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirmar exclusão");
        builder.setMessage("Tem certeza de que deseja excluir esta notícia?");

        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                DocumentReference noticiaRef = db.collection("Noticias").document(noticia.getIdNoticia());

                noticiaRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(VerNoticia.this, "Notícia excluída com sucesso!", Toast.LENGTH_SHORT).show();
                                setResult(32);
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(VerNoticia.this, "Erro ao excluir notícia: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                Log.e("AdicionarNoticia", "Erro ao excluir notícia", e);
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

    public void fechaTelaVerNoticia (View view) {
        setResult(00);
        finish();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 31 && resultCode == 33) {
            setResult(32);
            finish();
        }
        else {
            Log.e("STATUS CHECK","Solicitação cancelada.");
        }
    }

}