package com.example.mangalibrary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mangalibrary.Mocks.AvaliacoesDAO;
import com.example.mangalibrary.Mocks.MangasDAO;
import com.example.mangalibrary.Mocks.NoticiasDAO;
import com.example.mangalibrary.Mocks.UsuariosDAO;
import com.example.mangalibrary.Models.Noticia;
import com.example.mangalibrary.Models.Usuario;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Iterator;

public class TelaPrincipalNavegavel extends AppCompatActivity {

    private UsuariosDAO usuarios;
    private MangasDAO mangas;
    private NoticiasDAO noticias;
    private String usuarioAtivo;

    ImageView capaHomeUltimo;
    TextView tituloHomeUltimo;
    TextView paginasHomeUltimo;
    TextView editoraHomeUltimo;
    TextView dataPublicacaoHomeUltimo;
    TextView isbnHomeUltimo;
    ArrayList<Noticia> todasNoticias;
    ListView listaNoticias;
    ArrayAdapter adapter;
    int selected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_principal_navegavel);
        Intent intent = getIntent();
        this.mangas = (MangasDAO) intent.getSerializableExtra("mangas");
        this.noticias = (NoticiasDAO) intent.getSerializableExtra("noticias");
        this.usuarioAtivo = intent.getStringExtra("usuarioLogado");
        Log.e("CHECK", usuarioAtivo);
        this.usuarios = (UsuariosDAO) intent.getSerializableExtra("usuarios");
        if (this.mangas == null) {
            Log.e("CHECK", "mangas null");
        }
        if (this.noticias == null) {
            Log.e("CHECK", "noticias null");
        }
        if (this.usuarios == null) {
            Log.e("CHECK", "usuarios null");
        }
        setInformacaoUsuario(this.usuarioAtivo);
        setInformacaoUltimoMangaViews();
        setInformacaoUltimoManga(this.usuarios.buscarUser(this.usuarioAtivo).getUltimoCadastrado());
        setUltimasNoticias();

        selected = -1;

        listaNoticias.setOnItemClickListener( new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3)
            {
                selected = position;
                Intent verNoticia = new Intent(TelaPrincipalNavegavel.this, VerNoticia.class);
                verNoticia.putExtra("noticia", noticias.getNoticias().get(selected));
                verNoticia.putExtra("position", selected);
                startActivityForResult(verNoticia,31);
            }
        } );
    }

    private void setInformacaoUltimoMangaViews () {
        this.capaHomeUltimo = this.findViewById(R.id.capaHomeUltimo);
        this.tituloHomeUltimo = this.findViewById(R.id.tituloHomeUltimo);
        this.paginasHomeUltimo = this.findViewById(R.id.paginasHomeUltimo);
        this.editoraHomeUltimo = this.findViewById(R.id.editoraHomeUltimo);
        this.dataPublicacaoHomeUltimo = this.findViewById(R.id.dataPublicacaoHomeUltimo);
        this.isbnHomeUltimo = this.findViewById(R.id.isbnHomeUltimo);
    }

    private void setInformacaoUsuario (String email) {
        String nomeUsuarioAtual = this.usuarios.buscarUser(email).getNome();
        ImageView usuarioFoto = this.findViewById(R.id.homeProfileWelcome);
        TextView usuarioWelcome = this.findViewById(R.id.homeNomeWelcome);
        String changeFoto = this.usuarios.buscarUser(email).getFoto();
        Picasso.get().load(changeFoto).into(usuarioFoto);
        usuarioWelcome.setText(nomeUsuarioAtual + ".");
    }

    private void setInformacaoUltimoManga(String ultimoCadastrado) {
        setInformacaoUltimoMangaChanges();
    }

    private void setInformacaoUltimoMangaChanges () {
        String changeCapa = this.mangas.buscarManga(this.usuarios.buscarUser(this.usuarioAtivo).getUltimoCadastrado()).getCapa();
        String changeTituloHome = this.mangas.buscarManga(this.usuarios.buscarUser(this.usuarioAtivo).getUltimoCadastrado()).getTitulo();
        String changePaginasHome = this.mangas.buscarManga(this.usuarios.buscarUser(this.usuarioAtivo).getUltimoCadastrado()).getPaginas();
        String changeEditoraHome = this.mangas.buscarManga(this.usuarios.buscarUser(this.usuarioAtivo).getUltimoCadastrado()).getEditora();
        String changeDataPublicacaoHome = this.mangas.buscarManga(this.usuarios.buscarUser(this.usuarioAtivo).getUltimoCadastrado()).getDataPublicacao();
        String changeIsbnHome = this.mangas.buscarManga(this.usuarios.buscarUser(this.usuarioAtivo).getUltimoCadastrado()).getIsbn();
        setInformacaoUltimoMangaModify(changeCapa,changeTituloHome,changePaginasHome,changeEditoraHome,changeDataPublicacaoHome,changeIsbnHome);
    }

    private void setInformacaoUltimoMangaModify(String changeCapa, String changeTituloHome, String changePaginasHome,
                                                String changeEditoraHome, String changeDataPublicacaoHome, String changeIsbnHome) {
        Picasso.get().load(changeCapa).into(this.capaHomeUltimo);
        tituloHomeUltimo.setText(changeTituloHome);
        String paginasTexto = "" + changePaginasHome;
        paginasHomeUltimo.setText(" " + paginasTexto);
        editoraHomeUltimo.setText(" " + changeEditoraHome);
        dataPublicacaoHomeUltimo.setText(" " + changeDataPublicacaoHome);
        isbnHomeUltimo.setText(" " + changeIsbnHome);
    }

    private void setUltimasNoticias () {
        this.todasNoticias = noticias.getNoticias();
        this.listaNoticias = this.findViewById(R.id.listaNoticias);
        this.adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, todasNoticias);
        listaNoticias.setAdapter(adapter);
    }

    public void novaNoticia (View view) {
        Intent intent = new Intent(this, AdicionarNoticia.class);
        startActivityForResult(intent, 31);
    }

    public void verBiblioteca (View view) {
        Intent verBiblioteca = new Intent(this, Biblioteca.class);
        verBiblioteca.putExtra("mangas", mangas);
        verBiblioteca.putExtra("usuarios", this.usuarios);
        verBiblioteca.putExtra("usuarioAtivo", usuarioAtivo);
        startActivityForResult(verBiblioteca,41);
    }

    public void verAmigos (View view) {
        Intent verAmigos = new Intent(this, Amigos.class);
        verAmigos.putExtra("usuarios", this.usuarios);
        verAmigos.putExtra("mangas",this.mangas);
        verAmigos.putExtra("usuarioAtivo", this.usuarioAtivo);
        startActivityForResult(verAmigos,51);
    }

    public void verLojas (View view) {
        Intent verLojas = new Intent(this, Lojas.class);
        startActivity(verLojas);
    }

    public void verPerfil (View view) {
        Intent verPerfil = new Intent(this, Perfil.class);
        verPerfil.putExtra("usuario",usuarios.buscarUser(usuarioAtivo));
        startActivityForResult(verPerfil,61);
    }

    public void logout (View view) {
        Intent intent = new Intent();
        intent.putExtra("usuarios",this.usuarios);
        intent.putExtra("mangas",this.mangas);
        intent.putExtra("noticias",this.noticias);
        setResult(02,intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 31 && resultCode == 32) {
            String recebeNoticiaTitulo = (String) data.getExtras().get("tituloDaNovaNoticia");
            String recebeNoticiaImagem = (String) data.getExtras().get("imagemDaNovaNoticia");
            String recebeNoticiaTexto = (String) data.getExtras().get("textoDaNovaNoticia");
            noticias.adicionarNoticia(recebeNoticiaTitulo,recebeNoticiaImagem,recebeNoticiaTexto);
            Log.e("DATA CHECK", "Total de notícias cadastradas: " + noticias.getNoticias().size());
            setUltimasNoticias();
        }
        if (requestCode == 31 && resultCode == 33) {
            Noticia noticia = (Noticia) data.getExtras().get("noticiaEditada");
            int indiceNoticiaEdicao = (int) data.getExtras().get("posicaoDaNoticia");
            noticias.getNoticias().set(indiceNoticiaEdicao,noticia);
            Log.e("STATUS CHECK", "Notícia editada com sucesso.");
            Log.e("DATA CHECK", "Total de notícias cadastradas: " + noticias.getNoticias().size());
            setUltimasNoticias();
        }
        if (requestCode == 31 && resultCode == 34) {
            int indiceNoticiaEdicao = (int) data.getExtras().get("posicaoDaNoticia");
            noticias.getNoticias().remove(indiceNoticiaEdicao);
            Log.e("STATUS CHECK", "Notícia excluída com sucesso.");
            Log.e("DATA CHECK", "Total de notícias cadastradas: " + noticias.getNoticias().size());
            setUltimasNoticias();
        }
        if (requestCode == 41 && resultCode == 42) {
            MangasDAO mangas = (MangasDAO) data.getExtras().get("mangas");
            UsuariosDAO usuarios = (UsuariosDAO) data.getExtras().get("usuarios");
            this.mangas = mangas;
            this.usuarios = usuarios;
            setInformacaoUltimoManga(usuarioAtivo);
        }
        if (requestCode == 51 && resultCode == 52) {
            UsuariosDAO usuarios = (UsuariosDAO) data.getExtras().get("usuarios");
            this.usuarios = usuarios;
        }
        if (requestCode == 61 && resultCode == 62) {
            Usuario usuario = (Usuario) data.getExtras().get("usuario");
            for (int i = 0; i < this.usuarios.usuariosAtivos.size(); i++) {
                if (this.usuarios.usuariosAtivos.get(i).getEmail().equals(usuarioAtivo)) {
                    this.usuarios.usuariosAtivos.set(i,usuario);
                    this.usuarios.usuariosAtivos.get(i).getAmigos().set(0,usuario.getEmail());
                    this.usuarioAtivo = usuario.getEmail();
                    setInformacaoUsuario(this.usuarioAtivo);
                    break;
                }
            }
        }
        if (requestCode == 61 && resultCode == 99) {
            for (Usuario user : this.usuarios.usuariosAtivos) {
                if (user.getEmail() != this.usuarioAtivo) {
                    user.getAmigos().remove("usuarioAtivo");
                }
            }
            Intent intent = new Intent();
            intent.putExtra("usuarios",this.usuarios);
            intent.putExtra("mangas",this.mangas);
            intent.putExtra("noticias",this.noticias);
            intent.putExtra("usuarioExclusao",this.usuarios.buscarUser(this.usuarioAtivo));
            setResult(99,intent);
            finish();
        }
        else {
            Log.e("Result","Solicitação cancelada.");
        }
    }



}