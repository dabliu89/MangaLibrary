package com.example.mangalibrary.Mocks;

import android.util.Log;
import android.widget.Toast;

import com.example.mangalibrary.Models.Loja;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;

public class LojasDAO {

    private ArrayList<Loja> listaDeLojas = new ArrayList<Loja>();

    public ArrayList<Loja> getListaDeLojas() {
        return listaDeLojas;
    }

    public void carregarLojasDoFirestore() {

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        CollectionReference lojasRef = db.collection("Lojas");

        lojasRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (DocumentSnapshot document : task.getResult()) {
                    Loja loja = document.toObject(Loja.class);
                    if (loja != null) {
                        listaDeLojas.add(loja);
                    }
                }
            } else {
                Log.e("CHECK", "Houve um problema ao fazer a solicitação.");
            }
        });
    }
}
