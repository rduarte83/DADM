package pt.epua;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
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
    private AdapterParques mAdapter;
    private RequestQueue queue;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private static final int REQUEST_LOCATION_PERMISSION = 2;
    private float distancia;
    private Location here, l;
    private FusedLocationProviderClient mFusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parques);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        queue = Volley.newRequestQueue(this);

        parqueList = new ArrayList<>();

        RecyclerView recyclerView = findViewById(R.id.rv);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

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
                        // This method performs the actual data-refresh operation.
                        // The method calls setRefreshing(false) when it's finished.
                        getLocation();
                        parseJSON();
                        //Toast.makeText(getApplicationContext(),"Actualização efectuada com sucesso!", Toast.LENGTH_SHORT).show();
                        View snackbarLayout = findViewById(R.id.snackbarLayout);
                        Snackbar snackbar = Snackbar.make(snackbarLayout, R.string.sb_updateSucess, Snackbar.LENGTH_LONG);
                        snackbar.show();
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                }
        );
        getLocation();

        parseJSON();

        //Back arrow
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }

    private void gpsPrompt() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Localização Desligada!");  // GPS not found
        builder.setMessage("Activar?"); // Want to enable?
        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        });
        builder.setNegativeButton("Não", null);
        builder.create();
        builder.show();
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]
                    {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
        } else {
            mFusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    // Got last known location. In some rare situations this can be null.
                    if (location != null) {
                        // Logic to handle location object
                        here = location;
                        double lat = here.getLatitude();
                        double lon = here.getLongitude();
                        Log.v("HERE Lat: ", String.valueOf(lat));
                        Log.v("HERE Lon: ", String.valueOf(lon));
                    } else {
                        //Evita que o programa crashe quando o GPS está desligado
                        //java.lang.NullPointerException: Attempt to read from field 'double android.location.Location.mLatitude' on a null object reference
                        here = new Location("");
                        gpsPrompt();
                    }
                }
            });
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_LOCATION_PERMISSION:
                // If the permission is granted, get the location,
                // otherwise, alertdialog
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getLocation();
                } else {
                    Toast.makeText(getApplicationContext(), R.string.t_permission, Toast.LENGTH_LONG).show();
                }
                break;
        }
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
                                    return Float.compare(p1.getDistancia(), p2.getDistancia());
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

        //Limpar a lista
        parqueList.clear();
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
            // Signal SwipeRefreshLayout to start the progress indicator
            mSwipeRefreshLayout.setRefreshing(true);
            // Start the refresh background task.
            // This method calls setRefreshing(false) when it's finished.
            getLocation();
            parseJSON();
            mSwipeRefreshLayout.setRefreshing(false);

            View snackbarLayout = findViewById(R.id.snackbarLayout);
            Snackbar snackbar = Snackbar.make(snackbarLayout, R.string.sb_updateSucess, Snackbar.LENGTH_LONG);
            snackbar.show();
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
