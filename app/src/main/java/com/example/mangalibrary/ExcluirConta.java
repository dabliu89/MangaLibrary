package com.example.mangalibrary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mangalibrary.Models.Usuario;

public class ExcluirConta extends AppCompatActivity {

    Usuario usuario;

    TextView senha;
    TextView repeteSenha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_excluir_conta);
        Intent intent = getIntent();
        this.usuario = (Usuario) intent.getSerializableExtra("usuario");
        setViews();
    }

    private void setViews() {
        this.senha = (TextView) findViewById(R.id.editSenhaExcluirConta);
        this.repeteSenha = (TextView) findViewById(R.id.editSenhaExcluirContaRepete);
    }

    public void processarExclusaoDeConta (View view) {
        if (this.senha.getText().toString().isEmpty() || this.repeteSenha.getText().toString().isEmpty()) {
            Toast toast = Toast.makeText(this, "Ação requer o preenchimento de todos os campos.", Toast.LENGTH_LONG);
            toast.show();
            return;
        }
        if (this.senha.getText().toString().equals(this.repeteSenha.getText().toString()) != true) {
            Toast toast = Toast.makeText(this, "A senha fornecida está diferente nos dois campos.", Toast.LENGTH_LONG);
            toast.show();
            return;
        }
        if ((this.senha.getText().toString().equals(this.usuario.getSenha()) && this.repeteSenha.getText().toString().equals(this.usuario.getSenha())) != true) {
            Toast toast = Toast.makeText(this, "Senha incorreta.", Toast.LENGTH_LONG);
            toast.show();
            return;
        }
        setResult(64);
        finish();
    }

    public void fechaTelaExcluirConta (View view) {
        setResult(00);
        finish();
    }
}