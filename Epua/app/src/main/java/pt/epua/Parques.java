package pt.epua;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationProvider;
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
    private Parque parque;
    private RequestQueue queue;
    private AdapterParques adapter;
    private Activity act;
    //location api
    private FusedLocationProviderClient mFusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parques);

        //location
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            // Logic to handle location object

                            Log.v("lat", String.valueOf( location.getLatitude()));
                            Log.v("long", String.valueOf( location.getLongitude()));

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

                                    if (livre > capacidade) livre = capacidade;
                                    if (livre < 0) livre=0;

                                parque = new Parque(id, nome, capacidade, livre);
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

            //queue.getCache().clear();
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
