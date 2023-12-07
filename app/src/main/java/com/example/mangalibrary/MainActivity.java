package com.example.mangalibrary;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mangalibrary.Mocks.MangasDAO;
import com.example.mangalibrary.Mocks.NoticiasDAO;
import com.example.mangalibrary.Mocks.UsuariosDAO;
import com.example.mangalibrary.Models.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.Iterator;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    private EditText editEmail, editSenha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        if (mAuth.getCurrentUser() != null) {
            Intent intent = new Intent(this, TelaPrincipalNavegavel.class);
            startActivityForResult(intent, 01);
        }

        setViews();

    }

    private void setViews() {
        editEmail = (EditText) findViewById(R.id.editEmail);
        editSenha = (EditText) findViewById(R.id.editSenha);
    }

    public void abrirCadastro (View view) {
        Intent intent = new Intent(this, Cadastrar.class);
        startActivity(intent);
    }

    public void abrirRecuperarSenha (View view) {
        Intent intent = new Intent(this, RecuperarSenha.class);
        startActivity(intent);
    }

    public void login (View view) {
        String email = editEmail.getText().toString();
        String senha = editSenha.getText().toString();

        if (email.isEmpty() || senha.isEmpty()) {
            Toast toast = Toast.makeText(this, "Preencha todos os campos.", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Intent intent = new Intent(getApplicationContext(), TelaPrincipalNavegavel.class);
                    Toast toast = Toast.makeText(MainActivity.this, "Login realizado com sucesso.", Toast.LENGTH_SHORT);
                    toast.show();
                    startActivityForResult(intent,01);
                } else {
                    Toast toast = Toast.makeText(MainActivity.this, "Houve um problema ao fazer o login.", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 01 && resultCode == 02) {
            FirebaseAuth.getInstance().signOut();
        }
        if (requestCode == 01 && resultCode == 99) {
            String uid = mAuth.getCurrentUser().getUid();
            Log.e("CHECK", uid);

            removerAmigoDosOutrosUsuarios(uid);

            db.collection("Usuarios").document(uid).delete();
            mAuth.getCurrentUser().delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Toast.makeText(MainActivity.this, "Conta excluída com sucesso.", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(MainActivity.this, "Houve um problema ao excluir a conta.", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    public void removerAmigoDosOutrosUsuarios(String idContaExcluida) {
        db.collection("Usuarios")
                .whereArrayContains("amigos", idContaExcluida)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            DocumentReference docRef = db.collection("Usuarios").document(document.getId());
                            docRef.update("amigos", FieldValue.arrayRemove(idContaExcluida))
                                    .addOnSuccessListener(aVoid -> {
                                        Log.d("INFO", "ID removido dos amigos do usuário: " + document.getId());
                                    })
                                    .addOnFailureListener(e -> {
                                        Log.e("ERROR", "Erro ao remover ID dos amigos do usuário: " + document.getId(), e);
                                    });
                        }
                    } else {
                        Log.e("ERROR", "Erro ao buscar usuários que contêm o ID excluído", task.getException());
                    }
                });
    }


}