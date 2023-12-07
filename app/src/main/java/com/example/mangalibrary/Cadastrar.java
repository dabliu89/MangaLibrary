package com.example.mangalibrary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mangalibrary.Models.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.ktx.Firebase;

import java.util.HashMap;
import java.util.Map;

public class Cadastrar extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private FirebaseFirestore db;

    private EditText editNome, editEmail, editSenha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        setViews();
    }

    private void setViews() {
        editNome = (EditText) findViewById(R.id.editNomeCadastro);
        editEmail = (EditText) findViewById(R.id.editEmailCadastro);
        editSenha = (EditText) findViewById(R.id.editSenhaCadastro);
    }

    public void fazerCadastro (View view) {

        String nome = editNome.getText().toString().trim();
        String email = editEmail.getText().toString().trim();
        String senha = editSenha.getText().toString().trim();
        if (nome.isEmpty() || email.isEmpty() || senha.isEmpty()) {
            Toast toast = Toast.makeText(this, "Preencha todos os campos.", Toast.LENGTH_LONG);
            toast.show();
            return;
        }
        if (senha.length() < 6) {
            editSenha.setError("A senha deve ter pelo menos 6 caracteres.");
        }

        mAuth.createUserWithEmailAndPassword(email,senha)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {

                                    FirebaseUser user = mAuth.getCurrentUser();
                                    user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast toast = Toast.makeText(Cadastrar.this,"Um e-mail de verificação foi enviado.",Toast.LENGTH_LONG);
                                            toast.show();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.d("CHECK", "E-mail não enviado.");
                                        }
                                    });

                                    Toast toast = Toast.makeText(Cadastrar.this,"Usuário cadastrado com sucesso.",Toast.LENGTH_LONG);
                                    toast.show();
                                    String userId = mAuth.getCurrentUser().getUid();
                                    DocumentReference documentReference = db.collection("Usuarios").document(userId);
                                    Map<String,Object> usuario = new HashMap<>();
                                    usuario.put("nome",nome);
                                    usuario.put("email",email);
                                    documentReference.set(usuario).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Log.d("CHECK","Salvou na Database." + userId);
                                        }
                                    });
                                    finish();
                                }
                                else {
                                    Toast toast = Toast.makeText(Cadastrar.this,"Houve um problema na solicitação.",Toast.LENGTH_LONG);
                                    toast.show();
                                }
                            }
                        });

    }

    public void fechaTela (View view) {
        setResult(00);
        finish();
    }
}