package com.example.mangalibrary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class AlterarSenha extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser fuser;
    private FirebaseFirestore db;
    TextView novaSenha;
    TextView novaSenhaRepete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alterar_senha);
        setViews();

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        fuser = mAuth.getCurrentUser();

    }

    private void setViews() {
        this.novaSenha = (TextView) findViewById(R.id.editNovaSenha);
        this.novaSenhaRepete = (TextView) findViewById(R.id.editNovaSenhaRepete);
    }

    public void processarAlteracaoDeSenha (View view) {
        if (this.novaSenha.getText().toString().isEmpty() || this.novaSenhaRepete.getText().toString().isEmpty()) {
            Toast toast = Toast.makeText(this, "Ação requer o preenchimento de todos os campos.", Toast.LENGTH_LONG);
            toast.show();
            return;
        }
        else if (this.novaSenha.getText().toString().equals(this.novaSenhaRepete.getText().toString()) != true) {
            Toast toast = Toast.makeText(this, "Nova senha fornecida está diferente nos dois campos.", Toast.LENGTH_LONG);
            toast.show();
            return;
        }
        fuser.updatePassword(this.novaSenha.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast toast = Toast.makeText(AlterarSenha.this, "Senha alterada com sucesso.", Toast.LENGTH_LONG);
                toast.show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast toast = Toast.makeText(AlterarSenha.this, "Houve um problema ao tentar mudar a senha.", Toast.LENGTH_LONG);
                toast.show();
            }
        });
        finish();
    }

    public void fechaTelaAlterarSenha (View view) {
        setResult(00);
        finish();
    }
}