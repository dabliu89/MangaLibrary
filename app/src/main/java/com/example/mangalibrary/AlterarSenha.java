package com.example.mangalibrary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mangalibrary.Models.Usuario;

public class AlterarSenha extends AppCompatActivity {

    Usuario usuario;
    TextView novaSenha;
    TextView novaSenhaRepete;
    TextView senhaAtual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alterar_senha);
        Intent intent = getIntent();
        this.usuario = (Usuario) intent.getSerializableExtra("usuario");
        setViews();
    }

    private void setViews() {
        this.novaSenha = (TextView) findViewById(R.id.editNovaSenha);
        this.novaSenhaRepete = (TextView) findViewById(R.id.editNovaSenhaRepete);
        this.senhaAtual = (TextView) findViewById(R.id.editAlterarSenhaAtual);
    }

    public void processarAlteracaoDeSenha (View view) {
        if (this.novaSenha.getText().toString().isEmpty() || this.novaSenhaRepete.getText().toString().isEmpty() || this.senhaAtual.getText().toString().isEmpty()) {
            Toast toast = Toast.makeText(this, "Ação requer o preenchimento de todos os campos.", Toast.LENGTH_LONG);
            toast.show();
            return;
        }
        else if (this.novaSenha.getText().toString().equals(this.novaSenhaRepete.getText().toString()) != true) {
            Toast toast = Toast.makeText(this, "Nova senha fornecida está diferente nos dois campos.", Toast.LENGTH_LONG);
            toast.show();
            return;
        }
        else if (this.senhaAtual.getText().toString().equals(this.usuario.getSenha()) != true) {
            Toast toast = Toast.makeText(this, "A senha atual fornecida está incorreta.", Toast.LENGTH_LONG);
            toast.show();
            return;
        }
        Intent intent = new Intent();
        intent.putExtra("novaSenha", this.novaSenha.getText().toString());
        setResult(63, intent);
        finish();
    }

    public void fechaTelaAlterarSenha (View view) {
        setResult(00);
        finish();
    }
}