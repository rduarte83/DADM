package pt.epua;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Cantinas extends AppCompatActivity {

    private TextView tv;
    private Context ctx;
    private Button bt1, bt2, bt3, bt4, bt5, bt6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cantinas);
        ctx = this.getApplicationContext();
        tv = findViewById(R.id.tv);
        bt1 = findViewById(R.id.bt1);
        bt2 = findViewById(R.id.bt2);
        bt3 = findViewById(R.id.bt3);
        bt4 = findViewById(R.id.bt4);
        bt5 = findViewById(R.id.bt5);
        bt6 = findViewById(R.id.bt6);
        bt1.setText("SANTIAGO");
        bt2.setText("CRASTO");
        bt3.setText("SNACK-BAR");
        bt4.setText("ESTGA");
        bt5.setText("ESAN");
        bt6.setText("RESTAURANTE UNIVERSITARIO");

        tv.setText("Escolha a cantina:");

        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(ctx, Refeicao.class);
                it.putExtra("bt_txt", bt1.getText());
                startActivity(it);
            }
        });
        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(ctx, Refeicao.class);
                it.putExtra("bt_txt", bt2.getText());
                startActivity(it);
            }
        });
        bt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(ctx, Refeicao.class);
                it.putExtra("bt_txt", bt3.getText());
                startActivity(it);
            }
        });
        bt4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(ctx, Refeicao.class);
                it.putExtra("bt_txt", bt4.getText());
                startActivity(it);
            }
        });
        bt5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(ctx, Refeicao.class);
                it.putExtra("bt_txt", bt5.getText());
                startActivity(it);
            }
        });
        bt6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(ctx, Refeicao.class);
                it.putExtra("bt_txt", bt6.getText());
                startActivity(it);
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}