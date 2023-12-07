package com.example.mangalibrary.Mocks;

import android.util.Log;

import com.example.mangalibrary.Models.Usuario;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class UsuariosDAO {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public interface UsuariosLoadListener {
        void onUsuariosLoaded(List<Usuario> usuarios);
        void onUsuariosLoadFailed();
    }

    public void buscarTodosUsuarios(UsuariosLoadListener listener) {
        db.collection("Usuarios")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<Usuario> usuarios = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Usuario usuario = document.toObject(Usuario.class);
                            usuario.setId(document.getId());
                            ArrayList<String> amigos = (ArrayList<String>) document.get("amigos");
                            usuario.setAmigos(amigos);
                            usuarios.add(usuario);
                        }
                        listener.onUsuariosLoaded(usuarios);
                    } else {
                        listener.onUsuariosLoadFailed();
                    }
                });

    }

}
