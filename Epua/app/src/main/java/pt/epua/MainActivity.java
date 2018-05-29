package pt.epua;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity{
    private Context ctx;
    private CountDownTimer cdt;
    private ProgressBar pbar;
    private NumberPicker np;
    private int timeout;
    private SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedpreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        timeout = sharedpreferences.getInt("timeout", 5000);

        ctx = this.getApplicationContext();

        ImageButton btEmentas = findViewById(R.id.btEmentas);
        ImageButton btParques = findViewById(R.id.btParques);

        final TextView tv_progress = findViewById(R.id.tvPbar);

        pbar = findViewById(R.id.pbar);


        //Start activity after timeout(ms)
        cdt = new CountDownTimer (timeout, 1000) {
            @SuppressLint("SetTextI18n")
            public void onTick(long millisUntilFinished) {

                int delta = (int) (timeout - millisUntilFinished)/(timeout/100);
                pbar.setProgress(delta);
                tv_progress.setText(getString(R.string.progress)+ String.valueOf((millisUntilFinished / 1000)+1));
            }
            public void onFinish() {
                pbar.setVisibility(View.GONE);
                tv_progress.setVisibility(View.GONE);
                Intent it = new Intent(ctx, Parques.class);
                startActivity(it);
                Log.v("Sucess","DONE!");
            }
        }.start();

        /*final Handler mHandler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Intent it = new Intent(ctx, Parques.class);
                startActivity(it);
            }
        };

        mHandler.postDelayed(runnable, timeout);*/

        btEmentas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(ctx, Cantinas.class);
                startActivity(it);
                //Cancel Timer
                //mHandler.removeCallbacks(runnable);
                cdt.cancel();
                pbar.setVisibility(View.GONE);
                tv_progress.setVisibility(View.GONE);
            }
        });
        btParques.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(ctx, Parques.class);
                startActivity(it);
                //Cancel Timer
                //mHandler.removeCallbacks(runnable);
                cdt.cancel();
                pbar.setVisibility(View.GONE);
                tv_progress.setVisibility(View.GONE);
            }
        });
    }

    private void createPicker() {
        // Get the layout inflater
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Inflate and set the layout for the dialog
        LayoutInflater inflater = this.getLayoutInflater();
        // dialog layout)
        builder.setTitle("Defina o tempo (segundos)");
        builder.setMessage("Escolha um número:");

        View dialogView = inflater.inflate(R.layout.dialog_timeout, null);
        builder.setView(dialogView);
        np = dialogView.findViewById(R.id.dialog_number_picker);
        np.setMaxValue(60);
        np.setMinValue(1);
        np.setValue(5);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                SharedPreferences.Editor editor = sharedpreferences.edit();
                int timeout = np.getValue()*1000;
                editor.putInt("timeout", timeout);
                editor.apply();
                Toast.makeText(ctx,"Tempo de arranque alterado com sucesso!", Toast.LENGTH_SHORT).show();
            }
        });
        builder.create();
        builder.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //back button
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        if (item.getItemId() == R.id.menu_timeout) {
            cdt.cancel();
            createPicker();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //create menu
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }
}