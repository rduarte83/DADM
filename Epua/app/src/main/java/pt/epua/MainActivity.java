package pt.epua;
//TODO menu sobre?
//TODO menu c opcoes : so mostar almocos, mudar tempo do timeout

import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;

public class MainActivity extends AppCompatActivity {

    private Context ctx;
    private CountDownTimer cdt;
    private ProgressBar pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ctx = this.getApplicationContext();

        ImageButton bt1 = findViewById(R.id.bt1);
        ImageButton bt2 = findViewById(R.id.bt2);

        pb = findViewById(R.id.pb);

        //Start activity after timeout(ms)
        final int timeout = 10000;

        cdt = new CountDownTimer (timeout, 1000) {
            public void onTick(long millisUntilFinished) {
                int delta = (int)(timeout - millisUntilFinished)/100;
                pb.setProgress(delta);
                Log.v("seconds remaining:",String.valueOf(millisUntilFinished / 1000));
                Log.v("Progress:",String.valueOf(delta));
            }
            public void onFinish() {
                Intent it = new Intent(ctx, Parques.class);
                startActivity(it);
                pb.setVisibility(View.INVISIBLE);
                Log.v("Timer: ","DONE!");
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
                //Cancel Timer
                //mHandler.removeCallbacks(runnable);
                cdt.cancel();
                Intent it = new Intent(ctx, Cantinas.class);
                startActivity(it);
            }
        });
        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Cancel Timer
                //mHandler.removeCallbacks(runnable);
                cdt.cancel();
                Intent it = new Intent(ctx, Parques.class);
                startActivity(it);
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
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //create menu
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }
}