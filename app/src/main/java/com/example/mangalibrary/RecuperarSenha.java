package com.example.mangalibrary;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class RecuperarSenha extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar_senha);
    }

    public void recuperaSenha (View view) {
        Intent intent = new Intent();
        EditText editEmailRecuperacao;
        editEmailRecuperacao = (EditText) findViewById(R.id.editEmailRecuperacao);
        String emailRecuperacao = editEmailRecuperacao.getText().toString();
        if (emailRecuperacao.isEmpty()) {
            Toast toast = Toast.makeText(this, "Preencha o campo.", Toast.LENGTH_LONG);
            toast.show();
            return;
        }
        intent.putExtra("emailRecuperacao",emailRecuperacao);
        setResult(22, intent);
        finish();
    }

    public void fechaTela (View view) {
        setResult(00);
        finish();
    }
}