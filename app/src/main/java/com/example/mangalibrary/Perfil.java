package com.example.mangalibrary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mangalibrary.Models.Usuario;
import com.squareup.picasso.Picasso;

public class Perfil extends AppCompatActivity {

    Usuario usuario;
    TextView nome;
    TextView email;
    ImageView foto;
    int resultadoSolicitacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_perfil);
        Intent intent = getIntent();
        this.usuario = (Usuario) intent.getSerializableExtra("usuario");
        setPerfilViews();
        setPerfil();
    }

    private void setPerfil() {
        nome.setText(usuario.getNome());
        email.setText(usuario.getEmail());
        Picasso.get().load(usuario.getFoto()).into(foto);
    }

    private void setPerfilViews() {
        this.nome = this.findViewById(R.id.nomePerfilVerPerfil);
        this.email = this.findViewById(R.id.emailPerfilVerPerfil);
        this.foto = this.findViewById(R.id.imagemPerfilVerPerfil);
    }

    public void editarPerfil (View view) {
        Intent intent = new Intent(this, EditarPerfil.class);
        intent.putExtra("usuario",this.usuario);
        startActivityForResult(intent,61);
    }

    public void alterarSenha (View view) {
        Intent intent = new Intent(this, AlterarSenha.class);
        intent.putExtra("usuario",this.usuario);
        startActivityForResult(intent,61);
    }

    public void excluirConta (View view) {
        Intent intent = new Intent(this, ExcluirConta.class);
        intent.putExtra("usuario",this.usuario);
        startActivityForResult(intent,61);
    }

    public void fechaTelaVerPerfil (View view) {
        if (resultadoSolicitacao == 62) {
            Intent intent = new Intent();
            intent.putExtra("usuario",this.usuario);
            setResult(resultadoSolicitacao, intent);
            finish();
        }
        setResult(00);
        finish();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 61 && resultCode == 62) {
            String novoNome = (String) data.getExtras().get("novoNome");
            String novoEmail = (String) data.getExtras().get("novoEmail");
            this.usuario.setNome(novoNome);
            this.usuario.setEmail(novoEmail);
            setPerfil();
            this.resultadoSolicitacao = 62;
        }
        else if (requestCode == 61 && resultCode == 63) {
            String novaSenha = (String) data.getExtras().get("novaSenha");
            this.usuario.setSenha(novaSenha);
            this.resultadoSolicitacao = 62;
        }
        else if (requestCode == 61 && resultCode == 64) {
            setResult(99);
            finish();
        }
        else {
            Log.e("CHECK", "Operação cancelada.");
        }
    }

}