package com.example.evaluacionparcial;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import model.Rectangulo;

public class MainActivity extends AppCompatActivity {

    TextRecognizer recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);
    InputImage image;
    RequestQueue requestQueue;

    TextView txtResultado, txtJSON;
    ImageView imgPais;
    String pais, codPais;
    private static int RESULT_LOAD_IMAGE = 1;
    Rectangulo rectangulo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtResultado = (TextView) findViewById(R.id.txtResultado);
        requestQueue = Volley.newRequestQueue(this);
        imgPais = (ImageView) findViewById(R.id.imgPais);
        txtJSON = (TextView) findViewById(R.id.txtJSON);
    }

    public void api2(View view) {
        requestJSONBandera();
    }

    void requestJSONALL() {
        String url = "http://www.geognos.com/api/en/countries/info/all.json";

        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject jsonObject = response.getJSONObject("Results");
                            JSONArray codigos = jsonObject.names();
                            JSONArray jsonArray = jsonObject.toJSONArray(codigos);
                            System.out.println(jsonArray.toString());
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject json = jsonArray.getJSONObject(i);
                                if (pais.equalsIgnoreCase(json.getString("Name"))) {
                                    codPais = codigos.getString(i);
                                    requestJSONBandera();
                                    break;
                                }
                            }
                        } catch (JSONException e) {
                            System.out.println(e.getStackTrace());
                            e.printStackTrace();
                        }
                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError ex) {
                System.out.println("Error: " + ex.toString());
            }
        });
        requestQueue.add(jsonRequest);
    }

    void requestJSONBandera() {
        String url = "http://www.geognos.com/api/en/countries/info/" + codPais + ".json";
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject jsonResult = response.getJSONObject("Results");
                            JSONObject jsonRectangulo = jsonResult.getJSONObject("GeoRectangle");
                            JSONObject json = jsonResult.getJSONObject("GeoRectangle");

                            //GeoPt
                            rectangulo = new Rectangulo(jsonRectangulo.getDouble("West"),
                                    jsonRectangulo.getDouble("East"),
                                    jsonRectangulo.getDouble("North"),
                                    jsonRectangulo.getDouble("South"));
                            txtJSON.setText(response.toString());
                            Glide.with(MainActivity.this).load("http://www.geognos.com/api/en/countries/flag/"
                                    + codPais + ".png").into(imgPais);
                        } catch (
                                JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError ex) {
                System.out.println("Error: " + ex.toString());
            }
        });
        requestQueue.add(jsonRequest);
    }

    public void abrirMapa(View view) {
        Intent intent = new Intent(this, MapActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("rectangle", rectangulo);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void OpenGallery(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, RESULT_LOAD_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
                ImageView imageView = (ImageView) findViewById(R.id.imgCaptura);
                imageView.setImageURI(data.getData());
                image = InputImage.fromFilePath(this, data.getData());
                proceso();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void proceso() {
        Task<Text> result = recognizer.process(image).addOnSuccessListener(
                new OnSuccessListener<Text>() {
                    @Override
                    public void onSuccess(Text visionText) {
                        pais = visionText.getText();
                        codPais = pais.substring(0, 2);
                        txtResultado.setText(pais);
                        requestJSONALL();
                    }
                }).addOnFailureListener(
                new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        Toast.makeText(MainActivity.this,
                                ("Error: " + e.getMessage()), Toast.LENGTH_LONG).show();
                    }
                });
    }
}