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
        //Llena una lista con los datos de lasfacultades

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.miMapa);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mapa = googleMap;

        //Pone los marcadores de acuerdo a la lista creada en onCreate
        ponerRectangulo();
        centrar();
        mapa.setMapType(2);

        /*mapa.setInfoWindowAdapter(new InfoAdapter(MapActivity.this));

        mapa.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Toast.makeText(MapActivity.this,
                        ("Información de: " + marker.getTitle()), Toast.LENGTH_SHORT).show();
            }
        });

        mapa.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                marker.showInfoWindow();
                Toast.makeText(MapActivity.this, marker.getTitle(), Toast.LENGTH_SHORT).show();
                return true;
            }
        });*/
    }

    private void ponerRectangulo() {

        /*
        west, east, north, south,
        -|
        west -12 long
        east 5 long
        north 45 lat
        south, 34.5 lat
        * */


        PolylineOptions lineas = new PolylineOptions()
                .add(new LatLng(rectangulo.getNorth(), rectangulo.getEast()))
                .add(new LatLng(rectangulo.getNorth(), rectangulo.getWest()))
                .add(new LatLng(rectangulo.getSouth(), rectangulo.getWest()))
                .add(new LatLng(rectangulo.getSouth(), rectangulo.getEast()))
                .add(new LatLng(rectangulo.getNorth(), rectangulo.getEast()));
        lineas.width(8);
        lineas.color(Color.RED);

        mapa.addPolyline(lineas);


        LatLng latLng;
        /*for (int i = 0; i < marcadores.size(); i++) {
            facultad = marcadores.get(i);

            latLng = new LatLng(facultad.getLat(), facultad.getLng());

            mapa.addMarker(new MarkerOptions()
                    .position(latLng)
                    .title(facultad.getFacultad())
                    .snippet(facultad.toString()));
        }*/

    }

    public void centrar() {
        LatLng central = new LatLng( rectangulo.getNorth(), rectangulo.getWest());
        CameraPosition cameraPosition = new CameraPosition.Builder().target(central).build();
        mapa.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

}