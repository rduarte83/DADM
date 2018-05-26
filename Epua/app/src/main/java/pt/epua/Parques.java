package pt.epua;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parques);

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
