package com.example.mangalibrary.Mocks;

import android.util.Log;
import android.widget.Toast;

import com.example.mangalibrary.Models.Noticia;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.List;

public class NoticiasDAO {

    public interface NoticiasLoadListener {
        void onNoticiasLoaded(List<Noticia> noticias);
        void onNoticiasLoadFailed();
    }

    public void carregarNoticias(NoticiasLoadListener listener) {
        List<Noticia> noticias = new ArrayList<>();
        CollectionReference noticiasRef = FirebaseFirestore.getInstance().collection("Noticias");

        noticiasRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (DocumentSnapshot document : task.getResult()) {
                    String idDoDocumento = document.getId();
                    String titulo = document.getString("titulo");
                    String imagem = document.getString("imagem");
                    String texto = document.getString("texto");
                    Noticia noticia = new Noticia(idDoDocumento, titulo, imagem, texto);
                    noticias.add(noticia);
                }
                listener.onNoticiasLoaded(noticias);
            } else {
                listener.onNoticiasLoadFailed();
            }
        });
    }

    public void atualizarNoticiaNoFirestore(String idNoticia, String novoTitulo, String novaImagem, String novoTexto) {
        DocumentReference noticiaRef = FirebaseFirestore.getInstance().collection("Noticias").document(idNoticia);

        noticiaRef.update("titulo", novoTitulo, "imagem", novaImagem, "texto", novoTexto)
                .addOnSuccessListener(aVoid -> {
                    Log.e("CHECK", "Notícia atualizada com sucesso!");

                })
                .addOnFailureListener(e -> {
                    Log.e("AdicionarNoticia", "Erro ao atualizar notícia", e);
                });
    }

}