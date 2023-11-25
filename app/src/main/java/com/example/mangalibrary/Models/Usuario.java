package com.example.mangalibrary.Models;

import java.io.Serializable;
import java.util.ArrayList;

public class Usuario implements Serializable {


    public String nome;
    public String email;

    public String senha;
    public String foto;
    public ArrayList<String> amigos;
    public ArrayList<String> mangas;
    public String ultimoCadastrado;

    public Usuario(String nome, String email, String senha, String foto, String ultimoCadastrado) {

        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.foto = foto;
        this.amigos = new ArrayList<String>();
        this.amigos.add(email);
        this.mangas = new ArrayList<String>();
        this.ultimoCadastrado = ultimoCadastrado;
    }



    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public ArrayList<String> getAmigos() {
        return amigos;
    }

    public void setAmigos(ArrayList<String> amigos) {
        this.amigos = amigos;
    }

    public ArrayList<String> getMangas() {
        return mangas;
    }

    public void setMangas(ArrayList<String> mangas) {
        this.mangas = mangas;
    }

    public String getUltimoCadastrado() {
        return ultimoCadastrado;
    }

    public void setUltimoCadastrado(String ultimoCadastrado) {
        this.ultimoCadastrado = ultimoCadastrado;
    }

    public String toString () {
        return this.nome;
    }
}