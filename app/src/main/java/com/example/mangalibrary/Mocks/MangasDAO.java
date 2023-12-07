package com.example.mangalibrary.Mocks;

import android.util.Log;

import com.example.mangalibrary.Models.Manga;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class MangasDAO {

    public interface MangasLoadListener {
        void onMangasLoaded(List<Manga> mangas);
        void onMangasLoadFailed();
    }

    public interface MangaLoadListener {
        void onMangaLoaded(Manga manga);
        void onMangaLoadFailed();
    }

    public interface DocumentExistenceListener {
        void onDocumentExists(boolean exists);
        void onCheckFailed();
    }

    public interface MangaAddListener {
        void onMangaAdded();
        void onAddMangaFailed();
    }

    public void carregarMangasDoUsuario(String userId, MangasLoadListener listener) {
        List<Manga> mangas = new ArrayList<>();
        DocumentReference usuarioRef = FirebaseFirestore.getInstance().collection("Usuarios").document(userId);

        usuarioRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<String> mangasDoUsuario = (List<String>) task.getResult().get("mangas");

                if (mangasDoUsuario != null && !mangasDoUsuario.isEmpty()) {
                    for (String mangaId : mangasDoUsuario) {
                        DocumentReference mangaRef = FirebaseFirestore.getInstance().collection("Mangas").document(mangaId);
                        mangaRef.get().addOnCompleteListener(mangaTask -> {
                            if (mangaTask.isSuccessful()) {
                                DocumentSnapshot mangaDoc = mangaTask.getResult();
                                if (mangaDoc.exists()) {
                                    String isbn = mangaId;
                                    String titulo = mangaDoc.getString("titulo");
                                    String paginas = mangaDoc.getString("paginas");
                                    String editora = mangaDoc.getString("editora");
                                    String dataPublicacao = mangaDoc.getString("dataPublicacao");
                                    String capa = mangaDoc.getString("capa");

                                    Manga manga = new Manga(isbn, titulo, paginas, editora, dataPublicacao, capa);
                                    mangas.add(manga);

                                    if (mangas.size() == mangasDoUsuario.size()) {
                                        listener.onMangasLoaded(mangas);
                                    }
                                } else {
                                    listener.onMangasLoadFailed();
                                }
                            } else {
                                listener.onMangasLoadFailed();
                            }
                        });
                    }
                } else {
                    listener.onMangasLoaded(mangas);
                }
            } else {
                listener.onMangasLoadFailed();
            }
        });
    }

    public void buscarMangaPorId(String idDoManga, MangasDAO.MangaLoadListener listener) {
        DocumentReference docRef = FirebaseFirestore.getInstance().collection("Mangas").document(idDoManga);

        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    Manga manga = document.toObject(Manga.class);
                    if (manga != null) {
                        listener.onMangaLoaded(manga);
                    } else {
                        listener.onMangaLoadFailed();
                    }
                } else {
                    listener.onMangaLoadFailed();
                }
            } else {
                listener.onMangaLoadFailed();
            }
        });
    }

    public void verificarExistenciaManga(String idDoManga, MangasDAO.DocumentExistenceListener listener) {
        DocumentReference docRef = FirebaseFirestore.getInstance().collection("Mangas").document(idDoManga);

        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    listener.onDocumentExists(true);
                } else {
                    listener.onDocumentExists(false);
                }
            } else {
                listener.onCheckFailed();
            }
        });
    }

    public void adicionarNovoManga(String isbn, String titulo, String paginas, String editora, String dataPublicacao, String capa, String userId, MangaAddListener listener) {
        Manga newManga = new Manga(isbn, titulo, paginas, editora, dataPublicacao, capa);

        DocumentReference novoDocumentoRef = FirebaseFirestore.getInstance().collection("Mangas").document(isbn);

        novoDocumentoRef.set(newManga)
                .addOnSuccessListener(aVoid -> {
                    adicionarMangaAoUsuario(userId, isbn);
                    listener.onMangaAdded();
                })
                .addOnFailureListener(e -> {
                    listener.onAddMangaFailed();
                });
    }

    private void adicionarMangaAoUsuario(String idDoUsuario, String idDoManga) {
        DocumentReference usuarioRef = FirebaseFirestore.getInstance().collection("Usuarios").document(idDoUsuario);

        usuarioRef.update("mangas", FieldValue.arrayUnion(idDoManga))
                .addOnSuccessListener(aVoid -> {
                    Log.d("Firestore", "ID do manga adicionado ao usuário com sucesso");
                })
                .addOnFailureListener(e -> {
                    Log.e("Firestore", "Erro ao adicionar ID do manga ao usuário", e);
                });
    }

}
