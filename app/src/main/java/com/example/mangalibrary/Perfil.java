package com.example.mangalibrary;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mangalibrary.Models.Usuario;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;

public class Perfil extends AppCompatActivity {


    TextView nome;
    TextView email;
    ImageView foto;
    FirebaseAuth mAuth;
    FirebaseFirestore db;
    StorageReference storageReference;
    ListenerRegistration registration;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_perfil);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        userId = mAuth.getCurrentUser().getUid();

        setPerfilViews();
        setPerfil();
    }

    private void setPerfil() {
        DocumentReference documentReference = db.collection("Usuarios").document(userId);
        this.registration = documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                nome.setText(value.getString("nome"));
                email.setText(value.getString("email"));
            }
        });
        StorageReference profileRef = storageReference.child("users/"+mAuth.getCurrentUser().getUid()+"/profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(foto);
            }
        });
    }

    private void setPerfilViews() {
        this.nome = this.findViewById(R.id.nomePerfilVerPerfil);
        this.email = this.findViewById(R.id.emailPerfilVerPerfil);
        this.foto = this.findViewById(R.id.imagemPerfilVerPerfil);
    }

    public void alterarFotoDePerfil (View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Escolha uma opção");
        String[] opcoes = {"Câmera", "Galeria"};

        builder.setItems(opcoes, (dialog, which) -> {
            switch (which) {
                case 0:
                    Intent intentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intentCamera, 112);
                    break;
                case 1:
                    Intent intentGaleria = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intentGaleria,111);
                    break;
            }
        });

        builder.show();

    }

    public void editarPerfil (View view) {
        Intent intent = new Intent(this, EditarPerfil.class);
        startActivity(intent);
    }

    public void alterarSenha (View view) {
        Intent intent = new Intent(this, AlterarSenha.class);
        startActivity(intent);
    }

    public void excluirConta (View view) {
        Intent intent = new Intent(this, ExcluirConta.class);
        startActivityForResult(intent,61);
    }

    public void fechaTelaVerPerfil (View view) {
        finish();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 111 && resultCode == Activity.RESULT_OK) {
            Uri imageUri = data.getData();
            uploadImagemNoFirebase(imageUri);
        }
        if (requestCode == 112 && resultCode == Activity.RESULT_OK) {
            Bitmap imagemCapturada = (Bitmap) data.getExtras().get("data");
            uploadImagemNoFirebase(imagemCapturada);
        }
        if (requestCode == 61 && resultCode == 99) {
            setResult(99);
            finish();
        }
        else {
            Log.e("CHECK", "Operação cancelada.");
        }
    }

    private void uploadImagemNoFirebase(Uri imageUri) {
        StorageReference fileRef = storageReference.child("users/"+mAuth.getCurrentUser().getUid()+"/profile.jpg");
        fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).into(foto);
                    }
                });
                Toast toast = Toast.makeText(Perfil.this,"Upload de imagem concluído.",Toast.LENGTH_SHORT);
                toast.show();
                setResult(62);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast toast = Toast.makeText(Perfil.this,"Houve um problema ao fazer o upload.",Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }

    private void uploadImagemNoFirebase(Bitmap imagemCapturada) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imagemCapturada.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        StorageReference fileRef = storageReference.child("users/"+mAuth.getCurrentUser().getUid()+"/profile.jpg");

        fileRef.putBytes(data).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).into(foto);
                    }
                });
                Toast toast = Toast.makeText(Perfil.this,"Upload de imagem concluído.",Toast.LENGTH_SHORT);
                toast.show();
                setResult(62);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast toast = Toast.makeText(Perfil.this,"Houve um problema ao fazer o upload.",Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }

}