package pt.epua;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Parques extends AppCompatActivity {
    private List<Parque> parqueList;
    private ListView lv;
    private String id, nome;
    private int capacidade, livre;
    private Location l, here;
    private Float distancia;
    private Parque parque;
    private RequestQueue queue;
    private AdapterParques adapter;
    private Activity act;
    //location api
    private FusedLocationProviderClient mFusedLocationClient;
    private static final int REQUEST_CODE_PERMISSION = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parques);

        //location
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_CODE_PERMISSION);
            return;
        }
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {

                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            // Logic to handle location object
                            here = location;
                            here.getLatitude();
                            here.getLongitude();
                        }
                    }
                });


        queue = Volley.newRequestQueue(this);

        parqueList = new ArrayList<>();
        act = this;
        lv = findViewById(R.id.lv);

        parseJSON();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

        public void parseJSON() {
            String url = "http://services.web.ua.pt/parques/parques";
            JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            try {
                                for (int i = 1; i < response.length(); i++) {
                                    JSONObject obj = response.getJSONObject(i);
                                    id = obj.getString("ID");
                                    nome = obj.getString("Nome");
                                    capacidade = obj.getInt("Capacidade");
                                    livre = obj.getInt("Livre");

                                    l = new Location("");
                                    l.setLatitude(obj.getDouble("Latitude"));
                                    l.setLongitude(obj.getDouble("Longitude"));

                                    Log.v("Latitude: ", String.valueOf(l.getLatitude()));
                                    Log.v("Longitude: ", String.valueOf(l.getLongitude()));

                                    distancia = (l.distanceTo(here))/1000;
                                    distancia = (float)Math.round(distancia * 10) / 10;

                                    Log.v("Distancia: ", String.valueOf(distancia));

                                    if (livre > capacidade) livre = capacidade;
                                    if (livre < 0) livre=0;

                                    parque = new Parque(id, nome, capacidade, livre, distancia);
                                    parqueList.add(parque);

                                    adapter = new AdapterParques(parqueList, act);
                                    lv.setAdapter(adapter);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                }
            });

            queue.getCache().clear();
            queue.add(request);
        }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //create menu
        getMenuInflater().inflate(R.menu.menu_parques, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
