package com.example.mangalibrary;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.storage.FirebaseStorage;

import java.util.HashMap;
import java.util.Map;

public class EditarPerfil extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseUser fuser;

    TextView nome;
    TextView email;

    ListenerRegistration registration;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        userId = mAuth.getCurrentUser().getUid();
        fuser = mAuth.getCurrentUser();

        setEditarPerfilViews();
        setEditarPerfil();

    }

    private void setEditarPerfilViews() {
        this.nome = this.findViewById(R.id.editPerfilNome);
        this.email = this.findViewById(R.id.editPerfilEmail);
    }

    private void setEditarPerfil() {
        DocumentReference documentReference = db.collection("Usuarios").document(userId);
        this.registration = documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                nome.setText(value.getString("nome"));
                email.setText(value.getString("email"));
            }
        });
    }

    public void processarEdicaoPerfil (View view) {
        if (this.nome.getText().toString().isEmpty() || this.email.getText().toString().isEmpty()) {
            Toast toast = Toast.makeText(this, "Ação requer o preenchimento de todos os campos.", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }
        String changeNome = this.nome.getText().toString();
        String changeEmail = this.email.getText().toString();

        fuser.verifyBeforeUpdateEmail(fuser.getEmail()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast toast = Toast.makeText(EditarPerfil.this, "Verifique seu e-mail para confirmar a atualização.", Toast.LENGTH_LONG);
                toast.show();
                DocumentReference documentReference = db.collection("Usuarios").document(userId);
                Map<String,Object> editado = new HashMap<>();
                editado.put("email",changeEmail);
                editado.put("nome",changeNome);
                documentReference.update(editado).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast toast = Toast.makeText(EditarPerfil.this, "Perfil atualizado com sucesso.", Toast.LENGTH_SHORT);
                        toast.show();
                        finish();
                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast toast = Toast.makeText(EditarPerfil.this, "Houve um problema ao modificar o e-mail.", Toast.LENGTH_SHORT);
                toast.show();
            }
        });

    }

    public void fechaTelaVerPerfil (View view) {
        finish();
    }
}