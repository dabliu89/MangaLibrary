package com.example.mangalibrary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.mangalibrary.Mocks.LojasDAO;
import com.example.mangalibrary.Models.Loja;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.ArrayList;


public class Lojas extends AppCompatActivity implements OnMapReadyCallback {

    Spinner spinner;
    ListView lista;
    MapView mapa;
    GoogleMap gMap;
    private FusedLocationProviderClient fusedLocationClient;
    private Marker userMarker;
    private FirebaseFirestore db;
    ArrayAdapter adapter;
    int selected;
    LojasDAO lojasDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lojas);

        db = FirebaseFirestore.getInstance();
        selected = -1;

        setviews();
//        setSpinner();
        setLista();

        checkMyPermission();

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        mapa.getMapAsync(this);
        mapa.onCreate(savedInstanceState);

    }

    private void setviews() {
//        this.spinner = this.findViewById(R.id.spinner);
        this.lista = this.findViewById(R.id.listLojas);
        this.mapa = this.findViewById(R.id.mapaLojas);
    }

//    private void setSpinner() {
//        lojasDAO = new LojasDAO();
//        new LoadDataFromFirestore().execute();
//    }

    private void setLista() {
        lojasDAO = new LojasDAO();
        new LoadDataFromFirestore().execute();
    }

    private class LoadDataFromFirestore extends AsyncTask<Void, Void, ArrayList<Loja>> {
        @Override
        protected ArrayList<Loja> doInBackground(Void... voids) {
            lojasDAO.carregarLojasDoFirestore();
            return lojasDAO.getListaDeLojas();
        }

        @Override
        protected void onPostExecute(ArrayList<Loja> listaDeLojas) {
            super.onPostExecute(listaDeLojas);

            selected = -1;

            adapter = new ArrayAdapter<>(Lojas.this, android.R.layout.simple_list_item_1, listaDeLojas);
            lista.setAdapter(adapter);

            lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    selected = position;
                    Loja lojaSelecionada = listaDeLojas.get(selected);
                    double latitude = lojaSelecionada.getLatitude();
                    double longitude = lojaSelecionada.getLongitude();
                    String nomeLoja = lojaSelecionada.getNome();
                    adicionarMarcadorNoMapa(latitude, longitude, nomeLoja);
                    Toast toast = Toast.makeText(Lojas.this, "Selected: " + listaDeLojas.get(selected).getNome(), Toast.LENGTH_SHORT);
                    toast.show();
                }
            });
        }
    }

    private void checkMyPermission() {
        Dexter.withContext(this).withPermission(Manifest.permission.ACCESS_FINE_LOCATION).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                Toast toast = Toast.makeText(Lojas.this, "Permissão concedida.", Toast.LENGTH_SHORT);
                toast.show();
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package",getPackageName(),"");
                intent.setData(uri);
                startActivity(intent);
            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                permissionToken.continuePermissionRequest();
            }
        }).check();

    }

    public void fechaTelaLojas (View view) {
        finish();
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.gMap = googleMap;
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 222);
        }
        gMap.setMyLocationEnabled(true);
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            LatLng currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                            gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 20));
                            gMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                            userMarker = gMap.addMarker(new MarkerOptions()
                                    .position(currentLatLng)
                                    .title("Sua Localização")
                                    .snippet("Você está aqui!"));
                            Toast toast = Toast.makeText(Lojas.this, "Coletou a localização.", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    }
                });

    }

    private void adicionarMarcadorNoMapa(double latitude, double longitude, String nome) {
        LatLng lojaLatLng = new LatLng(latitude, longitude);
        MarkerOptions markerOptions = new MarkerOptions()
                .position(lojaLatLng)
                .title(nome);
        gMap.addMarker(markerOptions);
        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lojaLatLng, 20));
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapa.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapa.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapa.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapa.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapa.onDestroy();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState,outPersistentState);
        mapa.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory(){
        super.onLowMemory();
        mapa.onLowMemory();
    }

}