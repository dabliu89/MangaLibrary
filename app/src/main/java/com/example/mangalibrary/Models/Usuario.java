package com.example.mangalibrary.Models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Usuario implements Serializable {

    public String id;
    public String nome;
    public String email;
    public ArrayList<String> amigos;
    public ArrayList<String> mangas;

    public Usuario(String id, String nome, String email) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.amigos = new ArrayList<String>();
        this.mangas = new ArrayList<String>();
    }

    public Usuario () {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String toString () {
        return this.nome;
    }
}