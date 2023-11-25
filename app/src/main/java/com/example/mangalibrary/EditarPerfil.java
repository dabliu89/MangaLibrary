package com.example.mangalibrary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mangalibrary.Models.Usuario;

public class EditarPerfil extends AppCompatActivity {

    Usuario usuario;
    TextView nome;
    TextView email;
    TextView senha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil);
        Intent intent = getIntent();
        this.usuario = (Usuario) intent.getSerializableExtra("usuario");
        setEditarPerfilViews();
        setEditarPerfil();
    }

    private void setEditarPerfil() {
        this.nome.setText(usuario.getNome());
        this.email.setText(usuario.getEmail());
    }

    private void setEditarPerfilViews() {
        this.nome = this.findViewById(R.id.editPerfilNome);
        this.email = this.findViewById(R.id.editPerfilEmail);
        this.senha = this.findViewById(R.id.editPerfilSenhaAtual);
    }

    public void processarEdicaoPerfil (View view) {
        if (this.nome.getText().toString().isEmpty() || this.email.getText().toString().isEmpty() || this.senha.getText().toString().isEmpty()){
            Toast toast = Toast.makeText(this, "Ação requer o preenchimento de todos os campos.", Toast.LENGTH_LONG);
            toast.show();
            return;
        }
        else if (this.senha.getText().toString().equals(usuario.getSenha()) != true) {
            Toast toast = Toast.makeText(this, "Senha incorreta.", Toast.LENGTH_LONG);
            toast.show();
            return;
        }
        Intent intent = new Intent();
        intent.putExtra("novoNome",this.nome.getText().toString());
        intent.putExtra("novoEmail",this.email.getText().toString());
        setResult(62,intent);
        finish();
    }

    public void fechaTelaVerPerfil (View view) {
        setResult(00);
        finish();
    }
}