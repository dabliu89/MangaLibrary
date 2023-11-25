package com.example.mangalibrary.Mocks;

import android.util.Log;

import com.example.mangalibrary.Models.Manga;
import com.example.mangalibrary.Models.Noticia;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;

public class MangasDAO implements Serializable {

    public ArrayList<Manga> mangasCadastrados;

    public MangasDAO () {
        this.mangasCadastrados = new ArrayList<Manga>();
        mangasCadastrados.add(new Manga("6555122110", "Demon Slayer #06", "200", "Panini", "31/05/2022",
                "https://images-americanas.b2w.io/produtos/1885003890/imagens/demon-slayer-kimetsu-no-yaiba-vol-06/1885003890_1_large.jpg"));
        mangasCadastrados.add(new Manga("8577879461","YuYu Hakusho #04", "200", "JBC", "07/03/2023",
                "https://jbchost.com.br/editorajbc/wp-content/uploads/2023/01/yuyu-hakusho-04-capa-770x1169.jpg"));
        mangasCadastrados.add(new Manga("8542615506","Lobo Solitário #14", "320", "Panini", "17/05/2019",
                "https://m.media-amazon.com/images/I/812JLsD8gpL._AC_UF1000,1000_QL80_.jpg"));
        mangasCadastrados.add(new Manga("6555128224","Berserk #40","176","Panini","30/03/2022",
                "https://m.media-amazon.com/images/I/61uOlsJQr3L._AC_UF894,1000_QL80_.jpg"));
        mangasCadastrados.add(new Manga("8542606051","One-Punch Man #07","216","Panini","08/12/2014",
                "https://m.media-amazon.com/images/I/81fd89HBiZL._SL1500_.jpg"));
        mangasCadastrados.add(new Manga("8545702876","Akira #01","352","JBC","22/08/2022",
                "https://m.media-amazon.com/images/I/61ud5BuLRML._SL1000_.jpg"));
        mangasCadastrados.add(new Manga("857787463X","Hunter x Hunter #28","200","JBC","06/05/2022",
                "https://m.media-amazon.com/images/I/71+dkJkj48L._SL1000_.jpg"));
        mangasCadastrados.add(new Manga("8545707290","FullMetal Alchemist #21","192","JBC","24/06/2022",
                "https://m.media-amazon.com/images/I/61loOqxgL4L._SL1000_.jpg"));
        Log.e("Data check", "Mangás cadastrados: " + mangasCadastrados.size());
    }

    public Manga buscarManga (String isbn) {
        for (Manga manga : mangasCadastrados) {
            if (manga.getIsbn().equals(isbn)){
                return manga;
            }
        }
        return null;
    }

    public ArrayList<Manga> getMangasCadastrados () {
        return this.mangasCadastrados;
    }

}