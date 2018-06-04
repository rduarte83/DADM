package pt.epua;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Objects;

public class Ementa extends AppCompatActivity {
    private ArrayList<Cantina> cantinaArray;
    private RequestQueue queue;
    private TextView tvCanteen;
    private TextView tvMeal;
    private TextView tvSopa;
    private TextView tvCarne;
    private TextView tvPeixe;
    private TextView tvDieta;
    private TextView tvVeget;
    private TextView tvOpcao;
    private TextView tvSalada;
    private TextView tvDiversos;
    private TextView tvSobremesa;
    private Cantina cantina;
    private String zona, canteen, weekday, meal, sopa, carne, peixe, dieta, veget, opcao, salada, diversos, sobremesa, disabled;
    private final String nd = getString(R.string.nd);
    private final String encerrado = getString(R.string.encerrado);
    private int mealType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ementa);
        zona = Objects.requireNonNull(this.getIntent().getExtras()).getString("bt_txt");
        cantinaArray = new ArrayList<>();

        queue = Volley.newRequestQueue(this);

        tvCanteen = findViewById(R.id.tv_canteen);
        tvMeal = findViewById(R.id.tv_meal);
        tvSopa = findViewById(R.id.tv_content_sopa);
        tvCarne = findViewById(R.id.tv_content_carne);
        tvPeixe = findViewById(R.id.tv_content_peixe);
        tvDieta = findViewById(R.id.tv_content_dieta);
        tvVeget = findViewById(R.id.tv_content_veget);
        tvOpcao = findViewById(R.id.tv_content_opcao);
        tvSalada = findViewById(R.id.tv_content_salada);
        tvDiversos = findViewById(R.id.tv_content_diversos);
        tvSobremesa = findViewById(R.id.tv_content_sobremesa);

        parseJSON();
        invalidateOptionsMenu();
    }

    private void parseJSON() {
        String url = "http://services.web.ua.pt/sas/ementas?date=week&place=all&format=json";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            /*
                            SANTIAGO
                            Menus.[0].Menu.[0] - Almoco
                            Menus.[0].Menu.[1] - Jantar
                            Sopa 0 Carne 1 Peixe 2 Dieta 3 Veget 4 Opcao 5 Salada 6 Diversos 7 Sobremesa 8
                            CRASTO
                            Menus.[0].Menu.[2] - Almoco
                            Menus.[0].Menu.[3] - Jantar
                            Sopa 0 Carne 1 Peixe 2 Dieta 3 Veget 4 Opcao 5 Salada 6 Diversos 7 Sobremesa 8
                            SNACK-BAR
                            Menus.[0].Menu.[4] - Almoco
                            Menus.[0].Menu.[5] - ND
                            Sopa 0 Carne 1 Peixe 2 Dieta 3 Veget 4 Opcao Salada Diversos Sobremesa 5
                            ESTGA
                            Menus.[1].Menu.[0] - Almoco
                            Menus.[1].Menu.[1] - Jantar
                            Sopa 0 Carne 1 Peixe 2 Dieta 3 Veget 5 Opcao Salada 4 Diversos Sobremesa 6
                            ESAN
                            Menus.[2].Menu.[0] - Almoco
                            Menus.[1].Menu.[1] - Jantar
                            Sopa 0 Carne 1 Peixe 2 Dieta 3 Veget 5 Opcao Salada 4 Diversos Sobremesa 6
                            RESTAURANTE UNIVERSITARIO
                            Menus.[3].Menu.[0] - Almoco
                            Menus.[3].Menu.[1] - ND
                            Sopa 0 Carne 1 Peixe 2 Dieta Veget Opcao 3Salada 4 Diversos Sobremesa 5
                            */
                            JSONArray menusArray = response.getJSONArray("menus");

                            switch (zona) {
                                case "SANTIAGO": {
                                    for (int i = 0; i <= 1; i++) {
                                        JSONObject menuObj = menusArray.getJSONObject(0).getJSONArray("menu").getJSONObject(i).getJSONObject("@attributes");
                                        // if disabled /= 0 -> Encerrado
                                        disabled = menuObj.getString("disabled");
                                        if (!(disabled.equals("0"))) {
                                            canteen = menuObj.getString("canteen");
                                            weekday = menuObj.getString("weekday");
                                            meal = menuObj.getString("meal");
                                            cantina = new Cantina(canteen, weekday, meal, disabled);
                                            cantinaArray.add(cantina);
                                        } else {
                                            JSONArray itemsArray = menusArray.getJSONObject(0).getJSONArray("menu").getJSONObject(i).getJSONObject("items").getJSONArray("item");
                                            canteen = menuObj.getString("canteen");
                                            weekday = menuObj.getString("weekday");
                                            meal = menuObj.getString("meal");
                                            sopa = itemsArray.getString(0);
                                            carne = itemsArray.getString(1);
                                            peixe = itemsArray.getString(2);
                                            dieta = itemsArray.getString(3);
                                            veget = itemsArray.getString(4);
                                            opcao = itemsArray.getString(5);
                                            salada = itemsArray.getString(6);
                                            diversos = itemsArray.getString(7);
                                            sobremesa = itemsArray.getString(8);
                                            cantina = new Cantina(canteen, weekday, meal, sopa, carne, peixe, dieta, veget, opcao, salada, diversos, sobremesa, disabled);
                                            cantinaArray.add(cantina);
                                        }
                                    }
                                }
                                break;

                                case "CRASTO": {
                                    for (int i = 2; i <= 3; i++) {
                                        JSONObject menuObj = menusArray.getJSONObject(0).getJSONArray("menu").getJSONObject(i).getJSONObject("@attributes");
                                        // if disabled /= 0 -> Encerrado
                                        disabled = menuObj.getString("disabled");
                                        if (!(disabled.equals("0"))) {
                                            canteen = menuObj.getString("canteen");
                                            weekday = menuObj.getString("weekday");
                                            meal = menuObj.getString("meal");
                                            cantina = new Cantina(canteen, weekday, meal, disabled);
                                            cantinaArray.add(cantina);
                                        } else {
                                            JSONArray itemsArray = menusArray.getJSONObject(0).getJSONArray("menu").getJSONObject(i).getJSONObject("items").getJSONArray("item");
                                            canteen = menuObj.getString("canteen");
                                            weekday = menuObj.getString("weekday");
                                            meal = menuObj.getString("meal");
                                            sopa = itemsArray.getString(0);
                                            carne = itemsArray.getString(1);
                                            peixe = itemsArray.getString(2);
                                            dieta = itemsArray.getString(3);
                                            veget = itemsArray.getString(4);
                                            opcao = itemsArray.getString(5);
                                            salada = itemsArray.getString(6);
                                            diversos = itemsArray.getString(7);
                                            sobremesa = itemsArray.getString(8);
                                            cantina = new Cantina(canteen, weekday, meal, sopa, carne, peixe, dieta, veget, opcao, salada, diversos, sobremesa, disabled);
                                            cantinaArray.add(cantina);
                                        }
                                    }
                                }
                                break;

                                case "SNACK-BAR": {
                                    for (int i = 4; i <= 4; i++) {
                                        JSONObject menuObj = menusArray.getJSONObject(0).getJSONArray("menu").getJSONObject(i).getJSONObject("@attributes");
                                        // if disabled /= 0 -> Encerrado
                                        disabled = menuObj.getString("disabled");
                                        if (!(disabled.equals("0"))) {
                                            canteen = menuObj.getString("canteen");
                                            weekday = menuObj.getString("weekday");
                                            meal = menuObj.getString("meal");
                                            cantina = new Cantina(canteen, weekday, meal, disabled);
                                            cantinaArray.add(cantina);
                                        } else {
                                            JSONArray itemsArray = menusArray.getJSONObject(0).getJSONArray("menu").getJSONObject(i).getJSONObject("items").getJSONArray("item");
                                            canteen = menuObj.getString("canteen");
                                            weekday = menuObj.getString("weekday");
                                            meal = menuObj.getString("meal");
                                            sopa = itemsArray.getString(0);
                                            carne = itemsArray.getString(1);
                                            peixe = itemsArray.getString(2);
                                            dieta = itemsArray.getString(3);
                                            veget = itemsArray.getString(4);
                                            opcao = nd;
                                            salada = nd;
                                            diversos = nd;
                                            sobremesa = itemsArray.getString(5);
                                            cantina = new Cantina(canteen, weekday, meal, sopa, carne, peixe, dieta, veget, opcao, salada, diversos, sobremesa, disabled);
                                            cantinaArray.add(cantina);

                                        }
                                    }
                                    //SNACK-BAR não contem objecto jantar - fix
                                    cantina = new Cantina(canteen, weekday, "Jantar", encerrado);
                                    cantinaArray.add(cantina);
                                }
                                break;

                                case "ESTGA": {
                                    for (int i = 0; i <= 1; i++) {
                                        JSONObject menuObj = menusArray.getJSONObject(1).getJSONArray("menu").getJSONObject(i).getJSONObject("@attributes");
                                        // if disabled /= 0 -> Encerrado
                                        disabled = menuObj.getString("disabled");
                                        if (!(disabled.equals("0"))) {
                                            canteen = menuObj.getString("canteen");
                                            weekday = menuObj.getString("weekday");
                                            meal = menuObj.getString("meal");
                                            cantina = new Cantina(canteen, weekday, meal, disabled);
                                            cantinaArray.add(cantina);
                                        } else {
                                            JSONArray itemsArray = menusArray.getJSONObject(1).getJSONArray("menu").getJSONObject(i).getJSONObject("items").getJSONArray("item");
                                            canteen = menuObj.getString("canteen");
                                            weekday = menuObj.getString("weekday");
                                            meal = menuObj.getString("meal");
                                            sopa = itemsArray.getString(0);
                                            carne = itemsArray.getString(1);
                                            peixe = itemsArray.getString(2);
                                            dieta = itemsArray.getString(3);
                                            veget = itemsArray.getString(5);
                                            opcao = nd;
                                            salada = itemsArray.getString(4);
                                            diversos = nd;
                                            sobremesa = itemsArray.getString(6);
                                            cantina = new Cantina(canteen, weekday, meal, sopa, carne, peixe, dieta, veget, opcao, salada, diversos, sobremesa, disabled);
                                            cantinaArray.add(cantina);

                                        }
                                    }
                                }
                                break;

                                case "ESAN": {
                                    for (int i = 0; i <= 1; i++) {
                                        JSONObject menuObj = menusArray.getJSONObject(2).getJSONArray("menu").getJSONObject(i).getJSONObject("@attributes");
                                        // if disabled /= 0 -> Encerrado
                                        disabled = menuObj.getString("disabled");
                                        if (!(disabled.equals("0"))) {
                                            canteen = menuObj.getString("canteen");
                                            weekday = menuObj.getString("weekday");
                                            meal = menuObj.getString("meal");
                                            cantina = new Cantina(canteen, weekday, meal, disabled);
                                            cantinaArray.add(cantina);
                                        } else {
                                            JSONArray itemsArray = menusArray.getJSONObject(2).getJSONArray("menu").getJSONObject(i).getJSONObject("items").getJSONArray("item");
                                            canteen = menuObj.getString("canteen");
                                            weekday = menuObj.getString("weekday");
                                            meal = menuObj.getString("meal");
                                            sopa = itemsArray.getString(0);
                                            carne = itemsArray.getString(1);
                                            peixe = itemsArray.getString(2);
                                            dieta = itemsArray.getString(3);
                                            veget = itemsArray.getString(5);
                                            opcao = nd;
                                            salada = itemsArray.getString(4);
                                            diversos = nd;
                                            sobremesa = itemsArray.getString(6);
                                            cantina = new Cantina(canteen, weekday, meal, sopa, carne, peixe, dieta, veget, opcao, salada, diversos, sobremesa, disabled);
                                            cantinaArray.add(cantina);

                                        }
                                    }
                                }
                                break;

                                // Não serve jantares - estrutura diferente
                                case "RESTAURANTE UNIVERSITARIO": {
                                    for (int i = 0; i <= 0; i++) {
                                        JSONObject menuObj = menusArray.getJSONObject(3).getJSONArray("menu").getJSONObject(i).getJSONObject("@attributes");
                                        // if disabled /= 0 -> Encerrado
                                        disabled = menuObj.getString("disabled");
                                        if (!(disabled.equals("0"))) {
                                            canteen = menuObj.getString("canteen");
                                            weekday = menuObj.getString("weekday");
                                            meal = menuObj.getString("meal");
                                            cantina = new Cantina(canteen, weekday, meal, disabled);
                                            cantinaArray.add(cantina);
                                        } else {
                                            JSONArray itemsArray = menusArray.getJSONObject(3).getJSONArray("menu").getJSONObject(i).getJSONObject("items").getJSONArray("item");
                                            canteen = menuObj.getString("canteen");
                                            weekday = menuObj.getString("weekday");
                                            meal = menuObj.getString("meal");
                                            sopa = itemsArray.getString(0);
                                            carne = itemsArray.getString(1);
                                            peixe = itemsArray.getString(2);
                                            dieta = nd;
                                            veget = nd;
                                            opcao = nd;
                                            salada = itemsArray.getString(4);
                                            diversos = itemsArray.getString(3);
                                            sobremesa = itemsArray.getString(5);
                                            cantina = new Cantina(canteen, weekday, meal, sopa, carne, peixe, dieta, veget, opcao, salada, diversos, sobremesa, disabled);
                                            cantinaArray.add(cantina);
                                        }
                                    }
                                    //Rest. Univ não contem objecto jantar - fix
                                    cantina = new Cantina(canteen, weekday, "Jantar", encerrado);
                                    cantinaArray.add(cantina);

                                }
                                break;
                            }
                            mealType = 0;
                            verificaHora();
                            verificaMeal();

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

    private void verificaMeal() {
        switch (mealType) {
            case 0:
                insereJSON(mealType);
                mealType = 1;
                break;
            case 1:
                insereJSON(mealType);
                mealType = 0;
                break;
        }
        //Back Arrow
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }

    private void verificaHora() {
        Calendar calendar = new GregorianCalendar();
        //data actual
        Date now = calendar.getTime();
        //hora actual
        int afterLunch = 15;
        int afterDinner = 21;
        int hour = calendar.get(Calendar.HOUR_OF_DAY);

        Log.v("AGORA: ", now.toString());
        Log.v("HORA: ", String.valueOf(hour));

        if (hour>=afterLunch && hour<afterDinner) {
            insereJSON(mealType);
            mealType = 1;
        } else {
            insereJSON(mealType);
            mealType = 0;
        }
    }

    private void insereJSON(int mealType) {
        this.mealType = mealType;

        String ptWeekday = cantina.getWeekday();
        switch (ptWeekday) {
            case "Monday":
                ptWeekday = "Segunda-Feira";
                break;
            case "Tuesday":
                ptWeekday = "Terça-Feira";
                break;
            case "Wednesday":
                ptWeekday = "Quarta-Feira";
                break;
            case "Thursday":
                ptWeekday = "Quinta-Feira";
                break;
            case "Friday":
                ptWeekday = "Sexta-Feira";
                break;
            case "Saturday":
                ptWeekday = "Sábado";
                break;
            case "Sunday":
                ptWeekday = "Domingo";
                break;
        }

        tvCanteen.setText(canteen);
        tvMeal.setText(cantinaArray.get(mealType).getMeal());

        CardView card_disable = findViewById(R.id.card_disable);
        CardView card_sopa = findViewById(R.id.card_sopa);
        CardView card_prato = findViewById(R.id.card_prato);
        CardView card_salada = findViewById(R.id.card_salada);
        CardView card_diversos = findViewById(R.id.card_diversos);
        CardView card_sobremesa = findViewById(R.id.card_sobremesa);

        if (!(cantinaArray.get(mealType).getDisabled().equals("0"))) {
            card_disable.setVisibility(View.VISIBLE);
            card_sopa.setVisibility(View.GONE);
            card_prato.setVisibility(View.GONE);
            card_salada.setVisibility(View.GONE);
            card_diversos.setVisibility(View.GONE);
            card_sobremesa.setVisibility(View.GONE);

            TextView tvDisabled = findViewById(R.id.tv_disabled);
            tvDisabled.setText(cantinaArray.get(mealType).getDisabled());
        } else {
            card_disable.setVisibility(View.GONE);
            card_sopa.setVisibility(View.VISIBLE);
            card_prato.setVisibility(View.VISIBLE);
            card_salada.setVisibility(View.VISIBLE);
            card_diversos.setVisibility(View.VISIBLE);
            card_sobremesa.setVisibility(View.VISIBLE);

            if (cantinaArray.get(mealType).getSopa().contains("attributes")) {
                //alterar visibilidade
                //tvSopa.setVisibility(View.GONE);
                tvSopa.setText(nd);
            } else {
                tvSopa.setText(cantinaArray.get(mealType).getSopa());
            }

            if (cantinaArray.get(mealType).getCarne().contains("attributes")) {
                tvCarne.setText(nd);
            } else {
                tvCarne.setText(cantinaArray.get(mealType).getCarne());
            }

            if (cantinaArray.get(mealType).getPeixe().contains("attributes")) {
                tvPeixe.setText(nd);
            } else {
                tvPeixe.setText(cantinaArray.get(mealType).getPeixe());
            }

            if (cantinaArray.get(mealType).getDieta().contains("attributes")) {
                tvDieta.setText(nd);
            } else {
                tvDieta.setText(cantinaArray.get(mealType).getDieta());
            }

            if (cantinaArray.get(mealType).getVeget().contains("attributes")) {
                tvVeget.setText(nd);
            } else {
                tvVeget.setText(cantinaArray.get(mealType).getVeget());
            }
            if (cantinaArray.get(mealType).getOpcao().contains("attributes")) {
                tvOpcao.setText(nd);
            } else {
                tvOpcao.setText(cantinaArray.get(mealType).getOpcao());
            }

            if (cantinaArray.get(mealType).getSalada().contains("attributes")) {
                tvSalada.setText(nd);
            } else {
                tvSalada.setText(cantinaArray.get(mealType).getSalada());
            }

            if (cantinaArray.get(mealType).getSobremesa().contains("attributes")) {
                tvSobremesa.setText(nd);
            } else {
                tvSobremesa.setText(cantinaArray.get(mealType).getSobremesa());
            }

            if (cantinaArray.get(mealType).getDiversos().contains("attributes")) {
                tvDiversos.setText(nd);

            } else {
                tvDiversos.setText(cantinaArray.get(mealType).getDiversos());
            }
        }
        //use weekday in title
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(ptWeekday);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_refeicao, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //back button
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        if (item.getItemId() == R.id.menu_meal) {
            verificaMeal();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem menuMeal = menu.findItem(R.id.menu_meal);
        if (mealType == 0) {
            menuMeal.setTitle("Almoço");
        }
        if (mealType == 1) {
            menuMeal.setTitle("Jantar");
        }
        return super.onPrepareOptionsMenu(menu);
    }
}