package com.example.mangalibrary;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mangalibrary.Mocks.MangasDAO;
import com.example.mangalibrary.Mocks.NoticiasDAO;
import com.example.mangalibrary.Mocks.UsuariosDAO;
import com.example.mangalibrary.Models.Usuario;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Iterator;

public class MainActivity extends AppCompatActivity {

    UsuariosDAO usuarios;
    MangasDAO mangas;
    NoticiasDAO noticias;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        usuarios = new UsuariosDAO();
        mangas = new MangasDAO();
        noticias = new NoticiasDAO();

        Log.e("Data check", "Usuarios ativos: " + usuarios.getUsuariosAtivos());
    }

    public void abrirCadastro (View view) {
        Intent intent = new Intent(this, Cadastrar.class);
        startActivityForResult(intent, 11);
    }

    public void abrirRecuperarSenha (View view) {
        Intent intent = new Intent(this, RecuperarSenha.class);
        startActivityForResult(intent, 21);
    }

    public void login (View view) {
        EditText editEmail, editSenha;
        editEmail = (EditText) findViewById(R.id.editEmail);
        editSenha = (EditText) findViewById(R.id.editSenha);
        String email = editEmail.getText().toString();
        String senha = editSenha.getText().toString();
        if (this.validarLogin (email, senha)) {
            Toast toast = Toast.makeText(this, "Login realizado com sucesso.", Toast.LENGTH_LONG);
            toast.show();
            Intent intent = new Intent(this, TelaPrincipalNavegavel.class);
            Usuario user = usuarios.buscarUser(email);
            String emailPass = user.getEmail();
            intent.putExtra("usuarioLogado", emailPass);
            intent.putExtra("usuarios", usuarios);
            intent.putExtra("mangas", mangas);
            intent.putExtra("noticias", noticias);
            startActivityForResult(intent, 01);
        }
        else {
            Toast toast = Toast.makeText(this, "Usuário ou senha não encontrados.", Toast.LENGTH_LONG);
            toast.show();
        }
    }

    public boolean validarLogin (String email, String senha) {
        Usuario usuarioEmValidacao = this.usuarios.buscarUser(email);
        if (usuarioEmValidacao != null) {
            if (usuarioEmValidacao.getSenha().equals(senha)) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 11 && resultCode == 12) {
            String nome = (String) data.getExtras().get("nome");
            String email = (String) data.getExtras().get("email");
            String senha = (String) data.getExtras().get("senha");
            usuarios.cadastrarUsuario(nome,email,senha);
            Toast toast = Toast.makeText(this, "Cadastro realizado com sucesso!", Toast.LENGTH_LONG);
            toast.show();
            Log.e("Check","Novo usuário adicionado.");
            Log.e("Data check", "Usuarios ativos: " + usuarios.getUsuariosAtivos());
        }

        else if (requestCode == 21 && resultCode == 22) {
            String emailRecuperacao = (String) data.getExtras().get("emailRecuperacao");
            if(usuarios.buscarUser(emailRecuperacao) == null) {
                // envia o email.
                Log.e("Check","Email não encontrado na base de dados.");
            }
            Log.e("Check","Email encontrado na base de dados.");
            Toast toast = Toast.makeText(this, "E-mail de recuperação de senha enviado.", Toast.LENGTH_LONG);
            toast.show();
        }

        else if (requestCode == 01 && resultCode == 02) {
            this.usuarios = (UsuariosDAO) data.getExtras().get("usuarios");
            this.noticias = (NoticiasDAO) data.getExtras().get("noticias");
            this.mangas = (MangasDAO) data.getExtras().get("mangas");
        }

        else if (requestCode == 01 && resultCode == 99) {
            this.usuarios = (UsuariosDAO) data.getExtras().get("usuarios");
            this.noticias = (NoticiasDAO) data.getExtras().get("noticias");
            this.mangas = (MangasDAO) data.getExtras().get("mangas");
            Usuario user = (Usuario) data.getExtras().get("usuarioExclusao");
            Log.e("CHECK DATA",user.getNome());
            Iterator<Usuario> iterator = this.usuarios.usuariosAtivos.iterator();
            while (iterator.hasNext()) {
                if (iterator.next().getEmail().equals(user.getEmail())) {
                    iterator.remove();
                    break;
                }
            }
//            this.usuarios.usuariosAtivos.remove(user);
        }

        else {
            Log.e("Result","Solicitação cancelada.");
        }
    }

}