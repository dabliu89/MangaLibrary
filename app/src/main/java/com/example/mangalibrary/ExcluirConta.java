package com.example.mangalibrary;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

public class ExcluirConta extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_excluir_conta);
    }

    public void processarExclusaoDeConta (View view) {
        setResult(99);
        finish();
    }

    public void fechaTelaExcluirConta (View view) {
        setResult(00);
        finish();
    }
}