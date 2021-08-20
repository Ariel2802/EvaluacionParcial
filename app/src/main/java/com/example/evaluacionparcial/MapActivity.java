package com.example.evaluacionparcial;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import model.Rectangulo;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mapa;
    Rectangulo rectangulo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        Bundle bundle = getIntent().getExtras();
        rectangulo = (Rectangulo) bundle.getSerializable("rectangle");

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.miMapa);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mapa = googleMap;

        ponerRectangulo();
        centrar();
        mapa.setMapType(2);
    }

    private void ponerRectangulo() {

        PolylineOptions lineas = new PolylineOptions()
                .add(new LatLng(rectangulo.getNorth(), rectangulo.getEast()))
                .add(new LatLng(rectangulo.getNorth(), rectangulo.getWest()))
                .add(new LatLng(rectangulo.getSouth(), rectangulo.getWest()))
                .add(new LatLng(rectangulo.getSouth(), rectangulo.getEast()))
                .add(new LatLng(rectangulo.getNorth(), rectangulo.getEast()));
        lineas.width(8);
        lineas.color(Color.RED);

        mapa.addPolyline(lineas);
    }

    public void centrar() {
        LatLng central = new LatLng( rectangulo.getNorth(), rectangulo.getWest());
        CameraPosition cameraPosition = new CameraPosition.Builder().target(central).build();
        mapa.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

}