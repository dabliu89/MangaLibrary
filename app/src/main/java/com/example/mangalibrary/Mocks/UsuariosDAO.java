package com.example.mangalibrary.Mocks;

import android.util.Log;

import com.example.mangalibrary.Models.Usuario;

import java.io.Serializable;
import java.util.ArrayList;

public class UsuariosDAO implements Serializable {

    public ArrayList<Usuario> usuariosAtivos;

    public UsuariosDAO() {
        this.usuariosAtivos = new ArrayList<Usuario>();
        this.usuariosAtivos.add(new Usuario("Shinobu", "shinobu@kny.com", "123",
                "https://i.pinimg.com/1200x/71/fb/6f/71fb6f819c8801da07b6c3ee75a9c8d9.jpg", ""));
        this.usuariosAtivos.add(new Usuario("Uraraka", "uraraka@mha.com", "123",
                "https://criticalhits.com.br/wp-content/uploads/2021/10/nd2aj168wq621-910x512.jpg", ""));
        this.usuariosAtivos.add(new Usuario("Saitama", "saitama@opm.com", "123",
                "https://i1.sndcdn.com/artworks-000161780989-7q7o4w-t500x500.jpg", ""));
        this.usuariosAtivos.add(new Usuario("Edward", "elric@fma.com", "123",
                "https://i.pinimg.com/736x/05/5f/50/055f505cadf998aeb9a05f130d07d747.jpg", ""));
        this.usuariosAtivos.add(new Usuario("Gojou", "satoru@jujutsu.com", "123",
                "https://vocesabianime.com/wp-content/uploads/2022/10/Producao-de-Jujutsu-Kaisen-0-tomou-cuidado-com-os-Olhos-do-Gojo.jpg", ""));

        ArrayList<String> mangas = new ArrayList<String>();
        mangas.add("6555122110");
        mangas.add("8542606051");
        this.usuariosAtivos.get(0).setMangas(mangas);
        ArrayList<String> mangas1 = new ArrayList<String>();
        mangas1.add("8577879461");
        mangas1.add("8545702876");
        this.usuariosAtivos.get(1).setMangas(mangas1);
        ArrayList<String> mangas2 = new ArrayList<String>();
        mangas2.add("8542615506");
        mangas2.add("857787463X");
        this.usuariosAtivos.get(2).setMangas(mangas2);
        ArrayList<String> mangas3 = new ArrayList<String>();
        mangas3.add("6555128224");
        mangas3.add("8545707290");
        this.usuariosAtivos.get(3).setMangas(mangas3);
        ArrayList<String> mangas4 = new ArrayList<String>();
        mangas4.add("8545707290");
        this.usuariosAtivos.get(4).setMangas(mangas4);

        this.usuariosAtivos.get(0).getAmigos().add("saitama@opm.com");
        this.usuariosAtivos.get(0).getAmigos().add("elric@fma.com");
        this.usuariosAtivos.get(1).getAmigos().add("saitama@opm.com");
        this.usuariosAtivos.get(1).getAmigos().add("elric@fma.com");
        this.usuariosAtivos.get(2).getAmigos().add("shinobu@kny.com");
        this.usuariosAtivos.get(2).getAmigos().add("uraraka@mha.com");
        this.usuariosAtivos.get(3).getAmigos().add("uraraka@mha.com");
        this.usuariosAtivos.get(3).getAmigos().add("shinobu@kny.com");

        this.usuariosAtivos.get(0).setUltimoCadastrado("6555122110");
        this.usuariosAtivos.get(1).setUltimoCadastrado("8577879461");
        this.usuariosAtivos.get(2).setUltimoCadastrado("8542615506");
        this.usuariosAtivos.get(3).setUltimoCadastrado("6555128224");
        this.usuariosAtivos.get(4).setUltimoCadastrado("8545707290");

        Log.e("Data check", "Usuarios ativos: " + usuariosAtivos.size());
    }

    public void cadastrarUsuario (String nome, String email, String senha) {
        Usuario novoUser = new Usuario(nome,email,senha,"@drawable/ic_profile_blank","");
        this.addUsuarioAtivo(novoUser);
    }

    public void addUsuarioAtivo(Usuario user) {
        this.usuariosAtivos.add(user);
        Log.e("New user", "Usuário adicionado com sucesso!");
    }

    public Usuario buscarUser(String email) {
        for (Usuario usuario:this.usuariosAtivos) {
            if (email.equals(usuario.getEmail() )) {
                return usuario;
            }
        }
        return null;
    }

    public int getUsuariosAtivos () {
        return usuariosAtivos.size();
    }

}