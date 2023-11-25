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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Cadastrar extends AppCompatActivity {

//    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar);

//        mAuth = FirebaseAuth.getInstance();
    }

    public void fazerCadastro (View view) {

        Intent intent = new Intent();
        EditText editNome, editEmail, editSenha;
        editNome = (EditText) findViewById(R.id.editNomeCadastro);
        editEmail = (EditText) findViewById(R.id.editEmailCadastro);
        editSenha = (EditText) findViewById(R.id.editSenhaCadastro);
        String nome = editNome.getText().toString();
        String email = editEmail.getText().toString();
        String senha = editSenha.getText().toString();
        if (nome.isEmpty() || email.isEmpty() || senha.isEmpty()) {
            Log.e("Check","Não satisfez os parâmetros vazios.");
            Toast toast = Toast.makeText(this, "Preencha todos os campos.", Toast.LENGTH_LONG);
            toast.show();
            return;
        }

//        mAuth.createUserWithEmailAndPassword(email,senha)
//                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                            @Override
//                            public void onComplete(@NonNull Task<AuthResult> task) {
//                                if (task.isSuccessful()) {
//                                    Usuario usuario = new Usuario(nome,email,senha,"","");
//
//                                }
//                                else {
//                                    Toast toast = Toast.makeText(Cadastrar.this,"Houve um problema na solicitação.",Toast.LENGTH_LONG);
//                                    toast.show();
//                                }
//                            }
//                        });

        intent.putExtra("nome",nome);
        intent.putExtra("email",email);
        intent.putExtra("senha",senha);
        setResult(12, intent);
        finish();
    }

    public void fechaTela (View view) {
        setResult(00);
        finish();
    }
}