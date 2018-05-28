package pt.epua;
//TODO menu sobre?
//TODO menu c opcoes : so mostar almocos, mudar tempo do timeout
//TODO mudar layouts (performance)
//TODO recycler view performance???
//TODO mudar p jantar a partir duma determinada hora

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class MainActivity extends AppCompatActivity{
    private Context ctx;
    private CountDownTimer cdt;
    private ProgressBar pb;
    private NumberPicker np;


    public void createPicker () {
        // Get the layout inflater
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Inflate and set the layout for the dialog
        LayoutInflater inflater = this.getLayoutInflater();
        // dialog layout)
        builder.setTitle("Defina o tempo (seg)");
        builder.setMessage("Escolha um número:");

        View dialogView = inflater.inflate(R.layout.dialog, null);
        builder.setView(dialogView);
        np = dialogView.findViewById(R.id.dialog_number_picker);
        np.setMaxValue(60);
        np.setMinValue(1);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        int timeout = np.getValue();
                        Log.v("TIMEOUT: ", "" + timeout);
                    }
                });

        builder.create();
        builder.show();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ctx = this.getApplicationContext();

        ImageButton bt1 = findViewById(R.id.bt1);
        ImageButton bt2 = findViewById(R.id.bt2);

        pb = findViewById(R.id.pb);

        Calendar calendar = new GregorianCalendar();
        //data actual
        Date now = calendar.getTime();
        //hora actual
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        Log.v("AGORA: ", now.toString());
        Log.v("HORA: ", String.valueOf(hour));
        //Start activity after timeout(ms)
        final int timeout = 10000;

        cdt = new CountDownTimer (timeout, 1000) {
            public void onTick(long millisUntilFinished) {
                int delta = (int)(timeout - millisUntilFinished)/(timeout/100);
                pb.setProgress(delta);
                Log.v("Remaining: ",String.valueOf(millisUntilFinished / 1000));
                Log.v("Progress: ",String.valueOf(delta));
            }
            public void onFinish() {
                pb.setVisibility(View.INVISIBLE);
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

        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(ctx, Cantinas.class);
                startActivity(it);
                //Cancel Timer
                //mHandler.removeCallbacks(runnable);
                cdt.cancel();
                pb.setVisibility(View.INVISIBLE);
            }
        });
        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(ctx, Parques.class);
                startActivity(it);
                //Cancel Timer
                //mHandler.removeCallbacks(runnable);
                cdt.cancel();
                pb.setVisibility(View.INVISIBLE);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //back button
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        if (item.getItemId() == R.id.menu_timeout) {
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