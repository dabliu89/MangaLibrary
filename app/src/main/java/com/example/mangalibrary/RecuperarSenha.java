package com.example.mangalibrary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

public class RecuperarSenha extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText editEmailRecuperacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar_senha);

        mAuth = FirebaseAuth.getInstance();

        editEmailRecuperacao = (EditText) findViewById(R.id.editEmailRecuperacao);

    }

    public void recuperaSenha (View view) {
        String emailRecuperacao = editEmailRecuperacao.getText().toString();
        if (emailRecuperacao.isEmpty()) {
            Toast toast = Toast.makeText(this, "Preencha o campo.", Toast.LENGTH_LONG);
            toast.show();
            return;
        }
        mAuth.sendPasswordResetEmail(emailRecuperacao).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast toast = Toast.makeText(RecuperarSenha.this, "Link de recuperação enviado. Verifique sua caixa de e-mail.", Toast.LENGTH_LONG);
                toast.show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast toast = Toast.makeText(RecuperarSenha.this, "Ocorreu um erro! O link de recuperação não foi enviado.", Toast.LENGTH_LONG);
                toast.show();
            }
        });
        finish();
    }

    public void fechaTela (View view) {
        finish();
    }

}