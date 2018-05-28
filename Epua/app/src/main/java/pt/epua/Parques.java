package pt.epua;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class Parques extends AppCompatActivity {
    private List<Parque> parqueList;
    private Parque parque;
    private String id, nome;
    private int capacidade, livre;
    private float distancia;

    private AdapterParques mAdapter;

    private RequestQueue queue;

    private SwipeRefreshLayout mSwipeRefreshLayout;

    private Location l, here;
    private static final int REQUEST_CODE_PERMISSION = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parques);
        /*
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_PERMISSION);
            return;
        }

        if (lm != null) {
            location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            double lat = location.getLatitude();
            double lon = location.getLongitude();
            Log.a("HERE Lat: ", String.valueOf(lat));
            Log.a("HERE Lon: ", String.valueOf(lon));
        }
        */
        //location API
        FusedLocationProviderClient mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

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
                            double lat = location.getLatitude();
                            double lon = location.getLongitude();
                            Log.i("HERE Lat: ", String.valueOf(lat));
                            Log.v("HERE Lon: ", String.valueOf(lon));
                        }
                    }
                });


        queue = Volley.newRequestQueue(this);

        parqueList = new ArrayList<>();

        RecyclerView recyclerView = findViewById(R.id.rv);

        mAdapter = new AdapterParques(parqueList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        mSwipeRefreshLayout = findViewById(R.id.swiperefresh);
        /*
         * Sets up a SwipeRefreshLayout.OnRefreshListener that is invoked when the user
         * performs a swipe-to-refresh gesture.
         */
        mSwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        Log.v("", "onRefresh called from SwipeRefreshLayout");
                        // This method performs the actual data-refresh operation.
                        // The method calls setRefreshing(false) when it's finished.
                        parseJSON();
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                }
        );

        parseJSON();
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }

        private void parseJSON() {
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

                                    Log.v("PARQUE Lat: ", String.valueOf(l.getLatitude()));
                                    Log.v("PARQUE Lon: ", String.valueOf(l.getLongitude()));

                                    distancia = (l.distanceTo(here)) / 1000;
                                    distancia = (float) Math.round(distancia * 10) / 10;

                                    Log.v("Distancia: ", String.valueOf(distancia));

                                    if (livre > capacidade) livre = capacidade;
                                    if (livre < 0) livre = 0;

                                    parque = new Parque(id, nome, capacidade, livre, distancia);
                                    parqueList.add(parque);

                                    mAdapter.notifyDataSetChanged();
                                }
                                //Sort do array por distância
                                Collections.sort(parqueList, new Comparator<Parque>() {
                                    public int compare(Parque p1, Parque p2) {
                                        return  Float.compare(p1.getDistancia(), p2.getDistancia());
                                    }
                                });
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

            //Limpar a cache
            queue.getCache().clear();

            queue.add(request);

            //Limpar a lista e parar o refresh
            parqueList.clear();
            mSwipeRefreshLayout.setRefreshing(false);
        }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //back button
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        // Check if user triggered a refresh:
        if (item.getItemId() == R.id.menu_refresh) {
            Log.v("", "Refresh menu item selected");

            // Signal SwipeRefreshLayout to start the progress indicator
            mSwipeRefreshLayout.setRefreshing(true);

            // Start the refresh background task.
            // This method calls setRefreshing(false) when it's finished.
            parseJSON();
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
