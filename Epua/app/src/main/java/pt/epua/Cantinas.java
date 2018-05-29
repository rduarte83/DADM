package pt.epua;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Objects;

public class Cantinas extends AppCompatActivity {

    private Context ctx;
    private Button bt1, bt2, bt3, bt4, bt5, bt6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cantinas);
        ctx = this.getApplicationContext();
        TextView tv = findViewById(R.id.tv);
        bt1 = findViewById(R.id.btEmentas);
        bt2 = findViewById(R.id.btParques);
        bt3 = findViewById(R.id.bt3);
        bt4 = findViewById(R.id.bt4);
        bt5 = findViewById(R.id.bt5);
        bt6 = findViewById(R.id.bt6);
        bt1.setText(R.string.santiago);
        bt2.setText(R.string.crasto);
        bt3.setText(R.string.snackBar);
        bt4.setText(R.string.estga);
        bt5.setText(R.string.esan);
        bt6.setText(R.string.restUniv);

        tv.setText(R.string.pickCanteen);

        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(ctx, Ementa.class);
                it.putExtra("bt_txt", bt1.getText());
                startActivity(it);
            }
        });
        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(ctx, Ementa.class);
                it.putExtra("bt_txt", bt2.getText());
                startActivity(it);
            }
        });
        bt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(ctx, Ementa.class);
                it.putExtra("bt_txt", bt3.getText());
                startActivity(it);
            }
        });
        bt4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(ctx, Ementa.class);
                it.putExtra("bt_txt", bt4.getText());
                startActivity(it);
            }
        });
        bt5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(ctx, Ementa.class);
                it.putExtra("bt_txt", bt5.getText());
                startActivity(it);
            }
        });
        bt6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(ctx, Ementa.class);
                it.putExtra("bt_txt", bt6.getText());
                startActivity(it);
            }
        });

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //back button
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}